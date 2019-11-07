///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program4
// Files:            FileSystemMain.java, SimpleFileSystem.java, SimpleFile.java
//					 SimpleFolder.java, User.java, Access.java, Extension.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates and uses a SimpleFileSystem to represent and process information 
 * about a file system.
 * 
 * @author Heikal Badrulhisham
 */
public class FileSystemMain 
{
	//The file system
	private static SimpleFileSystem sys;
	//For printing to the console
	private static final PrintStream pr = new PrintStream(System.out);
	//Scanner for console input
	private static final Scanner scn = new Scanner(System.in);
	
	/**
	 * Launches the simulation. Reads a text file whose name is passed as a
	 * command line argument to load user, file and folder data onto the
	 * file system. The prompts user for commands and executes them
	 * @param args Command line arguments (should be just one)
	 */
	public static void main(String[] args) 
	{
		//Check if there is exactly one command-line argument
		if(args.length != 1)
		{
			pr.println("Usage: java FileSystemMain FileName");
			System.exit(1);
		}
		
		//Load data onto the file system
		loadData(args[0]);
		
		//Starts simulation
		sys.reset();
		String[] com;					//User's command + arguments
		boolean goOn = true;			//Whether to continue getting commands
		
		//Execute command
		do
		{
			pr.print(prompt());
			com = scn.nextLine().toLowerCase().split(" ");
			
			goOn = processCom(com);
		}
		while(goOn);
		
		//Exit simulation
		scn.close();
		System.exit(0);
	}	
	
	/**
	 * Loads data from file with the given name onto the file system
	 * @param dataFileName	Name of file from which data is loaded
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void loadData(String dataFileName)
	{
		if(dataFileName == null)
			throw new IllegalArgumentException();
		
		File dataFile = new File(dataFileName);				//Data file
		Scanner fileScn = null;								//Reads data file
		ArrayList<String> data = new ArrayList<String>();	//Holds data
		
		//Hook up scanner to file
		try 
		{
			fileScn = new Scanner(dataFile);	
		} 
		catch (FileNotFoundException e) 
		{
			System.exit(1);
		}
		
		//Store lines of data file into the list
		while(fileScn.hasNextLine())   
			data.add(fileScn.nextLine());
		
		ArrayList<User> users = new ArrayList<User>();		//List of users
		SimpleFolder root;									//Root folder
		User admin = new User("admin");						//Admin user
		users.add(admin);	
		
		//Instantiate root folder
		root = new SimpleFolder(data.get(0), "", null, admin);
		
		//Load users
		String[] userLine = data.get(1).split(", ");	//Stores names of users
		
		for(String name : userLine)
			users.add(new User(name));
		
		//Initialize file system
		sys = new SimpleFileSystem(root, users);
		
		//Load folders and files into the file system
		for(int i = 2; i < data.size(); i++)
		{
			//Holds file/folder names in a path:
			String[] fData = data.get(i).split("/");
			loadFileFolder(fData, 1, admin);
			sys.currLoc = sys.root;
		}
	}
	
	/**
	 * Helper method for loadData()
	 * @param data Lines of file/folder information
	 * @param i	Index on the line of file/folder information
	 * @param u Admin user
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void loadFileFolder(String[] data, int i, User u)
	{
		if(data == null || (Integer) i == null || u == null)
			throw new IllegalArgumentException();
		
		if(i >= data.length)
			return;
		
		//Add a file if a file name + content are encountered
		if(i == data.length - 1 && data[i].contains(" "))
		{
			//Name(with extension) and content of the new file:
			String fileName = data[i].substring(0, data[i].indexOf(" "));
			String fileContent = data[i].substring(data[i].indexOf(" ") + 1);
			
			//Create file
			sys.addFile(fileName, fileContent);
			
			//Initialize accesses
			fileName = fileName.substring(0, fileName.indexOf("."));
			initializeAccesses(sys.currLoc.getFile(fileName), sys.currUser);
			
			return;
		}
		
		//Add a new folder
		if(sys.currLoc.getSubFolder(data[i]) == null)
		{
			sys.mkdir(data[i]);
			initializeAccesses(sys.currLoc.getSubFolder(data[i]), sys.currUser);
		}
		
		//Add the next folder in the path to the newly added folder
		sys.currLoc = sys.currLoc.getSubFolder(data[i]);
		loadFileFolder(data, ++i, u);
	}
	
	/**
	 * Returns prompt with the name of the current user and the name of current 
	 * folder location.
	 * @return prompt
	 */
	private static String prompt()
	{
		return sys.currUser.getName() + "@" + "CS367$ ";
	}
	
