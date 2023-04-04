import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("2.1431");
		HPN y = new HPN("3.999", true);
		
		HPN result = HPN.subtract(x,y);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(result));
		
		
	}
}
