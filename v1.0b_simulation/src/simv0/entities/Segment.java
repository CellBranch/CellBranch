package simv0.entities;

import sim.engine.*;

import java.util.ArrayList;

import simv0.*;

public class Segment implements Steppable
{
	
	// a Segment can be partially or fully transcribed or not transcribed at all
	// in this Version 0 model - a Segment is either fully transcribed or not transcribed at all
	// Segments will be created in the 'off' state at time t = 0
	private boolean state = false;
	private boolean shared = false;
	private boolean tfbound = false;
	private boolean othertfbound = false;
	
	// Segments will initially be assumed to be not shared
	//private boolean shared = false;
	
	// activation of the segment will give rise to products
	// these will either be TF associated with regulation of transcription
	// or 'none'
	private ArrayList<String> products = new ArrayList<String>();
	
	// a Segment could be shared by several cistromes if for example
	// it is 'red' in two or more cistromes. We want to keep a record of 
	// which cistromes share this Segment
	ArrayList<Integer> sharedBy = new ArrayList<Integer>();
	
	// a Segment can have more than one TFBS
	// a Segment can have more than one TFBS of a given type
	// a Segment can have TFBS of different types and so can occur in other cistromes
	//      this will provide one mode of communication between cistromes
	private int numSites;
	
	// private ArrayList<Site> siteList = new ArrayList<Site>();
	
	// a Segment needs some screen coordinates to place it in the GUI display
	private int x;
	private int y;
	
	// we need a publicly accessible constructor to set up a Segment
	public Segment(int posx, int posy, int n, ArrayList<String> p, CBSimulation cbsim)
	{
		
		// define position of segment within the cistrome 
		x = posx;
		y = posy;
		
		numSites = n;
		
		products = p;
		
		// add the Segment to the simulation schedule
		addSegmentToSimulationSchedule(Schedule.EPOCH, 2, this, 1.0, cbsim);
		
	}
	
	public void step(SimState state)
	{
		
		CBSimulation cbsim = (CBSimulation) state;
		
		long step = cbsim.schedule.getSteps(); // the time step reached
		
		//System.out.println("Stepping " + this + " at step " + step);
		
		// make sure segments turned 'on' in the last step 
		// are now turned 'off'
		// transcription burst only lasts 1 time step...
		
		if (getState() == true)
		{
			setState(false);
		}
		
		//System.out.println("Finished step " + step + " for " + this);
		
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
	
	public void addSegmentToSimulationSchedule(double time, int order, Steppable generator, double interval, CBSimulation cbsim)
	{
		this.setStopper(cbsim.schedule.scheduleRepeating(time, order, generator, interval));
	} 
	
	public ArrayList<String> getProducts()
	{
		return products;
	}
	
	public int getNumSites()
	{
		return numSites;
	}
	
	public boolean getState()
	{
		return state;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public boolean isShared()
	{
		return shared;
	}
	
	public void setShared(boolean s)
	{
		shared = s;
	}
	
	public void setTFBound(boolean val)
	{
		tfbound = val;
	}
	
	public boolean getTFBound()
	{
		return tfbound;
	}
	public void setOtherTFBound(boolean val)
	{
		othertfbound = val;
	}
	
	public boolean getOtherTFBound()
	{
		return othertfbound;
	}
	
	public boolean getShared()
	{
		return shared;
	}
	
	public ArrayList<Integer> getSharedBy()
	{
		return sharedBy;
	}
	
	public void setSharedBy(int n)
	{
		n = (Integer) n;
		sharedBy.add(n);
	}
	
	public void setX(int xcoor)
	{
		x = xcoor;
	}
	
	public void setY(int ycoor)
	{
		x = ycoor;
	}
	
	public void setState(Boolean s)
	{
		state = s;
	}
	
}