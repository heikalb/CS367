///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 Reddit.java
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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the data associated with a single user: name, karma, 
 * subreddits the user is subscribed to, and lists of liked and disliked posts
 * 
 * @author Heikal Badrulhisham
 */
public class User 
{
	//Data fields
	final private String name;			//User's name.
	final private Karma karma;			//User's karma.
	private List<String> subscribed;	//User's list of subreddits.
	private List<Post> posted;			//List of posts created by the user.
	private List<Post> liked;			//User's list of liked posts.
	private List<Post> disliked;		//User's list of disliked posts
	
	/*
	 * Constructs a user with the specified name with other relevant data.
	 */
	public User(String name) 
	{
		this.name = name;
		this.karma = new Karma();
		this.subscribed = new ArrayList<String>();
		this.posted = new ArrayList<Post>();
		this.liked = new ArrayList<Post>();
		this.disliked = new ArrayList<Post>();
	}
	
	/*
	 * Returns the name of the current user.
	 * 
	 * @return The user's name
	 */
	public String getName() 
	{
		return this.name;
	}

	/*
	 * Returns the karma of the current user.
	 * 
	 * @return The karma of the current user.
	 */
	public Karma getKarma() 
	{
		return this.karma;
	}

	/*
	 * Returns a copy of the list of subreddits the user is subscribed to
	 * 
	 * @return Copy of the list of subreddits the user is subscribed to
	 */
	public List<String> getSubscribed() 
	{
		return new ArrayList<String>(this.subscribed);
	}
	
	/*
	 * Returns a copy of the list of posts posted by the user.
	 * 
	 * @return A copy of the list of posts posted by the user.
	 */
	public List<Post> getPosted() 
	{
		return new ArrayList<Post>(this.posted);
	}

	/*
	 * Returns a copy of the list of posts liked by the user.
	 * 
	 * @return A copy of the list of posts liked by the user.
	 */
	public List<Post> getLiked() 
	{
		return new ArrayList<Post>(this.liked);
	}
	
	/*
	 * Returns a copy of the list of posts disliked by the user
	 * 
	 * @ A copy of the list of posts disliked by the user
	 */
	public List<Post> getDisliked() 
	{
		return new ArrayList<Post>(this.disliked);
	}

	/*
	 * Add the specified subreddit to the List of subscribed subreddits if the 
	 * user is not already subscribed. If the user is already subscribed, 
	 * unsubscribe from the subreddit.
	 * 
	 * @param subreddit	The subreddit to subscribe to
	 */
	public void subscribe(String subreddit) 
	{
		//If the user is not subscribed, subscribe; else unsubscribe.
		if(!this.subscribed.contains(subreddit))
		{
			this.subscribed.add(subreddit);
		}
		else
		{
			this.unsubscribe(subreddit);
		}
			
	}
	
	/*
	 * Remove the specified subreddit from the List of subscribed subreddits if 
	 * present; if not, do nothing.
	 * 
	 * @param subreddit The subreddit to be unsubscribed from
	 */
	public void unsubscribe(String subreddit) 
	{
		if(this.subscribed.contains(subreddit))
		{
			this.subscribed.remove(subreddit);
		}
	}

	/*
	 * Instantiate a new post with the appropriate parameters and add it to the 
	 * list of posts posted by the user. A new post is automatically liked
	 * by the creator.
	 * 
	 * @param subreddit	The subreddit of the new post
	 * @param type		The post type
	 * @param title		The post title
	 * 
	 * @return The newly created post
	 */
	public Post addPost(String subreddit, PostType type, String title) 
	{
		Post newPost = new Post(this, subreddit, type, title);	//New post.
		this.like(newPost);		//Posting User likes the post.
		posted.add(newPost);	//Add new post to user's list of posted posts.
		return newPost;
	}
	
	/*
	 * Upvote the post and add it to the List of liked posts if not already 
	 * liked; else undo the like. If the post is currently disliked by
	 * the user, the dislike should be undone.
	 * 
	 * @param post	The post to be liked
	 */
	public void like(Post post) 
	{
		//If the post is not already liked, like it; else undo the like
		if(!this.liked.contains(post))
		{
			post.upvote();
			this.liked.add(post);
			//If the post has been disliked, undo the dislike:
			if(this.disliked.contains(post))
			{
				this.undoDislike(post);
			}
		}
		else
		{
			this.undoLike(post);
		}
	}
	
	/*
	 * Remove the post from the list of liked posts and update its karma 
	 * appropriately.
	 * 
	 * @param post The post to be un-liked
	 */
	public void undoLike(Post post) 
	{
		if(this.liked.contains(post))
		{
			this.liked.remove(post);
			//Need to do twice because an upvote is worth twice a downvote:
			post.downvote();	
			post.downvote();
		}
	}
	
	/*
	 * Downvote the post and add it to the List of disliked posts if not already 
	 * disliked; else undo the dislike. Id the post is currently liked by the 
	 * user, undo the like
	 * 
	 * @param post	The post to dislike
	 */
	public void dislike(Post post) 
	{
		//If the post is not already disliked, dislike it; else undo the dislike
		if(!this.disliked.contains(post))
		{
			post.downvote();
			this.disliked.add(post);
			if(this.liked.contains(post))	//Undo like if post has been liked.
			{
				this.undoLike(post);
			}
		}
		else
		{
			this.undoDislike(post);
		}
	}
	
	/*
	 * Remove the post from the list of disliked posts and update its karma 
	 * appropriately.
	 * 
	 * @param post	The post to be un-disliked
	 */
	public void undoDislike(Post post) 
	{
		if(this.disliked.contains(post))
		{
			this.disliked.remove(post);
			//Need to do like this because an upvote is worth twice a downvote:
			post.upvote();
			post.downvote();
		}
	}
}
