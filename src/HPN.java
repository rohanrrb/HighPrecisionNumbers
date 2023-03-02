import java.util.Arrays;
import java.util.Objects;

/**
 * 
 * @author Rohan Bopardikar
 * 
 *TO DO LIST 3/1:
 *
 *Subtraction doesn't work: 1.998 - 0.999
 *
 *Instead of all the if statements for sign:
 *			-copy() method to copy it over
 *			-negate() formula to simply change the sign
 *			-needed so 
 *				-code simplicity
 *				-does not change signs of original HPN
 *Multiplication Extra Zeros
 *Error handling for division
 *			-at what decimal val should non-terminating be rounded
 *			-add +1 at the very end, takes care of the rounding thing
 *Output format: +#.###±## or -#.###±###*
 *More efficient way to toString (there's a different structure: (x) ? (y)...\
 *Current sign handling for division is to complicated
 *Constructor when no decimal present
 *Make an add(HPN, int)?
 *
 */
public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private int error; 
	private boolean negative; //change to isNegative
	private static int numCalculations = 0; 
	private boolean errorIsTruncated;
	
	@Override
	public String toString() {
		if(negative) {
			if(errorIsTruncated) {
				return "HPN: -" + intPart + "." + Arrays.toString(fracPart) + "(" + error +"*)";
			}
			return "HPN: -" + intPart + "." + Arrays.toString(fracPart) + "(" + error +")";
		}else {
			if(errorIsTruncated) {
				return "HPN: " + intPart + "." + Arrays.toString(fracPart) + "(" + error +"*)";
			}
			return "HPN: " + intPart + "." + Arrays.toString(fracPart) + "(" + error +")";
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
	
	public HPN(int a, int error) {
		this.intPart = a;
		this.fracPart = new int[0];
		this.error = error; 
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
	public HPN(String s, int error) {
		this.error = error;
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
		System.out.println(a.toString() + " + " + b);
		HPN sum = new HPN(a.intPart + b); 
		sum.fracPart = a.fracPart;
		
		sum.error = a.error + 1;
		return sum;
	}
	
	
	/**
	 * Adds two HPNs
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, HPN b) {
		numCalculations++;
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
		
		sum.error = ( HPNHPNerror(a,b)).getErrorVal() + numCalculations;		
		if(HPNHPNerror(a,b).isTruncated()) {
			sum.errorIsTruncated = true;
		}
		System.out.println("NUM: " + numCalculations);
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
		numCalculations++;
		System.out.println(a.toString() + " - " + b.toString());
		HPN result = new HPN(a.intPart - b.intPart);

//		if (a.negative && !b.negative) {
//			b.negative = true; 
//			return add(a, b);
//		}
//		if(b.negative) {
//			b.negative = false; 
//			return add(a,b); 
//		}
//		
//		
//
//		
//		if(result.intPart < 0 || result.negative) {
//			HPN neg = subtract(b, a);
//			neg.negative = true; 
//			return neg;
//		}

		
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
			HPN neg = subtract(b, a);
			neg.negative = true; 
			return neg;
		}
		
		result.fracPart = resultFrac;
		
		//checkNegative(result);
		System.out.println("NUM: " + numCalculations);
		
		result.error = HPNHPNerror(a,b).getErrorVal() + numCalculations;
		if(HPNHPNerror(a,b).isTruncated()) {
			result.errorIsTruncated = true;
		}
		return result;
	}
	
	public static HPN subtract(HPN a, HPN b) {
		HPN result = new HPN(a.intPart - b.intPart);
		int[][] subtract = normalize(a.fracPart,b.fracPart);
		int[] resultFrac = new int[subtract[0].length];
		for(int i = 1; i < subtract[0].length; i++) {
			if(subtract[0][i] == 0) {
				subtract[0][i] = 10;
				subtract[0][i-1]--;
			}
			if(subtract[0][i] < subtract[1][i]) {
				subtract[0][i-1]--;
				subtract[0][i] = concat(1,subtract[0][i]);
			}
			
			resultFrac[i] = subtract[0][i] - subtract[1][i];
		}
		
		if(subtract[0][0] == 0) {
			result.intPart--;
			
		}
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
		System.out.println(a.toString() + " * " + b);
		
		if(a.negative && b > 0) {
			a.negative = false;
			HPN result = multiply(a,b); 
			result.negative = true;
			return result;
		}
		
		if (!a.negative && b<0) {
			b *= -1;
			HPN result = multiply(a,b); 
			result.negative = true;
			return result;
		}
		
		if(a.negative && b < 0) {
			a.negative = false; 
			b *= -1; 
			return multiply(a,b);
		}
		int intProduct = a.intPart * b; 
		int[] fracProduct = new int[a.fracPart.length];

		int temp = 0;

		for (int i = fracProduct.length -1; i >= 0; i--) {
			
			 temp = a.fracPart[i]*b + temp; 
			 if(temp > 9) {//new
				 String tempString = ""+ Integer.toString(temp);
				 fracProduct[i] = Integer.parseInt(tempString.substring(tempString.length() -1));
				 temp = Integer.parseInt(tempString.substring(0, tempString.length()-1));
				 if(i == 0) {
					 intProduct += temp;
				 }
			 }else {
				 fracProduct[i] = temp;
				 temp = 0;
			 }
			 
		 
		}
		
		
			
		HPN product = new HPN(intProduct, fracProduct);		
		return product;
	}
	
	/**
	 * Divides HPN by int
	 * @param a
	 * @param b
	 * @return
	 * NEEDS SOME MORE CASE TESTING...
	 * BREAKS OFF REPEATING DECIMALS AT 10 WITH i
	 * how should error work for this? clarify with dr. Heckman
	 */
	public static HPN divide(HPN a, int b) {
		System.out.println(a.toString() + " / " + b);
		
		if(a.negative && b < 0) {
			a.negative = false;
			b *= -1; 
			
			return divide(a,b);
		}
		
		if(!a.negative && b < 0) {
			b *= -1;
			HPN result = divide(a,b);
			result.negative = true;
			return result;
		}
		if(a.negative && b > 0) {
			a.negative = false;
			HPN result = divide(a,b);
			result.negative = true;
			return result;
		}
		
		
		int whole = 0; 
		int intQuotient;
		
		int dividend; 
		intQuotient = a.intPart/b;
		int intRemainder = a.intPart % b; 
		
		if (intQuotient != 0) {
			
			whole = intQuotient*b; 
			a.intPart -= whole; 
			HPN quotient = divide(a, b);
			quotient.intPart += intQuotient; 
			System.out.println("+" + intQuotient);
			return quotient; 

		}
		
		HPN remainder = new HPN(intRemainder, a.fracPart); 
		int[] rFrac = remainder.fracPart; 
		int[] qFrac = new int[rFrac.length]; 
		HPN quotient = new HPN(0, qFrac);
		
		
			
			dividend = concat(remainder.intPart, rFrac[0]);
			int miniQ = dividend/b;
			qFrac[0] = miniQ; 
			int product = miniQ * b; 
			dividend = dividend - product;
			
			int i = 1;
			while((dividend != 0 || i != rFrac.length) && i < 10) {
				if(i > rFrac.length -1){
					rFrac = expand(rFrac);
				}
				dividend = concat(dividend, rFrac[i]);
				
				miniQ = dividend/b;
				
				if(i > qFrac.length -1) {
					qFrac = expand(qFrac);
				}
				qFrac[i] = miniQ;
				
				product = miniQ *b;
				dividend = dividend - product; 
				
				i++;
			}
			quotient.fracPart = qFrac;

		return quotient;
	}
	
	public static errorArray HPNHPNerror(HPN a, HPN b) {
		int error = 0;
		boolean isTruncated = false; 
		
		int aLength = a.fracPart.length;
		int bLength = b.fracPart.length;

		double placeDiff;
		int firstError = 0;
		int secondError = 0;
		
		if(aLength == bLength) {
			error = a.error + b.error;

		}
		else if(aLength > bLength){
			placeDiff = aLength - bLength;
			if(placeDiff > 9) {
				placeDiff = 9;
				isTruncated = true;
			}
			firstError = (int)(a.error * java.lang.Math.pow(10,placeDiff));
			secondError = b.error;
			error = firstError + secondError;
		}
		else {
			placeDiff = bLength - aLength;
			if(placeDiff > 9) {
				placeDiff = 9;
				isTruncated = true;
				
			}

			firstError = (b.error * (int)java.lang.Math.pow(10,placeDiff));
			secondError = a.error;
			error = firstError + secondError;
		}
		
		errorArray Error = new errorArray(error, isTruncated);
		return Error;
	}
	
	
	public static int concat(int a, int b) {
		String s1 = Integer.toString(a);
        String s2 = Integer.toString(b);
        String s = s1 + s2;
        int c = Integer.parseInt(s);
        
        return c;
	}
	
	public static int[] expand(int[] a) {
		int[] b = new int[a.length + 1];
		for(int i = 0; i < a.length; i++) {
			b[i] = a[i]; 
		}
		
		b[b.length -1] = 0; 
		return b;  
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
	
	public static void resetNumCalculations() {
		numCalculations = 0;
	}
}
