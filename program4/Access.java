///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 FileSystemMain.java
// File:             Access.java
// Semester:         CS367 Summer 2015
//
// Author:           Heikal Badrulhisham
// Email:            badrulhisham@wisc.edu
// CS Login:         heikal
// Lecturer's Name:  Chelsea Stapleton
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     
// Email:            
// CS Login:         
// Lecturer's Name: 
// Lab Section:      
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//
// Persons:          
//
// Online sources:   
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * The Access class represents an access having a user and an accessType 
 * 
 * @author Heikal Badrulhisham
 */
public class Access 
{
	
	private User user;			//User associated with this access
	private char accessType;	//Access type
	
	/**
	 * Constructs an Access instance with user and access type initialized
	 * @param user 			The user with the access
	 * @param accessType	Access type
	 * @throws IllegalArgumentException for any null argument
	 */
	public Access(User user, char accessType) 
	{
		if(user == null || (Character) accessType == null)
			throw new IllegalArgumentException();
		
		this.user = user;
		this.accessType = accessType;
	}
	
	/**
	 * Returns user
	 * @return User associated with this Access
	 */
	public User getUser() 
	{
		return this.user;
	}
	
	/**
	 * Returns access type
	 * @return	Access type
	 */
	public char getAccessType() 
	{
		return this.accessType;
	}

	/**
	 * Sets access type
	 * @param accessType Access type
	 * @throws IllegalArgumentException for any null argument
	 */
	public void setAccessType(char accessType) 
	{
		if((Character) accessType == null)
			throw new IllegalArgumentException();
		
		this.accessType = accessType;
	}
	
	/**
	 * Returns String representation of this Access.
	 * @return String representation of this Access.
	 */
	@Override
	public String toString() 
	{
		return (user.getName() + ":" + accessType);
	}
	
}
