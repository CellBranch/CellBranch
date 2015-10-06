package simv0.entities;

import sim.engine.*;
import sim.field.grid.ObjectGrid2D;

import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import simv0.*;
import simv0.datalogging.*;

public class Cistrome implements Steppable
{

	private CBSimulation cbsim;
	
	private Cell cell;
	
	private static int numCistromesStepped = 0;
	
	private String source;
	private String name = "";
	private int width;
	private int height;
	private int initialReds;
	
	// we assume that we can calibrate the values of the cistrome branchingRates
	// we also assume in the Version 1 model, branchingRate will be held constant:
	// the branchingRate will be time-invariant 
	// the branchingRate will independent of the TF concentrations
	// the branchingRates of the cistromes will not be coupled in any way
	private int branchingRate;
	
	// we need an array to hold the Segments in...
	private ObjectGrid2D segmentGrid;
	
	// these fields influence the transcription factor binding process
	// numToPick is the number of targets to choose at random in each selection
	// numSelections is the number of times we select a sample of random targets
	// this corresponds to the number of red targets picked in the previous step
	private int numToPick = 1;
	private int numSelections = 1;
	
	int lastActiveReds = 0;
	int lastSharedReds = 0;
	
	private int numDissipated = 0;
	
	private boolean ignited = false;
	private boolean dissipated = false;
	
	private int cistromeNumber;
	
	// this is a flag that determines which sub-step of the step() method is sselected at each pass through step()
	int choose_action = 0;
	
	// these fields keep track of how many segments have been activated
	int activeReds;
	int activeWhites;
	int sharedReds;
	int chosenReds;
	
	// and we need to log the activated segments in this Cistrome
	ActiveSegmentDataLogger ActiveSegmentDL;
	
	ArrayList<Cistrome> cistromeList;
	
	// we need a publicly accessible constructor method
	public Cistrome(int w, int h, NodeList segmentsComponent, CBSimulation sim, int cisno)
	{
		
		cistromeNumber = cisno;
		CBSimulation cbsim = sim;
		
		// need to instantiate all the segments for this Cistrome
		width = w;
		height = h;
		
		// create a grid to keep the Segments on - used for the GUI
		segmentGrid = new ObjectGrid2D(width, height);
		
		// read each Segment in turn
		for (int seg = 0; seg < segmentsComponent.getLength(); seg++)
		{
			int posx = 0;
			int posy = 0;
			int numSites = 0;
			
			ArrayList<String> products = new ArrayList<String>();
			
			// need to instantiate this Segment for this Cistrome
			if (segmentsComponent.item(seg).getNodeType() == 1)
			{
				Element thisElement = (Element) segmentsComponent.item(seg);
				
				Node xNode = thisElement.getElementsByTagName("x").item(0);
				Node yNode = thisElement.getElementsByTagName("y").item(0);
				Node numSitesNode = thisElement.getElementsByTagName("numTF").item(0);
				NodeList productsList = thisElement.getElementsByTagName("Products").item(0).getChildNodes();
				
				for (int p = 0; p < productsList.getLength(); p++)
				{
					if (productsList.item(p).getNodeType()==1)
					{
						products.add(productsList.item(p).getTextContent());
					}
					
				}
				
				posx = Integer.parseInt(xNode.getTextContent());
				posy = Integer.parseInt(yNode.getTextContent());
				numSites = Integer.parseInt(numSitesNode.getTextContent());
				
			}
			
			Segment thisSegment = new Segment(posx, posy, numSites, products, cbsim);
			
			// add Segment to the segmentGrid from the Cistrome
			segmentGrid.set(posx, posy, thisSegment);
	
		}
		
		// add the Cistrome to the simulation schedule
		addCistromeToSimulationSchedule(Schedule.EPOCH, 3, this, 0.25, cbsim);
		
		// create an active site data logger to hold
		// the current tallies of active segments in this cistrome...
		// schedule it here
		ActiveSegmentDL = new ActiveSegmentDataLogger("Active Segments",cbsim);
		ActiveSegmentDL.setCallingCistrome(cisno);
		//cbsim.schedule.scheduleRepeating(Schedule.EPOCH,5,ActiveSegmentDL,1.0);
		
	}
	
