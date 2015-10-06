package simv0.datalogging;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sim.field.grid.ObjectGrid2D;
import sim.engine.*;
import simv0.CBSimulation;
import simv0.entities.*;
import simv0.datalogging.DrawGraph.SeriesAttributes;

public class ActiveSegmentDataLogger 
{
	
	public DrawGraph graph;				// the graph of the cell type, differentiating the states. 
	private Class segmentType;
	
	private CBSimulation cbsim;
	
	private int callingCistrome;
	
	/*
	 *  data will store the string name of each segment type and an integer representing the number of this segments 
	 * of this type that are active 
	 */
	HashMap<String, Integer> data = new HashMap<String, Integer>();
	
	public ActiveSegmentDataLogger(String title, CBSimulation cbsim)
	{
		segmentType = Segment.class;										// this class works only for Segments. 
		
		// set initial counts
	
		data.put("Red", 0);
		data.put("White", 0);
		data.put("cumulativeRed", 0);
		data.put("cumulativeWhite", 0);
		data.put("sharedRed", 0);
		data.put("cumulativeSharedRed", 0);
		
	}
	
	public void countSegments(CBSimulation sim)
	{
		cbsim = (CBSimulation) sim;
		
		/* create a data structure in which to store references to all of the segments in the cistrome, and populate it */
		ArrayList<Cistrome> cistromeList = cbsim.getCell().getCistromeList();
		int size = cistromeList.size();
		
		ObjectGrid2D segments = cistromeList.get(callingCistrome).getSegmentGrid();
			
		long step = cbsim.schedule.getSteps(); // the time step reached
		
		//System.out.println("Stepping " + this + " at step " + step);
		
		// reset count at each step
			
		data.put("Red", 0);
		data.put("White", 0);
		data.put("sharedRed", 0);
		
		/* iterate over each segment in the cistrome and count the active ones */
			
		for(int y = 0; y < cistromeList.get(callingCistrome).getHeight(); y++)
		{	
				
			for (int x = 0; x < cistromeList.get(callingCistrome).getWidth(); x++)
			{
					
					Segment thisSegment = (Segment) segments.field[x][y];
						
					if(((Segment) segments.field[x][y]).getState())
					{
							
						int numSites = thisSegment.getNumSites();
						ArrayList<String> products = thisSegment.getProducts();
							
						// don't want to count black squares
							
						if (numSites > 0)
						{
								
							// if it's red...
								
							if((!(products.get(0).equals("none"))))
							{
								
								// if the red post is shared...
								if (thisSegment.isShared())
								{
									data.put("sharedRed", data.get("sharedRed") + 1);
									data.put("cumulativeSharedRed", data.get("cumulativeSharedRed") + 1);
								} else {
									data.put("Red", data.get("Red") + 1);
									data.put("cumulativeRed", data.get("cumulativeRed") + 1);
								}
									
							}
							
							// if it's white...
								
							if((products.get(0).equals("none")))
							{
								data.put("White", data.get("White") + 1);
								data.put("cumulativeWhite", data.get("cumulativeWhite") + 1);
							}
								
						}
							
					}
						
				}
			
			}
		
		/* finally, log all the values with the graph, against time */
		
		if(cbsim.getUsingGUI())
		{
			//System.out.println("Using GUI to access simulation");
			for(String key : data.keySet())
				graph.logValue(key, cbsim.schedule.getSteps(), data.get(key));
		}
		
		//System.out.println("Finished step " + step + " for " + this);
		
	}
	
	public HashMap<String, Integer> getData()
	{
		return data;
	}
	
	public void setCallingCistrome(int c)
	{
		callingCistrome = c;
	}
	
}