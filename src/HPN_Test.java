import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("2.03", false);
		HPN y = new HPN("3.0", false);
		
		HPN result = HPN.geometricSum(2,1);
		
		System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		System.out.println(Objects.toString(result));
		
		
	}
}
