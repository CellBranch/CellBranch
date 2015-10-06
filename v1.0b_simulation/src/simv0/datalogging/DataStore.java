package simv0.datalogging;

import sim.engine.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import simv0.CBSimulation;
import simv0.entities.Cell;
import simv0.entities.Cistrome;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/*
 * The class collects data from a simulation run. 
 * It is designed to be run as a Steppable, alongside the simulation so it 
 * needs to be scheduled. When the step method is called the class will query all of 
 * the ActiveSegmentDataLogger objects (one per cistrome) for their data, and compile the relevant bits into 
 * its data structures. 
 * 
 * @author Richard Greaves - code structure based on that from ARTIMMUS written
 *                           by Mark Read and heavily adapted for the specific
 *                           data logging needs of the CBSimulation
 *
 */

public class DataStore
{
	CBSimulation cbsim;
	
	Cell cell;
	ArrayList<Cistrome> cistromeList;
	
	int numCistromes;
	
	DataColumnLong colStep = new DataColumnLong("time_step");
	
	ActiveSegmentDataLogger ActiveSegmentDL = new ActiveSegmentDataLogger("Active Segments", cbsim);
	
	ArrayList<DataColumnInteger> colsActiveRed = new ArrayList<DataColumnInteger>();
	ArrayList<DataColumnInteger> colsActiveWhite = new ArrayList<DataColumnInteger>();
	ArrayList<DataColumnInteger> colsCumulativeRed = new ArrayList<DataColumnInteger>();
	ArrayList<DataColumnInteger> colsCumulativeWhite = new ArrayList<DataColumnInteger>();
	ArrayList<DataColumnInteger> colsSharedRed = new ArrayList<DataColumnInteger>();
	ArrayList<DataColumnInteger> colsCumulativeSharedRed = new ArrayList<DataColumnInteger>();

	/*
	 * the quantities to be logged
	 */
	private long step = 0;
	
	private int activeReds = 0;
	private int activeWhites = 0;
	private int cumulativeReds = 0;
	private int cumulativeWhites = 0;
	private int sharedReds = 0;
	private int cumulativeSharedReds = 0;

	private String tableKey = "";
	
	HashMap<String, Integer> data;
	
	/*
	 * the default constructor
	 */
	public DataStore(CBSimulation simulation)
	{ 

		cbsim = simulation;
		
		Cell cell = cbsim.getCell();
		ArrayList<Cistrome> cistromeList = cell.getCistromeList();
		
		numCistromes = cistromeList.size();
		
		for(int c = 0; c < numCistromes; c++)
		{
			String arName = "Active_Red_"+((Integer) c).toString();
			String awName = "Active_White_" + ((Integer) c).toString();
			String crName = "Cumulative_Red_" + ((Integer) c).toString();
			String cwName = "Cumulative_White_" + ((Integer) c).toString();
			String srName = "Shared_Red_" + ((Integer) c).toString();
			String csrName = "Cumulative_Shared_Red_" + ((Integer) c).toString();
			
			// total populations of each cell type
			colsActiveRed.add(new DataColumnInteger(arName));
			colsActiveWhite.add(new DataColumnInteger(awName));
			colsCumulativeRed.add(new DataColumnInteger(crName));
			colsCumulativeWhite.add(new DataColumnInteger(cwName));
			colsSharedRed.add(new DataColumnInteger(srName));
			colsCumulativeSharedRed.add(new DataColumnInteger(csrName));

		}
		
		/*
		 * add DataStore to schedule
		 */
		//addDataStoreToSimulationScheduleRepeating(cbsim, 1.0);
		
	}
	
	/*
	 * add DataStore to schedule
	 */
	public void addDataStoreToSimulationScheduleRepeating(CBSimulation cbsim, double frequency)
	{
		//this.setStopper(cbsim.schedule.scheduleRepeating(Schedule.EPOCH, 6, this, frequency));
	} 
	
	public void collateData(CBSimulation sim)
	{
		
		resetValues();
		
		cbsim = (CBSimulation) sim;                                                     
		
		Cell cell = (Cell) cbsim.getCell();
		ArrayList<Cistrome> cistromeList = cell.getCistromeList();
		
		step = (int) ((cbsim.schedule.getSteps()-3)/4); // the time step reached
		
		//System.out.println("Stepping " + this + " at step " + step);
		
		for (int c = 0; c < numCistromes; c++)
		{
			
			Cistrome thisCistrome = cistromeList.get(c);
			
			//System.out.println("For cistrome " + c + " " + thisCistrome.getName());
			
			// get a copy of the ActiveSegmentDataLogger for each cistrome...
			ActiveSegmentDL = (ActiveSegmentDataLogger) thisCistrome.getActiveSegmentDataLogger();
		
			data  = ActiveSegmentDL.getData();
			
			/* 
			 * we want the contents of the ActiveSegmentDataLoggers here 
			 * so we can store them in our data to be written to file
			 */
		
			activeReds = data.get("Red");
			activeWhites = data.get("White");
			cumulativeReds = data.get("cumulativeRed");
			cumulativeWhites = data.get("cumulativeWhite");
			sharedReds = data.get("sharedRed");
			cumulativeSharedReds = data.get("cumulativeSharedRed");
			
			storeData(c);			// log the values into the data store. 
			
		}
		
		//System.out.println("Finished step " + step + " for " + this);
		
	}
	
