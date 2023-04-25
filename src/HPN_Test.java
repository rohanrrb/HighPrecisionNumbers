import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN(".5", true);
		HPN y = new HPN("-0.5", false);
		
		long begin = System.currentTimeMillis();
		
		HPN.pi2();
		
		long end = System.currentTimeMillis();
		long time = end - begin;
		System.out.println();
		System.out.println("Elapsed Time: " + time + " ms");
		
		//System.out.println(HPN.add(x,y));
		//System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		//System.out.println(HPN.printAns(result));
		
		
	}
}
