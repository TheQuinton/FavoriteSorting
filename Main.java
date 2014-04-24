package project; 
/* Group: Foxtrot
 * Team Members: Quinton Schafer, Homare Takase
 * Project: Team Assignment #2
 * Instructions: the included favorite file should sit in the project containing folder, which holds: .settings, bin, src...  
 * */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	static List<Favorite> favorites = new ArrayList<Favorite>();
	static int numberOfFavorites = 20; //sets the number of Favorites to be read from the file, can be any amount

	public static void main(String[] args) { // main
		setFavoriteNames();
		askRankAndComment();
		printEndList();
	}
	public static void setFavoriteNames(){ //creates the objects from the file
		try(BufferedReader reader = new BufferedReader(new FileReader("favorites.txt"))) { // finds the file in the containing folder
			 String line = reader.readLine();
			 for (int i = 0; i < numberOfFavorites; i++) { //this number is the number of objects we create
				 String fav = line.substring(line.indexOf("-")+2); // assuming the file's format is "# - item"
				 favorites.add(i,new Favorite(fav));	//adds new objects to the list of objects
				 System.out.println((i+1) + " - " + favorites.get(i).getName());
				 line = reader.readLine();
		     }
		} catch (IOException e) {
			 System.out.println("File read error");
		}
	}
	public static void askRankAndComment(){
		int rank = 0;
		boolean errorLoop = true;
		Scanner input = new Scanner(System.in);
		System.out.println("Please rank your favorites and add comments about them");
		for (int i = 0; i < favorites.size(); i++) { // main loop
			System.out.println("What would you like to rank " + favorites.get(i).getName() + "?");
			errorLoop = true;
			while (errorLoop == true){ //error loop to prevent invalid input
				try {
					rank = Integer.parseInt(input.nextLine());
					if (rank > 0 && rank <= favorites.size()) {
						errorLoop = false;
					} else {
						System.out.println("Invalid input: Rank must be between 1 and " + favorites.size());
					}
				}
				catch(Exception e){ 
					System.out.println("Invalid Input: Rank must be an integer");
					continue;
				}
			}
			for (int j = 0; j < favorites.size(); j++) {
				if (rank == favorites.get(j).getRank()) {
					System.out.println("That rank is currently being held by: " + favorites.get(j).getName());
					System.out.println("The list will be shifted down to accomodate: " + favorites.get(i).getName() + " at the rank: " + rank);
					setFavRank(i,rank);
					downRank(i,rank);
					break;
				} else if (j == favorites.size()-1) {
					setFavRank(i,rank);
				}
			}
			System.out.println("What comments would you like to add to "+ favorites.get(i).getName() + "?");
			String comment = input.nextLine();
			favorites.get(i).setComment(comment);
		}
		input.close();
	}
	public static void setFavRank(int fav, int rank){ //sets ranks 
		favorites.get(fav).setRank(rank);
	}
	public static void lowestAvailablePos(int fav){ //finds the lowest available position, and sets the favorite to it
		for (int i = favorites.size(); i > 0; i--) { //loops backwards equal to the number of favorites
			for (int j = 0; j < favorites.size(); j++) { //loops through the favorites
				int valCheck = favorites.get(j).getRank(); //gets the rank of each favorite 
				if (i == valCheck) {	//if the rank of a favorite is being used then it breaks the inner loop
					break;
				}
				if (j == favorites.size()-1){ //if the inner loop has gone through all of the favorites, then the outer loop variable is available
					setFavRank(fav,i);
					return;
				}
			}
		}
	}
	public static void downRank(int fav,int valCheck){ //finds items that need to moved, and moves them down. Then sets the original as 
		List<Integer> favoritesInTheWay = new ArrayList<Integer>(); //creates the list of favorites that need to be moved
		for (int i = 0; i < favorites.size(); i++) { //finds out what items are in the way and need to be moved
			if (valCheck == favorites.get(i).getRank()) {	//if the variable we are checking for matches the current rank of a favorite
				favoritesInTheWay.add(i);	//makes a list of the items in the way
				if (i == fav) {
					favoritesInTheWay.remove(i);
				}
				if (valCheck == favorites.size()) {	//if we are checking for a value equal to the end of the list, we look for lowest available position
					System.out.println("Unfortunately, there is not enough room to shift down. Instead, " + favorites.get(fav).getName() + " will be placed at the lowest available position");
					favoritesInTheWay.clear();	//empty the list of values
					lowestAvailablePos(fav); //call the lowestAvailablePosition finding method
					break;	//break the loop once the favorite has been placed at the lowest available position
				}else{	//if we are not checking for a value at the end of the list
					valCheck++;	//add one to the variable we are looking for
					downRank(fav,valCheck);	//call the method, and check for the next variable in line
				}
			}
		}
		for (int i = 0; i < favoritesInTheWay.size(); i++) { //moves all the items down in rank that need to be moved
			int fave = favoritesInTheWay.get(i);	//finds the favorite that needs to be shifted down from the list of favorites in the way
			int rank = favorites.get(fave).getRank();	//retrieves their current rank
			rank++;	//adds one to their current rank
			setFavRank(fave,rank);	//sets their new rank
		}
		favoritesInTheWay.clear();	//empties the list
	}
	public static void printEndList(){ //formatted to look nice
		System.out.println("Here are the end results:");
		String title = String.format("%1$-" + 15 + "s", "Favorite Item");
		System.out.println(title + " | Rank  | Comments");
		for (int i = 0; i < favorites.size(); i++) {
			String name = String.format("%1$-" + 15 + "s", favorites.get(i).getName());
			if (favorites.get(i).getRank() > 9) {
				System.out.println(name+ " |   " + favorites.get(i).getRank() + "  | " + favorites.get(i).getComment());
			}else{
				System.out.println(name+ " |   " + favorites.get(i).getRank() + "   | " + favorites.get(i).getComment());
			}
		}
	}
}
