import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("1.11", 1);
		HPN y = new HPN("1.111111111111", 1);
		
		
		HPN result = HPN.add(x, y);
		HPN.resetNumCalculations();
		
		System.out.println("---------------");
		//System.out.println(error);
		System.out.println(Objects.toString(result));
		

	}

}
