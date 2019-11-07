///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 VersionControlApp.java
// File:             ChangeSet.java
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
 * Represents a set of change(s) made to a repository.
 * 
 * @author Heikal Badrulhisham
 */
public class ChangeSet 
{
	
	/* Queue of changes contained within the change set. */
	private final  QueueADT<Change> changes;
	
	/* The name of the repository to which the changes belongs. */
	private final String repoName;
	
	/**
	 * Constructs a change set object. 
	 * @param reponame The name of the repository.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public ChangeSet(String repoName) 
	{
		if(repoName == null)
			throw new IllegalArgumentException();
		
		this.repoName = repoName;
		this.changes = new SimpleQueue<Change>();
	}
	
	/**
	 * Adds (queues) a new change to the change set.
	 * @param doc The doc to which the change was done.
	 * @param type The type of the change.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public void addChange(Document doc, Change.Type type) 
	{
		if(doc == null || type == null)
			throw new IllegalArgumentException();
		
		this.changes.enqueue(new Change(doc, type));
	}
	
	/**
	 * Returns the repository's name to which this change list belongs.
	 * @return The repository's name.
	 */
	public String getReponame() {
		return this.repoName;
	}
	
	/**
	 * Returns and removes the next change from the change set.
	 * @return The next change if present, null otherwise.
	 * @throws EmptyQueueException 
	 */
	public Change getNextChange() 
	{
    	try 
    	{
			return this.changes.dequeue();
		} 
    	catch (EmptyQueueException e) 
		{
			return null;
		}
	}
	
	/**
	* Returns the count of changes contained in the change set.
	* @return The count of changes.
	*/
	public int getChangeCount() 
	{
    	return this.changes.size();
	}
	
	/**
	 * Returns the String representation of the ChangeSet
	 */
	public String toString() {
		return this.changes.toString();	
	}
}
