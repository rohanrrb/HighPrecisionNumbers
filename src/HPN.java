import java.util.Arrays;
import java.util.Objects;

/**
 * HIGH PRECISION NUMBER PROJECT (HPN)
 * Using a custom High Precision Number object to calculate values of constants using common infinite series
 * 
 * @author Rohan Bopardikar under Dr. Christopher Heckman
 * 
 * 
 */
public class HPN {
	
	private int intPart; 
	private int[] fracPart; 
	private boolean isNegative;
	private boolean isTruncated; //needed?
	private boolean isExact; 

	
	
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
				return "-" + intPart + "." + fracNum + "(" + exact  +")" /*+ "("+this.fracPart.length+")*"*/;
			}
			return "-" + intPart + "." + fracNum + "(" + exact +")" /*+ "("+this.fracPart.length+")"*/;
		}else {
			if(isTruncated) {
				return  intPart + "." + fracNum + "(" + exact +")" /* + "("+this.fracPart.length+")*"*/;
			}
			return  intPart + "." + fracNum + "(" + exact +")" /* + "("+this.fracPart.length+")"*/;
		}
	}
	
//Constructors	
	//Only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[]{0};
		this.isNegative = false; 
		checkNegative(this);
		this.isExact = true; 
	
		
	}
	
	//False by default for a multiplication bug
	//System decision: Should constructors that do not accept
	//isExact be exact by default?
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.isNegative = false; 
		checkNegative(this);
		this.isExact = false; 

	}
	
	//String constructor
	public HPN(String s) {
		this.isNegative = false; 
		//split input
		int decimalIndex = s.indexOf('.');
		
		if(decimalIndex > 0) {
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
		if(decimalIndex >= 0) {
			for(int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
			}
		}
		
		if(s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);

		this.isExact = true;
	}
	
	public HPN(String s, int precision) {
		
		this.isNegative = false; 
		//split input
		int decimalIndex = s.indexOf('.');
		
		if(decimalIndex > 0) {
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
		if(decimalIndex >= 0) {
			for(int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
			}
		}
		
		if(s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);

		this.isExact = true;
		truncate(this, precision);
	}
	
	public HPN(String s, boolean isExact) {
		this.isNegative = false; 
		//split input
		int decimalIndex = s.indexOf('.');
		
		if(decimalIndex > 0) {
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
		if(decimalIndex >= 0) {
			for(int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i,i+1));
			}
		}
		
		if(s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);
		
		this.isExact = true;
		this.isExact = isExact; 
		
	}
	
	
	//Constants
	public final static HPN zero() {
		return new HPN("0.0", true);
	}
	public final static HPN one() {
		return new HPN("1.0", true);
	}

	
	
	/**
	 * Adds HPN and int by creating a new HPN for the int
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, int b) {	
		return add(a, new HPN(b));
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
		//Add intParts
		int intSum = a.intPart + b.intPart;
		//Create Sum HPN
		HPN sum = new HPN(intSum);
		
		//Calculation Display
		System.out.println(a.toString() + " + " + b.toString());
		
		//Check for negativity
		if(a.isNegative && !b.isNegative) {
			return subtract(b, negate(copy(a)));
		}
		
		if(!a.isNegative && b.isNegative) {
			return subtract(a, negate(copy(b)));
		}
		if(a.isNegative && b.isNegative) {
			return negate(add(negate(copy(a)),negate(copy(b))));
		}
		//Check for either adding zero
		if( a == zero()) {
			return copy(b);
		}
		if(b == zero()) {
			return copy(a);
		}
		
		//Fill into 2D array with zeros padded to match lengths
		int addition[][] = padZeros(a.fracPart, b.fracPart);
		
		//Initialize array for fracPart
		int[] fracSum = new int[addition[0].length]; 
		
		//Adding fracPart logic
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
		
		//Carry to intPart if needed
		sum.intPart += carry;
		
		//Assign added fracPart to sum
		sum.fracPart = fracSum;
		
		//If either addend is not exact, sum will follow suit
		if(!a.isExact || !b.isExact) {
			sum.isExact = false;
		}
		
		//Truncate if needed
		return truncate(a,b,sum);
	}


	/**WRITTEN BY DR. HECKMAN
	 * Subtracts two HPN's
	 * @param a
	 * @param b
	 * @return HPN result
	 */
	public static HPN subtract(HPN a,HPN b) {
		System.out.println(a.toString() + " - " + b.toString());
		/// Note that above, .toString() is unnecessary; println calls toString automatically.
		/// Thus, we can write this as System.out.println (a + " - " + b);
		
		// take care of some reductions first
		/// if there's only one command inside the block, the braces are optional.
		if (isZero(b)) 
			return copy(a); // a - 0 = a
		if (isZero(a))
			return negate(b); // 0 - b = (-b)
		if (b.isNegative)
			return add(a,negate(b)); // a - b = a + (-b)
			/// In this case, we don't care whether a < 0; add() will take care of that.
			/// I like putting spaces after commas, and before left parentheses if there's an argument.
			/// the return line above would look like return add (a, negate (b));
			/// It helps break up the formulas visually.
		if (a.isNegative)
			return negate(add(negate(a),b)); // a - b = -((-a) + b)
		
		// Now, a, b > 0. We need to check to see that a >= b, because otherwise
		//      the algorithm doesn't work.
		boolean wrongOrder = false;
		if(b.intPart > a.intPart) {
			System.out.println("uhoh");
			wrongOrder = true;
		}
		else {
			//System.out.println("else");
			for (int i = 0; i < a.fracPart.length; i++) {
				if (b.fracPart[i] > a.fracPart[i]) 
					wrongOrder = true;
				if (b.fracPart[i] != a.fracPart[i])
					break;
				} 
		}
		
		//Added by Rohan Bopardikar
		if(b.intPart < a.intPart) {
			wrongOrder = false;
		}
				/// you might want to step through this loop with a.fracPart = [1, 2, 3], b = [1, 2, 3];
				/// a = [1, 2, 3], b = [1, 4, 2]; and a = [1, 2, 3], b = [1, 1, 1] to see why this works.
		if (wrongOrder) {
			
			return negate(subtract(b, a)); // a - b = -(b - a)
		}
		
		// now we have a >= b > 0, so we can use the regular subtraction algorithm.
		HPN result = new HPN(a.intPart - b.intPart);
		int[][] subtract = padZeros(a.fracPart,b.fracPart);
		//System.out.println("padding");
		// result.fracPart will be subtract [0] after the algorithm.
		/// We don't need a new array if we change subtract [0].
		
		for(int i = subtract[0].length - 1; i > 0; i--) {
			if(subtract[1][i] > subtract[0][i]) {
				subtract[0][i-1] --;
				subtract[0][i] += 10;
				} // borrow from previous digit
			subtract[0][i] -= subtract[1][i];
			}
			
		if(subtract[1][0] > subtract[0][0]) {
			result.intPart --;
			subtract[0][0] += 10;
			} // borrow from intPart
		subtract[0][0] -= subtract[1][0];
		result.fracPart = subtract[0];

		result.isExact = a.isExact && b.isExact;
		return result;
	}

	public static HPN subtract(HPN a, int b) {
		return subtract(a, new HPN(b));
	}
	
	/**
	 * Multiplies a HPN by an int
	 * @param a
	 * @param b
	 * @return HPN product
	 */
	public static HPN multiply(HPN a, int b) {
		//Calculation Display
		System.out.println(a.toString() + " * " + b);
		
		//Multiplying by zero
		if(a == zero() || b == 0) {
			return zero();
		}
		
		//Check negativity
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
		
		//Multiply intPart
		int intProduct = a.intPart * b; 
		
		//Initialize product array & carry
		int[] fracProduct = new int[a.fracPart.length];
		int carry = 0;

	//fracPart multiplication logic
		
		//Iterating <--
		for (int i = fracProduct.length -1; i >= 0; i--) {
			 carry = a.fracPart[i]*b + carry; 
			 if(carry > 9) {
				 String carryString = ""+ Integer.toString(carry);
				 fracProduct[i] = Integer.parseInt(carryString.substring(carryString.length() -1));
				 carry = Integer.parseInt(carryString.substring(0, carryString.length()-1));
				 if(i == 0) {
					 intProduct += carry;
				 }
			 }else {
				 fracProduct[i] = carry;
				 carry = 0;
			 }
		}
		
		//Initialize product with calculated int and frac Parts
		HPN product = new HPN(intProduct, fracProduct);	

		//If HPN factor is exact, product follows
		if(a.isExact) {
			product.isExact = true;
		}
		return product;
	}
	
	/**
	 * Divides a HPN by int
	 * @param a
	 * @param b
	 * @return quotient
	 */
	public static HPN divide(HPN a, int b) {
		System.out.println(a.toString() + " / " + b);
		boolean exact = true;
		
		if(isZero(a)) {
			return zero();
		}
		
		if(b == 0 ) {
			return null;
		}
		
		//Check Negativity
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
		
		//Initialize
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
			
			//Repeat until division is complete
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
				
				//If the quotient exceeds max decimal value, stop and make not exact
				if(i == 9) {
					exact = false;
				}
				i++;
			}
			quotient.fracPart = qFrac;
			
		quotient.isExact = exact;

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
		
		//Initialize
		int[][] ordered = new int[2][];
		
	//Same Length?
		//No
		if(a.length != b.length) {
			
			//a is Longer, need to pad b
			if(a.length > b.length) {
				//New fracPart for b
				int[] bNorm = new int[a.length];
				
				//Copy values
				for(int i = 0; i < b.length; i++) {
					bNorm[i] = b[i];
				}
				
				//Fill remaining vals with 0
				for(int j = b.length; j < bNorm.length; j++) {
					bNorm[j] = 0;
				}
				
				//Assign & Return
				ordered[0] = a; 
				ordered[1] = bNorm; 
				return ordered;
			}
			//b is Longer, need to pad a
			else {
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
		//Yes
		}else {
			//Assign and return, no padding needed
			ordered[0] = a; 
			ordered[1] = b;
			return ordered;
		}
	}
	
	/**
	 * Checks and Updates sign
	 * @param a
	 */
	public static void checkNegative(HPN a) {
		if(!a.isNegative && a.intPart < 0) {
			a.isNegative = true; 
			a.intPart *= -1; 
		}
	}
	
	
	/***
	 * 
	 * @param a
	 * @return a: Returns SAME OBJECT but opposite sign
	 */
	public static HPN negate(HPN a) {
		if(a.isNegative) {
			a.isNegative = false;
		}
		else {
			a.isNegative = true;
		}
		
		return a;
	}
	
	/**
	 * 
	 * @param a
	 * @return HPN b: new object, same attributes as a
	 */
	public static HPN copy(HPN a) {
		//new HPN with same intPart
		HPN b = new HPN(a.intPart);
		
		//copy fracPart
		int bFrac [] = new int[a.fracPart.length];
		System.out.println("a length: " + a.fracPart.length);
		System.arraycopy(a.fracPart, 0, bFrac, 0, bFrac.length);
		b.fracPart = bFrac;
		
		//isExact
		boolean bExact = a.isExact;
		b.isExact = bExact;
		
		//isNegative
		boolean bNeg = a.isNegative;
		b.isNegative = bNeg;

		return b; 
	}

	/**
	 * Used in truncate()
	 * @param a
	 * @param b
	 * @return int precision
	 * 
	 * If one of the values is exact, return max precision
	 * If both are not exact, return min precision
	 */
	private static int findPrecision(HPN a, HPN b) {
		int aLength = a.fracPart.length;
		int bLength = b.fracPart.length;
		int diff = aLength - bLength;
		
		//if the same length, no truncation needed
		if (diff == 0)
			return aLength;
		
		if(!a.isExact && !b.isExact) {//shorten the long one
			if (diff > 0) {//a is longer
				return bLength;
			}else {//b is longer
				return aLength;
			}
		}else {//pad the short one
			if (diff > 0) {//b is shorter
				return aLength;
			}else {//a is shorter
				return bLength;
			}
		}
	}
	
	/**
	 * Used before returning in add() and subtract()
	 * 
	 * @param a: addend/minuend
	 * @param b: addend/subtrahend
	 * @param z: sum/difference
	 * @return z, but shortened or padded
	 * If shortened, z gets rounded
	 * 
	 *Is there even a case where z gets padded?
	 */
	public static HPN truncate(HPN a, HPN b, HPN z) {
		
		//Init
		int zLength = z.fracPart.length;
		int precision = findPrecision(a,b);
		
		//Need to cut off extra digits and round
		if(zLength > precision) {
			//Calculation Display
			System.out.println("truncating " + z + "to " + precision + " digit(s).");
			
			//Update attributes
			z.isTruncated = true;//?
			z.isExact = false;
			
			//Create new shortened array
			int[] newFrac = new int[precision];
			
			//Fill with values from original
			for(int i = 0; i < precision; i++) {
				newFrac[i] = z.fracPart[i];
			}
			
			//Rounding logic
			if(z.fracPart[precision] > 4) {
				//round
				for(int i = precision - 1; i > 0; i--) {
					
					newFrac[i]++;
					if(newFrac[i] != 10) {
						break;
					}
					newFrac[i] = 0;
					
				}
				
				if(z.fracPart[1] > 4) {
					newFrac[0]++;
				}
				
				//Tens place carries over to intPart
				if(newFrac[0] > 9) {
					newFrac[0] = 0;
					z.intPart++;
				}
			}
			
			//Assign
			z.fracPart = newFrac;
		
		//Padding logic, untested
		}else if( zLength < precision) {
			System.out.println("truncating " + z + "to " + precision + " digit(s).");
			z.isTruncated = true;
			z.isExact = false;
			int diff = precision - zLength;
			int[] newFrac = new int[precision];
			for(int i = 0; i < z.fracPart.length; i++) {
				newFrac[i] = z.fracPart[i];
			}
			for(int k= precision - diff; k < newFrac.length; k++) {
				newFrac[k] = 0;
			}
		}
		
		return z;
	}
	
	/**
	 * Truncates a HPN to given precision
	 * Needed for ("HPN", precision) and similar constructors
	 * @param z
	 * @param precision
	 * @return
	 */
	public static HPN truncate(HPN z, int precision) {

		int zLength = z.fracPart.length;
	
		if(zLength > precision) {
			z.isExact = false;
			System.out.println("truncating " + z + "to " + precision + " digit(s).");
			z.isExact = false;
			z.isTruncated = true;
			//cut off and round
			
			int[] newFrac = new int[precision];
			for(int i = 0; i < precision; i++) {
				newFrac[i] = z.fracPart[i];
			}
			
			
			if(z.fracPart[precision] > 4) {

				//round
				for(int i = precision - 1; i > 0; i--) {
					newFrac[i]++;
					if(newFrac[i] != 10) {
						break;
					}
					newFrac[i] = 0;
					
				}
				if(z.fracPart[1] > 4) {

					newFrac[0]++;
				}
				
				if(newFrac[0] > 9) {
					newFrac[0] = 0;
					z.intPart++;
				}
			}
			z.fracPart = newFrac;
			
		}else if( zLength < precision) {
			System.out.println("truncating to " + precision + " digits.");
			z.isTruncated = true;
			int diff = precision - zLength;
			int[] newFrac = new int[precision];
			for(int i = 0; i < z.fracPart.length; i++) {
				newFrac[i] = z.fracPart[i];
			}
			for(int k= precision - diff; k < newFrac.length; k++) {
				newFrac[k] = 0;
			}
		}
		return z;
	}
	
	
	/**
	 * [1,inf)SUM (a * r^n) =  1 + (a/b) + (a/b)^2 + ...
	 * a is 1
	 * @param a
	 * @param b
	 * @return sum
	 */
	public static HPN geometricSum(int a, int b) {
		String sequence = "";
		String sums = "";
		
		//check for convergence
		if ((Math.abs(a) > Math.abs(b) && b >= 0) || a == b) {
			System.out.println("DIVERGES");
			return null;
		}else {
			System.out.println("CONVERGES");
		}
        HPN sum = zero();
        HPN term = one();

        while(!isZero(term)) {
        	//Update strings
        	sequence += term + ", "; 
        	sums += sum + ", ";
        	
        	//Separating term calculations
            System.out.println("---");
            
        	sum = add(sum, term);
        	
        	//multiply by r: multiply by numerator, divide by denominator
        	term = multiply(term, a);
        	term = divide(term, b);
        	
        }
        //Calculation Display
        System.out.println("---------------");
        System.out.println("Sequence: " + sequence);
        System.out.println("Partial Sums: " + sums);
        
        return sum;
	}
	
	/**
	 * [first,inf)SUM (a * r^n) = m + m(a/b) + m(a/b)^2 + ...
	 * a is up to user
	 * @param a
	 * @param b
	 * @return sum
	 * Will be more accurate if I call origican geoSum and multiply m?
	 *
	 */
	public static HPN geometricSum(int first, int a, int b) {
		String sequence = "";
		String sums = "";
		 
		//check for convergence
		if ((Math.abs(a) > Math.abs(b) && b >= 0) || a == b) {
			System.out.println("DIVERGES");
			return null;
		}else {
			System.out.println("CONVERGES");
		}
        HPN sum = zero();
        HPN term = new HPN(first);

        while(!isZero(term)) {
        	//Update strings
        	sequence += term + ", "; 
        	sums += sum + ", ";
        	
        	//Separating term calculations
            System.out.println("---");
            
        	sum = add(sum, term);
        	
        	//multiply by r: multiply by numerator, divide by denominator
        	term = multiply(term, a);
        	term = divide(term, b);
        	
        }
        //Calculation Display
        System.out.println("---------------");
        System.out.println("Sequence: " + sequence);
        System.out.println("Partial Sums: " + sums);
        
        return sum;
	}
	
	//return e using eToX()
	public static HPN e() {
		return eToX(1);
	}
	
	/**
	 * e^x = [0,inf)SUM (x^n/n!) = 1 + x + (x^2/2!) + (x^3/3!) + ...
	 * @param x
	 * @return
	 */
	public static HPN eToX(int x) {
		//No convergence check, RC = inf
		//Edge cases
		if(x == 0) {
			return one();
		}
		if(x < 0) {
			x = 0;
		}
		
		//init
		String sequence = "";
		String sums = "";
		HPN sum = zero();
		HPN term = one();
		
		int n = 0;
		
		while(!isZero(term)) {
        	sequence += term + ", "; 
        	System.out.println(Objects.toString("sum: " +sum));
        	sums += sum + ", ";
        	System.out.println("---");
        	sum = add(sum, term);
        	
        	term = multiply(term, x);
        	if(sum != one()) {
        		term = divide(term, n+1);
        	}
        	n++;
        }
		System.out.println("---------------");
        System.out.println("Sequence: " + sequence);
        System.out.println("Partial Sums: " + sums);
        return sum;
	}
	
	/**
	 * Slow rate of Convergence
	 * @return
	 */
	public static HPN ln2() {
		//init
		String sequence = "";
		String sums = "";
		HPN sum = zero();
		HPN term = one();
		int n = 1;
		
		while(!isZero(term)) {
			
			sequence += term + ", "; 
        	System.out.println(Objects.toString("sum: " +sum));
        	sums += sum + ", ";
        	System.out.println("---");
        	System.out.println("ADDING " + term + " to " + sum);
        	sum = add(sum, term);
        	term = one();
        	System.out.println("quo");
        	term = divide(term, n+1);
        	if(n%2 != 0) {
        		negate(term);
        	}
        	if(n == 2000) {
        		break;
        	}
        	
        	
        	n++;
		}
		
		System.out.println("---------------");
        System.out.println("Sequence: " + sequence);
        System.out.println("Partial Sums: " + sums);
        System.out.println(n + " terms");
		return sum;
	}
	
	/**
	 * Not so fast
	 * @return
	 */
	public static HPN recipFibo() {
		String sequence = "";
		String sums = "";
		HPN sum = zero();
		HPN term = one();
		int denom = 0;

		int n = 0;
		int a = 1;
		int b = 1;
		
		sequence += one();
		while(!isZero(term)) {
			sequence += term + ", "; 
        	System.out.println(Objects.toString("sum: " +sum));
        	sums += sum + ", ";
         	sum = add(sum, term);
          	System.out.println("---");
        	
        	if(n %2 == 0) {
        		a += b;
        		denom  = a;
        		
        	}else {
        		b+=a;
        		denom  = b;
        	}
        	
        	term = divide(one(),denom );
        	
        	if(n == 40) {
        		break;
        	}
        	
        	
        	n++;
		}
		
		System.out.println("---------------");
        System.out.println("Sequence: " + sequence);
        System.out.println("Partial Sums: " + sums);
        System.out.println(n + " terms");
		return add(sum, 1);
	}
	
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