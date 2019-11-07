///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program1
// Files:            Karma.java, Post.java, User.java, Reddit.java, 
//					 RedditDB.java.
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * This is a simulation of Reddit. Text files of user and post information 
 * are read and a database is created. There is main menu that prompts for 
 * commands. If the user chooses to have posts displayed, the user will be
 * brought to a submenu where submenu commands are prompted for.
 * 
 * Bugs: None known
 * 
 * @author Heikal Badrulhisham
 */
public class Reddit 
{
	public static void main(String[] args) 
	{
		RedditDB dB = new RedditDB();			//Reddit database.
		User user = null;						//The user currently logged in.
		User admin;								//Reddit administrator.
		final String ADMIN_NAME = "admin";		//Administrator's name.
		Scanner scnr = new Scanner(System.in);	//Scanner to read commands.
		String com;								//User command.
		
		//Command options
		final String SUMMARY = "s",	//Display summary.
		DELETE = "d ",				//Delete user whose name follows.
		LOGIN = "l ",				//Login user whose name follows.
		LOGOUT = "l",				//Logout current user.
		FRONT_PAGE = "f",			//Display front page.
		SUBREDDIT_POSTS = "r ",	//Display posts in the subreddit that follows.
		USER_POSTS = "u ",		//Display posts by user whose name follows.
		EXIT = "x";				//Exit Reddit.
		
		//Exit if there is no command line argument
		if(args.length < 1)
		{
			System.out.print("Usage: java Reddit <FileNames>");
			System.exit(0);
		}
		
		//Add "admin" as the first user.
		admin = dB.addUser(ADMIN_NAME);
		
		//Create database.
		createDB(args, dB);	
		
		//Prompt for main menu commands until user chooses to exit:
		//s: summary (admin only), d userName: delete user (admin only)
		//l userName: login user, l: logout user, f:display front page,
		//r subredditName: display posts in the subreddit,
		//u userName: display posts by the given user, x: exit
		do
		{
			displayPrompt(user);		//Display prompt for menu command.
			com = scnr.nextLine();		//Get menu command.
			
			//Display summary
			if(com.equals(SUMMARY) && user != null && user.equals(admin))
			{
				summary(dB);
			}
			//Delete user
			else if(com.startsWith(DELETE) && 
											user != null && user.equals(admin))
			{
				deleteUser(dB, com.substring(2));
			}
			//Login user
			else if(com.startsWith(LOGIN))
			{
				user = logIn(dB, com.substring(2), user);
			}
			//Logout current user
			else if(com.equals(LOGOUT))
			{
				user = logOut(user);
			}
			//Get front page
			else if(com.equals(FRONT_PAGE))
			{
				frontPage(dB, user);
			}
			//Get subreddit posts
			else if(com.startsWith(SUBREDDIT_POSTS))
			{
				frontPage(dB, user, com.substring(2));
			}
			//Get posts by a specified user
			else if(com.startsWith(USER_POSTS))
			{
				frontPage2(dB, user, com.substring(2) );
			}
			//Exit
			else if(com.equals(EXIT))
			{
			}
			//If none of the options
			else
			{
				System.out.println("Invalid command!");
			}
		}
		while(!com.equals(EXIT));
			
		System.out.print("Exiting to the real world...");	//Exit message.
	}
	/*
	 * Creates a Reddit database with user and post information from inputted
	 * text files.
	 * 
	 * @param users List of text files with user and post data
	 * @param dB	Reddit database to be filled with user and post information 
	 */
	public static void createDB(String[] users, RedditDB dB) 
	{
		//Scanners to read user files
		Scanner scnr = null;
		Scanner scnr2;
		
		String subredditLine;	//Line of subreddit information in a user file.
		String postInfoLine;	//Line of post information in a user file.
		
		//Add a new user to the database with subreddit and post data
		for(int i = 0; i < users.length; i++)
		{
			//Read file
			File userFile = new File(users[i]);
			try 
			{
				scnr = new Scanner(userFile);
			} 
			catch (FileNotFoundException e) 
			{
				System.out.print("File " + users[i] + " not found");
				System.exit(0);
			}
			
			//User name: convert to lower case and ignore file extension
			String userName = users[i].toLowerCase();	
			userName = userName.substring(0, userName.lastIndexOf("."));
			dB.addUser(userName);
			
			//Read post information for each user.
			//Read subreddit data.
			//Convert subredditlLine to all lower case and delete commas.
			subredditLine = scnr.nextLine();
			subredditLine = subredditLine.toLowerCase().replaceAll(",", "");
			
			for(Scanner sc = new Scanner(subredditLine); sc.hasNext();)
			{
				dB.getUsers().get(i+1).subscribe(sc.next());
			}
			
			
			//Read post data.
			//Delete commas from postInfoLine. Subreddit name to lower case
			while(scnr.hasNextLine())
			{
				postInfoLine = scnr.nextLine();
				postInfoLine = postInfoLine.replaceAll(",", "");
				scnr2 = new Scanner(postInfoLine);
				
				String s = scnr2.next().toLowerCase();	//Subreddit of the post.
				PostType tp = PostType.valueOf(scnr2.next()); 	//Post type
				String tl = scnr2.nextLine(); 					//Post title
				
				dB.getUsers().get(i+1).addPost(s, tp, tl);
			}
		}
	}
	
