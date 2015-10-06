package simv0;

import sim.engine.*;
import ec.util.MersenneTwisterFast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Random;
import java.util.ArrayList;

import simv0.entities.*;
import simv0.datalogging.*;

public class CBSimulation extends SimState
{
	
	public static CBSimulation cbsim;				// static so that other objects can get hold of key items such as the schedule. 
	
	public static DataStore dataStore;
	
	// private boolean instantiateCell = false;
	
	// where to look for the parameters if we don't specify a location 
	// e.g. if we run via the Eclipse GUI...
	public String defaultXmlFileLocation= "cbsim_parameters_default.xml";
	public String xmlFileLocation;
	public String paramCopyName;
	
	public String defaultRunFilePath = "1";
	public String runFilePath;
	
	public String resultFilePath;
	public String description;
	
	public static long batchSize;
	public static long defaultBatchSize = 1;
	public static long batchNumber;
	public static long defaultBatchNumber = 1;
	public static long jobNumber;
	public static long defaultJobNumber = 1;
	
	public static long maxBatchSize = 1000;
	public static long maxBatchNumber = 20;
	
	/* if a simulation run takes longer than timeout seconds to complete, 
	 * then we halt the simulation and throw and exception.
	 */
	private static int timeOut = Integer.MAX_VALUE;				// by default there is no timeout. 									
	private static long startTime = System.currentTimeMillis();
	private static int simulationSteps = 0;
	private static long seed = 0;
	private static int initialReds = 0;
	private static int branchingRate;
	private static String name;
	private static String source;
	
	//ArrayList<String> moleculeNames = new ArrayList<String>();
	
	/*
	 * the CBSimulation class will actually oversee the running of the simulation
	 * 
	 * it needs to:
	 * set up the initial conditions
	 * read the simulation parameter file
	 * instantiate a Cell which 'contains' the system
	 * 
	 */
	
	// create a Cell object to house the system
	// this field is static since we assume that we must have only one cell in the simulation...
	// it is private because we don't want anything else to interfere with it...
	// the Cell is considered to be pluripotent - we implicitly assume that pluripotency
	// is a property of individual cells rather than a population level property
	private static Cell cell;
	
	// are we using the GUI or not?
	// assume yes, change to no if we need to
	private static boolean useGUI = false;
	
	// ArrayLists to store details of the cistromes in use that are
	// read from the simulation parameter file rather than the cistrome file...
	ArrayList<String> names;
	ArrayList<String> sources;
	ArrayList<Integer> branchingRates;
	ArrayList<Integer> numsInitialReds;
	
	/*
	 * Constructor. 
	 * Takes a specified seed - derived from the MASON Mersenne Twister. 
	 * 
	 * Specifying the seed is important:
	 * although java will seed experiments based on the internal clock, 
	 * when running batch experiments on a cluster it is possible that 
	 * two machines will start with the same seed. When gathering data 
	 * to form distributions representing experiments, it is critical 
	 * that we do not run simulations with the same seed, they skew the 
	 * distributions. 
	 */
    public CBSimulation(long seed, String[] args)
    {
        super(new MersenneTwisterFast(seed), new Schedule());
        cbsim = this;	
        startTime = System.currentTimeMillis();
        
       //System.out.println("Invoking constructor for simulation object: " + cbsim);
        
        // open parameter files specified for reading
        try{
        	cbsim.xmlFileLocation = args[0];
            cbsim.runFilePath = args[1];
        } catch (Exception e) {
        	System.out.println("Parameter file not specified - default values will be used for this input...");
            cbsim.xmlFileLocation = defaultXmlFileLocation;
            cbsim.runFilePath = defaultRunFilePath;
        }
         
        // set up the ArrayLists that will store information about the cistromes used in the simulation
        names = new ArrayList<String>();
        sources = new ArrayList<String>();
        branchingRates = new ArrayList<Integer>();
        numsInitialReds = new ArrayList<Integer>();
        
        int position = xmlFileLocation.lastIndexOf("/");
        paramCopyName = xmlFileLocation.substring(position+1);
        
    }
    
