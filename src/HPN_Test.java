import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("10.0");
		HPN y = new HPN("00.0");
		
		
		HPN result = HPN.subtract(x, y);
		System.out.println(Objects.toString(result));
		

	}

}
