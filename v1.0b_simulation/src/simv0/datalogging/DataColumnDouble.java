package simv0.datalogging;

import java.util.ArrayList;
import java.util.Iterator;

/* 
 * this class creates an ArrayList of Doubles in which to hold
 * the double values of certain simulation outputs
 */
public class DataColumnDouble
{
	String title;
	ArrayList<Double> data = new ArrayList<Double>();
	
	/*
	 * give the column a name (for the output table)
	 */
	public DataColumnDouble(String name)
	{
		title = name;
	}
	
	/* 
	 * make sure sure the current value of this property gets stored
	 */
	public void logValue(double value)
	{
		data.add(value);
	}
	
	/*
	 * provide an iterator so we can loop over the entire column
	 */
	public Iterator<Double> getIterator()
	{
		return data.iterator();
	}
	
	/*
	 * Converts the data into a string format and returns an iterator 
	 * on that data instead.
	 */
	public Iterator getStringIterator()
	{
		ArrayList<String> stringData = new ArrayList<String>();
		for(Double d : data)
			stringData.add(Double.toString(d));
		
		return stringData.iterator();
	}
		
	/* 
	 * accessor methods so DataStore can read the actual data stored
	 */
	public String getTitle()
	{	return title;	}
	
	public int getLength()
	{	return data.size();		}
	
}

