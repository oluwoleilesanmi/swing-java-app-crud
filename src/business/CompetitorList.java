package business;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import common.Level;
import common.Name;

/**
* This class containing an arraylist of competitors.
* In this class the data from the file is pre-processed
* and passed into the array list of competitors.
* This class is the storage architecturally.
*
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
*/

public class CompetitorList {
	
	// Store(Main) contains objects of all competitors.
	private ArrayList<Competitor> list = null;
	// Store(Table) specifically for use in the UI table.
	private Object[][] tableStore = {};
	// Store(Volatile) to temporariliy keep values of table.
	private List<String> tempTableList = new ArrayList<String>();
	// Store(UniqueID) for creating uinque id's.
	private ArrayList<Integer> randomizer = new ArrayList<Integer>();
	// Count of invalid lines processed in input file.
	private int invalidLinesProcessed = 0; 
	
	/** 
	 * calls method that opens and reads text file
	 * @param fileName is the location where the file to be read is stored
	 */
	public CompetitorList(String fileName) {
		list = new ArrayList<Competitor>();
		fileToArray(fileName);
	}
	
	/** 
	 * generate a unique id for each competitor.
	 * @return an int random number.
	 */
	private int genUniqueId() {
		int listIndex = 0;
		Random rd = new Random();
		if(randomizer.size() > 0) {listIndex = rd.nextInt(randomizer.size());}
	    return randomizer.remove(listIndex);
	}
		
	/** 
	 * populate a list with a set of numbers
	 * @param s is the count of the numbers needed to populate list.
	 */
	private void populateRandomList(int s) {
		int index;
		for(index = 1; index <= s; index++) randomizer.add(index);
	}
	
	/** 
	 * set a unique number as the competitor id.
	 */
	private void makeCompetitorIdUnique() {
		// get the length of the good inputs in file.
		populateRandomList(list.size());
		int index = 0;
		while(index < list.size()) {
			list.get(index).setNumber(genUniqueId());
			index++;
		}
	}
	
	/** 
	 * get two dimensional array of objects.
	 * @return tableStore, two dimensional array for UI table.
	 */
	public Object[][] getTableStore() {return tableStore;}
	
	public void updateOverallScoreTableStore(int uid, int index) {
		Object string = getCompetitor(uid).getOverallScore();
		tableStore[index][8] = string;
	}
	
	public void updateTableStore(Object uniqueId, Object scores) {
		for (int i = 0; i < tableStore.length; i++) {
			// Turn Object to string, Then String to Integer, Compare. 
			if (Integer.parseInt(String.valueOf(tableStore[i][0])) == Integer.parseInt(String.valueOf(uniqueId))) {
				tableStore[i][4] = scores;
				updateOverallScoreTableStore(Integer.parseInt(String.valueOf(uniqueId)), i);
			}
		}
	}
	
	/** 
	 * update the main store for application
	 * @param uniqueId is the unique identifier, corresponding to that in store
	 * @param scores is an Object string
	 */
	public void updateMainStore(Object uniqueId, Object scores) {
		for (Competitor c : list) {
			if(Integer.parseInt(String.valueOf(uniqueId)) == c.getNumber()) {
				c.setScores(splitScore(String.valueOf(scores)));
			}
		}
	}
	
	/** 
	 * read file line by line and store each to an array
	 * @param fileName is the location where the file to be read is stored
	 */
	private void fileToArray(String fileName){
		try {
			File text = new File(fileName);
	        Scanner file = new Scanner(text);
	        while(file.hasNextLine()){
	            String line = file.nextLine();
	            //check file input line for wrong data format
	            if (!checkEachLine(line.split(",")) && line.length() != 0 ) {invalidLinesProcessed++;}
	            //ignored if blank line
	       	    //pass each line into Competitor object for processing
	            else if(line.length() != 0) {
	            	//serializer
	            	processLine(line);
	            	//create a temporary list to hold data for table.
	            	tempTableList.add(line);
	            }
	        }
	        makeCompetitorIdUnique();
	        processLineForObjArr();
	        file.close();
		} catch (FileNotFoundException e){
			 System.out.print("There is no file to process");
		}
	}
	
	/** 
	 * @return count of invalid lines processed in file.
	 */
	public int getInvalidCompetitorsProcessed() {
		return invalidLinesProcessed;
	}

	/** 
	 * @return size of array
	 */
	public int size() {
		return list.size();
	}
	
	/** 
	 * read file line by line
	 * turn each string to a fitting data type
	 * construct an Competitor object
	 */
	private void processLine(String line) {
			Competitor instance = null;
			String[] arr = line.split(",");
			
			int number = Integer.parseInt(arr[0].trim());
			String[] nameArr = splitName(arr[1].trim());
 			Name name = new Name(nameArr[0],nameArr[1],nameArr[2]);
			Level level = Level.valueOf(arr[2].trim());
			int age = Integer.parseInt(arr[3].trim());
			ArrayList<Integer> scores = splitScore(arr[4].trim());
			
			/* Identify the competitor type using attributes provided for input file.
			 * a) FeatherWeight Competitors can have a crew.
			 * b) LightWeight Competitors can have a headGear.
			 * c) HeavyWeight Competitors can have a performance enhancers.
			 * Create instance of competitor based on competitor type.
			 * There is need for this because of the different constructors.
			 */
			if(!arr[5].equals("nil")) {
				int crewCount = Integer.parseInt(arr[5].trim());
				instance = new FeatherWeightCompetitor(age, number, name, level, crewCount, scores);	
			}
			if(!arr[6].equals("nil")) {
				boolean headGear = Boolean.parseBoolean(arr[6].trim());
				instance = new LightWeightCompetitor(age, number, name, level, headGear, scores);
			}
			if(!arr[7].equals("nil")) {
				int enhancer = Integer.parseInt(arr[7].trim());
				instance = new HeavyWeightCompetitor(age, number, name, level, enhancer, scores);
			}
			list.add(instance);
	}
	
