///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:	 AmazonStore.java
// File:             Product.java
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
 * Stores the name, category, price and rating of a product
 */
public class Product 
{
	
	private String name;		//Name of the product
	private String category;	//The category of the product
	private int price;			//The price of the product
	private float rating;		//The rating of the product
	
	/**
     * Constructs a Product with a name, category, price and rating. 
     * 
     * @param name name of product
     * @param category category of product
     * @param price price of product in $ 
     * @param rating rating of product out of 5
     */
	public Product(String name, String category, int price, float rating)
	{
		if(name == null || category == null || (Integer) price == null || 
														(Float) rating == null)
			throw new IllegalArgumentException();
		
		this.name = name;
		this.category = category;
		this.price = price;
		this.rating = rating;
	}
	
	/** 
     * Returns the name of the product
     * @return the name
     */
	public String getName()
	{
		return this.name;
	}
	
	/** 
     * Returns the category of the product
     * @return the category
     */
	public String getCategory()
	{
		return this.category;
	}
	
	/** 
     * Returns the price of the product
     * @return the price
     */
	public int getPrice()
	{
		return this.price;
	}
	
	/** 
     * Returns the rating of the product
     * @return the rating
     */
	public float getRating()
	{
		return this.rating;
	}
	
	/** 
     * Returns the Product's information in the following format: 
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public String toString()
	{
		return this.name + " [Price:$"+ this.price + " Rating:" + this.rating 
				+ " stars]";
	}

}
