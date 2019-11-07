///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 AmazonStore.java
// File:             DLinkedList.java
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
 * A DLinkedList is a chain of nodes with each node containing the data, a 
 * reference to the previous and next nodes.
 * 
 * @author Heikal badrulhisham
 *
 */
public class DLinkedList<E> implements ListADT<E>
{
	private Listnode<E> head, tail;	//(Dummy) head and tail nodes
	private int numItems;			//Number of items in a linked list
	
	/**
	 * Constructs a doubly linked list with a dummy head and 0 number of items
	 */
	public DLinkedList()
	{
		head = new Listnode<E>(null);
		tail = head;
		numItems = 0;
	}
	
	/**
	 * Add item at the end of the list, and update number of items
	 * 
	 * @param item Item to add
	 */
	public void add(E item) 
	{
		if(item == null)
			throw new IllegalArgumentException();
		
		Listnode<E> n = new Listnode<E>(item);	//New node
		tail.setNext(n);
		n.setPrev(tail);
		tail = n;
		numItems++;
	}
	
	/**
	 * Add item at the given position in the list and update number of items
	 * 
	 * @param pos	Position in the list
	 * @param item	Item to add
	 */
	public void add(int pos, E item) 
	{
		if(item == null)
			throw new IllegalArgumentException();
		
		if (pos < 0 || pos > numItems)	//For invalid positions
			throw new IndexOutOfBoundsException("" + pos);
		
		if(pos == numItems)	//If pos is at the end of the list
			add(item);
		else	//Else go to the node before the position and add after it
		{
			Listnode<E> n = goTo(pos);					//The node before pos.
			Listnode<E> n1 = n.getNext();					//The node at pos.
			Listnode<E> nNew = new Listnode<E>(item, n1, n);	//New node.
			n.setNext(nNew);
			n1.setPrev(nNew);
			numItems++;
		}
	}

	/**
	 * Tells whether a list contains the given item
	 * 
	 * @param item	Item to look for
	 * 
	 * @return true if the list contains the item, else false
	 */
	public boolean contains(E item) 
	{
		if(item == null)
			throw new IllegalArgumentException();
		
		Listnode<E> c = head.getNext();	//Pointer on the list
		
		while (c != null) 	//Go through list
		{
			if (c.getData() == item)
				return true;
			c = c.getNext();
		}
		return false;
	}
	
	/**
	 * Returns the item at the given position. 
	 * 
	 * @param pos	The given position
	 * 
	 * @return The item at that position
	 */
	public E get(int pos) 
	{
		if (pos < 0 || pos >= numItems)		//For invalid positions
			throw new IndexOutOfBoundsException("" + pos);
		
		return goTo(pos + 1).getData();
	}
	
	/**
	 * Tells whether the list is empty
	 * 
	 * @return True if the list is empty, else false
	 */
	public boolean isEmpty() 
	{
		return numItems == 0;
	}

	/**
	 * Removes and returns the item at the given position and updates
	 * number of items
	 * 
	 * @param pos	Position in the list
	 * 
	 * @return The item at the position
	 */
	public E remove(int pos) 
	{
		if (pos < 0 || pos >= numItems)	//For invalid positions
			throw new IndexOutOfBoundsException("" + pos);
		
		Listnode<E> d = goTo(pos + 1);	//Node to delete
		Listnode<E> n1 = d.getPrev();	//Node before the one to delete
		Listnode<E> n2 = d.getNext();	//Node after the one to delete
		
		n1.setNext(n2);
		
		if(pos == numItems - 1)	//If pos is at the end update tail
			tail = n1;
		else					//Do this if pos is not at the end
			n2.setPrev(n1);
		
		numItems--;
		return d.getData();
	}

	/**
	 * Returns the number of items in the list
	 * 
	 * @return The number of items in the list
	 */
	public int size() 
	{
		return numItems;
	}

	/**
	 * Traverse to the node in the list directly before the given position
	 * 
	 * @param pos	The given position
	 * @return The node at the the given position
	 */
	private Listnode<E> goTo(int pos) 
	{
		if (pos < 0 || pos > numItems)	//For invalid positions
			throw new IndexOutOfBoundsException("" + pos);
		
		Listnode<E> c = this.head;		//Pointer on the list
		for (int i = 0; i < pos; i++)	//Go to the position 
			c = c.getNext();
		
		return c;
	}
}