	/**
	 * Helper method for processCom. Checks if the number of arguments is 
	 * correct for a command.
	 * @param com	Command + arguments
	 * @return true if the number of arguments is correct, else false
	 * @throws IllegalArgumentException for any null argument
	 */
	private static boolean checkNumArgs(String[] com)
	{
		if(com == null)
			throw new IllegalArgumentException();
		
		//For commands that require no arguments
		if(com[0].equals("reset") || com[0].equals("pwd") ||
		   com[0].equals("ls") || com[0].equals("uinfo"))
		{
			if(com.length != 1)
			{
				pr.println(prompt() + "No Argument Needed");
				return false;
			}	
		}
		//For commands that require 1 argument
		else if(com[0].equals("u") || com[0].equals("cd") || com[0].equals("rm") 
				|| com[0].equals("mkdir"))
		{
			if(com.length != 2)
			{
				pr.println(prompt() + "One Argument Needed");
				return false;
			}
		}
		//For commands that require 3 arguments
		else if(com[0].equals("sh"))
		{
			if(com.length != 4)
			{
				pr.println(prompt() + "Three Arguments Needed");
				return false;
			}
		}
		//For commands that require 1 argument with an optional second
		else if(com[0].equals("mkfile"))
		{
			if(com.length < 2 || com.length > 3)
			{
				pr.println(prompt() + "One Argument Needed");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Processes user commands. Returns true or false to control the loop 
	 * prompting for commands in the main method
	 * @param com Command + arguments
	 * @return true, if 'x' is not chosen, if it is return false
	 * @throws IllegalArgumentException for any null argument
	 */
	private static boolean processCom(String[] com)
	{
		if(com == null)
			throw new IllegalArgumentException();
		
		//Check if the number of arguments is correct for the command.
		//If not exit this round of command processing
		if(!checkNumArgs(com))
			return true;
		
		//Reset location and user
		if(com[0].equals("reset"))
		{
			sys.reset();
			pr.println(prompt() + "Reset done");
		}
		//Prints the present working directory
		else if(com[0].equals("pwd"))
		{
			pr.println(sys.getPWD());
		}
		//List out files and folders accessible to the user in the present 
		//working directory
		else if(com[0].equals("ls"))
		{
			sys.printAll();
		}
		//Change current user	
		else if(com[0].equals("u"))
		{
			if(!sys.setCurrentUser(com[1]))
				pr.println(prompt() + "User " + com[1] + " does not exist");
		}
		//Prints out user information (admin only)
		else if(com[0].equals("uinfo"))
		{
			if(!sys.printUsersInfo())
				pr.println(prompt() + "Insufficient privileges");
		}
		//Change directory
		else if(com[0].equals("cd"))
		{
			if(!sys.moveLoc(com[1]))
				pr.println(prompt() + "Invalid location passed");
		}
		//Remove an immediate child or folder of the current location
		else if(com[0].equals("rm"))
		{
			handleRM(com[1]);
		}
		//Create a folder in the current location
		else if(com[0].equals("mkdir"))
		{
			sys.mkdir(com[1]);
			pr.println(prompt() + com[1] + " added");
		}
		//Create a new file in the curent location
		else if(com[0].equals("mkfile"))
		{
			handleMKFILE(com);
		}
		//Grant a user access to a file/folder
		else if(com[0].equals("sh"))
		{
			handleSH(com[1], com[2], com[3]);
		}
		//Exit. Return false to stop the loop in the main method
		else if(com[0].equals("x"))
		{
			return false;
		}
		else if(com[0].length() > 0)
		{
			pr.println(prompt() + "Invalid command");
		}
		
		return true;
	}
	
	/**
	 * Helper method to handle sh command to add access to a file/folder
	 * @param fName	File/folder name 
	 * @param usrName User to give access to
	 * @param accType Access type. Passed here as a String to check that
	 * 			      only one character is passed.
	 * @throws IllegalArgumentException for any null arguments
	 */
	private static void handleSH(String fName, String usrName, String accType)
	{
		if(fName == null || usrName == null || accType == null)
			throw new IllegalArgumentException();
		
		//File/folder to which access is to be given
		SimpleFile file = sys.currLoc.getFile(fName.toLowerCase());
		SimpleFolder folder = sys.currLoc.getSubFolder(fName.toLowerCase());
		
		//Reject if no file/folder with the name exists
		if(file == null && folder == null)
		{
			pr.println(prompt() + "Invalid file/folder name");
			return;
		}
		//Reject if the current user is not the owner of the file/folder
		if(!sys.currUser.getFiles().contains(file) &&
				!sys.currUser.getFolders().contains(folder))
		{
			pr.println(prompt() + "Insufficient privilege");
			return;
		}
		//Reject if permission type is invalid
		if(accType.length() != 1 && accType != "r" && accType != "w")
		{
			pr.println(prompt() + "Invalid permission type");
			return;
		}
		//Reject if username is invalid
		if(sys.containsUser(usrName) == null)
		{
			pr.println(prompt() + "Invalid user");
			return;
		}
		
		//Add access to the file/folder
		sys.addUser(fName, usrName, accType.charAt(0));
		
		pr.println(prompt() + "Privilege granted");
	}
	
	/**
	 * Helper method to handle mkfile command to create a file in the current
	 * location
	 * @param com command + arguments
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void handleMKFILE(String[] com)
	{
		if(com == null)
			throw new IllegalArgumentException();
		
		//If file name is invalid
		if(!isFileName(com[1]))
		{
			pr.println(prompt() + "Invalid filename");
			return;
		}
		
		//Create file without content
		if(com.length == 2)
			sys.addFile(com[1], "");
		//Create file with content
		if(com.length == 3)
			sys.addFile(com[1], com[2]);
		
		pr.println(prompt() + com[1] + " added");
	}
	
	/**
	 * Helper method to handle RM command to remove a file/folder
	 * @param fName Name of file/folder to delete
	 * @throws IllegalArgumentException for any null argument
	 */
	private static void handleRM(String fName)
	{
		if(fName == null)
			throw new IllegalArgumentException();
		
		if(sys.remove(fName))
			pr.println(prompt() + fName + " removed");
		else
		{
			//IF file/folder name is invalid
			if(!sys.containsFileFolder(fName))
				pr.println(prompt() + "Invalid name");
			//If the user is not allowed to remove
			else
				pr.println(prompt() + "Insufficient privilege");
		}
	}
	
	/**
	 * Helper method to verify that a file/folder name is a file name
	 * @param name File/folder name
	 * @return	true if it is, false if it is not
	 * @throws IllegalArgumentException for any null argument
	 */
	private static boolean isFileName(String name)
	{
		if(name == null)
			throw new IllegalArgumentException();
		
		name = name.toLowerCase();
		
		//Check if there is a valid extension
		try
		{
			Extension.valueOf(name.substring(name.length() - 3));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Helper method to initialize accesses to a newly created file during 
	 * loading the file system. Gives 'r' access to non-admin users
	 * @param f	File to process
	 * @param admin The user not given 'r' access
	 * @throws IllegalArgumentException for any null argument 
	 */
	private static void initializeAccesses(SimpleFile f, User admin)
	{
		if(f == null || admin == null)
			throw new IllegalArgumentException();
		
		//Give 'r' access to on-admin users
		for(User u : sys.users)
		{
			if(!u.equals(admin))
				f.addAllowedUser(new Access(u, 'r'));
		}
	}
	
	/**
	 * Helper method to initializes accesses to a newly created folder when 
	 * loading the file system. Gives 'r' to non-admin users
	 * @param f	Folder to process
	 * @param admin The user not given 'r' access
	 * @throws IllegalArgumentException for any null argument 
	 */
	private static void initializeAccesses(SimpleFolder f, User admin)
	{
		if(f == null || admin == null)
			throw new IllegalArgumentException();
		
		//Give 'r' access to everyone else
		for(User u : sys.users)
		{
			if(!u.equals(admin))
				f.addAllowedUser(new Access(u, 'r'));
		}
	}
}
