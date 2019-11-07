///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program3
// Files:            Change.java, ChangeSet.java, Document.java,
//					 EmptyQueueException.java, EmptyStackException.java, 
//					 ErrorType.java, QueueADT.java, Repo.java, RepoCopy.java,
//					 SimpleQueue.java, SimpleStack.java, StackADT.java,
//					 User.java, VersionControlApp.java, VersionControlDb.java
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

import java.util.Scanner;

/**
 * Version control application. Implements the command line utility
 * for Version control.
 * 
 * @author Heikal Badrulhisham
 *
 */
public class VersionControlApp {

	/* Scanner object on input stream. */
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * An enumeration of all possible commands for Version control system.
	 */
	private enum Cmd {
		AU, DU,	LI, QU, AR, DR, OR, LR, LO, SU, CO, CI, RC, VH, RE, LD, AD,
		ED, DD, VD, HE, UN
	}

	/**
	 * Displays the main menu help. 
	 */
	private static void displayMainMenu() {
		System.out.println("\t Main Menu Help \n" 
				+ "====================================\n"
				+ "au <username> : Registers as a new user \n"
				+ "du <username> : De-registers a existing user \n"
				+ "li <username> : To login \n"
				+ "qu : To exit \n"
				+"====================================\n");
	}

	/**
	 * Displays the user menu help. 
	 */
	private static void displayUserMenu() {
		System.out.println("\t User Menu Help \n" 
				+ "====================================\n"
				+ "ar <reponame> : To add a new repo \n"
				+ "dr <reponame> : To delete a repo \n"
				+ "or <reponame> : To open repo \n"
				+ "lr : To list repo \n"
				+ "lo : To logout \n"
				+ "====================================\n");
	}

	/**
	 * Displays the repo menu help. 
	 */
	private static void displayRepoMenu() {
		System.out.println("\t Repo Menu Help \n" 
				+ "====================================\n"
				+ "su <username> : To subcribe users to repo \n"
				+ "ci: To check in changes \n"
				+ "co: To check out changes \n"
				+ "rc: To review change \n"
				+ "vh: To get revision history \n"
				+ "re: To revert to previous version \n"
				+ "ld : To list documents \n"
				+ "ed <docname>: To edit doc \n"
				+ "ad <docname>: To add doc \n"
				+ "dd <docname>: To delete doc \n"
				+ "vd <docname>: To view doc \n"
				+ "qu : To quit \n" 
				+ "====================================\n");
	}

	/**
	 * Displays the user prompt for command.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered command (Max: 2 words).
	 */
	private static String[] prompt(String prompt) {
		System.out.print(prompt);
		String line = scnr.nextLine();				//Command
		String[]words = line.trim().split(" ", 2);	//Segmented command
		return words;
	}

	/**
	 * Displays the prompt for file content.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered content.
	 */
	private static String promptFileContent(String prompt) {
		System.out.println(prompt);
		String line = null;				//A line in file content
		String content = "";			//File content
		while (!(line = scnr.nextLine()).equals("q")) {
			content += line + "\n";
		}
		return content;
	}

	/**
	 * Validates if the input has exactly 2 elements. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput2(String[] input) {
		if (input.length != 2) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input has exactly 1 element. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput1(String[] input) {
		if (input.length != 1) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the Cmd equivalent for a string command. 
	 * @param strCmd The string command.
	 * @return The Cmd equivalent.
	 */
	private static Cmd stringToCmd(String strCmd) {
		try {
			return Cmd.valueOf(strCmd.toUpperCase().trim());
		}
		catch (IllegalArgumentException e){
			return Cmd.UN;
		}
	}

	/**
	 * Handles add user. Checks if a user with name "username" already exists; 
	 * if exists the user is not registered. 
	 * @param username The user name.
	 * @return USER_ALREADY_EXISTS if the user already exists, SUCCESS otherwise.
	 */
	private static ErrorType handleAddUser(String username) {
		if (VersionControlDb.addUser(username) != null) {
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USERNAME_ALREADY_EXISTS;
		}
	}