	public void step(SimState state)
	{
		
		CBSimulation cbsim = (CBSimulation) state;
		
		//System.out.println("Retrieving cell from " + cbsim);
		cell = cbsim.getCell();
		//System.out.println("Retrieving cistrome list from " + cell);
		cistromeList = cell.getCistromeList();
		
		long step = cbsim.schedule.getSteps(); // the time step reached
		
		//System.out.println("Stepping " + this + " at step " + step);
		
		/*
		 * whatever step we are at we need to
		 * i) repeat numSelections times
		 * ii) repeat numToPick times
		 * iii) pick a random Segment (by choosing its (x,y) position in the cistrome)
		 * iv) if the target is 'red' i.e. has products we are interested in
		 *                             switch target 'on'
		 *                             let ActiveSegmentDataLogger count it as an active 'red' target
		 *                             add one to number of Selections for next step
		 *                             switch target 'off'
		 * v) if the target is 'white' i.e. has no products of interest
		 *                             switch target 'on'
		 *                             let ActiveSegmentDataLogger count it as an active 'white' target
		 *                             switch target 'off'
		 */
		
		switch (choose_action)
		{
		
			case 0: if(!(ignited))
					{
						ignite(cbsim);
						
						// we start each step with no active segments
						activeWhites = 0;
						activeReds = 0;
						sharedReds = 0;
						chosenReds = 0;
					} else {
						bindTF();
						
						// we start each step with no active segments
						activeWhites = 0;
						activeReds = 0;
						sharedReds = 0;
						chosenReds = 0;
					}
					break;
			case 1: communicateActivation();
					break;
			case 2: activateSegments();
					ActiveSegmentDL.countSegments(cbsim);
					break;
			case 3: releaseTF();
					++numCistromesStepped;
					
					if (numCistromesStepped == cistromeList.size())
					{
						// if all the Cistromes have been fully stepped
						// gather data from their ActiveSegmentDataLoggers
						cbsim.getDataStore().collateData(cbsim);
						numCistromesStepped = 0;
					}
					
					break;
		
		}
		
		choose_action = (choose_action + 1) % 4;
		
		//System.out.println("Currently there are " + this.getLastActiveReds() + " active reds in " + this);
		//System.out.println("Finished stepping Cistrome " + this + " " + this.getName());
		//System.out.println("Finished step " + step + " for " + this);
		
	}
	
	/*
	 * these methods act as steps in the branching process algorithm
	 * the sparks (TF) must land (bind) - the receiving post (Segment) will be marked as having received a spark
	 * sparks landed on a shared post must be transferred to the corresponding post in another Arena if it shares this post with 
	 * the current Arena
	 * posts currently possessing a spark will be activated
	 * each activated post can produce m sparks to commence the next step with...
	 */
	
	public void ignite(CBSimulation sim)
	{
		
		cbsim = sim;
		
		//System.out.println("Stepping " + this + " at mini-step a : ignite");
		
		// we just want one set of initialReds targets picked here
		numSelections = 1;
		numToPick = getInitialReds();
		
		boolean redIsOn;
		boolean black;
		boolean white;     
		
		//System.out.println(this + " selecting " + numSelections + " set of " + numToPick + " targets...");
		
		for (int a = 0; a < numSelections; a++)
		{
			
			for (int c = 0; c < numToPick; c++)
			{
				int segx;
				int segy;
				
				Segment thisSegment = null;
				ObjectGrid2D seggrid = this.getSegmentGrid();
				
				// if the segment is red but already active then we keep on choosing
				
				do{
					
					segx = cbsim.random.nextInt(this.getWidth());
					segy = cbsim.random.nextInt(this.getHeight());
					
					thisSegment = (Segment) seggrid.field[segx][segy];
					
					black = (thisSegment.getNumSites() == 0);
					white = ((thisSegment.getNumSites() > 0) && (thisSegment.getProducts().get(0).equals("none")));
					redIsOn = (((thisSegment.getNumSites()) > 0) && (!(thisSegment.getProducts().get(0).equals("none")) && (thisSegment.getTFBound() == true)));
					
				} while(redIsOn||white||black);
				
				// OK we have selected a 'red' segment
				// If it hasn't already bound TF we want to bind
				// this TF to it to prevent further TF binding
				
				if (thisSegment.getTFBound() == false)
				{
					thisSegment.setTFBound(true);
					++chosenReds;
					//System.out.println(this + " marking red segment as chosen, making " + chosenReds + " chosen reds...");
				}
					
			} 
			
		}
		
		ignited = true;
	}
	
