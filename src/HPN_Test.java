import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("1.2", 1);
		HPN y = new HPN("1.9", 1);
		
		
		HPN result = HPN.copy(x);
		HPN.resetNumCalculations();
		
		System.out.println("---------------");
		//System.out.println(error);
		System.out.println(Objects.toString(result));
		

	}

}
