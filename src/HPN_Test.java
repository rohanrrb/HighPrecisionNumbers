import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("1.0", false);
		HPN y = new HPN("-0.5", false);
		
		HPN result = HPN.ln2();
		
		System.out.println("---------------");
		//System.out.println(Objects.toString(x));
		System.out.println(Objects.toString(result));
		
		
	}
}
