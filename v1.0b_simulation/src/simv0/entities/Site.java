package simv0.entities;

import sim.engine.*;

import simv0.*;

public class Site implements Steppable
{
	// we need access to the simulation object
	private CBSimulation cbsim;
	
	// the state of the site
	// off= not bound by its TF = false
	// on = bound by its TF = true
	// all sites are assumed to begin life as 'off'
	private boolean state = false;
	
	// TFBS can be either promoter or repressor sites
	// if it isn't a promoter it is a repressor
	// we assume for this Version 0 model that all sites ar promoters for simplicity
	private boolean isPromoter = true;
	
	// is this site selected when the Segment it is in receives molecular input?
	// default to false...
	private boolean selected = false;
	
	// the site will have a type which corresponds to the name of the TF it binds
	private String type = "";
	
	// we need a publicly accessible constructor method to set up the sites
	public Site(String t)
	{
		type = t;
		
		// add the Segment to the simulation schedule
		addSiteToSimulationSchedule(Schedule.EPOCH, 1, this, 1.0);
		
	}
	public void step(SimState state)
	{
		// if we bind the correct TF to this TFBS then we can switch 'on'
		if(selected)
		{
			becomeOn();
		}
		
		// for simplicity we assume that the burst lasts for just 1 time step in the Version 0 model
		// ensure we are switched 'off' before we leave
		becomeOff();
	}
	
	private void becomeOn()
	{
		state = true;
	}
	
	private void becomeOff()
	{
		state = false;
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
	
	public void addSiteToSimulationSchedule(double time, int order, Steppable generator, double interval)
	{
		this.setStopper(cbsim.schedule.scheduleRepeating(time, order, generator, interval));
	} 
	
	public boolean getState()
	{
		return state;
	}
	
	public void setSelected(boolean sel)
	{
		selected = sel;
	}
	
	public void setType(String t)
	{
		type = t;
	}
	
}