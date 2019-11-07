///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 VersionControlApp.java
// File:             SimpleStack.java
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
 *A SimpleStack is a stack with an underlying array to hold data
 * 
 * @author Heikal Badrulhisham
 */
public class SimpleStack<E> implements StackADT<E> 
{
	private E[] items;						//Array to hold items
	private int numItems;					//Number of items in the stack
	private static final int INITSIZE = 10;	//Initial size of the array
	
	
	/**
	 * Constructs a SimpleStack with number of items initialized to 0 and 
	 * the underlying array initialized with a size of 10
	 */
	@SuppressWarnings("unchecked")
	public SimpleStack() 
	{
		items = (E[]) new Object[INITSIZE];
		numItems = 0;
	}

	 /**
     * Checks if the stack is empty.
     * @return true if stack is empty; otherwise false
     */
	public boolean isEmpty() 
	{
		return numItems == 0;
	}
	
	/**
     * Returns (but does not remove) the top item of the stack.
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	public E peek() throws EmptyStackException 
	{
		if(numItems == 0)
			throw new EmptyStackException();
		
		return items[numItems - 1];
	}

	/**
     * Pops the top item off the stack and returns it. 
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	public E pop() throws EmptyStackException 
	{
		if(numItems == 0)
			throw new EmptyStackException();
		
		numItems--;
		return items[numItems];
	}

	 /**
     * Pushes the given item onto the top of the stack.
     * @param item the item to push onto the stack
     * @throws IllegalArgumentException if item is null.
     */
	public void push(E item) 
	{
		if(items.length == numItems)
			expandArray();
		
		items[numItems] = item;
		numItems++;
	}

	/**
     * Returns the size of the stack.
     * @return the size of the stack
     */
	public int size() 
	{
		return numItems;
	}
	
	/**
     * Returns a string representation of the stack (for printing).
     * @return a string representation of the stack
     */
    public String toString()
    {
    	String s = "";
    	
    	for(int i = numItems - 1; i >= 0; i--)
    		s += items[i].toString() + "\n";
    	
    	return s;
    }
    
    /**
     * Expands the underlying array to twice the size
     */
    @SuppressWarnings("unchecked")
	private void expandArray() 
    {
    	E[] oldArray = items;	//Holds the old version of the array
    	items = (E[]) new Object[items.length * 2];
    	
    	for(int i = 0; i < oldArray.length; i++)
    		items[i] = oldArray[i];
    }
}
