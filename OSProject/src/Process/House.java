package Process;

import java.util.ArrayList;


public class House {
	int RowNumber;
	int ColumnNumber;
	int Id;
	int NumberOfAnimals;
	public ArrayList<Process> AnimalsInHouse = new ArrayList<Process>();
	public House Left;
	public House UpLeft;
	public House Up;
	public House UpRight;
	public House Right;
	public House DownRight;
	public House Down;
	public House DownLeft;
	public ArrayList<House> neighbours = new ArrayList<House>();
	public House(int id, int num){
		Id = id;
		NumberOfAnimals = num;
	}
	
	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}

	public void setColumnNumber(int columnNumber) {
		ColumnNumber = columnNumber;
	}

}
