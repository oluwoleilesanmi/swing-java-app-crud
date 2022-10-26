package business;

import java.util.ArrayList;

import common.Level;
import common.Name;

/**
 * This class is an interface implemented by classes: 
 * a) FeatherWeightCompetitor. 
 * b) HeavyWeightCompetitor.
 * c) LightWeightCompetitor.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public interface Competitor {
	
	// All methods below are meant to be implemented in subclasses.
	int getNumber();
	
	void setNumber(int number) ;
	
	Name getName();

	void setScores(ArrayList<Integer> scores);

	Level getLevel() ;

	double average();

	String arrToString();

	ArrayList<Integer> getScoreArray();

	String getShortDetails();


	/**
	 * below methods have different implementations in classes
	 * FeatherWeightCompetitor, HeavyWeightCompetitor, LightWeightCompetitor
	 */
	double getOverallScore();
		
	String getFullDetails();
}
