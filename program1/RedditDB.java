///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 Reddit.java
// File:             RedditDB.java
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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Reddit database storing user and post information
 * 
 * @author 	Heikal Badrulhisham
 */
public class RedditDB 
{
	//Data field
	private List<User> users;	//List of users.
	
	/*
	 * Constructs an empty database.
	 */
	public RedditDB() 
	{
		this.users = new ArrayList<User>();
	}
	
	/*
	 * Returns a copy of the list of users
	 * 
	 * @return A copy of the list of users
	 */
	public List<User> getUsers() 
	{
		return new ArrayList<User>(this.users);
	}

	/*
	 * If a user with the given name does not already exist, add a user with 
	 * the given name to the database and return it; else simply return null
	 * 
	 * @param name Name of user to add
	 * 
	 * @return	The added user, return null if the user already exists
	 */
	public User addUser(String name) throws IllegalArgumentException
	{
		//Throw an exception if a null value is passed
		if(name == null)
		{
			throw new IllegalArgumentException();
		}
		
		//Return null if there is an existing user with the same name.
		for(Iterator<User> it = this.users.iterator(); it.hasNext();)
		{
			if(it.next().getName().equals(name))
			{
				return null;
			}
		}
		
		//If there's no existing user add new user to users List and return it
		User newUser = new User(name);			//New user.
		this.users.add(newUser);
		return newUser;
		
	}

	/*
	 * Search the database for a user with the given name and return the User
	 * if it is found; if not return null
	 * 
	 * @param name	Name of user to find
	 * 
	 * @return The User if found, null if not
	 */
	public User findUser(String name) throws IllegalArgumentException
	{
		//Throw an exception if a null value is passed
		if(name == null) 
		{
			throw new IllegalArgumentException();
		}
		
		//If a user by the given name exists return it; else return null
		for(Iterator<User> it = this.users.iterator(); it.hasNext();)
		{
			User user = it.next();			//Temporary user for evaluation
			if(user.getName().equals(name))
			{
				return user;
			}
		}
		
		return null;
	}

	/*
	 * Delete a user by the given name. For each posted post, remove it from the 
	 * liked and disliked information of all users, undo all likes and undo all 
	 * dislikes.
	 * 
	 * @param name	Name of user to be deleted
	 * 
	 * @return	true if the user exists and is deleted, false if else
	 */
	public boolean delUser(String name) throws IllegalArgumentException
	{
		//Throw an exception if a null value is passed
		if(name == null) 
		{
			throw new IllegalArgumentException();
		}
		
		User toDelete = this.findUser(name);	//User to be deleted
		
		//If the user exists, remove his posts from the (dis)likes of all users, 
		//undo (dis)likes, return true and remove the user from users List. 
		//Else return false. 
		if(toDelete != null)
		{
			//The list of posts of the user to be deleted
			List<Post> dPost = toDelete.getPosted();
			
			//Remove the user's posts from the list of (dis)likeds of others
			for(Iterator<User> it = this.users.iterator(); it.hasNext();)
			{
				User u = it.next();				//Temporary user for evaluation
				if(!u.equals(toDelete))
				{
					u.getLiked().removeAll(dPost);
					u.getDisliked().removeAll(dPost);
				}
			}
			
			//Undo the user's likes
			for(Iterator<Post> it = toDelete.getLiked().iterator(); 
				it.hasNext();)
			{
				toDelete.undoLike(it.next());
			}
				
			
			//Undo the user's dislikes
			for(Iterator<Post> it = toDelete.getDisliked().iterator(); 
				it.hasNext();)
			{
				toDelete.undoDislike(it.next());
			}
			
			this.users.remove(toDelete);									
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * Get all the posts to be displayed on the front page of a particular user 
	 * and return them.If the user is null this is simply all posts from all 
	 * users. If a user is specified, return all the posts from the user's 
	 * subscribed subreddits only; posts which have previously been liked or 
	 * disliked by the user should not be returned, except when the post was 
	 * created by the user themselves.
	 * 
	 * @param user	The given user
	 * 
	 * @return List of relevant posts
	 */
	public List<Post> getFrontpage(User user) 
	{
		//List of posts by all users
		List<Post> posts = new ArrayList<Post>();	
		
		//Fill up list of posts
		for(Iterator<User> it = this.users.iterator(); it.hasNext();)
		{
			posts.addAll(it.next().getPosted());
		}
		
		//If user is not specified return the whole List of posts
		if(user == null)
		{
			return posts;
		}
		
		//If a user is specified filter out undesired posts
		for(Iterator<Post> it = posts.iterator(); it.hasNext();)
		{
			Post p = it.next();		//Temporary post for filtering
			
			//Remove posts that are not of the user's subreddit
			if(!user.getSubscribed().contains(p.getSubreddit()))
			{
				it.remove();
			}
			//Remove posts by other users that have been (dis)liked
			else if(	!user.getPosted().contains(p) && 
						(	user.getLiked().contains(p) || 
							user.getDisliked().contains(p))	)
			{
					it.remove();
			}
		}
		return posts; 
	}

	/*
	 * Get all the posts from the specified subreddit to be displayed on the 
	 * front page of a particular user and return them. If the user is null this 
	 * is simply all posts from all users. If a user is specified, return all 
	 * the posts from the subreddit which have previously been liked or disliked 
	 * by the user, except when the post was created by the user themselves.
	 * 
	 * @param user		The given user
	 * @param subreddit	The subreddit from which posts are to be displayed
	 * 	
	 * @return List of relevant posts
	 */
	public List<Post> getFrontpage(User user, String subreddit) 
	{
		//List of all posts of the subreddit
		List<Post> posts = new ArrayList<Post>();	
		
		//Add posts that are of the subreddit from total posts
		for(Iterator<User> it = this.users.iterator(); it.hasNext();)
		{
			User u = it.next();		//User who's posts are to be accessed
			
			for(Iterator<Post> it2 = u.getPosted().iterator(); it2.hasNext();)
			{
				Post p = it2.next();	//Post potentially to be added
				if(p.getSubreddit().equals(subreddit))
				{
					posts.add(p);
				}
			}
			
		}
			
		//If there is a user return posts from the specified subreddit
		// which are either posted by the user or have not been (dis)liked.
		//If a user is not specified, return all posts of the subreddit.
		if(user != null)
		{
			for(Iterator<Post> it = posts.iterator(); it.hasNext();)
			{
				Post p = it.next();	//Post potentially to be removed
				
				if(!user.getPosted().contains(p))
				{
					if(user.getLiked().contains(p) || 
							   user.getDisliked().contains(p) )
					{
						it.remove();
					}
				}
			}
		}
		
		return posts;
	}

}
