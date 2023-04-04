import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("2.8999", false);
		HPN y = new HPN("3.000", false);
		
		HPN result = HPN.add(x,y);
		
		System.out.println("---------------");
		System.out.println(Objects.toString(HPN.truncate(x,y,result)));
		
		
	}
}
