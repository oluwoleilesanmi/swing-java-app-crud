package common;

/**
 * This class is used to create a name.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

public class Name {
	private String firstName = "";
	private String lastName = "";
	private String middleName = "";
	
	public Name(String firstName, String lastName, String middleName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
	}

	public String getFirstName() {return firstName;}

	public String getLastName() {return lastName;}

	public String getMiddleName() {return middleName;}
	
	public String getFullName() {return firstName +" "+ middleName +" "+ lastName;}
	
	
}
