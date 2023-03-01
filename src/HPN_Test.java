import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("1.998", 1);
		HPN y = new HPN("0.999", 1);
		
		
		HPN result = HPN.subtract(x, y);
		HPN.resetNumCalculations();
		
		System.out.println("---------------");
		//System.out.println(error);
		System.out.println(Objects.toString(result));
		

	}

}
