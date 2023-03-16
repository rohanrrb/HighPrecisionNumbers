import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("1.1230000000000000001", true);
		HPN y = new HPN(".111", true);
		
		HPN result = HPN.geometricSum(4,2);
		
		System.out.println("---------------");
		//System.out.println(error);
		System.out.println(Objects.toString(result));
	}
}
