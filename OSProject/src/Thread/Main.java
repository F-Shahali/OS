package Thread;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class Main {
	
	static int r;					// number of types
	static int s;					// base number of each type
	static int n;					// radif A
	static int m;					// sotoon A
	static int k;					// zarfiat
	static int t;
	static House[][] A;
	static ArrayList<AnimalThread> animals;
	static Map<Integer, ArrayList<House>> EachIdLocations;
	static int x = 0;
	static int moved = 0;
	static Semaphore semaphore = new Semaphore(1, true);
	static boolean Tabsare = false;
	
	static class AnimalThread extends Thread{
		
		int id;
		House house;
		boolean CanMove = true;
		
		AnimalThread(int id, House house){
			this.id = id;
			this.house = house;
		}
		
		public void setHouse(House house) {
			this.house = house;
		}
		public void run() {
			
			while(true) {
				
				try {
					
					semaphore.acquire();
						if(CanMove  == true) {
							ArrayList<House> Condidates = new ArrayList<House>();
							for(int i = 0; i < house.neighbours.size(); i++) {
								if((house.neighbours.get(i).Id == 0 || house.neighbours.get(i).Id == id) && house.neighbours.get(i).NumberOfAnimals < Math.floor(k/id)) {
									Condidates.add(house.neighbours.get(i));
								}
							}
							if(Condidates.size() != 0) {
								Random r = new Random();
								int number = r.nextInt(Condidates.size());
								house.NumberOfAnimals--;
								ArrayList<House> New = new ArrayList<House>();
								New = EachIdLocations.get(id);
								house.AnimalsInHouse.remove(this);
								if(house.NumberOfAnimals == 0) {
									house.Id = 0;
									New.remove(house);
								}
								house = Condidates.get(number);
								house.NumberOfAnimals++;
								house.AnimalsInHouse.add(this);
								if(house.Id == 0) {
									house.Id = id;
									SetHouseNeighbors(house, house.RowNumber, house.ColumnNumber, A, n, m);
								}
								if(!EachIdLocations.get(id).contains(house))
									New.add(house);
								EachIdLocations.replace(id, New);
								
							}
							CanMove = false;
						}
					semaphore.release();
					
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String st = sc.nextLine();
		if(st.equals("true"))
			Tabsare = true;
		r = sc.nextInt();					// number of types
		s = sc.nextInt();					// base number of each type
		n = sc.nextInt();					// radif A
		m = sc.nextInt();					// sotoon A
		k = sc.nextInt();					// zarfiat
		t = sc.nextInt();					// number of time to print
		

		A = new House[n][m];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				A[i][j] = new House(0, 0);
				A[i][j].setColumnNumber(j+1);
				A[i][j].setRowNumber(i+1);
			}
		}
		
		animals = new ArrayList<AnimalThread>();
		
		EachIdLocations = new HashMap<Integer,ArrayList<House>>();
		
		for(int i = 1; i <= r; i++) {
			
			int x = (int)Math.floor(n/r)*i;
			ArrayList<House> ar = new ArrayList<House>();
			
			for(int j = 1; j <= s; j++) {
				
				int y = (int)Math.floor(m/s)*j;
				House ho = new House(i, 1);
				ho.setRowNumber(x);
				ho.setColumnNumber(y);
				A[x-1][y-1] = ho;
				SetHouseNeighbors(ho, x, y, A, n, m);
				AnimalThread th = new AnimalThread(i, ho);
				ar.add(ho);
				animals.add(th);
				ho.AnimalsInHouse.add(th);
			}
			EachIdLocations.put(i, ar);
		}
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println(x);
				System.out.println(animals.size());
				
					try {
					semaphore.acquire();
					if(x != 0) {
						for(Integer key: EachIdLocations.keySet()) {
							for(int i = 0; i < EachIdLocations.get(key).size(); i++) {
								if(EachIdLocations.get(key).get(i).NumberOfAnimals > Math.floor(k/key)) {
									int num = EachIdLocations.get(key).get(i).NumberOfAnimals - (int)Math.floor(k/key);
									Random r = new Random();
									for(int j = 0; j < num; j++) {
										int in = r.nextInt(EachIdLocations.get(key).get(i).AnimalsInHouse.size());
										AnimalThread toremove = EachIdLocations.get(key).get(i).AnimalsInHouse.get(in);
										toremove.stop();
										animals.remove(toremove);
										EachIdLocations.get(key).get(i).AnimalsInHouse.remove(toremove);
										EachIdLocations.get(key).get(i).NumberOfAnimals--;
									}
									
									System.out.println(key + " after " + EachIdLocations.get(key).get(i).NumberOfAnimals);
								}
							}
							
						}
						ArrayList<Integer> TypeRemoved = new ArrayList<Integer>();
						for(Integer key: EachIdLocations.keySet()) {
							ArrayList<House> ToKill = new ArrayList<House>();
							for(House h : EachIdLocations.get(key)) {
								boolean removingdone = false;
								Map<Integer, Integer> neighbourNumber = new HashMap<Integer, Integer>();
								for(House ho : h.neighbours) {
									if(key > Math.floor(r/2) && ho.Id < key && Tabsare == true) {
										int num = 0;
										if(ho == h.Up || ho == h.Down || ho == h.Right ||ho == h.Left)
											num += ho.NumberOfAnimals;
										for(House house : h.neighbours) {
											if(house.Id == ho.Id && (house == h.Up || house == h.Down || house == h.Right ||house == h.Left)) {
												num += house.NumberOfAnimals;
											}
										}
										if(ho.Id*num > key*h.NumberOfAnimals) {
											h.AnimalsInHouse.forEach((a) -> animals.remove(a));
											h.AnimalsInHouse.forEach((a) -> a.stop());
											h.AnimalsInHouse.removeAll(h.AnimalsInHouse);
											ToKill.add(h);
											h.Id = 0;
											h.NumberOfAnimals = 0;
											removingdone = true;
											break;
										}
									}
									else
										if(Tabsare == true) {
											if(ho.Id != 0 && ho.Id > h.Id ) {
												if(neighbourNumber.containsKey(ho.Id))
													neighbourNumber.replace(ho.Id, neighbourNumber.get(ho.Id)+ho.NumberOfAnimals);
												else
													neighbourNumber.put(ho.Id, ho.NumberOfAnimals);
											}
											
										}
										else
											if(ho.Id != 0 && ho.Id != h.Id ) {
												if(neighbourNumber.containsKey(ho.Id))
													neighbourNumber.replace(ho.Id, neighbourNumber.get(ho.Id)+ho.NumberOfAnimals);
												else
													neighbourNumber.put(ho.Id, ho.NumberOfAnimals);
											}
								}
								if(removingdone == false) {
									for(Integer id : neighbourNumber.keySet()) {
										if(id*neighbourNumber.get(id) > key*h.NumberOfAnimals) {
											h.AnimalsInHouse.forEach((a) -> animals.remove(a));
											h.AnimalsInHouse.forEach((a) -> a.stop());
											h.AnimalsInHouse.removeAll(h.AnimalsInHouse);
											ToKill.add(h);
											h.Id = 0;
											h.NumberOfAnimals = 0;
											
											break;
										}
									}
								}
							}
							EachIdLocations.get(key).removeAll(ToKill);
							if(EachIdLocations.get(key).size() == 0) {
								TypeRemoved.add(key);
							}
						}
						if(TypeRemoved.size() != 0)
							TypeRemoved.forEach((a) -> EachIdLocations.remove(a));
					}
					semaphore.release();
					semaphore.acquire();
						if(x%t == 0 && x!= 0) {
							for(House[] h : A) {
								for(House house : h) {
									System.out.print("(Type: " + house.Id + ", number: " + house.NumberOfAnimals + ")	");
								}
								System.out.println();
							}
						}
					semaphore.release();
					semaphore.acquire();
						if(x != 0) {
							ArrayList<AnimalThread> news = new ArrayList<Main.AnimalThread>();
							ArrayList<AnimalThread> NewInHouse;
							for(Integer key: EachIdLocations.keySet()) {
							
								if(x%key == 0) {
									for(int k = 0; k < EachIdLocations.get(key).size(); k++) {
										NewInHouse = new ArrayList<AnimalThread>();
										for(int i = 0; i < EachIdLocations.get(key).get(k).NumberOfAnimals; i++){
											AnimalThread th = new AnimalThread(key, EachIdLocations.get(key).get(k));
											animals.add(th);
											th.setHouse(EachIdLocations.get(key).get(k));
											NewInHouse.add(th);
										}
										for(int j = 0; j < NewInHouse.size(); j++) {
											EachIdLocations.get(key).get(k).NumberOfAnimals++;
										}
										EachIdLocations.get(key).get(k).AnimalsInHouse.addAll(NewInHouse);
										news.addAll(NewInHouse);
									}
								}
							}
							
								for(int i = 0; i < news.size(); i++) {
									news.get(i).start();
								}
						}
						semaphore.release();
						for(AnimalThread animal : animals) {
							animal.CanMove = true;
						}
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				x++;
			}
		}, 0, 1000);
		
		for(int i = 0; i < animals.size(); i++) {
			animals.get(i).start();
//			animals.get(i).CanMove = true;
		}
		
	}

	public static void SetHouseNeighbors(House ho, int x, int y, House[][] A, int n, int m) {
		if(x-1 != 0) {
			ho.Up = new House(0, 0);
			ho.Up = A[x-2][y-1];
			ho.neighbours.add(A[x-2][y-1]);
			if(y-1 != 0) {
				ho.UpLeft = new House(0, 0);
				ho.UpLeft = A[x-2][y-2];
				ho.neighbours.add(A[x-2][y-2]);
			}
			if(y+1 != m+1) {
				ho.UpRight = new House(0, 0);
				ho.UpRight = A[x-2][y];
				ho.neighbours.add(A[x-2][y]);
			}
		}
		
		if(x+1 != n+1) {
			ho.Down = new House(0, 0);
			ho.Down = A[x][y-1];
			ho.neighbours.add(A[x][y-1]);
			if(y-1 != 0) {
				ho.DownLeft = new House(0, 0);
				ho.DownLeft = A[x][y-2];
				ho.neighbours.add(A[x][y-2]);
			}
			if(y+1 != m+1) {
				ho.DownRight = new House(0, 0);
				ho.DownRight = A[x][y];
				ho.neighbours.add(A[x][y]);
			}
		}
		
		if(y-1 != 0) {
			ho.Left = new House(0, 0);
			ho.Left = A[x-1][y-2];
			ho.neighbours.add(A[x-1][y-2]);
		}
		
		if(y+1 != m+1) {
			ho.Right = new House(0, 0);
			ho.Right = A[x-1][y];
			ho.neighbours.add(A[x-1][y]);
		}
		
	}
}
