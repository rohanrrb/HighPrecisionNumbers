import java.util.Objects; 

public class HPN_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HPN x = new HPN("0.111", 1);
		HPN y = new HPN("0.222222", 1);
		
		
		//HPN result = HPN.add(x, y);
		int error = HPN.HPNHPNerror(x, y);
		
		System.out.println("---------------");
		System.out.println(error);
		//System.out.println(Objects.toString(result));
		

	}

}
