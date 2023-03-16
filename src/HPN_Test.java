import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN(".123", true);
		HPN y = new HPN(".1234", false);
		
		
		HPN result = HPN.multiply(x,0);
		
		System.out.println("---------------");
		//System.out.println(error);
		System.out.println(Objects.toString(result));

	}
}