	public static void main(String[] args)
	{
	    
		//System.out.println("In main() on CBSimulation class");
		
		// we're not using the GUI if we enter via this method...
				useGUI = false;
				
		/*
	     * this starts the run off 
	     *
	     * the parameters for the simulation will ultimately be read from the parameter file
	     */
				 try{
			            String[] tokens = args[1].split("/");
			            batchSize = Long.parseLong(tokens[0]);
			            batchNumber = Long.parseLong(tokens[1]);
			            jobNumber = Long.parseLong(tokens[2]);
			        } catch (Exception e) {
			            batchSize = defaultBatchSize;
			            batchNumber = defaultBatchNumber;
			            jobNumber = defaultJobNumber;
			        }
			         
		//System.out.println("Batch size is " + batchSize);
		//System.out.println("Maximum batch size is " + maxBatchSize);
		//System.out.println("Batch number is " + batchNumber);
		//System.out.println("Maximum batch number is " + maxBatchNumber);
		//System.out.println("Job number is " + jobNumber);
		
		seed = (batchSize * maxBatchSize * 10000000);
	    seed += (batchNumber * maxBatchNumber * 10000);
	    seed += jobNumber;
	    
		//System.out.println("Random number seed used " + seed);
		CBSimulation cbsim = new CBSimulation(seed, args);
		  
		cbsim.start();
		    
		long steps;
		double time;
	    
	    do
	    {            
	           
	    	if (!cbsim.schedule.step(cbsim)) 			// performs the step, and if return is false, stops looping.
	    		break;
	            
	    	steps = cbsim.schedule.getSteps();		// How many steps have been performed?
	        time = cbsim.schedule.getTime();			// retrieve the current time in the simulation.  
	            
	        //System.out.println(cbsim + " currently at step: " + steps);
	            
	        final long timeNow = System.currentTimeMillis();
	            
	        if (timeNow - startTime >= (timeOut * 3600000))
	        	break;
	            
	     } while(steps <= (simulationSteps + 4));					// stopping condition. 
	        
	    	cbsim.finish();
	    	
	 }
	   
