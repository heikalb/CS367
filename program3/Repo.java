///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 VersionControlApp.java
// File:             Repo.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository which stores and tracks changes to a collection of 
 * documents.
 * 
 * @author Heikal Badrulhisham
 */
public class Repo {
	
	/* The current version of the repo. */
	private int version;
	
	/* The name of the repo. It's a unique identifier for a repository. */
	private final String repoName;
	
	/* The user who is the administrator of the repo. */
	private final User admin;
	
	/* The collection(list) of documents in the repo. */
	private final List<Document> docs;
	
	/* The check-ins queued by different users for admin approval. */
	private final QueueADT<ChangeSet> checkIns;
	
	/* The stack of copies of the repo at points when any check-in was applied. */
	private final StackADT<RepoCopy> versionRecords; 

	/**
	 * Constructs a repo object.
	 * @param admin The administrator for the repo.
	 * @param reponame The name of the repo.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public Repo(User admin, String repoName) 
	{
		if(admin == null || repoName == null)
			throw new IllegalArgumentException();
		
		this.admin = admin;
		this.repoName = repoName;
		this.docs = new ArrayList<Document>();
		this.checkIns = new SimpleQueue<ChangeSet>();
		this.version = 0;
		this.versionRecords = new SimpleStack<RepoCopy>();
		versionRecords.push(new RepoCopy(this.repoName, this.version, 
																	this.docs));
	}
	
	/**
	 * Return the name of the repo.
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.repoName;
	}
	
	/**
	 * Returns the user who is administrator for this repository.
	 * @return The admin user.
	 */
	public User getAdmin() {
		return this.admin;
	}
	
	/**
	 * Returns a copy of list of all documents in the repository.
	 * @return A list of documents.
	 */
	public List<Document> getDocuments() {
		return new ArrayList<Document>(this.docs);
	}
	
	/**
	 * Returns a document with a particular name within the repository.
	 * @param searchName The name of document to be searched.
	 * @return The document if found, null otherwise.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public Document getDocument(String searchName) {
    	if (searchName == null) {
			throw new IllegalArgumentException();
		}
    	
		for (Document d : this.docs) {
			if (d.getName().equals(searchName)) {
				return d;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the current version of the repository.
	 * @return The version of the repository.
	 */
	public int getVersion() {
		return this.version;
	}
	
	/**
	 * Returns the number of versions (or changes made) for this repository.
	 * @return The version count.
	 */
	public int getVersionCount() 
	{
		return this.versionRecords.size();
	}
	
	/**
	 * Returns the history of changes made to the repository. 
	 * @return The string containing the history of changes.
	 */
	public String getVersionHistory() 
	{
		return this.versionRecords.toString();
	}
	
	/**
	 * Returns the number of pending check-ins queued for approval.
	 * @return The count of changes.
	 */
	public int getCheckInCount() 
	{
		return this.checkIns.size();
	}
	
	/**
	 * Queue a new check-in for admin approval.
	 * @param checkIn The check-in to be queued.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void queueCheckIn(ChangeSet checkIn) 
	{
		if(checkIn == null)
			throw new IllegalArgumentException();
		
		this.checkIns.enqueue(checkIn);
	}
	
	/**
	 * Returns and removes the next check-in in the queue 
	 * if the requesting user is the administrator.
	 * @param requestingUser The user requesting for the change set.
	 * @return The checkin if the requestingUser is the admin and a checkin
	 * exists, null otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ChangeSet getNextCheckIn(User requestingUser) 
	{
		if(requestingUser == null)
			throw new IllegalArgumentException();
		
		if(!requestingUser.equals(this.admin))
			return null;
		
		try 
		{
			return this.checkIns.dequeue();
		} 
		catch (EmptyQueueException e) 
		{
			return null;
		}
	}
	
	/**
	 * Applies the changes contained in a particular checkIn and adds
	 * it to the repository if the requesting user is the administrator.
 	 * Also saves a copy of changed repository in the versionRecords.
	 * @param requestingUser The user requesting the approval.
	 * @param checkIn The checkIn to approve.
	 * @return ACCESS_DENIED if requestingUser is not the admin, SUCCESS 
	 * otherwise.
	 * @throws EmptyQueueException 
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType approveCheckIn(User requestingUser, ChangeSet checkIn) 
	{
		if(requestingUser == null || checkIn == null)
			throw new IllegalArgumentException();
		
		//Return ACCESS_DENIED if requestingUser is not the admin
		if(!requestingUser.equals(this.admin))
			return ErrorType.ACCESS_DENIED;
		
		//Go through the change set and implement each change 
		
		boolean changesMade = false;	//Whether changes were made
		while(checkIn.getChangeCount() > 0)
		{
			Change ch = checkIn.getNextChange();	//A change in the change set
			makeChanges(ch);						//Change documents list
			changesMade = true;
		}
		
		if(changesMade)
		{
			this.version++;
			this.versionRecords.push(new RepoCopy(this.repoName, this.version, 
					this.docs));
		}
		
		return ErrorType.SUCCESS;
	}
	
	/**
	 * Reverts the repository to the previous version if present version is
	 * not the oldest version and the requesting user is the administrator.
	 * @param requestingUser The user requesting the revert.
	 * @return ACCESS_DENIED if requestingUser is not the admin, 
	 * NO_OLDER_VERSION if the present version is the oldest version, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType revert(User requestingUser) 
	{
		if(requestingUser == null)
			throw new IllegalArgumentException();
		
		//Return ACCESS_DENIED if requestingUser is not the admin
		if(!requestingUser.equals(this.admin))
			return ErrorType.ACCESS_DENIED;
		
		//Try revert
		try 
		{
			this.versionRecords.pop();
			RepoCopy prev = versionRecords.peek();//The repo's previous version
			this.docs.clear();
			this.docs.addAll(prev.getDocuments());
			this.version--;
			return ErrorType.SUCCESS;
		} 
		catch (EmptyStackException e) 
		{
			return ErrorType.NO_OLDER_VERSION;
		}
	}
	
	/**
	 * Helper method to execute approved checked in changes
	 * 
	 * @param change Change to be executed
	 * @param docs	List of documents to be changed
	 * @return changed list of documents
	 * @throws IllegalArgumentException if any argument is null.
	 */
	private List<Document> makeChanges(Change change)
	{
		if(change == null)
			throw new IllegalArgumentException();
		
		Document chDoc = change.getDoc();		//Document to change
		Change.Type chType = change.getType();	//The type of the change
		
		//Add a document if it is not already in the list
		if(chType.equals(Change.Type.ADD) && 
									  this.getDocument(chDoc.getName()) == null)
			this.docs.add(chDoc);
		//Remove a document in the list
		else if(chType.equals(Change.Type.DEL) && 
									  this.getDocument(chDoc.getName()) != null)
			this.docs.remove(chDoc);
		//Edit a document in the list
		else if(chType.equals(Change.Type.EDIT) && 
									  this.getDocument(chDoc.getName()) != null)
		{
			//Remove existing document before replacing it with the edited one.
			//Document to replace
			Document replDoc = this.getDocument(chDoc.getName());	
			this.docs.remove(replDoc);
			this.docs.add(chDoc);
		}
		return docs;
	}
}
