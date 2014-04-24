package project;

public class Favorite {
	String name;
	String comment;
	int rank;
	Favorite(String nameString){
		name = nameString;
	}
	public String getName(){
		return name;
	}
	public void setRank(int intRank) {
		rank = intRank;
	}
	public int getRank(){
		return rank;
	}
	public void setComment(String commentString) {
		comment = commentString;
	}
	public String getComment(){
		return comment;
	}
}