	/**
	 * Handles delete user. Checks if a user with name "username" exists; if 
	 * does not exist nothing is done. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleDelUser(String username) {
		User user = VersionControlDb.findUser(username); //User to delete
		if (user == null) {
			return ErrorType.USER_NOT_FOUND;
		}
		else {
			VersionControlDb.delUser(user);
			return ErrorType.SUCCESS;
		}
	}

	/**
	 * Handles a user login. Checks if a user with name "username" exists; 
	 * if does not exist nothing is done; else the user is taken to the 
	 * user menu. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleLogin(String username) {
		User currUser = VersionControlDb.findUser(username); //User to login
		if (currUser != null) {
			System.out.println(ErrorType.SUCCESS);
			processUserMenu(currUser);				//Go to user menu
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USER_NOT_FOUND;
		}
	}

	/**
	 * Handles user menu command to add a repo
	 * 
	 * @param u	The logged in user
	 * @param repoName	The name of the repo
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void userMenuAdd(User u, String repoName)
	{
		if(u == null || repoName == null)
			throw new IllegalArgumentException();
		
		//Print this if the repo name already exist
		if(VersionControlDb.findRepo(repoName) != null)
			System.out.println(ErrorType.REPONAME_ALREADY_EXISTS);
		else//Else add a new repo and subscribe user to it
		{
			VersionControlDb.addRepo(repoName, u);
			u.subscribeRepo(repoName);
			System.out.println(ErrorType.SUCCESS);
		}
	}
	
	/**
	 * Handles user menu command to delete a repo
	 * 
	 * @param u	The logged in user
	 * @param repoName	The name of the repo
	 * * @throws IllegalArgumentException for any null argument
	 */
	private static void userMenuDelete(User u, String repoName)
	{
		if(u == null || repoName == null)
			throw new IllegalArgumentException();
		
		Repo delRepo = VersionControlDb.findRepo(repoName); //Repo to delete
		
		//Print message and exit if repo doesn't exist
		if(delRepo == null)	
		{
			System.out.println(ErrorType.REPO_NOT_FOUND);
			return;
		}
		
		//Delete repo if the user is the admin
		if(delRepo.getAdmin().equals(u))
		{
			VersionControlDb.delRepo(delRepo);
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.ACCESS_DENIED);
	}
	
	/**
	 * Handles user menu command to open a repo
	 * 
	 * @param u	The logged in user
	 * @param repoName	The name of the repo
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void userMenuOpen(User u, String repoName)
	{
		if(u == null || repoName == null)
			throw new IllegalArgumentException();
		
		//Print this and exit method if the repo does not exist
		if(VersionControlDb.findRepo(repoName) == null)
		{
			System.out.println(ErrorType.REPO_NOT_FOUND);
			return;
		}
		
		//Open the repo if the user is subscribed to it
		if(u.isSubRepo(repoName))
		{
			//Check out a working copy if the user doesn't have one
			if(u.getWorkingCopy(repoName) == null)
				u.checkOut(repoName);
			
			System.out.println(ErrorType.SUCCESS);
			//Go to repo menu
			processRepoMenu(u, repoName);					
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.REPO_NOT_SUBSCRIBED);
	}
	
	/**
	 * 	Handles repo menu command to subscribe a user to a repo
	 * 
	 * @param u	Logged in
	 * @param repoName	Name of the repo
	 * @param uSubName	Name of user to subscribe
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void repoMenuSubscribe(User u, String repoName, 
															String uSubName)
	{
		if(u == null || repoName == null || uSubName == null)
			throw new IllegalArgumentException();
		
		Repo repo = VersionControlDb.findRepo(repoName);	//Repo by the name
		User uSub = VersionControlDb.findUser(uSubName);	//User to subscribe
		
		//Print this if user is not found
		if(uSub == null)	
		{
			System.out.println(ErrorType.USER_NOT_FOUND);
			return;
		}
		
		//Subscribe the user if the current user is the admin
		if(repo.getAdmin().equals(u))
		{
			uSub.subscribeRepo(repoName);
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.ACCESS_DENIED);
	}
	
	/**
	 * Handles repo menu command to edit a document in a repo
	 * 
	 * @param u	Logged in user
	 * @param repoName	Name of the repo
	 * @param docName	Name of the document
	 */
	private static void repoMenuEdit(User u, String repoName, String docName)
	{
		if(u == null || repoName == null || docName == null)
			throw new IllegalArgumentException();
		
		RepoCopy rC = u.getWorkingCopy(repoName);	//Repo copy by the name
		Document doc = rC.getDoc(docName);			//Document to edit
		
		//If the document exists get document content from user
		if(doc != null)
		{
			//Document content:
			String content = promptFileContent("Enter the file "
								+ "content and press q to quit:");
			
			doc.setContent(content);
			u.addToPendingCheckIn(doc, Change.Type.EDIT, repoName);
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.DOC_NOT_FOUND);
	}
	
