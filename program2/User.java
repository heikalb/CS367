///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 AmazonStore.java
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

import java.util.Random;
import java.io.PrintStream;

/**
 * The User class uses DLinkedList to build a price ordered list called 
 * 'wishlist' of products 
 * Products with higher Price fields should come earlier in the list.
 */
public class User {
	//Random number generator, used for generateStock. DO NOT CHANGE
	private static Random randGen = new Random(1234);
	
	private String username;			//The user's username
	private String passwd;				//The user's password
	private int credit;					//The user's credit
	private ListADT<Product> wishList;	//The user's wishlist
	
	/**
     * Constructs a User instance with a name, password, credit and an empty 
     * wishlist. 
     * 
     * @param username name of user
     * @param passwd password of user
     * @param credit amount of credit the user had in $ 
     */
	public User(String username, String passwd, int credit)
	{
		if(username == null || passwd == null || (Integer) credit == null)
				throw new IllegalArgumentException();
		
		this.username = username;
		this.passwd = passwd;
		this.credit = credit;
		this.wishList = new DLinkedList<Product>();
	}
	
	/**
     * Checks if login for this user is correct
     * @param username the name to check
     * @param passwd the password to check
     * @return true if credentials correct, false otherwise
     */
	public boolean checkLogin(String username, String passwd)
	{
		if(username == null || passwd == null)
			throw new IllegalArgumentException();
		
		return this.username.equals(username) && this.passwd.equals(passwd);
	}
	
	/**
     * Adds a product to the user's wishlist. 
     * Maintains the order of the wishlist from highest priced to lowest priced 
     * products.
     * @param product the Product to add
     */
	public void addToWishList(Product product)									
	{
		if(product == null)
			throw new IllegalArgumentException();
		
		int j = 0;	//Index where the product is added to
		
		for(int i = 0; i < wishList.size(); i++)
		{
			if(product.getPrice() >= wishList.get(i).getPrice())
			{
				break;
			}
			else
				j++;
		}
		
		wishList.add(j, product);
	}
	
	/**
     * Removes a product from the user's wishlist. 
     * Do not charge the user for the price of this product
     * @param productName the name of the product to remove
     * @return the product on success, null if no such product found
     */
	public Product removeFromWishList(String productName)
	{
		if(productName == null)
			throw new IllegalArgumentException();
		
		for(int i = 0; i < wishList.size(); i++)
		{
			Product p = wishList.get(i);	//Product to be evaluated
			if(p.getName().equals(productName))
			{
				wishList.remove(i);
				return p;
			}
		}
		
		return null;	//If the product is not found
	}
	
	/**
     * Print each product in the user's wishlist in its own line using the 
     * PrintStream object passed in the argument
	 * @param printStream The printstream object on which to print out the 
	 * 					  wishlist
     */
	public void printWishList(PrintStream printStream)
	{
		if(printStream == null)
			throw new IllegalArgumentException();
		
		//Print string representation of wishlist products
		for(int i = 0; i < wishList.size(); i++)
			printStream.println(wishList.get(i));
	}
	
	/**
     * Buys the specified product in the user's wishlist.
     * Charge the user according to the price of the product by updating the credit
     * Remove the product from the wishlist as well
     * Throws an InsufficientCreditException if the price of the product is greater than the credit available.
     * 
     * @param productName name of the product
     * @return true if successfully bought, false if product not found 
     * @throws InsufficientCreditException if price > credit 
     */
	public boolean buy(String productName) throws InsufficientCreditException
	{
		if(productName == null)
			throw new IllegalArgumentException();
		
		Product p = removeFromWishList(productName);	//Product to buy
		
		//If the product is in the wishlist attempt purchase
		if(p != null)	
		{
			if(this.credit < p.getPrice())	//Check if there's enough credit
			{
				addToWishList(p);	//Put the product back into the wish list
				throw new InsufficientCreditException();
			}
			
			//If there's enough credit charge the user and return true
			this.credit -= p.getPrice();
			return true;
		}
		//If the product isn't on the wish list return false
		return false;
	}
	
	/** 
     * Returns the credit of the user
     * @return the credit
     */
	public int getCredit()
	{
		return credit;
	}
	
	/**
	 * This method is already implemented for you. Do not change.
	 * Declare the first N items in the currentUser's wishlist to be in stock
	 * N is generated randomly between 0 and size of the wishlist
	 * 
	 * @returns list of products in stock 
	 */
	public ListADT<Product> generateStock() {
		ListADT<Product> inStock= new DLinkedList<Product>();

		int size=wishList.size();
		if(size==0)
			return inStock;

		int n=randGen.nextInt(size+1);//N items in stock where n>=0 and n<size

		//pick first n items from wishList
		for(int ndx=0; ndx<n; ndx++)
			inStock.add(wishList.get(ndx));
		
		return inStock;
	}

}
