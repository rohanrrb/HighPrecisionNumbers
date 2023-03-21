import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("123", true);
		HPN y = new HPN(".111", true);
		
		HPN result = HPN.geometricSum(2,3);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(result));
		
		
	}
}
