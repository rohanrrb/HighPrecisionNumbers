import java.util.Arrays;
import java.util.Objects; 

public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private int error; 
	private boolean negative; //change to isNegative
	
	@Override
	public String toString() {
		return "HPN: " + intPart + "." + Arrays.toString(fracPart) + "Error= " + error;
	}
	
//Constructors	
	//only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[0];
		this.error = 0; 
		this.negative = false; 
	}
	
	//int with int[] constructor
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.error = 0;
		this.negative = false; 
	}
	
	
	//String constructor
	public HPN(String s) {
		this.error = 0;
		this.negative = false; 
		
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
	
	
	/**
	 * Adds HPN and int
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, int b) {
		printHPN(a);
		System.out.println(b);
		a.intPart += b;
		return a;
	}
	
	
	/**
	 * Adds two HPNs
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, HPN b) {
		printHPN(a);
		printHPN(b);
		
		int intSum = a.intPart + b.intPart;
		HPN sum = new HPN(intSum);
		//processing
		int addition[][] = normalize(a.fracPart, b.fracPart);
		
		//initialize array for fracPart
		int[] fracSum = new int[addition[0].length]; 
		
		//adding logic
		int carry = 0;
		for(int i = fracSum.length-1; i >= 1; i--) {
			fracSum[i] = addition[0][i] + addition[1][i] + carry;
			carry = 0;
			
				if(fracSum[i] >= 10) {
					fracSum[i] = fracSum[i]%10;
					carry = 1;
				}	
		}
		
		fracSum[0] = addition[0][0] + addition[1][0] + carry;
		carry = 0;
		
		if(fracSum[0] >= 10) {
			fracSum[0] = fracSum[0]%10;
			carry = 1;
		}
		sum.intPart += carry;
		sum.fracPart = fracSum;
		
		return sum;
	}
	
	
	/**
	 * Adds two int[] 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[] add(int[] a, int[] b) {

		//processing
		int addition[][] = normalize(a, b);
		int[] sum = new int[addition[0].length];
		//initialize array for fracPart 
		
		//adding logic
		int carry = 0;
		for(int i = sum.length-1; i >= 1; i--) {
			sum[i] = addition[0][i] + addition[1][i] + carry;
			carry = 0;
			
				if(sum[i] >= 10) {
					sum[i] = sum[i]%10;
					carry = 1;
				}
		}
		
		sum[0] = addition[0][0] + addition[1][0] + carry;
		carry = 0;
		
		if(sum[0] >= 10) {
			sum[0] = sum[0]%10;
			carry = 1;
			int[] newSum = new int[sum.length + 1];
			newSum[0] = 0;
			
			for(int i = 1; i < newSum.length; i++) {
				newSum[i] = sum[i];
			}
			newSum[0] += carry; 
			return newSum; 
		}
		return sum;
	}
	
	/**
	 * Subtracts int from HPN
	 * @param a
	 * @param b
	 * @return
	 *///BUG: NEEDS TO RETURN A NEW HPN, EMPLOY A COPYHPN METHOD
	public static HPN subtract(HPN a, int b) { 
		printHPN(a);
		System.out.println(b);
		a.intPart -= b;
		return a; 
	}
	
	
	/**
	 * Uses Left to Right Subtraction 
	 * @param a
	 * @param b
	 * @return difference beteen two HPNs
	 * BUG: CANNOT CORRECTLY DISPLAY VALUES (-1,0)
	 */
	public static HPN subtract(HPN a, HPN b) {
		printHPN(a);
		printHPN(b);
		HPN result = new HPN(a.intPart - b.intPart);
		
		int[][] subtract = new int[2][0];
		subtract = normalize(a.fracPart, b.fracPart);
		int[] resultFrac = new int[subtract[0].length];
		
		//use left to right algo
		boolean[] tracker = new boolean[subtract[0].length];
		
		for(int i =0; i < subtract[0].length; i++) {
			resultFrac[i] = subtract[0][i] - subtract[1][i];
			
			if(i == 0 && resultFrac[i] < 0) {
				result.intPart--;
				resultFrac[i] += 10; 
			}
			if(resultFrac[i] < 0) {
				tracker[i-1] = true; 
				resultFrac[i] += 10; 
			}
		}
		for(int i = 0; i < subtract[0].length; i++) {
			if(tracker[i]) {
				resultFrac[i]--;
			}
		}
		result.fracPart = resultFrac;
		
		if(result.intPart < 0) {
			HPN neg = subtract(b, a);
			printHPN(neg);
			return neg;
		}
		return result;
	}
	
	
	public static HPN multiply(HPN a, int b) {
		int intProduct = a.intPart * b; 
		int[] fracProduct = new int[a.fracPart.length];

		int temp = 0;

		for (int i = fracProduct.length -1; i >= 0; i--) {
			
			 temp = a.fracPart[i]*b + temp; 
			 String tempString = ""+ Integer.toString(temp);
			 fracProduct[i] = Integer.parseInt(tempString.substring(tempString.length() -1));
			 temp = Integer.parseInt(tempString.substring(0, tempString.length()-1));
			 
//			 System.out.println(fracProduct[i]);
//			 System.out.println(temp);
			 
		}
		intProduct += temp;
		temp = 0;
			
		HPN product = new HPN(intProduct, fracProduct);
		System.out.println(Objects.toString(product));
		return product;
	}
	
	/**
	 * Fill HPN frac Parts into 2d Array and fills "empty spaces" with zeros
	 * @param a
	 * @param b
	 * @return 2d Array
	 */
	public static int[][] normalize(int[] a, int[] b){
		int[][] ordered = new int[2][];
		int diff = 0;
		
		if(a.length != b.length) {
			
			if(a.length > b.length) {
				diff = a.length - b.length;
				int[] bNorm = new int[a.length];
				
				for(int i = 0; i < b.length; i++) {
					bNorm[i] = b[i];
				}
				for(int j = b.length; j < bNorm.length; j++) {
					bNorm[j] = 0;
				}
				
				ordered[0] = a; 
				ordered[1] = bNorm; 
				return ordered;
			}
			
			else {
				diff = b.length - a.length;
				int[] aNorm = new int[b.length];
				
				for(int i = 0; i < a.length; i++) {
					aNorm[i] = a[i];
				}
				for(int j = a.length; j < aNorm.length; j++) {
					aNorm[j] = 0;
				}
				
				ordered[0] = aNorm; 
				ordered[1] = b; 
				return ordered;
			}
			
			
		}else {
			ordered[0] = a; 
			ordered[1] = b;
			return ordered;
		}
	}
	
	
	/**
	 * Prints HPN int and frac part
	 * @param a
	 */
	public static void printHPN(HPN a) {
		System.out.print(a.intPart + ".");
		for(int v : a.fracPart) {
			System.out.print(v);
		}
		System.out.println();
//		System.out.println("Error: " + a.error);
//		System.out.println();
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
		return ordered;
	}		
}