	/*
	 * Shows summary of all users
	 * 
	 * @param dB	Reddit database
	 */
	public static void summary(RedditDB dB)
	{
		User user;				//A user whose summary is to be displayed.
		String 	name;			//The user's name.
		int		linkKarma,		//User's link karma.
				commentKarma;	//User's comment karma.
		
		for(Iterator<User> itr = dB.getUsers().iterator(); itr.hasNext();)
		{
			user = itr.next();
			name = user.getName();
			linkKarma = user.getKarma().getLinkKarma();
			commentKarma = user.getKarma().getCommentKarma();
			
			System.out.println(name + "\t" + linkKarma + "\t" + commentKarma);
		}
	}

	/*
	 * Deletes user with the specified name.
	 * 
	 * @param dB		Reddit database
	 * @param toDelete	Name of user to be deleted
	 * 
	 */
	public static void deleteUser(RedditDB dB, String toDelete)
	{
		if(dB.delUser(toDelete))	//True if user is successfully deleted
		{
			System.out.println("User " + toDelete + " deleted");
		}
		else					//If the user by the given name does not exist
		{
			System.out.println("User " + toDelete + " not found");
		}
			
	}
	
	/*
	 * Logs in the user by that name. Print appropriate messages if another
	 * user is already logged in or the user by that name does not exist
	 * 
	 * @param dB	Reddit database
	 * @param name	Name of user to log in
	 * @param loggedIn	The user currently logged in
	 * 
	 * @return 	The user by the specified name if login is successful,
	 * 		   	if there is already another user logged in return that one,
	 * 			if user does not exist return null.
	 */
	public static User logIn(RedditDB dB, String name, User loggedIn)
	{
		User toLogIn = dB.findUser(name);	//User to log in.
		
		//If another user is already logged in.
		if(loggedIn != null)
		{
			System.out.println("User " + loggedIn.getName() + 
					" already logged in.");
			return loggedIn;
		}
		//If the user doesn't exist
		else if(toLogIn == null)
		{
			System.out.println("User " + name + " not found.");
			return loggedIn;
		}
		//If no else is logged in and the user exist
		else
		{
			System.out.println("User " + name + " logged in.");
			return toLogIn;
		}
	}

	/*
	 * Logs out the current user. Tells that no user is logged in if that is so
	 * 
	 * @param loggedIn	User currently logged in
	 * 
	 * @return 	null indicating that the user has been logged out or that
	 * 			there is no user logged in in the first place
	 */
	public static User logOut(User loggedIn)
	{
		if(loggedIn != null)	//The user is null if no one is logged in.
		{
			System.out.println("User " + loggedIn.getName() + " logged out.");
			return null;
		}
		else
		{
			System.out.println("No user logged in.");
			return null;
		}
			
	}

	/*
	 * Displays the front page and prompts for submenu options for each post
	 * 
	 * @param dB	Reddit database
	 * @param user	The user to whom the front page is relativized
	 */
	public static void frontPage(RedditDB dB, User user)
	{
		//To read in submenu commands:
		Scanner scnr = new Scanner(System.in);	
		//Access user's list of posts:
		Iterator<Post> itr = dB.getFrontpage(user).iterator(); 
		
		Post post;				//Post to be displayed.
		String com = "";		//Submenu command.
		boolean validCom;		//Whether a valid submenu command is received.
		final String EXIT ="x";	//Exit command.
		
		System.out.println("Displaying the front page...");
		
		//Show posts and prompt for submenu commands until user chooses to exit:
		//a:upvote post, z:downvote post, j:move to next post, x:exit submenu
		//else:invalid command
		while(itr.hasNext() && !com.equals(EXIT))
		{
			validCom = false;
			post = itr.next();
			
			while(!validCom)
			{
				System.out.println(post.getKarma() + "\t" + post.getTitle());
				displayPrompt(user);
				com = scnr.nextLine();
				validCom = subMenu(user, post, com);
			}
		}
		
		//Display there are no more posts to display
		if(!itr.hasNext() && !com.equals(EXIT))
		{
			System.out.println("No posts left to display.");
		}
		
		System.out.println("Exiting to the main menu...");	//Exit message.
	}

