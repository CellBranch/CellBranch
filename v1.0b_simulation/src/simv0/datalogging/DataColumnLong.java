package simv0.datalogging;

import java.util.ArrayList;
import java.util.Iterator;

/* 
 * this class creates an ArrayList of Integers in which to hold
 * the integer values of certain simulation outputs
 */
public class DataColumnLong
{
	String title;
	ArrayList<Long> data = new ArrayList<Long>();;	
	
	/*
	 * give the column a name (for the output table)
	 */
	public DataColumnLong(String name)
	{
		title = name;
	}
	
	/* 
	 * make sure sure the current value of this property gets stored
	 */
	public void logValue(long value)
	{
		data.add(value);
	}
	
	/*
	 * provide an iterator so we can loop over the entire column
	 */
	public Iterator getIterator()
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
		for(Long i : data)
			stringData.add(Long.toString(i));
		
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

