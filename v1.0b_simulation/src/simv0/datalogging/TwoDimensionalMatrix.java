package simv0.datalogging;

import java.util.ArrayList;
import java.util.List;

/*
 * this class provides a 2D Matrix data structure 
 * 
 * used to store time series data for plotting
 * a summary graph at the end of the simulation
 */
public class TwoDimensionalMatrix {

	// implement as a 2D ArrayList...
	private ArrayList<Double> firstDimension;
	private ArrayList<Double> secondDimension;
	
	public TwoDimensionalMatrix() 
	{
		firstDimension = new ArrayList<Double>();
		secondDimension = new ArrayList<Double>();
	}
	
	public void add(double firstValue, double secondValue) 
	{
		firstDimension.add(firstValue);
		secondDimension.add(secondValue);
	}
	

	public double[][] toArray() 
	{
		return new double[][]{listToArray(firstDimension), listToArray(secondDimension)};
	}
	
	
	private double[] listToArray(List<Double> list) 
	{
		final double[] array = new double[list.size()];
		
		for (int index = 0; index < array.length; index++) {
			array[index] = list.get(index);
		}
		
		return array;
	}
	
}