	/**
	 * Handles repo menu command to add a document to a repo
	 * 
	 * @param u	Logged in user
	 * @param repoName	Name of the repo
	 * @param docName	Name of the document
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void repoMenuAdd(User u, String repoName, String docName)
	{
		if(u == null || repoName == null || docName == null)
			throw new IllegalArgumentException();
		
		RepoCopy rC = u.getWorkingCopy(repoName);	//Repo copy by the name
		
		//If the document does not exists get file content from user
		if(rC.getDoc(docName) == null)
		{
			//Content of file to add
			String content = promptFileContent("Enter the file "
											+ "content and press q to quit:");
			
			//New document to add
			Document doc = new Document(docName ,content, repoName);
			rC.addDoc(doc);
			u.addToPendingCheckIn(doc, Change.Type.ADD, repoName);
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.DOCNAME_ALREADY_EXISTS);
	}
	
	/**
	 * Handles repo menu command to delete a document from a repo
	 * 
	 * @param u	Logged in user
	 * @param repoName	Name of the repo
	 * @param docName	Name of the document
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void repoMenuDelete(User u, String repoName, String docName)
	{
		if(u == null || repoName == null || docName == null)
			throw new IllegalArgumentException();
		
		RepoCopy rC = u.getWorkingCopy(repoName);	//Repo by the name
		Document docDel = rC.getDoc(docName);		//Document to delete
		
		//Delete document if it exists
		if(docDel != null)	
		{
			rC.delDoc(docDel);
			u.addToPendingCheckIn(docDel, Change.Type.DEL, repoName);
		
			System.out.println(ErrorType.SUCCESS);
		}
		else
			System.out.println(ErrorType.DOC_NOT_FOUND);
	}
	
	/**
	 * Handles repo menu command to display a document in a repo
	 * 
	 * @param u	Logged in user
	 * @param repoName	Name of the repo
	 * @param docName	Name of the document
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void repoMenuDisplay(User u, String repoName, String docName)
	{
		if(u == null || repoName == null || docName == null)
			throw new IllegalArgumentException();
		
		//Document to display:
		Document doc = u.getWorkingCopy(repoName).getDoc(docName);
		
		if(doc != null)
			System.out.println(doc.toString());
		else
			System.out.println(ErrorType.DOC_NOT_FOUND);
	}
	
	/**
	 * Handles repo menu command to review check ins to a repo
	 * 
	 * @param u	Logged in user
	 * @param repoName	Name of the repo
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void repoMenuReviewCheckIns(User u, String repoName)
	{
		if(u == null || repoName == null)
			throw new IllegalArgumentException();
		
		Repo repo = VersionControlDb.findRepo(repoName);	//Current repo
		
		//Print this if there are no pending check ins
		if(repo.getCheckInCount() == 0)	
		{
			System.out.println(ErrorType.NO_PENDING_CHECKINS);
			return;
		}
		
		//Print this if the user is not the admin
		if(!repo.getAdmin().equals(u))
		{
			System.out.println(ErrorType.ACCESS_DENIED);
			return;
		}
		
		//ChangeSet to store changes from approved check ins
		ChangeSet approved = new ChangeSet(repoName);	
		
		//Ask admin's approval of check ins one by one 
		while(repo.getCheckInCount() > 0)
		{
			ChangeSet cS = repo.getNextCheckIn(u);	//Current check in
			
			System.out.print(cS.toString() + "\n");
			//Stores admin's answer to a check in:
			String[] answer = 
					prompt("Approve changes? Press y to accept: ");
			
			if(answer.length == 1 && answer[0].equals("y"))
			{
				while(cS.getChangeCount() > 0)
				{
					Change ch = cS.getNextChange();	//A change in the check in
					approved.addChange(ch.getDoc(), ch.getType());
				}
			}
		}
		//Implement Changes of approved check ins
		System.out.println(repo.approveCheckIn(u, approved));
	}
	
	/**
	 * Processes the main menu commands.
	 * 
	 */
	public static void processMainMenu() {

		String mainPrompt = "[anon@root]: ";	//Prompt text
		boolean execute = true;	  				//Whether to continue with menu

		while (execute) {
			String[] words = prompt(mainPrompt);	//Segmented command
			Cmd cmd = stringToCmd(words[0]);		//Function part of command

			switch (cmd) {
			//Add a user
			case AU:	
				if (validateInput2(words)) {
					System.out.println(handleAddUser(words[1].trim()));
				}
				break;
			//Delete a user
			case DU:
				if (validateInput2(words)) {
					System.out.println(handleDelUser(words[1].trim())); 
				}
				break;
			//Login a user
			case LI:
				if (validateInput2(words)) {
					System.out.println(handleLogin(words[1].trim()));
				}
				break;
			//Display help
			case HE:
				if (validateInput1(words)) {
					displayMainMenu();
				}
				break;
			//Quit
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Processes the user menu commands for a logged in user.
	 * @param logInUser The logged in user.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processUserMenu(User logInUser) {

		if (logInUser == null) {
			throw new IllegalArgumentException();
		}

		//Prompt text
		String userPrompt = "[" + logInUser.getName() + "@root" + "]: ";
		boolean execute = true;				//Whether to continue with menu

		while (execute) {

			String[] words = prompt(userPrompt);	//Segmented command
			Cmd cmd = stringToCmd(words[0]);		//Function part of command

			switch (cmd) {
			//Add a repo
			case AR:	
				if (validateInput2(words)) 
					userMenuAdd(logInUser, words[1]);
				break;
			//Delete a repo	
			case DR:	
				if (validateInput2(words)) 
					userMenuDelete(logInUser, words[1]);
				break;
			//List user's subscribed repos	
			case LR:	
				if (validateInput1(words)) 
					System.out.println(logInUser.toString());
				break;
			//Open a repo	
			case OR:	
				if (validateInput2(words)) 
					userMenuOpen(logInUser, words[1]);
				break;
			//Log the user out and return to main menu prompt	
			case LO:	
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			//Prints user menu help	
			case HE:	
				if (validateInput1(words)) {
					displayUserMenu();
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}
		}
	}

	/**
	 * Process the repo menu commands for a logged in user and current
	 * working repository.
	 * @param logInUser The logged in user. 
	 * @param currRepo The current working repo.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processRepoMenu(User logInUser, String currRepo) {

		if (logInUser  == null || currRepo == null) {
			throw new IllegalArgumentException();
		}

		String repoPrompt = "["+ logInUser.getName() + "@" + currRepo + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(repoPrompt);	//Segmented command
			Cmd cmd = stringToCmd(words[0]);		//Function part of command

			switch (cmd) {
			//Subscribes an existing user to a repo
			case SU:	
				if (validateInput2(words)) 
					repoMenuSubscribe(logInUser, currRepo, words[1]);
				break;
			//List all documents in the local working set	
			case LD:	
				if (validateInput1(words)) 
				{
					RepoCopy rC = logInUser.getWorkingCopy(currRepo);
					System.out.println(rC.toString());
				}
				break;
			//Edits a document in the local working copy	
			case ED:	
				if (validateInput2(words)) 
					repoMenuEdit(logInUser, currRepo, words[1]);
				break;
			//Adds a document to local working copy	
			case AD:	
				if (validateInput2(words)) 
					repoMenuAdd(logInUser, currRepo, words[1]);
				break;
			//Deletes a document from local working copy	
			case DD:	
				if (validateInput2(words)) 
					repoMenuDelete(logInUser, currRepo, words[1]);
				break;
			//Display document in the local working copy	
			case VD:	
				if (validateInput2(words)) 
					repoMenuDisplay(logInUser, currRepo, words[1]);
				break;
			//Check in changes to a repo	
			case CI:	
				if (validateInput1(words)) 
					System.out.println(logInUser.checkIn(currRepo));
				break;
			//Checks out latest version of current repo	
			case CO:	
				if (validateInput1(words))
					System.out.println(logInUser.checkOut(currRepo));
				break;
			//Review check ins	
			case RC:	
				if (validateInput1(words)) 
					repoMenuReviewCheckIns(logInUser, currRepo);
				break;
			//Display version history	
			case VH:	
				if (validateInput1(words)) 
					System.out.println(
					VersionControlDb.findRepo(currRepo).getVersionHistory());
				break;
			//Revert the repo to the previous version	
			case RE:	
				if (validateInput1(words)) 
				{
					System.out.println(
					VersionControlDb.findRepo(currRepo).revert(logInUser));
				}
				break;
			//Prints repo help menu	
			case HE:	
				if (validateInput1(words)) {
					displayRepoMenu();
				}
				break;
			//Quits repo menu prompt and go back to user menu prompt	
			case QU:	
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}
		}
	}

	/**
	 * The main method. Simulation starts here.
	 * @param args Unused
	 */
	public static void main(String []args) {
		try {
			processMainMenu(); 
		}
		// Any exception thrown by the simulation is caught here.
		catch (Exception e) {
			System.out.println(ErrorType.INTERNAL_ERROR);
			// Uncomment this to print the stack trace for debugging purpose.
			//e.printStackTrace();
		}
		// Any clean up code goes here.
		finally {
			System.out.println("Quitting the simulation.");
		}
	}
}
