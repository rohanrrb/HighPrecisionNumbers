import java.util.ArrayList;

public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private int error; 
	
	//only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[0];
		this.error = 0; 
	}
	
	//int with int[] constructor
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.error = 0;
	}
	
	//String constructor
	public HPN(String s) {
		this.error = 0;
		
		//split input
		int decimalIndex = s.indexOf('.');
		String intPart = s.substring(0, decimalIndex);
		String fracPart = s.substring(decimalIndex + 1);
		
		int fracLength = fracPart.length(); 
		
		this.intPart = Integer.parseInt(intPart);
		this.fracPart = new int[fracLength];
		
		System.out.print(this.intPart + ".");
		//fills fracPart into array
		for(int i = 0; i < fracLength; i++) {
			this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
			System.out.print(this.fracPart[i]);
		}
		System.out.println();
		System.out.println("Error: " + this.error);
		System.out.println();
	}
	
	public static HPN add(HPN a, int b) {
		printHPN(a);
		System.out.println(b);
		a.intPart += b;
		return a;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, HPN b) {
		printHPN(a);
		printHPN(b);
		
		int intSum = a.intPart + b.intPart;
		HPN sum = new HPN(intSum);
		//which is the longer array 
		int addition[][] = orderAdd(a.fracPart, b.fracPart);
		
		//initialize array for fracPart
		int[] fracSum = new int[addition[0].length]; 
		//copy values from bigger array that will remain untouched to fracSum
		int startAdd = addition[0].length -1 - addition[2][0];
		if(addition[2][0] != 0) {
			for(int i = addition[0].length -1; i>startAdd; i--) {
				fracSum[i] = addition[0][i];
			}
		}
		
		//adding logic
		int carry = 0;
		for(int i = startAdd; i > 0; i--) {
			fracSum[i] = addition[0][i] + addition[1][i] + carry;
			carry = 0;
			
				if(fracSum[i] >= 10) {
					fracSum[i] = fracSum[i]%10;
					carry = 1;
				}
			
			
		}
		
		fracSum[0] = addition[0][0] + addition[1][0] + carry;
		
		if(fracSum[0] >= 10) {
			fracSum[0] = fracSum[0]%10;
			carry = 1;
		}
		sum.intPart += carry;
		sum.fracPart = fracSum;
		
		return sum;
	}
	/**
	 * 
	 * @param a
	 * @param b
	 * @return jagged array:
	 * 0: larger array
	 * 1: smaller array
	 * 2: length difference
	 */
	public static int[][] orderAdd(int[] a, int[] b){
		int[][] ordered = new int[3][];
		ordered[2] = new int[1]; 
		
		if( b.length > a.length) {
			ordered[0] = b; 
			ordered[1] = a;
			ordered[2][0] = b.length - a.length; 
		}
		else {
			ordered[0] = a; 
			ordered[1] = b;
			ordered[2][0] = a.length - b.length; 
		}
		
		/*Print 2d Array
		 * for (int i = 0; i < ordered.length; i++) { for (int j = 0; j <
		 * ordered[i].length; j++) System.out.print(ordered[i][j] + " ");
		 * System.out.println(); }
		 */
		 
		
		return ordered;
	}
	
	public static HPN subtract(HPN a, int b) {
		printHPN(a);
		System.out.println(b);
		a.intPart -= b;
		return a; 
	}
	
	/**
	 * 
	 * @param a
	 */
	public static void printHPN(HPN a) {
		System.out.print(a.intPart + ".");
		for(int v : a.fracPart) {
			System.out.print(v);
		}
		System.out.println();
	}
	

	
}
