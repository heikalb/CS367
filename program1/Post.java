///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 Reddit.java
// File:             Post.java
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
 * Represents a single post that keeps track of the user who posted it, 
 * the subreddit it was posted to, the type of the post, the title of the 
 * post and the karma (points) received by it. 
 * 
 * @author Heikal Badrulhisham
 */
public class Post 
{
	//Data fields
	final private User user;		//User who created the post.
	final private String subreddit;	//The subreddit of the post.
	final private PostType type;	//Post type
	final private String title;		//Post title
	private int karma;				//Karma of the post
	
	/*
	 * Constructs a post with specified data with starting karma of 0
	 * 
	 * @param user		The user who created the post
	 * @param subreddit	The subreddit the post belongs to
	 * @param type		The post type
	 * @param title		Post title
	 */
	public Post(User user, String subreddit, PostType type, String title) 
	{
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		this.karma = 0;
	}

	/*
	 * Increases the karma of this post and the relevant karma of the user who 
	 * created the post by two each.
	 */
	public void upvote() 
	{
		this.karma += 2;						//Increase post's karma.
		this.user.getKarma().upvote(this.type);	//Increase poster's karma.
	}
	
	/*
	 * Decreases the karma of this post and the relevant karma of the user who 
	 * created the post by one each.
	 */
	public void downvote() 
	{
		this.karma -= 1;							//Decrease post's karma.
		this.user.getKarma().downvote(this.type);	//Decrease poster's karma.
	}

	/*
	 * Returns the user who created this post.
	 * 
	 * @return The user who created this post.
	 */
	public User getUser() 
	{
		return this.user;
	}

	/*
	 * Returns the subreddit this was posted to
	 * 
	 * @return The post's subreddit
	 */
	public String getSubreddit() 
	{
		return this.subreddit;
	}
	
	/*
	 * Returns the type of the post.
	 * 
	 * @return This post's PostType
	 */
	public PostType getType() 
	{
		return this.type;
	}
	
	/*
	 * Returns the title of the post.
	 * 
	 * @return The post's title
	 */
	public String getTitle() 
	{
		return this.title;
	}

	/*
	 * Returns the karma aggregated by the post.
	 * 
	 * @return The post's karma
	 */
	public int getKarma() 
	{
		return this.karma;
	}
}
