///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program2
// Files:            AmazonStore.java, DLinkedList.java, 
//					 InsufficientCreditException.java, Product.java, User.java.
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
import java.util.Scanner;

/**
 * This is a simulation of Amazon store
 * 
 * @author Heikal badrulhisham
 *
 */
public class AmazonStore 
{

	//Store record of users and products
	private static ListADT<Product> products = new DLinkedList<Product>();
	private static ListADT<User> users = new DLinkedList<User>();
	private static User currentUser = null;//current user logged in

	//scanner for console input
	public static final Scanner stdin= new Scanner(System.in);

	/**
	 * Runs the Amazon store simulation. Information from text files given
	 * in the command line argument are used to create a list of products
	 * and users. A user logs in and is prompted for commands.
	 * 
	 * @param args	Names of text files to read to load product and user data
	 */
	//main method
	public static void main(String args[]) 
	{
		//Populate the two lists using the input files: 
		//Products.txt User1.txt User2.txt ... UserN.txt
		if (args.length < 2) {
			System.out.println("Usage: java AmazonStore [PRODUCT_FILE] "
											+ "[USER1_FILE] [USER2_FILE] ...");
			System.exit(0);
		}

		//load store products
		loadProducts(args[0]);

		//load users one file at a time
		for(int i=1; i<args.length; i++)
			loadUser(args[i]);

		//User Input for login
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter username : ");
			String username = stdin.nextLine();
			System.out.print("Enter password : ");
			String passwd = stdin.nextLine();

			if(login(username, passwd)!=null)
			{
				//generate random items in stock based on this user's wish list
				ListADT<Product> inStock=currentUser.generateStock();
				//show user menu
				userMenu(inStock);
			}
			else
				System.out.println("Incorrect username or password");

			System.out.println("Enter 'exit' to exit program or anything else "
													+ "to go back to login");
			if(stdin.nextLine().equals("exit"))
				done = true;
		}

	}

	/**
	 * Tries to login for the given credentials. Updates the currentUser if 
	 * successful login
	 * 
	 * @param username name of user
	 * @param passwd password of user
	 * @returns the currentUser 
	 */
	public static User login(String username, String passwd)
	{
		if(username == null || passwd == null)
			throw new IllegalArgumentException();
		
		User u; 			//User to be evaluated.
		
		//Find a user with the given credentials, make that the logged in user
		//and return that
		for(int i = 0; i < users.size(); i++)
		{
			u = users.get(i);
			if(u.checkLogin(username, passwd))
			{
				currentUser = u;
				return u;
			}
		}
		return null;
	}

	/**
	 * Reads the specified file to create and load products into the store.
	 * Every line in the file has the format: <NAME>#<CATEGORY>#<PRICE>#<RATING>
	 * Create new products based on the attributes specified in each line and 
	 * insert them into the products list. Order of products list should be the 
	 * same as the products in the file. For any problem in reading the file 
	 * print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadProducts(String fileName)
	{
		if(fileName == null)
			throw new IllegalArgumentException();
		
		File productsFile = new File(fileName);	//Products file.
		Scanner scn = null;			//Scanner on the file
		String[] pLine;				//Line of data about a single product.
		String	name,				//The product's name
				cat;				//The product's category
		int price;					//The product's price
		float rating;				//The product's rating
		
		//Try to read file. Exit if file cannot be accessed
		try 
		{
			scn = new Scanner(productsFile);	
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: Cannot access file");
			System.exit(0);
		}
		
		//Read each line of products file, instantiate a new product
		//and add it to list of products
		while(scn.hasNext())
		{
			pLine = scn.nextLine().split("#");
			name = pLine[0];
			cat = pLine[1];
			price = Integer.valueOf(pLine[2]);
			rating = Float.valueOf(pLine[3]);
			
			//New product to add
			Product newProduct = new Product(name, cat, price, rating);
			products.add(newProduct);
		}
	}

	/**
	 * Reads the specified file to create and load a user into the store.
	 * The first line in the file has the format:<NAME>#<PASSWORD>#<CREDIT>
	 * Every other line after that is a name of a product in the user's 
	 * wishlist, format:<NAME>
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadUser(String fileName)
	{
		if(fileName == null)
			throw new IllegalArgumentException();
		
		File userFile = new File(fileName);		//User file.
		Scanner scn = null;						//Scanner on the file
		String[] uLine ;						//A line in the file
		String 	username,						//User name
				passwd;							//User's password
		int credit;								//User's credit
		
		//Try to read file. Exit if file cannot be accessed
		try 
		{
			scn = new Scanner(userFile);	
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error: Cannot access file");
			System.exit(0);
		}
		
		//Read the first line about user information and add a new user
		//instance
		uLine = scn.nextLine().split("#");
		username = uLine[0];
		passwd = uLine[1];
		credit = Integer.valueOf(uLine[2]);
		
		//New user to add
		User newUser = new User(username, passwd, credit);
		users.add(newUser);
		
		//Read the rest of the lines of the file containing product names
		//and add those products to the user's wishlist
		while(scn.hasNext())
		{
			String pName = scn.nextLine();	//Product name
			Product prod;					//Product whose name to evaluate
			for(int i = 0; i < products.size(); i++)
			{
				prod = products.get(i);
				if(prod.getName().equals(pName))
					newUser.addToWishList(prod);
			}
		}
	}

	/**
	 * See sample outputs
     * Prints the entire store inventory formatted by category
     * The input text file for products is already grouped by category, use the same order as given in the text file 
     * format:
     * <CATEGORY1>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * 
     * <CATEGORY2>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public static void printByCategory()
	{
		String catName = null; 	//Category name
		Product p;			//Current product.
		
		for(int i = 0; i < products.size(); i++)
		{
			p = products.get(i);
			
			//Print category header if a product of new category is encountered
			if(!p.getCategory().equals(catName))
			{
				catName = p.getCategory();
				System.out.println();
				System.out.println(catName + ":");
			}
			
			System.out.println(p.toString());
		}
		System.out.println();	//Space after all has been displayed.
	}
	
	/**
	 * Interacts with the user by processing commands
	 * 
	 * @param inStock list of products that are in stock
	 */
	public static void userMenu(ListADT<Product> inStock)
	{
		if(inStock == null)
			throw new IllegalArgumentException();
		
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter option : ");
			String input = stdin.nextLine();		//user input

			//only do something if the user enters at least one character
			if (input.length() > 0) 
			{
				//Array based on user input. Split on colon, because names have 
				//spaces in them
				String[] commands = input.split(":");
				if(commands[0].length() > 1)
				{
					System.out.println("Invalid Command");
					continue;
				}
				switch(commands[0].charAt(0))
				{
				case 'v':
				{
					//View all products in the store
					if(commands[1].equals("all"))
						printByCategory();
					//Display wishlist products
					else if(commands[1].equals("wishlist"))
						currentUser.printWishList(System.out);
					//Display all products in stock
					else if(commands[1].equals("instock"))
					{
						for(int i = 0; i < inStock.size(); i++)
							System.out.println(inStock.get(i).toString());
					}
					else
						System.out.println("Invalid Command");
					break;
				}
				case 's':	//Search and display matching products
				{
					String pString; 	//String representation of a product
					
					for(int i = 0; i < products.size(); i++)
					{
						pString = products.get(i).toString();
						if(pString.contains(commands[1]))
							System.out.println(pString);
					}
					break;
				}
				case 'a':	//Add specified product to user's wishlist	
				{
					//Product to be added
					Product p = getProduct(commands[1], products);
					
					//If the product exists add it to wishlist
					if(p != null)
					{
						currentUser.addToWishList(p);
						System.out.println("Added to wishlist");
					}
					else
						System.out.println("Product not found");
					break;
				}
				case 'r':	//Remove specified product from user's wishlist
				{
					//Try remove from wishlist
					if(currentUser.removeFromWishList(commands[1]) == null)
						System.out.println("Product not found");
					else
						System.out.println("Removed from wishlist");
					break;
				}
				case 'b':	//Buy in stock products
				{
					String pBuy;	//Name of product to buy
					
					for(int i = 0; i < inStock.size(); i++)
					{
						pBuy = inStock.get(i).getName();
						try 
						{
							if(!currentUser.buy(pBuy))
								continue;
							System.out.println("Bought " + pBuy);
						} 
						catch (InsufficientCreditException e) 
						{
							System.out.println("Insufficient funds for "
									+ pBuy);
						}
					}
					break;
				}
				case 'c':	//Shows the credit of the current user
				{
					System.out.println("$" + currentUser.getCredit());
					break;
				}
				case 'l':	//Log out current user
				{
					done = true;
					System.out.println("Logged Out");
					break;
				}
				default:  //A command with no argument
					System.out.println("Invalid Command");
					break;
				}
			}
		}
	}

	/**
	 * 
	 * Returns the product by the given name
	 * 
	 * @param pName	The product's name
	 * @param inStock	List of in stock products
	 * 
	 * @return Product by the given name
	 */
	public static Product getProduct(String pName, ListADT<Product> inStock)
	{
		if(pName == null || inStock == null)
			throw new IllegalArgumentException();
		
		for(int i = 0; i < inStock.size(); i++)
		{
			if(inStock.get(i).getName().equals(pName))
				return inStock.get(i);
		}
		return null;
	}
}
