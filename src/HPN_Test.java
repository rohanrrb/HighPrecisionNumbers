import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("12312.9968");
		HPN y = new HPN("00.0");
		
		
		HPN result = HPN.multiply(x, -2);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(result));
		

	}

}