	/*
	 * Displays a subreddit front page and prompts for submenu options for each 
	 * post
	 * 
	 * @param dB		Reddit database
	 * @param user		The user to whom the front page is relativized
	 * @param subreddit	Subreddit of the frontpage posts
	 * 
	 */
	public static void frontPage(RedditDB dB, User user, String subreddit)
	{
		//To read submenu commands:
		Scanner scnr = new Scanner(System.in);	
		//To access user's List of posts:
		Iterator<Post> itr = dB.getFrontpage(user, subreddit).iterator();
		
		Post post;					//Post to be displayed.
		String com = "";			//Submenu command.
		boolean validCom = false;	//Whether a valid submenu command is given.
		final String EXIT ="x";		//Exit command.
		
		System.out.println("Displaying /r/" + subreddit + "...");
		
		//Show posts and prompt for submenu commands until user chooses to exit:
		//a:upvote post, z:downvote post, j:move to next post, x:exit submenu
		//else:invalid command
		while(itr.hasNext() && !com.equals(EXIT))
		{
			validCom = false;
			post = itr.next();
			
			while(!validCom)
			{
				System.out.println(post.getKarma() + "\t" + post.getTitle());
				displayPrompt(user);
				com = scnr.nextLine();
				validCom = subMenu(user, post, com);
			}
		}
		
		//Display when there are no more posts to display
		if(!itr.hasNext() && !com.equals(EXIT))
		{
			System.out.println("No posts left to display.");
		}
		
		System.out.println("Exiting to the main menu...");		//Exit message.
	}

	/*
	 * Displays a front page of posts by a user and prompts for submenu options
	 * for each post
	 * 
	 * @param dB		Reddit database
	 * @param viewer	The user to whom the front page is relativized
	 * @param viewed	The user whose posts are to be displayed
	 */
	public static void frontPage2(RedditDB dB, User viewer, String viewedName)
	{
		//To read in submenu commands.
		Scanner scnr = new Scanner(System.in);
		//The user whose posts to be displayed
		User viewed = dB.findUser(viewedName);
		
		Post post;						//Post to be displayed.
		String com = "";				//Submenu command.
		boolean validCom = false;		//Whether a submenu command is valid.
		final String EXIT ="x";			//Exit command.
		
		//Check if the specified user exists, if not notify so and exit
		if(viewed == null)
		{
			System.out.println("User " + viewedName + " not found");
			return;
		}
		
		//List of posts by the user selected
		List<Post> posts = viewed.getPosted();
		Iterator<Post> itr = posts.iterator();	
		
		System.out.println("Displaying /u/" + viewed.getName() + "...");
		
		//Show posts and prompt for submenu commands until user chooses to exit:
		//a:upvote post, z:downvote post, j:move to next post, x:exit submenu
		//else:invalid command
		while(itr.hasNext() && !com.equals(EXIT))
		{
			validCom = false;
			post = itr.next();
			
			while(!validCom)
			{
				System.out.println(post.getKarma() + "\t" + post.getTitle());
				displayPrompt(viewer);
				com = scnr.nextLine();
				validCom = subMenu(viewer, post, com);
			}
		}
		
		//Display when there are no more posts to display
		if(!itr.hasNext() && !com.equals(EXIT))
		{
			System.out.println("No posts left to display.");
		}
		
		System.out.println("Exiting to the main menu...");	//Exit message.
	}
	
	/*
	 * Displays a prompt to the user. If a user is logged in, that is it is not
	 * null, it displays "[<UserName>@reddit]$ ", else it displays
	 * "[anon@reddit]$ ".
	 * 
	 * @param loggedIn	User currently logged in. It is null if no one is logged
	 * 					in.
	 */
	public static void displayPrompt(User loggedIn)
	{
		if(loggedIn != null)	//The user is null if no one is logged in.
		{
			System.out.print("[" + loggedIn.getName() + "@reddit]$ ");
		}
		else
		{
			System.out.print("[anon@reddit]$ ");
		}
	}

	/*
	 * Processes a frontpage submenu command.
	 * 
	 * @param user	The user
	 * @param post	The post currently displayed in the front page
	 * @param com	Submenu command
	 * 
	 * @return	true if the command is valid or is executable because because 
	 * 			the user is logged in, else false
	 */
	public static boolean subMenu(User user, Post post, String com)
	{
		boolean ret = false;	//True if the submenu command is valid or
								//the action is executable because the user
								//is logged in
		
		//Submenu commands
		final String UPVOTE = "a",	//Upvote a post.
		DOWNVOTE = "z",				//Dowvote a post.
		NEXT = "j",					//Move to the next post.
		EXIT = "x";					//Exit submenu.
		
		//a:upvote post, z:downvote post, j:move to next post, x:exit submenu
		//else:invalid command. Refuse action if user is not logged in
		//(when the user is null) for upvoting and downvoting
		if(com.equals(UPVOTE))		//Upvote a post
		{
			if(user != null)
			{
				user.like(post);
				ret = true;
			}
			else
			{
				System.out.println("Login to like post.");
			}
		}
		else if(com.equals(DOWNVOTE))	//Downvote a post
		{
			if(user != null)
			{
				user.dislike(post);
				ret = true;
			}
			else
			{
				System.out.println("Login to dislike post.");
			}
		}
		else if(com.equals(NEXT))	//Move to the next post.
		{
			ret = true;
		}
		else if(com.equals(EXIT))	//Exit submenu.
		{
			ret = true;
		}
		else						//If none of the above
		{
			System.out.println("Invalid command!");
		}
		
		return ret;
	}
}
