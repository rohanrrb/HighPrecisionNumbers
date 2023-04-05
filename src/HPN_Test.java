import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("3.4728", 4);
		HPN y = new HPN("-0.5", false);
		
		HPN result = HPN.divide(x,2);
		
		System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		System.out.println(Objects.toString(result));
		
		
	}
}
