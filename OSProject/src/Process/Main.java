package Process;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import Process.House;

public class Main {
	static int x = 0;
	public static int r;
	public static int s;
	public static int m;
	public static int k;
	public static int n;
	public static int t;
	Scanner scanner;
	static boolean Tabsare;
	static Map<Integer, ArrayList<House>> EachIdLocations;
	static PrintWriter pw;
	public static House[][] A;
	private static Map<Process, Integer> animals;
	static Semaphore semaphore = new Semaphore(1, true);
	
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
		animals = new HashMap<Process, Integer>();
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
				try {
					Process p = Runtime.getRuntime().exec("java  -cp /home/fsh/eclipse-workspace/OSProject/bin Process.ProcessAnimal");
//					pw = new PrintWriter(p.getOutputStream());
//					pw.println(n + " " + m);
//					pw.println("set house");
//					pw.println(x + " " + y);
					ar.add(ho);
					animals.put(p, i);
					ho.AnimalsInHouse.add(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
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
//							mutex.acquire();
							for(Integer key: EachIdLocations.keySet()) {
								for(int i = 0; i < EachIdLocations.get(key).size(); i++) {
									if(EachIdLocations.get(key).get(i).NumberOfAnimals > Math.floor(k/key)) {
										int num = EachIdLocations.get(key).get(i).NumberOfAnimals - (int)Math.floor(k/key);
										Random r = new Random();
										System.out.println(key + " before " + EachIdLocations.get(key).get(i).NumberOfAnimals);
										for(int j = 0; j < num; j++) {
//											System.out.println("yes");
											int in = r.nextInt(EachIdLocations.get(key).get(i).AnimalsInHouse.size());
											Process toremove = EachIdLocations.get(key).get(i).AnimalsInHouse.get(in);
											toremove.destroy();
											animals.remove(toremove);
											EachIdLocations.get(key).get(i).AnimalsInHouse.remove(toremove);
											EachIdLocations.get(key).get(i).NumberOfAnimals--;
										}
										System.out.println(key + " after " + EachIdLocations.get(key).get(i).NumberOfAnimals);
//										System.out.println(key + " removed" );
									}
								}
								
							}
//							mutex.release();
//							System.out.println("remove done" );
//							mutex.acquire();
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
												h.AnimalsInHouse.forEach((a) -> a.destroy());
												h.AnimalsInHouse.removeAll(h.AnimalsInHouse);
//												System.out.println(ho.Id + " killed " + h.Id + " in (" + h.RowNumber + ", " + h.ColumnNumber + ")");
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
												h.AnimalsInHouse.forEach((a) -> a.destroy());
												h.AnimalsInHouse.removeAll(h.AnimalsInHouse);
												System.out.println(id + " killed " + h.Id + " in (" + h.RowNumber + ", " + h.ColumnNumber + ")");
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
//							System.out.println("killing done");
						}
//							mutex.release();
							System.out.println("killing done");
//						}
						semaphore.release();
						semaphore.acquire();
						if(x%t == 0 && x!= 0) {
							for(House[] h : A) {
								for(House house : h) {
									System.out.print("(Type: " + house.Id + ", number: " + house.NumberOfAnimals + ")	");
								}
								System.out.println();
							}
//							System.out.println(t + " times left");
						}
						semaphore.release();
						semaphore.acquire();
						if(x != 0) {
							ArrayList<Process> news = new ArrayList<Process>();
							ArrayList<Process> NewInHouse;
							for(Integer key: EachIdLocations.keySet()) {
								if(x%key == 0) {
//									System.out.println(key + " " + EachIdLocations.get(key).size());
									for(int k = 0; k < EachIdLocations.get(key).size(); k++) {
										NewInHouse = new ArrayList<Process>();
										for(int i = 0; i < EachIdLocations.get(key).get(k).NumberOfAnimals; i++){
											Process p = null;
											try {
												p = Runtime.getRuntime().exec("java  -cp /home/fsh/eclipse-workspace/OSProject/bin Process.ProcessAnimal");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											animals.put(p, key);
//											animals.get(i).house.NumberOfAnimals++;
//											th.setHouse(EachIdLocations.get(key).get(k));
											NewInHouse.add(p);
//											System.out.println("new of " + th.id);
										}
										for(int j = 0; j < NewInHouse.size(); j++) {
											EachIdLocations.get(key).get(k).NumberOfAnimals++;
//											animals.addAll(NewInHouse);
										}
										EachIdLocations.get(key).get(k).AnimalsInHouse.addAll(NewInHouse);
										news.addAll(NewInHouse);
									}
								}
//								System.out.println(key + "" + );
							}
//							if(news.size() != 0) {
//								animals.addAll(news);
							
//							}
							
//						} 
//						for(int i = 0; i < animals.size(); i++) {
//							animals.get(i).stop = false;
//						}
						}
						semaphore.release();
						semaphore.acquire();
						for(Integer key: EachIdLocations.keySet()) {
							for(int s = 0; s < EachIdLocations.get(key).size(); s++) {
								for(int i = 0; i < EachIdLocations.get(key).get(s).NumberOfAnimals; i++){
									ArrayList<House> Condidates = new ArrayList<House>();
//									System.out.println(id + " " + house.neighbours.size());
									for(int j = 0; j < EachIdLocations.get(key).get(s).neighbours.size(); j++) {
										if((EachIdLocations.get(key).get(s).neighbours.get(j).Id == 0 || EachIdLocations.get(key).get(s).neighbours.get(j).Id == key) && EachIdLocations.get(key).get(s).neighbours.get(j).NumberOfAnimals < Math.floor(k/key)) {
											Condidates.add(EachIdLocations.get(key).get(s).neighbours.get(j));
										}
									}
									if(Condidates.size() != 0) {
										Process p = EachIdLocations.get(key).get(s).AnimalsInHouse.get(i);
										pw = new PrintWriter(p.getOutputStream());
										Scanner scanner = new Scanner(p.getInputStream());
										pw.println("choose a random number");
										pw.println(Condidates.size());
										pw.flush();
										String st = scanner.nextLine();
										int number = Integer.parseInt(st);
										EachIdLocations.get(key).get(s).NumberOfAnimals--;
										ArrayList<House> New = new ArrayList<House>();
										New = EachIdLocations.get(key);
										EachIdLocations.get(key).get(s).AnimalsInHouse.remove(p);
										if(EachIdLocations.get(key).get(s).NumberOfAnimals == 0) {
											EachIdLocations.get(key).get(s).Id = 0;
											New.remove(EachIdLocations.get(key).get(s));
										}
//										System.out.println(key + " last house: (" + EachIdLocations.get(key).get(s).RowNumber + ", " + EachIdLocations.get(key).get(s).ColumnNumber + ") " + EachIdLocations.get(key).get(s).NumberOfAnimals);
										House house = Condidates.get(number);
										house.NumberOfAnimals++;
										house.AnimalsInHouse.add(p);
										if(house.Id == 0) {
											house.Id = key;
											SetHouseNeighbors(house, house.RowNumber, house.ColumnNumber, A, n, m);
										}
										if(!EachIdLocations.get(key).contains(house))
											New.add(house);
										EachIdLocations.replace(key, New);
										
//										System.out.println(EachIdLocations.get(key).get(s) + " new house: (" + house.RowNumber + ", " + house.ColumnNumber + ") " + house.NumberOfAnimals);
									}
								}
							}
						}
//						moved++;
//						moved = true;
					semaphore.release();
						
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				x++;
			}
		}, 0, 1000);
		
//		for(int i = 0; i < animals.size(); i++) {
//			animals.get(i).start();
//		}
		
//		for(int i = 0; i < animals.size(); i++) {
			
//		}
		
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
			ho.neighbours.add( A[x-1][y]);
		}
		
	}
}