	 /**
     * This method is called to start the simulation. 
     * the run parameters are copied to the results output directory.
     * The run output file is created ready for writing.
     */
    public void start()
    {
    	super.start();									// call supertype's start method.
    	
    	//System.out.println("In main on CBSimulation class");
    	
    	// need to read the parameter file
    	readParameters(true);
    	
        // And I want to write the random number seed used for this run - for record keeping 
    	
		PrintWriter dataOutput;
		
		new File(resultFilePath + "/" + description + "/" + runFilePath).mkdirs();
		File runSeedFile = new File(resultFilePath + "/" + description + "/" + runFilePath + "/simRunSeed");
		System.out.println("Creating output file " + runSeedFile.getName());
		
		String paramCopyFile = resultFilePath + "/" + description + "/" + runFilePath + "/" + paramCopyName;
		copyFile(xmlFileLocation, paramCopyFile);
		
		try{
			dataOutput = new PrintWriter(runSeedFile);
			dataOutput.print(seed);
			dataOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	   	// I'll need to instantiate the data store here 
    	// I'll schedule it from the constructor...
        dataStore = new DataStore(this);
        //System.out.println("Created DataStore object " + dataStore);
        
    }
    
    /*
     * This method is called to wind down the simulation. 
     */
    public void finish()
    {
    	super.finish();
    	
    	System.out.println("Terminating the simulation");
    	
    	/* 
    	 * write CSV and XML output from simulation
    	 */
    	File csvDataFile = new File(resultFilePath + "/" + description + "/" + runFilePath + "/simOutputData.csv");
    	System.out.println("Output written to " + csvDataFile);
    	
		//File xmlDataFile = new File(resultFilePath + "/" + description + "/Results/" + runFilePath + "/simOutputData.xml");
		
		PrintWriter dataOutput;
		
		try{
			dataOutput = new PrintWriter(csvDataFile);
	    	dataOutput.print(dataStore.compileTableToString());
	    	dataOutput.close();
	    	
		//	dataStore.compileXMLOutput(xmlDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Run took " + (System.currentTimeMillis() - startTime) + " milliseconds");
		
    }
    
    public long getStartTime()
    {
    	return startTime;
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
    
	public void addToSimulationScheduleRepeating(double time, int order, Steppable generator, double interval)
	{
		this.setStopper(cbsim.schedule.scheduleRepeating(time, order, generator, interval));
	} 
	
	public void readParameters(boolean ic)
	{
		
		try{
				// open the xmlFileLocation to read input
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(xmlFileLocation));
			
				Element setupComponent = (Element) doc.getElementsByTagName("simulation_setup").item(0);
				processSetupParams(setupComponent);
			
				// locate the <cistromes> tag
				Element cistromesComponent = (Element) doc.getElementsByTagName("cistromes").item(0);
				
				// process this into details of individual cistromes
				processCistromesComponent(cistromesComponent);
				
				// we'll pass the Cistrome components on to the Cell Object to make sense of...
				//if(instantiateCell)
				//{
					//We'll set up a Cell object to hold our Cistrome(s)
					//System.out.println("Cistrome data sources are " + sources);
					cell = new Cell(sources, this);
					//System.out.println("Created cell object " + cell);
				//}
				
				//System.out.println("Fetching cistrome data from " + cell);
				ArrayList<Cistrome> cistromeList = cell.getCistromeList();
				
				for (int c = 0; c< cistromeList.size(); c++)
				{	
					Cistrome thisCistrome = ((Cistrome) cistromeList.get(c));
					
					//System.out.println("Cistrome " + c);
					thisCistrome.setName(names.get(c));
					//System.out.println("Name: " + names.get(c));
					thisCistrome.setBranchingRate(branchingRates.get(c));
					//System.out.println("Branching rate: " + branchingRates.get(c));
					thisCistrome.setInitialReds(numsInitialReds.get(c));
					//System.out.println("Initial reds: " + numsInitialReds.get(c));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	public void processSetupParams(Element setupComponents)
	{
		simulationSteps = Integer.parseInt(setupComponents.getElementsByTagName("simulationSteps").item(0).getTextContent());
		
		System.out.println("Number of simulation steps to take " + simulationSteps);
		
		resultFilePath = setupComponents.getElementsByTagName("resultFilePath").item(0).getTextContent();
		description = setupComponents.getElementsByTagName("description").item(0).getTextContent();
		timeOut = Integer.parseInt(setupComponents.getElementsByTagName("timeOut").item(0).getTextContent());
	
	}
	
	public void processCistromesComponent(Element cistromesComponent)
	{
		
		NodeList allCistromes = cistromesComponent.getElementsByTagName("cistrome");
		
		for(int i = 0; i < allCistromes.getLength(); i++)
		{
			
				Element cistromeComponent = (Element) allCistromes.item(i);
				processCistromeParameters(cistromeComponent);
		
		}
	
	}
	
	public void processCistromeParameters(Element cistromeComponent)
	{
		
		name = cistromeComponent.getElementsByTagName("name").item(0).getTextContent();
		System.out.println("Cistrome name found " + name);
		names.add(name);
		System.out.println(names);
		source = cistromeComponent.getElementsByTagName("source").item(0).getTextContent();
		System.out.println("Cistrome data source found " + source);
		sources.add(source);
		branchingRate = Integer.parseInt(cistromeComponent.getElementsByTagName("branchingRate").item(0).getTextContent());
		System.out.println("Cistrome branching rate found " + branchingRate);
		branchingRates.add(branchingRate);
		initialReds = Integer.parseInt(cistromeComponent.getElementsByTagName("initialReds").item(0).getTextContent());
		System.out.println("Cistrome initial reds found " + initialReds);
		numsInitialReds.add(initialReds);
		
	}
	
	public void copyFile(String source, String dest)
	{
		
		try{
			File sf = new File(source);
			File df = new File(dest);
			InputStream in = new FileInputStream(sf);
			OutputStream out = new FileOutputStream(df);
			
			byte[] buf = new byte[1024];
			int len;
			
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			System.out.println("File " + source + " copied to " + dest);
			
		} catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + "in the specified directory");
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public Cell getCell()
	{
		return cell;
	}
	
	public DataStore getDataStore()
	{
		return dataStore;
	}
	
	public int getSimulationSteps()
	{
		return simulationSteps;
	}
	
	public boolean getUsingGUI()
	{
		return this.useGUI;
	}
	
	public void setUsingGUI(boolean gui)
	{
		useGUI = gui;
	}
	
}