	public void bindTF()
	{
		
		//System.out.println("Stepping " + this + " at mini-step a");
		
		//System.out.println(this + "selecting " + numSelections + " set of " + numToPick + " targets...");
		
		for (int a = 0; a < numSelections; a++)
		{
			
			for (int b = 0; b < numToPick; b++)
			{
				
				// if the segment is black we need to keep on choosing 
				
				int segx;
				int segy;
				boolean black;
				boolean white;
				Segment thisSegment = null;
				ObjectGrid2D seggrid = this.getSegmentGrid();
					
				do {
					
					segx = cbsim.random.nextInt(this.getWidth());
					segy = cbsim.random.nextInt(this.getHeight());
					
					thisSegment = (Segment) seggrid.field[segx][segy];
					
					
					black = ((thisSegment.getNumSites()) == 0);
					white = ((thisSegment.getNumSites() > 0) && (thisSegment.getProducts().get(0).equals("none")));
					
				} while (black);
				
				if (!((thisSegment.getProducts().get(0).equals("none"))))
				{
					// i.e. red...
					// and not already bound by TF...
					
					if (thisSegment.getTFBound() == false)
					{
						thisSegment.setTFBound(true);
					}
					
				} else if (thisSegment.getNumSites() >= 1) {
					// i.e. white but not black
					if (thisSegment.getTFBound() == false)
					{
						thisSegment.setTFBound(true);
					}
					
				}
				
			}
			
		}
		
	}
	
	public void communicateActivation()
	{

		//System.out.println("Stepping " + this + " at mini-step b");
		
		ObjectGrid2D seggrid = this.getSegmentGrid();
		//ArrayList<Integer> triedCistromes = new ArrayList<Integer>();
		
		for (int segx = 0; segx < this.getWidth(); segx++)
		{
			
			for (int segy = 0; segy < this.getHeight(); segy++)
			{
				
				Segment thisSegment = (Segment) seggrid.field[segx][segy];
						
				if ((thisSegment.getTFBound())&&(thisSegment.isShared()))
				{
					
					ArrayList<Integer> eligibleCistromes = thisSegment.getSharedBy();
					
					while ((eligibleCistromes.size() > 0)&&(thisSegment.getTFBound()))
					{
						
						//System.out.println("Spark on post in cistrome " + cistromeNumber + " at (" + segx + "," + segy +") can be transferred to cistromes: " + eligibleCistromes);
						//System.out.println("So a possible " + eligibleCistromes.size() + " destinations for spark to move to");
						
						int chosenCistromeIndex = cbsim.random.nextInt(eligibleCistromes.size());
						int chosenCistromeNumber  = eligibleCistromes.get(chosenCistromeIndex).intValue();
						Cistrome chosenCistrome = cistromeList.get(chosenCistromeNumber);
						
						//System.out.println("Attempting to move spark to cistrome " + chosenCistromeNumber + " " + chosenCistrome.getName());
						
						Segment chosenSegment = chosenCistrome.getSegmentAtXY(segx, segy);
						
						if (chosenSegment.getOtherTFBound())
						{
							if(eligibleCistromes.contains((Integer) chosenCistromeNumber))
							{
								eligibleCistromes.remove((Integer) chosenCistromeNumber);
								//System.out.println("Removing " + chosenCistromeNumber + " from eligible targets list leaving " + eligibleCistromes);
							}
						}
						
						if (!(chosenSegment.getOtherTFBound()))
						{
							chosenSegment.setOtherTFBound(true);
							thisSegment.setTFBound(false);
							//System.out.println("Spark from cistrome " + cistromeNumber + " successfully reallocated to Cistrome " + chosenCistromeNumber);
						}
						
					} 
					
					//System.out.println("Ended attempts to reallocate spark");
					
				}
				
			}
			
		}
		
	}
	
