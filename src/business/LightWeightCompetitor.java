package business;

import java.util.ArrayList;

import common.Level;
import common.Name;

/**
* This class "is a" competitor since it implements the Competitor interface.
* LightWeightCompetitors are entitled to headGears. 
* This helps Sets LightWC apart from other types of competitors.
*
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
*/

public class LightWeightCompetitor implements Competitor {

	private int age = 0;
	private int number = 0;
	private Name name = null;
	private Level level = null;
	private boolean headGear = false;
	private ArrayList<Integer> scores = new ArrayList<Integer>(5);
	
	public LightWeightCompetitor(int age, int number, Name name, Level level, boolean headGear, ArrayList<Integer> scores) {
		this.age = age;
		this.number = number;
		this.name = name;
		this.level = level;
		this.headGear = headGear;
		this.scores = scores;
	}
	
	//get the number value
	public int getNumber() {return number;}
	//set the number value
	public void setNumber(int number) {this.number = number;}
	//get the name object
	public Name getName() {return name;}
	//get the enum object level
	public Level getLevel() {return level;}
	//helper method to calculate the mean of competitor's score
	public double average() {
		double accumulator = 0;
		for(Integer value : scores) { accumulator += value; }
		accumulator = accumulator/scores.size();
		return accumulator;
	}
	//helper method to turn an array of numbers to a string
	public String arrToString() {
		String str="";
		for(Integer value : scores) { str += "," + value.toString(); }
		//remove the first item of the string
		return str.substring(1);
	}
			
	//helper method that returns an array of integers
	public ArrayList<Integer> getScoreArray(){return scores;}

	public String getShortDetails() {
		String nameInitials, shortDetail;
		int overallScore = (int) getOverallScore();
		//capitalize the 1st letter of competitors first, middle & lastname
		nameInitials = new StringBuilder()
							.append(name.getFirstName().toUpperCase().charAt(0))
							.append(name.getLastName().toUpperCase().charAt(0))
							.append(name.getMiddleName().toUpperCase().charAt(0))
							.toString();
		shortDetail = "CN " + number + " (" + nameInitials + ") " + 
					  "has overall score " + overallScore + ".";
		return shortDetail;
	}

	@Override
	public double getOverallScore() {
		double mean, score=0;
		mean = average();
		if (mean <= 3) {
			switch (level) {
			case NOVICE:
			score = mean + 1;
			break;
			case INTERMEDIATE:
			score = (int) (mean + 0.5);
			break;
			case ADVANCED:
			score = (int) (mean - 0.5);
			break;
			case EXPERT:
			score = mean - 1;
			break;
			}
		}else if(headGear) {
			score = mean + 1;
		}else {score = mean;}
		return score; 
	}
	
	@Override
	public String getFullDetails() {
		 String fullDetail = "Competitor Number " + number + "," + " with name " + name.getFullName();
				fullDetail += " is a " + age + " year old " + level + " at this competition. ";
		   		fullDetail += "\r\n" + name.getFirstName() + " recieved these scores : " + arrToString();
		   		fullDetail += "\r\n" + "This gives him an overall score of " + getOverallScore() + ".";
		   		fullDetail += "\r\n" + "LightWeight competitor's " + "are entitled to headGears, i " 
		   				   + checkForHeadGear() + " one here with me.";  
		return fullDetail;
	}
	
	public String checkForHeadGear() {return (headGear) ? "have" : "don't have";}
	
	@Override
	public void setScores(ArrayList<Integer> scores) {
		this.scores = scores;
	}

}
