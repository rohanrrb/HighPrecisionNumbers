import java.util.Arrays;
import java.util.Objects;

/**
 * HIGH PRECISION NUMBER PROJECT (HPN)
 * @author Rohan Bopardikar
 * 
 *TO DO LIST 3/25:
 *

 *More efficient way to toString (there's a different structure: (x) ? (y)...\
 *Current sign handling for division is to complicated
 *Constructor when no decimal present 
 *
 *Questions:
 *". When truncating an exact HPN, the answer will be exact iff (if and
only if) all digits removed are 0s"
	If HPN a is inExact and only zeros are removed, it is to stay inExact right?
 */
public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private boolean isNegative; //change to isNegative
	private boolean isTruncated;
	
	private boolean isExact; 
	private int precision; 

	
	
	@Override
	public String toString() {
		String exact = "";
		if(isExact) {
			exact = "exact";
		}else {
			exact = "not exact";
		}
		
		String fracNum = "";
		
		for(int i = 0; i < fracPart.length; i++) {
			fracNum += fracPart[i];
		}
		
		
		if(isNegative) {
			if(isTruncated) {
				return "-" + intPart + "." + fracNum + "(" + exact  +")" + "("+precision+")*";
			}
			return "-" + intPart + "." + fracNum + "(" + exact +")" + "("+precision+")";
		}else {
			if(isTruncated) {
				return  intPart + "." + fracNum + "(" + exact +")" + "("+precision+")*";
			}
			return  intPart + "." + fracNum + "(" + exact +")" + "("+precision+")";
		}
	}
	
