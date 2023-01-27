import java.util.ArrayList;

public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private int error; 
	
	//HPN int
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[0];
		this.error = 0; 
	}
	
	//String input
	public HPN(String s) {
		this.error = 0;
		
		//split input
		int decimalIndex = s.indexOf('.');
		String intPart = s.substring(0, decimalIndex);
		String fracPart = s.substring(decimalIndex + 1);
		
		int fracLength = fracPart.length(); 
		
		this.intPart = Integer.parseInt(intPart);
		this.fracPart = new int[fracLength];
		
//		System.out.println(intPart);
//		System.out.println(fracPart);
//		System.out.println(fracLength);
//		System.out.println("");
		
		//fills fracPart into array
		for(int i = 0; i < fracLength; i++) {
			this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
			System.out.print(this.fracPart[i]);
		}
		
		System.out.println();
		System.out.println(this.intPart);
		System.out.println(this.error);
	}
	
//	public static HPN add(HPN a, HPN b) {
//		
//	}
}