	/** 
	 * Create the datastructure for the table in the user interface
	 * The user interface component jtable uses a specific datastructure object[][]
	 */
	private void processLineForObjArr() {
		Object[][] arr = new Object[tempTableList.size()][8];
		int index = 0;
		for (String s : tempTableList) { 
			List<String> list = new ArrayList<String>(Arrays.asList(s.split(",")));
			list.set(0, String.valueOf(this.list.get(index).getNumber()));
			list.add(String.valueOf(this.list.get(index).getOverallScore()));
			arr[index] = list.toArray();
			index++;
		}
		tableStore = arr;
	} 
	
	/** 
	 * returns a list of OAICompetitor
	 * @return an array of objects OAICompetitor
	 */
	public ArrayList<Competitor> getList() {
		return list;
	}
	
	/** 
	 * split name into first, last & middle names using " " as delimeter
	 * @param str is for storing name string
	 * @return array containing firstname, lastname, middlename
	 */
	private String[] splitName(String str) {
		return str.split(" ");
	}
	
	/** 
	 * split number string using " " as delimeter, 
	 * turn a string of numbers into type String[], then 
	 * turn a type String[] to List<String>, then
	 * turn a type List<String> to ArrayList<Integer>
	 * @param str for storing string of scores
	 * @return ArrayList<Integer> to the caller
	 */
	private ArrayList<Integer> splitScore(String str) {
		List<String> list = new ArrayList<String>();
		// copy all the elements of string array to an ArrayList using the asList() method 
		list = Arrays.asList(str.split(" "));
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		for(String string: list){ arrList.add(Integer.parseInt(string)); }
		return arrList;
	}
	
	/** 
	 * find competitor with the highest overall score
	 * @return value of the highest scoring competitor
	 */
	public double maxOverall() {
		double max = 0;
		for(Competitor competitor:list) {
			if (max < competitor.getOverallScore()) {
				max = competitor.getOverallScore();
			}
		}
		return max;
	}
	
	/** 
	 * find competitor with the lowest overall score
	 * @return value of the lowest overall score
	 */
	public double minOverall() {
		double firstCompetitorOverallScore = list.get(0).getOverallScore();
		double min = firstCompetitorOverallScore;
		for(Competitor competitor:list) {
			if (min > competitor.getOverallScore()) {
				min = competitor.getOverallScore();
			}
		}
		return min;
	}
	
	/** 
	 * count the frequency of occurence of scores for each competitor
	 * store this frequency as a key, value pair(HashMap)
	 * @return HashMap object of score frequency
	 */
	public HashMap<String,Integer> scoreFrequency(){
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		for(Competitor instance : list) {
			ArrayList<Integer> scores = instance.getScoreArray();
			//sort array so we can always return scores in asending order
			Collections.sort(scores);
			for(Integer num :scores) {
				String key = num.toString();
				if (map.containsKey(key)) { 
		            Integer value = map.get(key);
		            value += 1;
		            map.put(key, value);
		        } else {
		        	map.put(key, 1);
		        }
			}
		}
		return map;
	}
	
	/** 
	 * check competitor number for validity
	 * @param competitorNum is for storing the list number
	 * @return boolean to signify num is valid or invalid
	 */
	public boolean isValid(int competitorNum) {
		for(Competitor instance:list) {
			if (instance.getNumber() == competitorNum) return true;
		}
		return false;
	}
	
	/** 
	 * find and return a competitor object
	 * @param competitorNum is for storing the list number
	 * @return Competitor instance object associated with competitorNum
	 */
	public Competitor getCompetitor(int competitorNum) {
		Competitor competitor = null;
		for(Competitor instance : list) {
			if(instance.getNumber() == competitorNum) {
				competitor = instance;
			}
		}
		return competitor;
	}
	
	/** 
	 * get the name of competitor with highest overall score 
	 * @return Name object of the competitor with the highest overall score
	 */
	public Name getName() {
		Competitor competitor = null;
		for(Competitor instance:list) {
			if(instance.getOverallScore() == maxOverall()) {
				competitor = instance;
			}
		}
		return competitor.getName();
	}
	
	/** 
	 * return the status of each line.
	 * @param arr is for storing the splited line of text. 
	 * @return boolean of the status of each line, line can either be malformed or wellformed.
	 */
	private boolean checkEachLine(String[] arr) {
		//if each line in file has more than 5 properties return false
		if (arr.length > 8) {
			System.out.println("Skipped line in file it has more than the required number of properties");
			return false;
		}
		//if each line in file has less than 5 properties return false
		if (arr.length < 8) {
			System.out.println("Skipped line in file it has less than the required number of properties");
			return false;
		}
		//if the NumberFormatException is thrown then we know an integer has not been entered for theses properties in file
		try {
			Integer.parseInt(arr[0].trim());
			Integer.parseInt(arr[3].trim());
		} catch(NumberFormatException e) {
			System.out.println("Skipped line in file with wrong type format");
			return false;
		}
		//if user has more than 3 names then the line is invalid, the requirement is to provide at most 3 names
		if (splitName(arr[1].trim()).length > 3 || splitName(arr[1].trim()).length < 3) {
			System.out.println("Skipped line in file with wrong number of provided names");
			return false;
		}
		return true;
	}
}
