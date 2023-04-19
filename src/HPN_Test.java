import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("0.5", true);
		HPN y = new HPN("-0.5", false);
		
		HPN result = HPN.ln1x(1,2);
		
		System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		System.out.println(HPN.printAns(result));
		
		
	}
}
