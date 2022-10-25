package common;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This class is used to reduce hardcoding values in code.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public class Constant {
	
	public static final int DIMENSION = 10;
	public static final int WIDTH = 950;
	public static final int HEIGHT = 650;
	public static final String TITLE = "Boxing Game";
	public static final String SUBMIT = "Submit";
	public static final String FILETR = "AgeFilter";
	public static final String REFRESH = "Refresh";
	public static final String HINT = "Enter Number";
	public static final String EXIT = "FileWriter/Exit";
	public static final String ASCENDING_SORT = "Ascending Sort";
	public static final String HEADER_TEXT = "Factsheet & Results";
	public static final String DESCENDING_SORT = "Descending Sort";
	
	// Used to color border for UI component black.
	public static final Border border = BorderFactory.createLineBorder(Color.BLACK);
	
	// Column names for the table UI component.
	public static final String[] TABLE_COLUMN_NAMES = {"No.","Competitor", "Level","age","Scores","CrewCount","HeadGear","Enhancer","Overall"};
}
