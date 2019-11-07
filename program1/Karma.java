///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 Reddit.java
// File:             Karma.java
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
 * A Karma represents points accrued by a Reddit uses.  A user's karma comprise
 * link karmas and comment karmas
 * 
 * 	Bugs:	None known
 * .
 * @author:	Heikal Badrulhisham
 */
public class Karma 
{
	//Data fields
	private int linkKarma;		//Karma of link posts.
	private int commentKarma;	//Karma of comment posts.
	
	/*
	 * Constructs a Karma object initialized with zero link and comment karmas
	 */
	public Karma() 
	{
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	/*
	 * Increases the karma of this type by two for this instance.
	 * 
	 * @param type	Post type, either link or comment
	 */
	public void upvote(PostType type) 
	{
		//How much karma increases for every upvote
		final int UPVOTE_INCR = 2;
		
		//If the post is of type link, increase link karma. 
		//If it's of type comment, increase comment karma.
		if(type.equals(PostType.LINK))
		{
			this.linkKarma += UPVOTE_INCR;
		}
		else if(type.equals(PostType.COMMENT))
		{
			this.commentKarma += UPVOTE_INCR;
		}
	}

	/*
	 * Decreases the karma of this type by one for this instance
	 * 
	 * @param type	Post type, either link or comment
	 */
	public void downvote(PostType type) 
	{
		//How much karma decreases for every downvote
		final int DOWNVOTE_DEC = -1;
		
		//If the post is of type link, decrease link karma. 
		//If it's of type comment, decrease comment karma.
		if(type.equals(PostType.LINK))
		{
			this.linkKarma += DOWNVOTE_DEC;
		}
		else
		{
			this.commentKarma += DOWNVOTE_DEC;
		}
	}

	/*
	 * Returns link karma
	 * 
	 * @return Number of link karmas
	 */
	public int getLinkKarma() 
	{
		return this.linkKarma;
	}

	/*
	 * Returns comment karma
	 * 
	 * @return Number of comment karmas
	 */
	public int getCommentKarma() 
	{
		return this.commentKarma;
	}
}
