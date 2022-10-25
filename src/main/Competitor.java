package main;

import java.util.ArrayList;

import common.Level;

/**
 * This class is an interface implemented by classes: 
 * a) FeatherWeightCompetitor. 
 * b) HeavyWeightCompetitor.
 * c) LightWeightCompetitor.
 * 
 * After Reachingout to my lecturer Dr.Gabbay, he permitted 
 * the use of interfaces for my solution.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public interface Competitor {
	
	// All methods below are ment to be implemented in subclasses.
	public int getNumber();
	
	public void setNumber(int number) ;
	
	public Name getName();
	
	public void setName(Name name);
	
	public void setScores(ArrayList<Integer> scores);
	
	public Level getLevel() ;
	
	public void setLevel(Level level);
	
	public int getAge() ;
	
	public void setAge(int age);
		
	public double average();
		
	public String arrToString();
		
	public ArrayList<Integer> getScoreArray();

	public String getShortDetails();
	
	/*
	* The two abstract methods below have different 
	* implementations in the classes below
	* FeatherWeightCompetitor,HeavyWeightCompetitor,
	* LightWeightCompetitor.
	*/ 	
	public double getOverallScore();
		
	public String getFullDetails();
}
