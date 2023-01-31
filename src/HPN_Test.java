
public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HPN x = new HPN("2.34569383");
		HPN y = new HPN("2.895678");
		
		HPN result = HPN.subtract(x, 2);
		HPN.printHPN(result);

	}

}
