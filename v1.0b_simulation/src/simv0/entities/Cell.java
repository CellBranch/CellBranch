package simv0.entities;

import sim.engine.*;

import java.util.ArrayList;

import sim.field.grid.ObjectGrid2D;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import simv0.*;

public class Cell implements Steppable
{
	// we need access to the simulation object
	private CBSimulation cbsim;
		
	/*
	 * the Cell class is basically the container for everything that happens within the simulation
	 * 
	 * the CBSimulation class will instantiate the Cell
	 * 
	 * the Cell object will then set up the cistromes that it needs to describe its genome
	 * we assume that in this Version 0 model we will only want to model one, two or all of the
	 * core pluripotency TFs - NANOG / Oct4 / Sox2
	 * 
	 * the Cell will also act as a 'molecular accountant' - keeping track of how many of each molecule
	 * have been produced and consumed at each step...
	 * 
	 */
	
	// the Cell will contain the Cistromes that we add to the simulation
	
	ArrayList<Cistrome> cistromeList;
	
	private ArrayList<String> sources = new ArrayList<String>();
	
	// we need to construct the Cell object
	
	//public Cell(Element cistromesComponent, CBSimulation cbsim)
	public Cell(ArrayList<String> s, CBSimulation cbsim)
	{
		
		//System.out.println("Invoking the constructor on " + this);
		
		// first we need to look at the cistromesComponent element
		// and read back the name of the file hat contains the cistrome we are interested in
		sources = s;
		
		// open the cistrome file and grab the tags we're interested in...
		// that will be
		// cistrome name - name
		// its dimensions in segments - xsegs and ysegs
		// the associated branching rate - branchingRate
		
		Element cistromeComponent = null;
		
		// read in the cistrome data for each of the cistromes specified
		// also need to create the cistromeList to kerep a working copy of the cistromes we're using handy
		
		cistromeList = new ArrayList<Cistrome>();
		
		for (int c = 0; c < sources.size(); c++)
		{
			try{
				// open the xmlFileLocation to read input
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(sources.get(c)));
					
				cistromeComponent = (Element) doc.getElementsByTagName("cistrome").item(0);
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			NodeList segmentsComponent = null;
			
			int width = Integer.parseInt(cistromeComponent.getElementsByTagName("xsegs").item(0).getTextContent());
			int height = Integer.parseInt(cistromeComponent.getElementsByTagName("ysegs").item(0).getTextContent());
				
			segmentsComponent = cistromeComponent.getElementsByTagName("Segment");
				
			Cistrome cistrome = new Cistrome(width, height, segmentsComponent, cbsim, c);
			
			//System.out.println("Created cistrome " + cistrome + " : " + cistrome.getName());
			cistromeList.add(cistrome);
				
		}
		
		// now we need to work through the cistromes on cistromeList to see which segments are shared
		checkSharedSegments();
		
		// add the Cell to the simulation schedule
		addCellToSimulationSchedule(Schedule.EPOCH, 4, this, 1.0, cbsim);
		
		//System.out.println("Exiting constructor method for " + this);
		
	}	
			
	public void step(SimState state)
	{
		CBSimulation cbsim = (CBSimulation) state;
		
		long step = cbsim.schedule.getSteps(); // the time step reached
		
		//System.out.println("Stepping " + this + " at step " + step);
		
		//System.out.println("Finished step " + step + " for " + this);
	}
	
	public void checkSharedSegments()
	{
		
		//System.out.println("Checking cistromes in use for shared segments...");
		
		for (int i = 0; i < cistromeList.size(); i++)
		{
			Cistrome cistromei = (Cistrome) cistromeList.get(i);
			
			for (int j = 0; j < cistromeList.size(); j++)
			{

				Cistrome cistromej = (Cistrome) cistromeList.get(j);
				
				if (i != j)
				{
					
					//System.out.println("CHECKSHAREDSEGMENTS: Looking for segments shared between cistrome " + i + " and cistrome " + j);
					
					// this bit of code assumes that all the cistromes are the same size
					// i.e. the same X and Y dimensions...
					
					for (int x = 0; x < cistromeList.get(i).getWidth(); x++)
					{
						
						for (int y = 0; y < cistromeList.get(j).getHeight(); y++)
						{
							
							// get the segments at X,Y from cistrome i and cistrome j
							
							Segment segmenti = cistromei.getSegmentAtXY(x, y);
							Segment segmentj = cistromej.getSegmentAtXY(x, y);
							
							// if they are both 'red'
							// i.e. have numSites > 0 and products.size() > 0
							if ((segmenti.getNumSites() > 0)&&(segmentj.getNumSites() > 0))
							{
								if(!(segmenti.getProducts().get(0).equals("none"))&&(!(segmentj.getProducts().get(0).equals("none"))))
								{
									
									// mark segment i as shared
									// add j to the list of cistromes it is shared between
									//System.out.println("Red segment at " + x + " " + y + " is shared between cistromes " + i + " and " + j);
									segmenti.setShared(true);
									//System.out.println("Segment at " + x + "," + y + "in cistrome " + i + " is shared " + segmenti.getShared());
									segmenti.setSharedBy(j);
									//System.out.println("Segment at " + x + "," + y + "in cistrome " + i + " is shared by cistromes " + segmenti.getSharedBy());
									
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		//System.out.println("Overlap checking complete.");
		
	}
	
	/* 
	 * these methods provide us with a convenient way of stopping 
	 * an agent and removing it from the simulation schedule all in 
	 * one step  - this way we have no danger of 'zombie' agents carrying
	 * on interacting after they have died...
	 */
	public Stoppable stopper = null;

	public void setStopper(Stoppable stopper) { this.stopper = stopper;}

	public void stop() {stopper.stop();}
	
	public void addCellToSimulationSchedule(double time, int order, Steppable generator, double interval, CBSimulation cbsim)
	{
		this.setStopper(cbsim.schedule.scheduleRepeating(time, order, generator, interval));
	} 
	
	/*
	 * 'accessor' methods to provide other classes with access
	 * to the cistromes we are working with...
	 */
	
	public ArrayList<Cistrome> getCistromeList()
	{
		return cistromeList;
	}
	
	public Cistrome getCistrome(String n)
	{
		
		for(int i=0; i<cistromeList.size(); i++)
		{
			Cistrome thisCistrome = cistromeList.get(i);
			String thisName = thisCistrome.getName();
			
			if(thisName.equals(n))
			{
				return thisCistrome;
			}
			
		}
		
		return null;
		
	}
	
}
