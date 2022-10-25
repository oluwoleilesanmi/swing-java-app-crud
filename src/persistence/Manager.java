package persistence;

import business.Competitor;
import business.CompetitorList;
import presentation.GraphicalUserInterface;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class implements methods that write to a file.
 * This is the "view" architecturally. 
 * This class calls the GUI.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public class Manager {
	private CompetitorList competitors = null;
	private FileWriter writer = null;
	
	/** 
	 * constructor creates instances of FileWriter and CompetitorList
	 * @param readFromUri is the dir of file to be read
	 * @param writeToUri is the dir of file to be written to
	 */
	public Manager(String readFromUri, String writeToUri) {
		this.competitors = new CompetitorList(readFromUri);
		try {this.writer = new FileWriter(writeToUri);} 
		catch (IOException e) {e.printStackTrace();}
		// Create the GUI and pass the instance of this class to it.
		new GraphicalUserInterface(this);
	}
	
	/** 
	 * The UI table stores its values as string object
	 * @param uri for file location
	 */
	public void updateMainAndTableDs(Object uniqueId, Object scores) {
		competitors.updateMainStore(uniqueId, scores);
		competitors.updateTableStore(uniqueId, scores);
	}
	
	/** 
	 * change the file where competitor class reads data from 
	 * @param uri for file location
	 */
	public void setCompetitors(String uri) {
		this.competitors = new CompetitorList(uri);
	}
	
	/** 
	 * return the datastructure of table GUI
	 * @return array of objects
	 */
	public Object[][] getTableStore() {
		return competitors.getTableStore();
	}
	/** 
	 * change the file where competitor class reads data from 
	 * @param uri for file location
	 */
	public CompetitorList getCompetitors() {
		return this.competitors;
	}
	
	/** 
	 * change the file that the filewriter writes to
	 * @param uri for file location
	 */
	public void setWriter(String uri) {
		try {
			this.writer = new FileWriter(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * arranges data in tabular form using columns as shown below
	 * Competitor 	*	Level   *   Scores   *  Overall   
	 * writes table to text file
	 */
	private void writeTable(int num) throws IOException {
		//write table heading to file
		if(competitors.isValid(num)) {
			writer.write(String.format("%-28s %-15s %-10s %-25s %-10s\r\n", "Competitor", "Level", "Scores", "Type", "Overall"));
			ArrayList<Competitor> list = competitors.getList();
			for (Competitor instance : list) {
				String scores = "";
				for (int n : instance.getScoreArray()) { 
					scores += n + " ";
				}
				String type = instance.getClass().getSimpleName();
				String concatNumName = instance.getNumber() +" "+ instance.getName().getFullName();
				//write competitor instance data in tablular format one at a time
				writer.write(String.format("%-28s %-15s %-10s %-25s %-10s\r\n", concatNumName, instance.getLevel(), scores, type, instance.getOverallScore())); 
			}	
		} else {System.out.println("Competitor with number " + num + " cannot be found and Table cannot be written to file");}
		writer.write("\r\n");
	}
	
	/** 
	 * calls methods that write reports to a single file
	 * closes file
	 * @param competitorNum for user number input 
	 */
	public void createCompleteReport(int competitorNum) {
		try {
			writeTable(competitorNum);
			writeCompetitorFullDetails(competitorNum);
			writeCompetitorShortDetails(competitorNum);
			writeSummaryStats(competitorNum);
			writer.close();
		} catch (FileNotFoundException e){
			 System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * calls methods that write reports to User Interface
	 * @param competitorNum for user number input 
	 */
	public String createCompleteReportUI(int competitorNum) {
		String concat = "";
		if(competitors.isValid(competitorNum)) {
			concat = writeCompetitorFullDetailsUI(competitorNum);
			concat += printCompetitorShortDetailsUI(competitorNum);
		    concat += writeSummaryStatsUI(competitorNum);
		}else {return ("Number is Invalid");}
		return concat;
	}
	
	/** 
	 * calls method that writes full detail about specific competitor to file
	 * @param num is for storing competitor number
	 */
	private void writeCompetitorFullDetails(int num) throws IOException {
		Competitor competitor = null;
		//check for validity of competitor number
		if(competitors.isValid(num)) {
			competitor = competitors.getCompetitor(num);
			String str = "Full details for " + num + ":" + "\r\n";
			writer.write(str + competitor.getFullDetails() + "\r\n" + "\r\n");
		} else {System.out.println("Competitor with number " + num + " cannot be found and full details cannot be written to file");}
	}
	
	/** 
	 * calls method that writes full detail about specific competitor to UI
	 * @param num is for storing competitor number
	 */
	private String writeCompetitorFullDetailsUI(int num){
		Competitor competitor = null;
		//check for validity of competitor number
		if(competitors.isValid(num)) {
			competitor = competitors.getCompetitor(num);
			String str = "Full details for " + num + ":" + "\n";
			return (str + competitor.getFullDetails() + "\n" + "\n");
		} else {return ("Competitor with number " + num + " cannot be found and full details cannot be written to file");}
	}
	
	/** 
	 * calls methods that write short detail about specific competitor to file
	 * @param num is for storing competitor number 
	 */
	private void writeCompetitorShortDetails(int num) throws IOException {
		Competitor competitor = null;
		//check for validity of competitor number
		if(competitors.isValid(num)) {
			competitor = competitors.getCompetitor(num);
			String str = "Short details for " + num + ":" + "\r\n";
			writer.write(str + competitor.getShortDetails() + "\r\n" + "\r\n");
		} else {System.out.println("Competitor with number " + num + " cannot be found and short details cannot be written to file");}
	}
	
	/** 
	 * calls methods that print short detail to console
	 * @param num is for processing competitor number 
	 */
	public void printCompetitorShortDetails(int num)  {
		Competitor competitor = null;
		//check for validity of competitor number
		if(competitors.isValid(num)) {
			competitor = competitors.getCompetitor(num);
			String str = "Short details for " + num + ":" + "\n";
			System.out.println(str + competitor.getShortDetails() + "\n");
		} else {System.out.println("Competitor with number " + num + " cannot be found and short details cannot be printed to console");}
	}
	
	/** 
	 * calls methods that print short detail to UI
	 * @param num is for processing competitor number 
	 * @return string containing short detail.
	 */
	public String printCompetitorShortDetailsUI(int num)  {
		Competitor competitor = null;
		//check for validity of competitor number
		if(competitors.isValid(num)) {
			competitor = competitors.getCompetitor(num);
			String str = "Short details for " + num + ":" + "\n";
			return (str + competitor.getShortDetails() + "\n" + "\n");
		} else {
			return ("Competitor with number " + num + " cannot be found, short details cannot be printed");
		}
	}
	
	/** 
	 * calls methods that write summary report only to file 
	 */
	private void writeSummaryStats(int num) throws IOException {
		HashMap<String,Integer> map = competitors.scoreFrequency();
		if(competitors.isValid(num)) {
			//construct string the is displayed at the  of the statistical section of the file output
			String top  = "STATISTICAL" + "\r\n" + "There are a total of ";
				   top += (competitors.size() + competitors.getInvalidCompetitorsProcessed()) + " competitors ";
				   top += competitors.size() + " are VALID " + "& ";
				   top += competitors.getInvalidCompetitorsProcessed() +" are INVALID. " + "\r\n";
				   top += "The person with the highest overallscore is ";
				   top += competitors.getName().getFullName() + " with a score of " + competitors.maxOverall() ;
				   top += "." + "\r\n" + "The minimum score for all competitors is " + competitors.minOverall() + "." ;
				   top += "\r\n" + "The following individual scores were awarded:" + "\r\n";
			
			String score = "Score :", frequency = "Frequency :";
			for (HashMap.Entry<String, Integer> item : map.entrySet()) {
				score += " " + item.getKey();
				frequency += " " + item.getValue();
			}
			//construct string the is displayed at the bottom of the statistical section of the file output
			String bottom = "\r\n" + "From the above we can see that ";
				   bottom += "the lowest score by any competitor is ";
				   bottom += getMapasArray(map)[0].getKey()+" and "+ "highest is ";
				   bottom += getMapasArray(map)[map.size()-1].getKey() + "."; 
			
			writer.write(top + score + "\r\n" + frequency + bottom + "\r\n");	
			
		} else {System.out.println("Competitor with number " + num + " cannot be found so summary cannot be printed to console");}
		
	}
	
	/** 
	 * calls methods that write summary report to UI 
	 */
	private String writeSummaryStatsUI(int num) {
		HashMap<String,Integer> map = competitors.scoreFrequency();
		if(competitors.isValid(num)) {
			//construct string the is displayed at the  of the statistical section of the file output
			String top  = "STATISTICAL" + "\n" + "There are a total of ";
				   top += (competitors.size() + competitors.getInvalidCompetitorsProcessed()) + " competitors ";
				   top += competitors.size() + " are VALID " + "& ";
				   top += competitors.getInvalidCompetitorsProcessed() +" are INVALID. " + "\n";
				   top += "The person with the highest overallscore is ";
				   top += competitors.getName().getFullName() + " with a score of " + competitors.maxOverall() ;
				   top += "." + "\n" + "The minimum score for all competitors is " + competitors.minOverall() + "." ;
				   top += "\n" + "The following individual scores were awarded:" + "\n";
			
			String score = "Score :", frequency = "Frequency :";
			for (HashMap.Entry<String, Integer> item : map.entrySet()) {
				score += " " + item.getKey();
				frequency += " " + item.getValue();
			}
			//construct string the is displayed at the bottom of the statistical section of the file output
			String bottom = "\n" + "From the above we can see that ";
				   bottom += "the lowest score by any competitor is ";
				   bottom += getMapasArray(map)[0].getKey()+" and "+ "highest is ";
				   bottom += getMapasArray(map)[map.size()-1].getKey() + "."; 
			
			return (top + score + "\r\n" + frequency + bottom + "\n");	
			
		} else {return "Competitor with number " + num + " cannot be found so summary cannot be printed to console";}
		
	}
	
	/** 
	 * turns HashMap to array
	 * @param map is for HashMap to be processed 
	 * the code below was inspired by a solution to convert maps to string found in stackoverflow link below
	 * https://stackoverflow.com/questions/1936462/java-linkedhashmap-get-first-or-last-entry
	 */
	private Entry<Integer,String>[] getMapasArray(HashMap<String,Integer> map) {
		Set<Entry<String, Integer>> mapValues = map.entrySet();
        int maplength = mapValues.size();
        @SuppressWarnings("unchecked")
		Entry<Integer,String>[] test = new Entry[maplength];
        return mapValues.toArray(test);
	}
	
	/** 
	 * executable
	 */
	public void run() {
		int num = 0;
		Scanner input = null;
		
		//accept user input & check if its a valid number
		try {
	    	input = new Scanner(System.in); 
		    System.out.println("\n" +"Please Enter Number greater than or equal to zero: " + "\n");
		    num = input.nextInt();
		    //while user input is 0 or a negative number, request the user to re-enter a positive number
		    while(num < 0){
		    	System.out.println("Please Enter Number greater than or equal to zero: " + "\n");
		    	input.nextLine();
		    	num = input.nextInt();
		    }
		    //print to console the short details of the competitor using the number the user entered
		    printCompetitorShortDetails(num);
		    
        } catch (InputMismatchException exception) {
        	/* if the InputMismatchException is thrown it is caught in this block, the reason is
    		 * because the user has entered something other than a number.
    		 * request user to re-enter a number
    		 */
          while(true) {
        	  System.out.println("Please remember to enter number greater than or equal to zero next time:"+ "\n");
        	  input.nextLine();
        	  if (input.hasNextInt()) {
        		  num = input.nextInt();
        		  printCompetitorShortDetails(num);
        		  break;
        	  }
          }
        }
		//write a complete report using input provided by user just entered
		createCompleteReport(num); 
	}

}
