///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 VersionControlApp.java
// File:             User.java
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
 * Represents a user. Maintains the list of subscribed repositories, working
 * copy of the subscribed repositories and their changelist. 
 * 
 * @author Heikal Badrulhisham
 *
 */
public class User {
	
	/* The name of the user. It's a unique identifier for a user. */
	private final String userName;
	
	/* The list of names of the repositories to which the user is subscribed. */
	private final List<String> subRepos;
	
	/* The list of all pending check-ins not yet made by the user. */
	private final List<ChangeSet> pendingCheckIns;
	
	/* The list of all local working copies of the user. */
	private final List<RepoCopy> workingCopies;
	
	/**
	 * Constructor for User. 
	 * @param username The user name.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public User(String userName) 
	{
		if(userName == null)
			throw new IllegalArgumentException();
		
		this.userName = userName;
		this.subRepos = new ArrayList<String>();
		this.pendingCheckIns = new ArrayList<ChangeSet>();
		this.workingCopies = new ArrayList<RepoCopy>();
	}
	
	/**
	 * Returns the name of the user.
	 * @return the user name.
	 */
	public String getName() {
		return this.userName;
	}
		
	/**
	 * Returns a copy of list of subscribed repositories.
	 * @return The subscribed repo list.
	 */
	public List<String> getAllSubRepos() {
		return new ArrayList<String>(this.subRepos);
	}
	
    /**
     * Returns the working copy for a repository.
     * @param repoName The name of the repository.
     * @return The working copy if exists, null otherwise.
     * @throws IllegalArgumentException if any argument is null. 
     */
    public RepoCopy getWorkingCopy(String repoName) 
    {
    	if(repoName == null)
			throw new IllegalArgumentException();
    	
    	for(RepoCopy rC : this.workingCopies)
    	{
    		if(rC.getReponame().equals(repoName))
    			return rC;
    	}
    	
    	return null;
	}
    
	/**
	 * Subscribes the user to a repository. Adds a new repository to the 
	 * subscribed list. 
	 * @param repoName The name of the repository to subscribe.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void subscribeRepo(String repoName) {
		
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		
		if (!this.subRepos.contains(repoName)) {
			this.subRepos.add(repoName); 
		}
	}
	
	/**
	 * Un-subscribes the user from a repository. Deletes a repository from 
	 * the subscribed list.
	 * @param repoName The name of the repository to unsubscribe. 
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void unsubscribeRepo(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		this.subRepos.remove(repoName);
	}
	
	/**
	 * Checks if the user is subscribed to a particular repository.
	 * @param repoName The name of the repository to subscribe.
	 * @return True if the repository is subscribed, false otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public boolean isSubRepo(String repoName) {
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		return subRepos.contains(repoName);
	}
	
	/**
	 * Adds a new change (add, edit or delete) to the pending checkIn for the 
	 * repository. If a checkIn does not exits, a new checkIn is
	 * created.
	 * @param doc The document added, deleted or edited.
	 * @param type The type of change.
	 * @param repoName The name of the repository on which the change is done.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void addToPendingCheckIn(Document doc, Change.Type type, 
									String repoName) 
	{
		if (doc == null || type == null || repoName == null)
			throw new IllegalArgumentException();
		
		//If a checkIn already exists add to it, else, add a new one
		ChangeSet cS = this.getPendingCheckIn(repoName); //An existing check in
		
		if(cS != null)
			cS.addChange(doc, type); 
		else
		{
			cS = new ChangeSet(repoName);
			cS.addChange(doc, type);
			this.pendingCheckIns.add(cS);
		}
	}
	
	/**
	 * Returns the pending check-in for a repository.
	 * @param repoName The name of the repository.
	 * @return The pending check-in for the repository if exists, 
	 * null otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
    public ChangeSet getPendingCheckIn(String repoName) 
    {
    	if(repoName == null)
    		throw new IllegalArgumentException();
    	
    	for(ChangeSet cS : this.pendingCheckIns)
		{
			if(cS.getReponame().equals(repoName))
				return cS;
		}
    	
    	return null;
	}
    
    /**
     * Checks in or queues a pending checkIn into a repository and removes it
     * from the local pending CheckIns list.   
     * @param repoName The name of repository.
     * @return NO_LOCAL_CHANGES, if there are no pending changes for the
     * repository, SUCCESS otherwise.
     * @throws IllegalArgumentException if any argument is null. 
     */
	public ErrorType checkIn(String repoName) 									
	{
		if(repoName == null)
			throw new IllegalArgumentException();
		
		ChangeSet cS = this.getPendingCheckIn(repoName);//Change set to queue
		
		if(cS != null)
		{
			VersionControlDb.findRepo(repoName).queueCheckIn(cS);
			this.pendingCheckIns.remove(cS);
			return ErrorType.SUCCESS;
		}
		return ErrorType.NO_LOCAL_CHANGES;
	}
	
	/**
	 * Gets a latest version of the documents from the repository and puts
	 * them onto a working copy, if the user is currently subscribed to the
	 * repository. When the latest version is checked out, a new working copy 
	 * is created and existing one is deleted.
	 * @param repoName The name of the repository to check out.
	 * @return REPO_NOT_SUBSCRIBED if the repository is not subscribed, 
	 * SUCCESS otherwise. 
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType checkOut (String repoName) 
	{
		if(repoName == null)
			throw new IllegalArgumentException();
		
		//If the user is not subscribed to the repo
		if(!subRepos.contains(repoName))
			return ErrorType.REPO_NOT_SUBSCRIBED;
		
		//If the user is subscribed:
		//Delete an existing working copy of the repo
		RepoCopy delRC = this.getWorkingCopy(repoName);	//Existing repo copy
		this.workingCopies.remove(delRC);
				
		//Delete an existing pending checkin for that repo
		ChangeSet delCS = this.getPendingCheckIn(repoName);//Existing check in
		this.pendingCheckIns.remove(delCS);
		
		//Get a new working copy of the repo for the user.
		//Current version of the repo
		Repo curRepo = VersionControlDb.findRepo(repoName);
		//Copy of the current repo
		RepoCopy newRC = 
		new RepoCopy(repoName, curRepo.getVersion(), curRepo.getDocuments());
		//Add
		this.workingCopies.add(newRC);
		
    	return ErrorType.SUCCESS;
	}
		
	/**
	 * Returns the String representation of the user object
	 */
	public String toString() 
	{
		//String to return
		String str = "=================================== \n";
		//User information:
		str += "Username: " + this.userName + "\n"
				+ "-----------Repos------------------ \n";
		
		int count = 0;	//To regulate repo listing
		for (String r : this.subRepos) {
			str += ++count + ". " + r + "\n";
		}
		str += this.subRepos.size() + " repos(s) subscribed.\n"
				+ "===================================";
		return str;
	}
}
