package Thread;

import java.util.ArrayList;

import Thread.Main.AnimalThread;

public class House {
	int RowNumber;
	int ColumnNumber;
	int Id;
	int NumberOfAnimals;
	int Capacity;
	public ArrayList<AnimalThread> AnimalsInHouse = new ArrayList<AnimalThread>();
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
//		Capacity = capacity;
//		initialize();
	}
	
	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}

	public void setColumnNumber(int columnNumber) {
		ColumnNumber = columnNumber;
	}

	private void initialize() {
		neighbours = new ArrayList<House>();
//		Left = new House(0, 0);
//		UpLeft = new House(0, 0);
//		Up = new House(0, 0);
//		UpRight = new House(0, 0);
//		Right = new House(0, 0);
//		DownRight = new House(0, 0);
//		Down = new House(0, 0);
//		DownLeft = new House(0, 0);
	}
}
