import java.util.Arrays;
import java.util.Objects; 
/**
 * 
 * @author rohan
 *TO DO LIST 2/9:
 *
 *All methods need to return a new HPN by default. maybe create an AddTo method for other cases. 
 *ERROR CALCULATION
 *rewrite normalize description
 *Review meeting notes add to list
 *
 */
public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private int error; 
	private boolean negative; //change to isNegative
	
	@Override
	public String toString() {
		if(negative) {
			return "HPN: -" + intPart + "." + Arrays.toString(fracPart) + " Error(" + error +")";
		}else {
			return "HPN: " + intPart + "." + Arrays.toString(fracPart) + " Error(" + error +")";
		}
	}
	
//Constructors	
	//only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[0];
		this.error = 0; 
		this.negative = false; 
		
		checkNegative(this);
		
	}
	
	//int with int[] constructor
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.error = 0;
		this.negative = false; 
		
		checkNegative(this);
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

		//fills fracPart into array
		for(int i = 0; i < fracLength; i++) {
			this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
		}
		if(s.charAt(0) == '-') {
			this.negative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		
		checkNegative(this);
		
	}
	
	
	/**
	 * Adds HPN and int
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, int b) {
		System.out.print(a.toString() + " + b= ");
		HPN sum = new HPN(a.intPart + b); 
		sum.fracPart = a.fracPart;
		
		return sum;
	}
	
	
	/**
	 * Adds two HPNs
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, HPN b) {
		int intSum = a.intPart + b.intPart;
		HPN sum = new HPN(intSum);
		
		System.out.println(a.toString() + " + " + b.toString());
		
		if(a.negative && !b.negative) {
			a.negative = false; 
			return subtract(b, a);
		}
		
		if(!a.negative && b.negative) {
			b.negative = false; 
			return subtract(a,b);
		}
		if(a.negative && b.negative) {
			a.negative = false; 
			b.negative = false;
			
			sum = add(a, b);
			sum.negative = true; 
			return sum; 
		}
		

		
		
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
		//checkNegative(sum);
		
		return sum;
	}

	
	/**
	 * Subtracts int from HPN
	 * @param a
	 * @param b -> new HPN
	 * @return
	 */
	public static HPN subtract(HPN a, int b) { 
		HPN B = new HPN(b); 
		return subtract(a,B);
	}
	
	
	/**
	 * Uses Left to Right Subtraction 
	 * @param a
	 * @param b
	 * @return difference between two HPNs
	 */
	public static HPN subtract(HPN a, HPN b) {
		System.out.println(a.toString() + " - " + b.toString());

		if (a.negative && !b.negative) {
			b.negative = true; 
			return add(a, b);
		}
		if(b.negative) {
			b.negative = false; 
			return add(a,b); 
		}
		
		HPN result = new HPN(a.intPart - b.intPart);

		
		if(result.intPart < 0 || result.negative) {
			HPN neg = subtract(b, a);
			neg.negative = true; 
			return neg;
		}

		
		int[][] subtract = new int[2][0];
		subtract = normalize(a.fracPart, b.fracPart);
		int[] resultFrac = new int[subtract[0].length];
		
		//use left to right algo
		boolean[] tracker = new boolean[subtract[0].length];
		
		for(int i =0; i < subtract[0].length; i++) {
			resultFrac[i] = subtract[0][i] - subtract[1][i];
			
			if(i == 0 && resultFrac[i] < 0) {
				if(result.negative) {
					result.intPart++;
				}
				else{
					result.intPart--;
				}
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
		
		if(result.intPart < 0 || result.negative) {
			System.out.print("switchSWITCH");
			HPN neg = subtract(b, a);
			neg.negative = true; 
			return neg;
		}
		
		result.fracPart = resultFrac;
		
		//checkNegative(result);
		return result;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * Issue? Returns a 0 at the end for some cases ex) 20
	 */
	public static HPN multiply(HPN a, int b) {
		int intProduct = a.intPart * b; 
		int[] fracProduct = new int[a.fracPart.length];

		int temp = 0;

		for (int i = fracProduct.length -1; i >= 0; i--) {
			
			 temp = a.fracPart[i]*b + temp; 
			 String tempString = ""+ Integer.toString(temp);
			 fracProduct[i] = Integer.parseInt(tempString.substring(tempString.length() -1));
			 temp = Integer.parseInt(tempString.substring(0, tempString.length()-1));
		 
		}
		intProduct += temp;
		temp = 0;
			
		HPN product = new HPN(intProduct, fracProduct);
		System.out.println(Objects.toString(product));
		
		checkNegative(product);
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
	
	public static void checkNegative(HPN a) {
		if(!a.negative && a.intPart < 0) {
			a.negative = true; 
			a.intPart *= -1; 
		}
		
	}
	
//	public static boolean isAdd(HPN a, HPN b) {
//		String aNeg, bNeg; 
//		if(a.negative) {
//			aNeg = "neg"; 
//		}
//		else {
//			aNeg = "pos"; 
//		}
//		
//		if(b.negative) {
//			bNeg = "neg"; 
//		}
//		else {
//			bNeg = "pos"; 
//		}
//		
//		switch(aNeg+bNeg) {
//		case "pospos":
//			return true; 
//			break; 
//		case "posneg
//			
//		}
//
//
//		
//	}

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