//Constructors	
	//only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[]{0};
		this.isNegative = false; 
		checkNegative(this);
		this.isExact = true; 
		this.precision = 0; 
		
	}
	
	//int with int[] constructor
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.isNegative = false; 
		checkNegative(this);
		this.isExact = true; 
		this.precision = b.length; 
	}
	
	
	//String constructor
	public HPN(String s) {
		this.isNegative = false; 
		//split input
		int decimalIndex = s.indexOf('.');
		if(decimalIndex != 0) {
			String intPart = s.substring(0, decimalIndex);
			this.intPart = Integer.parseInt(intPart);
		}else if(decimalIndex < 0) {
			this.intPart = Integer.parseInt(s);
		}
		else {
			intPart = 0;
		}
		String fracPart = s.substring(decimalIndex + 1);
		
		int fracLength = fracPart.length(); 
		
		
		this.fracPart = new int[fracLength];

		//fills fracPart into array
		for(int i = 0; i < fracLength; i++) {
			this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
		}
		if(s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);
		this.precision = this.fracPart.length;
		this.isExact = true;
	}
	

	
	public HPN(String s, boolean isExact) {
		this.isNegative = false; 
		//split input
		int decimalIndex = s.indexOf('.');
		
		String fracPart = s.substring(decimalIndex + 1);
		
		int fracLength = fracPart.length(); 
		
		if(decimalIndex < 0) {
			String intPart = s.substring(0, decimalIndex);
			this.intPart = Integer.parseInt(intPart);
		}else if(decimalIndex < 0) {
			this.intPart = Integer.parseInt(s);
		}
		else {
			intPart = 0;
		}
		
		
		this.fracPart = new int[fracLength];

		//fills fracPart into array
		for(int i = 0; i < fracLength; i++) {
			this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
		}
		if(s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);
		this.precision = this.fracPart.length; 
		this.isExact = isExact; 
		
	}
	
	
	
	static final HPN zero = new HPN("0.0");
	
	
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
		checkNegative(sum);
		if(!a.isExact) {
			sum.isExact = false;
		}
		return sum;
	}
	
	public static HPN add(int a, HPN b) {
		return add(b,a);
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
		
		if(a.isNegative && !b.isNegative) {
			return subtract(b, negate(copy(a)));
		}
		
		if(!a.isNegative && b.isNegative) {
			return subtract(a, negate(copy(b)));
		}
		if(a.isNegative && b.isNegative) {
			return negate(add(negate(copy(a)),negate(copy(b))));
		}
		
		//processing
		int addition[][] = padZeros(a.fracPart, b.fracPart);
		
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
		
		if(!a.isExact || !b.isExact) {
			sum.isExact = false;
		}
		

		sum.precision = calculatePrecision(a,b);
		
		//,how many digits have been cut off
		truncate(sum,a.fracPart.length - a.precision);
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
	
	
	public static HPN subtract(HPN a, HPN b) {
		System.out.println(a.toString() + " - " + b.toString());
		
		HPN result = new HPN(a.intPart - b.intPart);
		
		int[][] subtract = padZeros(a.fracPart,b.fracPart);
		int[] resultFrac = new int[subtract[0].length];
		
		for(int i = subtract[0].length - 1; i > 0; i--) {

			if(subtract[0][i] == -1) {
				subtract[0][i] = 9;
				subtract[0][i-1]--;
			}
			if(subtract[0][i] < subtract[1][i]) {
				subtract[0][i-1]--;
				subtract[0][i] = concat(1,subtract[0][i]);
			}
				resultFrac[i] = subtract[0][i] - subtract[1][i];
		}

		if(subtract[0][0] < 0) {
			subtract[0][0] = 9;
			a.intPart--;
		}
		
		if(subtract[0][0] < subtract[1][0]) {
			a.intPart--;
			subtract[0][0] = concat(1,subtract[0][0]);
		}
		
		resultFrac[0] = subtract[0][0] - subtract[1][0];
		result.fracPart = resultFrac;
		result.intPart = a.intPart - b.intPart; 
		
		checkNegative(result);
		
		if(result.isNegative) {
			return(negate(subtract(b,a))); 
		}
		
		result.precision = calculatePrecision(a,b);
		return result;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * Issue? Returns a 0 at the end for some cases ex) 20
	 * Sign handling not updated
	 */
	public static HPN multiply(HPN a, int b) {
		System.out.println(a.toString() + " * " + b);
		
		if(a.isNegative && b > 0) {
			a.isNegative = false;
			HPN result = multiply(a,b); 
			result.isNegative = true;
			return result;
		}
		
		if (!a.isNegative && b<0) {
			b *= -1;
			HPN result = multiply(a,b); 
			result.isNegative = true;
			return result;
		}
		
		if(a.isNegative && b < 0) {
			a.isNegative = false; 
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
		if(b == 0) {
			return zero;
		}
		return product;
	}
	
	
	public static HPN divide(HPN a, int b) {
		System.out.println(a.toString() + " / " + b);
		boolean exact = true;

		if(a.isNegative && b < 0) {
			a.isNegative = false;
			b *= -1; 
			
			return divide(a,b);
		}
		
		if(!a.isNegative && b < 0) {
			b *= -1;
			HPN result = divide(a,b);
			result.isNegative = true;
			return result;
		}
		if(a.isNegative && b > 0) {
			a.isNegative = false;
			HPN result = divide(a,b);
			result.isNegative = true;
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
				
				if(i == 9) {
					exact = false;
				}
				i++;
			}
			quotient.fracPart = qFrac;
			
		quotient.isExact = exact;
		updatePrecision(quotient);
		return quotient;
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
	public static int[][] padZeros(int[] a, int[] b){
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
		if(!a.isNegative && a.intPart < 0) {
			a.isNegative = true; 
			a.intPart *= -1; 
		}
	}
	
	public static HPN negate(HPN a) {
		if(a.isNegative) {
			a.isNegative = false;
		}
		else {
			a.isNegative = true;
		}
		
		return a;
	}
	
	
	public static HPN copy(HPN a) {
		HPN b = new HPN(a.intPart);
		b.isExact = a.isExact;
		b.fracPart = a.fracPart;
		b.isNegative = a.isNegative;
		b.isTruncated = a.isTruncated;
		updatePrecision(a);
		return b; 
	}
	
	/**
	 * Adds two int[] 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[] add(int[] a, int[] b) {

		//processing
		int addition[][] = padZeros(a, b);
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
	
	public static void updatePrecision(HPN a) {
		a.precision = a.fracPart.length; 
	}
	
	public static int calculatePrecision(HPN a, HPN b) {
		int aP = a.precision;
		int bP = b.precision;
		
		if(a.isExact || b.isExact) {
			//max
			if(aP > bP) {
				return aP;
			}else {
				return bP;
			}
		}else {
			//min
			if(aP > bP) {
				return bP;
			}else {
				return aP;
			}
		}
	}
	
	public static void truncate(HPN a, int diff) {

		if(diff!=0) {

			if(a.isExact) {
				for(int i = a.fracPart.length-1; i >= a.fracPart.length-diff; i--) {
					if(a.fracPart[i] != 0) {
						System.out.println("chopping off non-zeros");
						a.isExact = false;
					}
				}
			}
			int[] replacement = new int[a.precision];
			
			for(int i = 0; i < replacement.length; i++) {
				replacement[i] = a.fracPart[i]; 
			}
			
			a.fracPart = replacement; 
			a.isTruncated = true;
		}
	}
	
	public static void applyPrecision(HPN a) {

		int diff = a.fracPart.length - a.precision ;

		if(diff!=0) {
			System.out.println("enter");
			if(a.isExact) {
				for(int i = a.fracPart.length; i >= a.fracPart.length-diff; i--) {
					if(a.fracPart[i] != 0) {
						System.out.println("chopping off non-zeros");
						a.isExact = false;
					}
				}
			}
			int[] replacement = new int[a.precision];
			
			for(int i = 0; i < replacement.length; i++) {
				replacement[i] = a.fracPart[i]; 
			}
			
			a.fracPart = replacement; 
			if(a.isExact == false) {
				a.isTruncated = true;
			}
		}
	}
	
	public static HPN geometricSum(int a, int b) {
		String series = "";
		 // 1 + (a/b) + (a/b)^2 + ...
//		check for convergence
		if ((Math.abs(a) > Math.abs(b) && b >= 0) || a == b) {
			return null;
		}else {
			System.out.println("converges");
		}
        HPN sum = copy(zero);
        HPN term = new HPN(1);

        while(isZero(term) == false) {
        	series += term + ", "; 
        	System.out.println(Objects.toString("sum: " +sum));
        	sum = add(sum, term);
        	term = multiply(term, a);
        	term = divide(term, b);
        	
        }
        System.out.println(series);
        return sum;
	}
	
	//return e
	
	
	
	public static int factorial(int a) {
		int product = 1; 
		if( a < 1) {
			return 1; 
		}else {
			for(int i = a; i > 0; i--) {
				System.out.println(i);
				product *= i;
			}
			
			return product;
		}
	}
	
	private static boolean isZero(HPN term) {
		if(term.intPart != 0) {
			return false;
		}
		for(int i = 0; i < term.fracPart.length; i++) {
			if(term.fracPart[i] != 0) {
				return false;
			}
		}
		return true;
	}

	//Getters & Setters
	public int getInt() {
		return this.intPart;
	}
	public void setInt(int intPart) {
		this.intPart = intPart;
	}
	public int[] getFracPart() {
		return this.fracPart;
	}
	public void setFracPart(int[] fracPart) {
		this.fracPart = fracPart;
	}

	public boolean getIsNegative() {
		return this.isNegative;
	}
	public void setIsNegative(boolean isNegative) {
		this.isNegative = isNegative;
	}


}