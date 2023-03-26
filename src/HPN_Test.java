import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("0.2345", true);
		HPN y = new HPN("0.111", true);
		
		HPN result = HPN.add(x,y);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(result));
		
		
	}
}
