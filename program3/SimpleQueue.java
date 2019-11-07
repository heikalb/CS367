///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 VersionControlApp.java
// File:             SimpleQueue.java
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
 * A SimpleQueue is a queue with an underlying circular array to hold data
 * 
 * @author Heikal Badrulhisham
 *
 */
public class SimpleQueue<E> implements QueueADT<E> 
{
	private E[] items;						//Array to hold data
	private int numItems;					//Number of items in the queue
	private int frontIndex, rearIndex;		//Indices of front and rear of queue
	private static final int INITSIZE = 10;	//Initial size of the array
	
	/**
	 * Constructs a simple queue
	 */
	@SuppressWarnings("unchecked")
	public SimpleQueue() 
	{
		items = (E[]) new Object[INITSIZE];
		numItems = 0;
		frontIndex = 0; 
		rearIndex = -1;
	}

	/**
     * Checks if the queue is empty.
     * @return true if queue is empty; otherwise false.
     */
	public boolean isEmpty() 
	{
		return numItems == 0;
	}

	/**
     * Removes and returns the front item of the queue.
     * @return the front item of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
	public E dequeue() throws EmptyQueueException 
	{
		if(numItems == 0)
			throw new EmptyQueueException();
		
		E ret = items[frontIndex];		//Item at the front
		items[frontIndex] = null;	
		
		//Update front index
		if(frontIndex == items.length - 1)
			frontIndex = 0;
		else
			frontIndex++;
		
		numItems--;
		
		return ret;
	}

	/**
     * Adds an item to the rear of the queue.
     * @param item the item to add to the queue.
     * @throws IllegalArgumentException if item is null.
     */
	public void enqueue(E item) 
	{
		if(item == null)
			throw new IllegalArgumentException();
		
		//Expand array if it is full
		if(numItems == items.length)
			items = expand();
		
		//Update rear index. Wrap around if have to
		if(rearIndex == items.length - 1)
			rearIndex = 0;
		else
			rearIndex++;
		
		//Add item
		items[rearIndex] = item;
		numItems++;
	}

	/**
     * Returns (but does not remove) the front item of the queue.
     * @return the front item of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
	public E peek() throws EmptyQueueException 
	{
		if(numItems == 0)
			throw new EmptyQueueException();
		
		return items[frontIndex];
	}

	/**
     * Returns the size of the queue.
     * @return the size of the queue
     */
	public int size() 
	{
		return numItems;
	}
	
	/**
     * Returns a string representation of the queue (for printing).
     * @return a string representation of the queue.
     */
	public String toString()
	{
		String s = "";	//String to return
		
		if(frontIndex <= rearIndex)	//For no wrap around cases
		{
			for(int i = frontIndex; i < frontIndex+numItems; i++)
				s += items[i].toString() + "\n";
		}
		else	//For wrap around cases
		{
			for(int i = frontIndex; i < items.length; i++)
				s += items[i].toString() + "\n";
			
			if(frontIndex > rearIndex)
			{
				for(int i = 0; i <= rearIndex; i++)
					s += items[i].toString() + "\n";
			}
		}
		
		return s;
	}
	
	/**
	 * Helper method to expand array
	 * 
	 * @return expanded array
	 */
	private E[] expand()
	{
		@SuppressWarnings("unchecked")
		E[] newA = (E[]) new Object[items.length * 2]; //New Array
		
		//Copy items from front of queue to end of old array
		System.arraycopy(items, frontIndex, newA, 0, items.length - frontIndex);
		
		//Copy items at beginning of old array to front of queue 
		if(frontIndex != 0)
			System.arraycopy(items, 0, newA, items.length - frontIndex, 
																	frontIndex);
		//Update indices
		frontIndex = 0;
		rearIndex = numItems - 1;
			
		return newA;
	}
}