	public void activateSegments()
	{
		//System.out.println("Stepping " + this + " at mini-step c");
		
		ObjectGrid2D seggrid = this.getSegmentGrid();
		
		for (int segx = 0; segx < this.getWidth(); segx++)
		{
			
			for (int segy = 0; segy < this.getHeight(); segy++)
			{
				
				Segment thisSegment = (Segment) seggrid.field[segx][segy];	
				
				// activate the Segment and remove its bound TF
				if ((thisSegment.getTFBound())||(thisSegment.getOtherTFBound()))
				{
					
					// if this segment is 'white' count it...
					
					if ((thisSegment.getProducts().get(0).equals("none") && (thisSegment.getNumSites() > 0)))
					{
						thisSegment.setState(true);
						++activeWhites;
						//System.out.println("Activating white post in " + this + " giving " + activeWhites + " active white posts...");
					}
					
					// if this segment is 'red' count it...
					if ((!(thisSegment.getProducts().get(0).equals("none")) && (thisSegment.getNumSites() > 0)))
					{
						if (thisSegment.isShared())
						{
							thisSegment.setState(true);
							++sharedReds;
							//System.out.println("Activating shared red post in " + this + " giving " + sharedReds + " active shared red posts...");
						} else {
							thisSegment.setState(true);
							++activeReds;
							//System.out.println("Activating red post in " + this + " giving " + activeReds + " active red posts...");
						}
						
					}
					
					thisSegment.setState(true);
					thisSegment.setTFBound(false);
					thisSegment.setOtherTFBound(false);
					
				}
				
			}
			
		}
		
	}
	
	public void releaseTF()
	{
		
		//System.out.println("Stepping " + this + " at mini-step d");
		
		/*
		 *  in the next step we will need to pick a set of 
		 *  m targets for each activated red segment from the previous step
		 */
		lastActiveReds = getActiveReds();
		lastSharedReds = getSharedReds();
		
		//System.out.println("Last step had " + getActiveReds() + " active reds");
		//System.out.println("Last step had " + getSharedReds() + " shared reds");
		
		checkDissipation();
		
		numSelections = getActiveReds() + getSharedReds();
		numToPick = this.getBranchingRate();
		
		//System.out.println(this + " will select " + numSelections + " set(s) of " + numToPick + " target(s) in next step...");
		
	}
	
	public void checkDissipation()
	{
		
		if ((getActiveReds() + getSharedReds()) == 0)
		{
			System.out.println("Branching process has dissipated in " + this + "!!!");
			dissipated = true;
			numSelections = 0;
			numToPick = 0;
				
			// check if all the other cistromes are dissipated too
			numDissipated = 0;
				
			for(int c = 0; c < cistromeList.size(); c++)
			{
					
				Cistrome thisCistrome = (Cistrome) cistromeList.get(c);
					
				if (thisCistrome.isDissipated())
				{
					++numDissipated;
				}
					
			}
				
			if(numDissipated == cistromeList.size())
			{
				// if ALL the cistromes are dissipated then finish the simulation
				cbsim.finish();
			}
				
		} 
		
	}
	
	/* 
	 * these methods provide us with a convenient way of stopping 
	 * an agent and removing it from the simulation schedule all in 
	 * one step  - this way we have no danger of 'zombie' agents carrying
	 * on interacting after they have died...
	 */
	public Stoppable stopper = null;

	public void setStopper(Stoppable stopper) { this.stopper = stopper;}

	public void stop() 
	{
		//System.out.println("Stopping " + this);
		stopper.stop();
	}
	
	public void addCistromeToSimulationSchedule(double time, int order, Steppable generator, double interval, CBSimulation cbsim)
	{
		this.setStopper(cbsim.schedule.scheduleRepeating(time, order, generator, interval));
	} 
	
	/*
	 * accessor and mutator methods to allow other classes to access and set fields on this class
	 */
	public String getName()
	{
		return name;
	}
	
	public int getBranchingRate()
	{
		return branchingRate;
	}
	
	public int getInitialReds()
	{
		return initialReds;
	}
	
	public ActiveSegmentDataLogger getActiveSegmentDataLogger()
    {
    	return ActiveSegmentDL;
    }
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public boolean isDissipated()
	{
		return dissipated;
	}
	
	public void setDissipated(Boolean b)
	{
		dissipated = b;
	}
	
	public ObjectGrid2D getSegmentGrid()
	{
		return segmentGrid;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public void setBranchingRate(int rate)
	{
		branchingRate = rate;
	}
	
	public void setInitialReds(int reds)
	{
		initialReds = reds;
	}
	
	public Segment getSegmentAtXY(int x, int y)
	{
		return (Segment) segmentGrid.get(x, y);
	}
	
	public int getActiveReds()
	{
		return activeReds;
	}
	
	public int getSharedReds()
	{
		return sharedReds;
	}
	
}
