package Process;
import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;


public class ProcessAnimal{
	
	
	public static void main(String[] args) throws InterruptedException {
		Scanner s = new Scanner(System.in);
		String st;
		while(true) {
			st = s.nextLine();
			if(st.equals("choose a random number")) {
				st = s.nextLine();
				int size = Integer.parseInt(st);
				Random r = new Random();
				int number = r.nextInt(size);
				System.out.println(number);
			}
		}
//		s.close();
		
//		Thread.sleep(1000);
//		System.exit(0);
	}
}