	/*
	 * we want to be able to quickly zero all the tallies
	 */
	private void resetValues()
	{
		activeReds = 0;
		activeWhites = 0;
		cumulativeReds = 0;
		cumulativeWhites = 0;
		sharedReds = 0;
		cumulativeSharedReds = 0;
	}

	/*
	 * actually store the data in the data column objects
	 */
	private void storeData(int c)
	{
		
		if (c == 0)
		{
			colStep.logValue(step);
		}
		
		colsActiveRed.get(c).logValue(activeReds);
		colsActiveWhite.get(c).logValue(activeWhites);
		colsCumulativeRed.get(c).logValue(cumulativeReds);
		colsCumulativeWhite.get(c).logValue(cumulativeWhites);
		colsSharedRed.get(c).logValue(sharedReds);
		colsCumulativeSharedRed.get(c).logValue(cumulativeSharedReds);

	}
	
	/* 
	 * this method will create and open an output file
	 * and dump the simulation results in CSV format
	 */
	public String compileTableToString()
	{

		Cell cell = (Cell) cbsim.getCell();
		ArrayList<Cistrome> cistromeList = cell.getCistromeList();
		
		StringBuilder output = new StringBuilder();
		StringBuilder key = new StringBuilder();
												
		key.append(colStep.getTitle() + " ");
		
		// First of all we want to create the table headers
		for (int c = 0; c < cistromeList.size(); c++)
		{							
			key.append(colsActiveRed.get(c).getTitle() + " ");						
			key.append(colsActiveWhite.get(c).getTitle() + " ");						
			key.append(colsCumulativeRed.get(c).getTitle() + " ");					
			key.append(colsCumulativeWhite.get(c).getTitle() + " ");
			key.append(colsSharedRed.get(c).getTitle() + " ");
			key.append(colsCumulativeSharedRed.get(c).getTitle() + " ");
		}
		
			tableKey = key.toString();		
			
			output.append("#" + tableKey + "\n");			
			
			// Now we need to create iterators for each data column
			Iterator<Long> stepIter = colStep.getIterator();
			
			ArrayList<Iterator<Integer>> activeRedIters = new ArrayList<Iterator<Integer>>();
			ArrayList<Iterator<Integer>> activeWhiteIters = new ArrayList<Iterator<Integer>>();
			ArrayList<Iterator<Integer>> cumulativeRedIters = new ArrayList<Iterator<Integer>>();
			ArrayList<Iterator<Integer>> cumulativeWhiteIters = new ArrayList<Iterator<Integer>>();
			ArrayList<Iterator<Integer>> sharedRedIters = new ArrayList<Iterator<Integer>>();
			ArrayList<Iterator<Integer>> cumulativeSharedRedIters = new ArrayList<Iterator<Integer>>();
			
			for (int c = 0; c < cistromeList.size(); c++)
			{	
					
				activeRedIters.add(colsActiveRed.get(c).getIterator());								
				activeWhiteIters.add(colsActiveWhite.get(c).getIterator());							
				cumulativeRedIters.add(colsCumulativeRed.get(c).getIterator());						
				cumulativeWhiteIters.add(colsCumulativeWhite.get(c).getIterator());	
				sharedRedIters.add(colsSharedRed.get(c).getIterator());
				cumulativeSharedRedIters.add(colsCumulativeSharedRed.get(c).getIterator());
				
			}
			
			while(stepIter.hasNext())
			{
				
				output.append(stepIter.next()); 							
				output.append(" ");
				
				for (int c = 0; c < cistromeList.size(); c++)
				{	
					
					output.append(activeRedIters.get(c).next());						
					output.append(" ");
					output.append(activeWhiteIters.get(c).next());						
					output.append(" ");
					output.append(cumulativeRedIters.get(c).next());					
					output.append(" ");
					output.append(cumulativeWhiteIters.get(c).next());					
					output.append(" ");
					output.append(sharedRedIters.get(c).next());						
					output.append(" ");
					output.append(cumulativeSharedRedIters.get(c).next());					
					output.append(" ");
					
				}
				
				output.append("\n");
			}	

		return output.toString();
		
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

}
