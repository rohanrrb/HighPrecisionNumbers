
public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HPN x = new HPN("40.55678");
		HPN y = new HPN("30.789899");
		
		HPN result = HPN.subtract(x, y);
		HPN.printHPN(result);
		

	}

}
