import persistence.Manager;

/**
 * This class is the main entry point of the program.
 * This class is used to execute the program.
 * 
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public class ExecuteMe {
	public static void main(String[] args) {
		String sourceFile ="./resources/input.txt";
		String outputFile ="./resources/output.txt";

		new Manager(sourceFile, outputFile);
	}
}
