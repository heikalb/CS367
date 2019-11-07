///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 AmazonStore.java
// File:             InsufficientCreditException.java
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
 * An InsufficientCreditException is thrown when a purchase is attempted
 * when the product's price exceeds a user's remaining credit.
 * 
 * @author Heikal Badrulhisham
 */

public class InsufficientCreditException extends Exception 
{
	/**
	 * This class's serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an InsufficientCreditException without a message
	 */
	public InsufficientCreditException() 
	{
	}
	
	/**
	 * Constructs an InsufficientCreditException with a message
	 * 
	 * @param arg0	Message
	 */
	public InsufficientCreditException(String arg0) 
	{
		super(arg0);
	}
}
