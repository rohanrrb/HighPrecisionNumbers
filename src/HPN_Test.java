import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("-1.35");
		HPN y = new HPN("00.0");
		
		
		HPN result = HPN.divide(x, -5);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(result));
		

	}

}
