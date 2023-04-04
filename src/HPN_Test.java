import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("2.8999", false);
		HPN y = new HPN("3.0", false);
		
		HPN result = HPN.add(x,2);
		
		System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		System.out.println(Objects.toString(result));
		
		
	}
}
