
//import java.util.Arrays;
//import java.util.Objects;
//import java.io.*;
import java.util.*;

/**
 * HIGH PRECISION NUMBER PROJECT (HPN) Using a custom High Precision Number
 * object to calculate values of constants using common infinite series
 * 
 * @author Rohan Bopardikar under Dr. Christopher Heckman
 * 
 *ToDo
 *         Rewrite the toString it is so annoying
 *         Something broke arctan
 *         
 *         java stream
 * 
 */
public class HPN {

	private int intPart;
	private int[] fracPart;
	private boolean isNegative;
	private boolean isTruncated; // needed? No
	private boolean isExact;
	final static char approx = '≈';

	@Override
	public String toString() {
		String exact = "";
		if (isExact) {
			exact = "exact";
		} else {
			exact = "not exact";
		}

		String fracNum = "";

		for (int i = 0; i < fracPart.length; i++) {
			fracNum += fracPart[i];
		}

		if (isNegative) {
			if (isTruncated) {
				return "-" + intPart + "." + fracNum /* */;
			}
			return "-" + intPart + "." + fracNum /* + "(" + exact +")" */;
		} else {
			if (isTruncated) {
				return intPart + "." + fracNum /* + "(" + exact +")" */;
			}
			return intPart + "." + fracNum /* + "(" + exact +")" */;
		}
	}

	public static String printAns(HPN a) {
		if (a == null) {
			return ("Something went wrong... Dividing by zero? Please try again");
		}
		String output = a.equalSign() + " " + a.toString();
		return output;
	}

//Constructors	
	// Only int constructor
	public HPN(int a) {
		this.intPart = a;
		this.fracPart = new int[] { 0 };
		this.isNegative = false;
		checkNegative(this);
		this.isExact = true;

	}

	public HPN(int a, int precision) {
		this.intPart = a;
		this.fracPart = new int[] { 0 };
		this.isNegative = false;
		checkNegative(this);
		this.isExact = true;
		truncate(this, precision);
	}

	// False by default for a multiplication bug
	// System decision: Should constructors that do not accept
	// isExact be exact by default?
	public HPN(int a, int[] b) {
		this.intPart = a;
		this.fracPart = b;
		this.isNegative = false;
		checkNegative(this);
		this.isExact = false;

	}

	// String constructor
	public HPN(String s) {
		this.isNegative = false;
		// split input
		int decimalIndex = s.indexOf('.');

		if (decimalIndex > 0) {
			String intPart = s.substring(0, decimalIndex);
			this.intPart = Integer.parseInt(intPart);
		} else if (decimalIndex < 0) {
			this.intPart = Integer.parseInt(s);
		} else {
			intPart = 0;
		}
		String fracPart = s.substring(decimalIndex + 1);

		int fracLength = fracPart.length();

		this.fracPart = new int[fracLength];

		// fills fracPart into array
		if (decimalIndex >= 0) {
			for (int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i, i + 1));
			}
		}

		if (s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);

		this.isExact = true;
	}

	public HPN(String s, int precision) {

		this.isNegative = false;
		// split input
		int decimalIndex = s.indexOf('.');

		if (decimalIndex > 0) {
			String intPart = s.substring(0, decimalIndex);
			this.intPart = Integer.parseInt(intPart);
		} else if (decimalIndex < 0) {
			this.intPart = Integer.parseInt(s);
		} else {
			intPart = 0;
		}
		String fracPart = s.substring(decimalIndex + 1);

		int fracLength = fracPart.length();

		this.fracPart = new int[fracLength];

		// fills fracPart into array
		if (decimalIndex >= 0) {
			for (int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i, i + 1));
			}
		}

		if (s.charAt(0) == '-') {
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
		// split input
		int decimalIndex = s.indexOf('.');

		if (decimalIndex > 0) {
			String intPart = s.substring(0, decimalIndex);
			this.intPart = Integer.parseInt(intPart);
		} else if (decimalIndex < 0) {
			this.intPart = Integer.parseInt(s);
		} else {
			intPart = 0;
		}
		String fracPart = s.substring(decimalIndex + 1);

		int fracLength = fracPart.length();

		this.fracPart = new int[fracLength];

		// fills fracPart into array
		if (decimalIndex >= 0) {
			for (int i = 0; i < fracLength; i++) {
				this.fracPart[i] = Integer.parseInt(fracPart.substring(i, i + 1));
			}
		}

		if (s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
			this.intPart *= -1;
		}

		checkNegative(this);

		this.isExact = true;
		this.isExact = isExact;

	}

	// Constants
	public final static HPN zero() {
		return new HPN("0.0", true);
	}

	public final static HPN one() {
		return new HPN("1.0", true);
	}

	public final static HPN zero(int precision) {
		return new HPN("0.0", precision);
	}

	public final static HPN one(int precision) {
		return new HPN("1.0", precision);
	}

	/**
	 * Adds HPN and int by creating a new HPN for the int
	 * 
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, int b) {
		return add(a, new HPN(b));
	}
	
	/**
	 * 
	 * @param a = integer value
	 * @param 
	 * @return
	 */
	public static HPN add(int a, HPN b) {
		return add(b, a);
	}

	/**
	 * Adds two HPNs
	 * 
	 * @param a
	 * @param b
	 * @return HPN sum
	 */
	public static HPN add(HPN a, HPN b) {
		// Add intParts
		int intSum = a.intPart + b.intPart;
		// Create Sum HPN
		HPN sum = new HPN(intSum);

		// Calculation Display
		System.out.println(a.toString() + " + " + b.toString());

		// Check for negativity
		if (a.isNegative && !b.isNegative) {
			return subtract(b, negate(copy(a)));
		}

		if (!a.isNegative && b.isNegative) {
			return subtract(a, negate(copy(b)));
		}
		if (a.isNegative && b.isNegative) {
			return negate(add(negate(copy(a)), negate(copy(b))));
		}
		// Check for either adding zero
		if (a == zero()) {
			return copy(b);
		}
		if (b == zero()) {
			return copy(a);
		}

		// Fill into 2D array with zeros padded to match lengths
		int addition[][] = padZeros(a.fracPart, b.fracPart);

		// Initialize array for fracPart
		int[] fracSum = new int[addition[0].length];

		// Adding fracPart logic
		int carry = 0;
		for (int i = fracSum.length - 1; i >= 1; i--) {
			fracSum[i] = addition[0][i] + addition[1][i] + carry;
			carry = 0;

			if (fracSum[i] >= 10) {
				fracSum[i] = fracSum[i] % 10;
				carry = 1;
			}
		}

		fracSum[0] = addition[0][0] + addition[1][0] + carry;
		carry = 0;

		if (fracSum[0] >= 10) {
			fracSum[0] = fracSum[0] % 10;
			carry = 1;
		}

		// Carry to intPart if needed
		sum.intPart += carry;

		// Assign added fracPart to sum
		sum.fracPart = fracSum;

		// If either addend is not exact, sum will follow suit
		if (!a.isExact || !b.isExact) {
			sum.isExact = false;
		}

		// Truncate if needed
		return truncate(a, b, sum);
	}

	/**
	 * Subtracts two HPN's
	 * 
	 * @param a
	 * @param b
	 * @return HPN result
	 * @author Christopher Carl Heckman
	 */
	public static HPN subtract(HPN a, HPN b) {
		System.out.println(a.toString() + " - " + b.toString());
		/// Note that above, .toString() is unnecessary; println calls toString
		/// automatically.
		/// Thus, we can write this as System.out.println (a + " - " + b);

		// take care of some reductions first
		/// if there's only one command inside the block, the braces are optional.
		if (isZero(b))
			return copy(a); // a - 0 = a
		if (isZero(a))
			return negate(b); // 0 - b = (-b)
		if (b.isNegative)
			return add(a, negate(b)); // a - b = a + (-b)
		/// In this case, we don't care whether a < 0; add() will take care of that.
		/// I like putting spaces after commas, and before left parentheses if there's
		/// an argument.
		/// the return line above would look like return add (a, negate (b));
		/// It helps break up the formulas visually.
		if (a.isNegative)
			return negate(add(negate(a), b)); // a - b = -((-a) + b)

		// Now, a, b > 0. We need to check to see that a >= b, because otherwise
		// the algorithm doesn't work.
		boolean wrongOrder = false;
		if (b.intPart > a.intPart) {
			System.out.println("uhoh");
			wrongOrder = true;
		} else if (b.intPart == a.intPart) { // modified
			// System.out.println("else");
			for (int i = 0; i < a.fracPart.length; i++) {
				if (b.fracPart[i] > a.fracPart[i])
					wrongOrder = true;
				if (b.fracPart[i] != a.fracPart[i])
					break;
			}
		}

		// Added by Rohan Bopardikar. Probably unnecessary because I fixed the 'else'
		// above.
		if (b.intPart < a.intPart) {
			wrongOrder = false;
		}
		/// you might want to step through this loop with a.fracPart = [1, 2, 3], b =
		/// [1, 2, 3];
		/// a = [1, 2, 3], b = [1, 4, 2]; and a = [1, 2, 3], b = [1, 1, 1] to see why
		/// this works.
		if (wrongOrder) {

			return negate(subtract(b, a)); // a - b = -(b - a)
		}

		// now we have a >= b > 0, so we can use the regular subtraction algorithm.
		HPN result = new HPN(a.intPart - b.intPart);
		int[][] subtract = padZeros(a.fracPart, b.fracPart);
		// System.out.println("padding");
		// result.fracPart will be subtract [0] after the algorithm.
		/// We don't need a new array if we change subtract [0].

		for (int i = subtract[0].length - 1; i > 0; i--) {
			if (subtract[1][i] > subtract[0][i]) {
				subtract[0][i - 1]--;
				subtract[0][i] += 10;
			} // borrow from previous digit
			subtract[0][i] -= subtract[1][i];
		}

		if (subtract[1][0] > subtract[0][0]) {
			result.intPart--;
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
	 * 
	 * @param a
	 * @param b
	 * @return HPN product
	 */
	public static HPN multiply(HPN a, int b) {
		// Calculation Display
		System.out.println(a.toString() + " * " + b);

		// Multiplying by zero
		if (a == zero() || b == 0) {
			return zero();
		}

		// Check negativity
		if (a.isNegative && b > 0) {
			a.isNegative = false;
			HPN result = multiply(a, b);
			result.isNegative = true;
			return result;
		}

		if (!a.isNegative && b < 0) {
			b *= -1;
			HPN result = multiply(a, b);
			result.isNegative = true;
			return result;
		}

		if (a.isNegative && b < 0) {
			a.isNegative = false;
			b *= -1;
			return multiply(a, b);
		}

		// Multiply intPart
		int intProduct = a.intPart * b;

		// Initialize product array & carry
		int[] fracProduct = new int[a.fracPart.length];
		int carry = 0;

		// fracPart multiplication logic

		// Iterating <--
		for (int i = fracProduct.length - 1; i >= 0; i--) {
			carry = a.fracPart[i] * b + carry;
			if (carry > 9) {
				String carryString = "" + Integer.toString(carry);
				fracProduct[i] = Integer.parseInt(carryString.substring(carryString.length() - 1));
				carry = Integer.parseInt(carryString.substring(0, carryString.length() - 1));
				if (i == 0) {
					intProduct += carry;
				}
			} else {
				fracProduct[i] = carry;
				carry = 0;
			}
		}

		// Initialize product with calculated int and frac Parts
		HPN product = new HPN(intProduct, fracProduct);

		// If HPN factor is exact, product follows
		if (a.isExact) {
			product.isExact = true;
		}
		return product;
	}

//	/**
//	 * 
//	 * Divides a HPN by int
//	 * 
//	 * @param a
//	 * @param b
//	 * @return quotient
//	 * @author Christopher Carl Heckman
//	 */
//	/// CCH's version of divide
//
//	// .5/2 = .2 why
//	public static HPN divide(HPN a, int b) {
//		System.out.println(a.toString() + " / " + b);
//
//		if (isZero(a)) {
//			return zero(a.precision());
//		}
//
//		if (b == 0) {
//			return zero();
//		}
//
//		if (a.isNegative)
//			return negate(divide(negate(a), b)); // a / b = -[ (-a) b]
//		if (b < 0)
//			return negate(divide(a, -b)); // a / b = - [a / (-b)]
//		if (b == 1)
//			return copy(a); // a / 1 = a.
//		if (isZero(a))
//			return HPN.zero(a.precision()); // 0 / b = 0 ... We're assuming b is not zero here.
//
//		// now, a >= 0 and b > 0
//		HPN quotient = new HPN(a.intPart / b, a.precision());
//		int currentRemainder = a.intPart % b;
//		for (int i = 0; i < a.precision(); i++) {
//			currentRemainder *= 10;
//			currentRemainder += a.fracPart[i];
//			quotient.fracPart[i] = currentRemainder / b;
//			currentRemainder %= b;
//		}
//		quotient.isExact = a.isExact && (currentRemainder == 0);
//		if (currentRemainder > b / 2)
//			quotient.fracPart[a.precision() - 1]++; // round
//		return quotient;
//	}

	public static HPN divide(HPN a, int b) {
		System.out.println(a.toString() + " / " + b);
		boolean exact = true;

		if (isZero(a)) {
			return zero();
		}

		if (b == 0) {
			return null;
		}

		// Check Negativity
		if (a.isNegative && b < 0) {
			a.isNegative = false;
			b *= -1;

			return divide(a, b);
		}

		if (!a.isNegative && b < 0) {
			b *= -1;
			HPN result = divide(a, b);
			result.isNegative = true;
			return result;
		}
//		if(a.isNegative && b > 0) {
//			a.isNegative = false;
//			HPN result = divide(negate(copy(a)),b);
//			result.isNegative = true;
//			return result;
//		}

		// Initialize
		int whole = 0;
		int intQuotient;

		int dividend;
		intQuotient = a.intPart / b;
		int intRemainder = a.intPart % b;

		if (intQuotient != 0) {

			whole = intQuotient * b;
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
		int miniQ = dividend / b;
		qFrac[0] = miniQ;
		int product = miniQ * b;
		dividend = dividend - product;

		int i = 1;

		// Repeat until division is complete
		while ((dividend != 0 || i != rFrac.length) && i < 10) {
			if (i > rFrac.length - 1) {
				rFrac = expand(rFrac);
			}
			dividend = concat(dividend, rFrac[i]);

			miniQ = dividend / b;

			if (i > qFrac.length - 1) {
				qFrac = expand(qFrac);
			}
			qFrac[i] = miniQ;

			product = miniQ * b;
			dividend = dividend - product;

			// If the quotient exceeds max decimal value, stop and make not exact
			if (i == 9) {
				exact = false;
			}
			i++;
		}
		quotient.fracPart = qFrac;

		quotient.isExact = exact;
		if (a.isNegative && b > 0) {
			quotient.isNegative = true;
		}

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
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i];
		}

		b[b.length - 1] = 0;
		return b;
	}

	/**
	 * Fill HPN frac Parts into 2d Array and fills "empty spaces" with zeros
	 * 
	 * @param a
	 * @param b
	 * @return 2d Array
	 */
	public static int[][] padZeros(int[] a, int[] b) {

		// Initialize
		int[][] ordered = new int[2][];

		// Same Length?
		// No
		if (a.length != b.length) {

			// a is Longer, need to pad b
			if (a.length > b.length) {
				// New fracPart for b
				int[] bNorm = new int[a.length];

				// Copy values
				for (int i = 0; i < b.length; i++) {
					bNorm[i] = b[i];
				}

				// Fill remaining vals with 0
				for (int j = b.length; j < bNorm.length; j++) {
					bNorm[j] = 0;
				}

				// Assign & Return
				ordered[0] = a;
				ordered[1] = bNorm;
				return ordered;
			}
			// b is Longer, need to pad a
			else {
				int[] aNorm = new int[b.length];

				for (int i = 0; i < a.length; i++) {
					aNorm[i] = a[i];
				}
				for (int j = a.length; j < aNorm.length; j++) {
					aNorm[j] = 0;
				}

				ordered[0] = aNorm;
				ordered[1] = b;
				return ordered;
			}
			// Yes
		} else {
			// Assign and return, no padding needed
			ordered[0] = a;
			ordered[1] = b;
			return ordered;
		}
	}

	/**
	 * Checks and Updates sign
	 * 
	 * @param a
	 */
	public static void checkNegative(HPN a) {
		if (!a.isNegative && a.intPart < 0) {
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
		if (a.isNegative) {
			a.isNegative = false;
		} else {
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
		// new HPN with same intPart
		HPN b = new HPN(a.intPart);

		// copy fracPart
		int bFrac[] = new int[a.fracPart.length];
		System.arraycopy(a.fracPart, 0, bFrac, 0, bFrac.length);
		b.fracPart = bFrac;

		// isExact
		boolean bExact = a.isExact;
		b.isExact = bExact;

		// isNegative
		boolean bNeg = a.isNegative;
		b.isNegative = bNeg;

		return b;
	}

	/**
	 * Used in truncate()
	 * 
	 * @param a
	 * @param b
	 * @return int precision
	 * 
	 *         If one of the values is exact, return max precision If both are not
	 *         exact, return min precision
	 */
	private static int findPrecision(HPN a, HPN b) {
		int aLength = a.fracPart.length;
		int bLength = b.fracPart.length;
		int diff = aLength - bLength;

		// if the same length, no truncation needed
		if (diff == 0)
			return aLength;

		if (!a.isExact && !b.isExact) {// shorten the long one
			if (diff > 0) {// a is longer
				return bLength;
			} else {// b is longer
				return aLength;
			}
		} else {// pad the short one
			if (diff > 0) {// b is shorter
				return aLength;
			} else {// a is shorter
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
	 * @return z, but shortened or padded If shortened, z gets rounded
	 * 
	 *         Is there even a case where z gets padded?
	 */
	public static HPN truncate(HPN a, HPN b, HPN z) {

		// Init
		int zLength = z.fracPart.length;
		int precision = findPrecision(a, b);

		// Need to cut off extra digits and round
		if (zLength > precision) {
			// Calculation Display
			System.out.println("truncating " + z + " " + (precision - zLength) + " digit(s).");

			// Update attributes
			z.isTruncated = true;// ?
			z.isExact = false;

			// Create new shortened array
			int[] newFrac = new int[precision];

			// Fill with values from original
			for (int i = 0; i < precision; i++) {
				newFrac[i] = z.fracPart[i];
			}

			// Rounding logic
			if (z.fracPart[precision] > 4) {
				// round
				for (int i = precision - 1; i > 0; i--) {

					newFrac[i]++;
					if (newFrac[i] != 10) {
						break;
					}
					newFrac[i] = 0;

				}

				if (z.fracPart[1] > 4) {
					newFrac[0]++;
				}

				// Tens place carries over to intPart
				if (newFrac[0] > 9) {
					newFrac[0] = 0;
					z.intPart++;
				}
			}

			// Assign
			z.fracPart = newFrac;

			// Padding logic, untested
		} else if (zLength < precision) {

			System.out.println("padding " + z + " " + (precision - zLength) + " zeros");
			z.isTruncated = true;
			z.isExact = false;
			int diff = precision - zLength;
			int[] newFrac = new int[precision];
			for (int i = 0; i < z.fracPart.length; i++) {
				newFrac[i] = z.fracPart[i];
			}
			for (int k = precision - diff; k < newFrac.length; k++) {
				newFrac[k] = 0;
			}
		}

		return z;
	}

	/**
	 * Truncates a HPN to given precision Needed for ("HPN", precision) and similar
	 * constructors
	 * 
	 * @param z
	 * @param precision
	 * @return
	 */
	public static HPN truncate(HPN z, int precision) {

		int zLength = z.fracPart.length;

		if (zLength > precision) {
			z.isExact = false;
			System.out.println("truncating " + z + "to " + precision + " digit(s).");
			z.isExact = false;
			z.isTruncated = true;
			// cut off and round

			int[] newFrac = new int[precision];
			for (int i = 0; i < precision; i++) {
				newFrac[i] = z.fracPart[i];
			}

			if (z.fracPart[precision] > 4) {

				// round
				for (int i = precision - 1; i > 0; i--) {
					newFrac[i]++;
					if (newFrac[i] != 10) {
						break;
					}
					newFrac[i] = 0;

				}
				if (z.fracPart[1] > 4) {

					newFrac[0]++;
				}

				if (newFrac[0] > 9) {
					newFrac[0] = 0;
					z.intPart++;
				}
			}
			z.fracPart = newFrac;

		} else if (zLength < precision) {
			System.out.println("padding " + z + " " + (precision - zLength) + " zeros.");

			int diff = precision - zLength;

			int[] newFrac = new int[precision];
			for (int i = 0; i < z.fracPart.length; i++) {
				newFrac[i] = z.fracPart[i];
			}
			for (int k = precision - diff; k < newFrac.length; k++) {
				newFrac[k] = 0;
			}

			z.fracPart = newFrac;
		}
		return z;
	}

	public int precision() {
		return this.fracPart.length;
	}

	public static ArrayList<HPN> sequence = new ArrayList<HPN>();
	public static ArrayList<HPN> sums = new ArrayList<HPN>();

	/**
	 * [1,inf)SUM (a * r^n) = 1 + (a/b) + (a/b)^2 + ... a is 1
	 * 
	 * @param a
	 * @param b
	 * @return sum
	 */
	public static HPN geometricSum(int a, int b) {

		sequence.clear();
		sums.clear();

		// check for convergence
		if ((Math.abs(a) > Math.abs(b) && b >= 0) || a == b) {
			System.out.println("DIVERGES");
			return null;
		} else {
			System.out.println("CONVERGES");
		}
		HPN sum = zero();
		HPN term = one();

		while (!isZero(term)) {
			// Update strings
			// sequence += term + ", ";
			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);

			// Separating term calculations
			System.out.println("---");

			sum = add(sum, term);

			// multiply by r: multiply by numerator, divide by denominator
			term = multiply(term, a);
			term = divide(term, b);

		}

		// Calculation Display
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);
		System.out.println("---------------");
		System.out.println("The geometric sum for (" + a + "/" + b + ")^n " + sum.equalSign() + " " + sum);

		return sum;
	}

	/**
	 * [first,inf)SUM (a * r^n) = m + m(a/b) + m(a/b)^2 + ... a is up to user
	 * 
	 * @param a
	 * @param b
	 * @return sum
	 *
	 */
	public static HPN geometricSum(int first, int a, int b) {

		sequence.clear();
		sums.clear();

		// check for convergence
		if ((Math.abs(a) > Math.abs(b) && b >= 0) || a == b) {
			System.out.println("Series Diverges");
			return null;
		} else {
			System.out.println("CONVERGES");
		}
		HPN sum = zero();
		HPN term = new HPN(first);

		while (!isZero(term)) {
			// Update strings
			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);

			// Separating term calculations
			System.out.println("---");

			sum = add(sum, term);

			// multiply by r: multiply by numerator, divide by denominator
			term = multiply(term, a);
			term = divide(term, b);

		}


		// Calculation Display
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);
		System.out.println("---------------");
		System.out.println("The geometric sum for (" + first + ")(" + a + "/" + b + ")^n " + sum.equalSign() + " " + sum);
		return sum;
	}

	// return e using eToX()
	public static HPN e() {
		return eToX(1);
	}

	/**
	 * e^x = [0,inf)SUM (x^n/n!) = 1 + x + (x^2/2!) + (x^3/3!) + ...
	 * 
	 * @param x
	 * @return
	 * 
	 *         fix negative x fractional x = a/b
	 */
	public static HPN eToX(int x) {
		// No convergence check, RC = inf
		// Edge cases
		if (x == 0) {
			
			return one();
		}

		// init
		sequence.clear();
		sums.clear();
		HPN sum = zero();
		HPN term = one();

		int n = 0;

		while (!isZero(term)) {
			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);
			System.out.println("---");
			sum = add(sum, term);

			term = multiply(term, x);
			if (!Objects.equals(one(), sum)) { // make equals(), this compares same object
				term = divide(term, n + 1);
			}
			n++;
		}
		
		
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);
		System.out.println("---------------");
		System.out.println("The sum approximation for e^" + x + sum.equalSign() + " " + sum);

		return sum;
	}

	public static HPN eToX(int a, int b) {
		// No convergence check, RC = inf
		// Edge cases
		if (b == 0) {
			return null;
		}
		if (a == 0) {
			return one();
		}

		// init
		sequence.clear();
		sums.clear();
		HPN sum = zero();
		HPN term = one();

		int n = 0;

		while (!isZero(term)) {
			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);
			System.out.println("---");
			sum = add(sum, term);
			term = multiply(term, a);
			term = divide(term, b);
			if (!Objects.equals(one(), sum)) { // make equals(), this compares same object
				term = divide(term, n + 1);
			}
			n++;
		}
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);
		System.out.println("---------------");
		System.out.println("The infinite sum approximation for e^(" +a  +"/"+b+") " + sum.equalSign() + " " + sum);
		return sum;
	}

	/**
	 * Uses Euler's Machin-Like Formula
	 * 
	 * @return
	 */
	public static HPN pi() {
		HPN pi = add(arctan(1, 2), arctan(1, 3)); // should be pi/4
		pi = multiply(pi, 4);
		System.out.println("---------------");
		System.out.println("The infinite sum approximation for "+  pi.equalSign() + pi);

		return pi;
	}

	/**
	 * Uses Hermann's Machin-Like Formula
	 * 
	 * @return
	 */
	public static HPN pi2() {
		HPN pi = multiply(arctan(1, 2), 2);
		pi = subtract(pi, arctan(1, 7)); // should be pi/4
		pi = multiply(pi, 4);
		System.out.println("---------------");
		System.out.println("The infinite sum approximation for "+  pi.equalSign() + pi);


		return pi;
	}

	/**
	 * Calculated maclaurin sum for arctan(x)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static HPN arctan(int a, int b) {

		// Edge cases
		if (a == 0) {
			return zero();
		}
		if (b == 0) {
			return null;
		}

		boolean isNegative = false;
		if (b * a < 0) {
			isNegative = true;
		}

		// check for convergence, RC = 1
		double ratio = a / b;
		if (ratio > 1 || ratio < -1) {
			return null;
		}

		HPN term = divide(new HPN(a), b);
		System.out.println(term);
		HPN sum = copy(term);
		System.out.println(sum);
		sequence.add(term);
		sums.add( sum);

		//sequence.add(term);
		HPN squared = multiply(term, a);
		squared = divide(squared, b);

		int n = 1;
		int counter = 3;

		// stop when term gets very small
		while (!Objects.equals(term, zero())) {

			term = multiply(term, a);
			term = multiply(term, a);
			term = divide(term, b);
			term = divide(term, b);

			

			HPN dterm = divide(term, counter);
			if(isNegative) {
				if (n % 2 == 0) {
					dterm.isNegative =  true;
				}else {
					dterm.isNegative = false;
				}
			}else {
				if (n % 2 == 0) {
					dterm.isNegative = false;
				}else {
					dterm.isNegative = true;
				}
			}
			
			sum = add(sum, dterm);

			sequence.add(dterm);
			sums.add(sum);

			n++;
			counter += 2;

			if (n > 10) {
				break;
			}
		}
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);

//		if (isNegative) {
//			negate(sum);
//		}
		System.out.println();
		System.out.println("The infinite sum approximation for arctan(" +a  +"/"+b+") " + sum.equalSign() + " " + sum);
		System.out.println(isNegative);

		return sum;
	}

	// x = a/b
	// log(x) = ln(1+x)
	public static HPN log(int a, int b) {

		// Edge cases
		if (a == 0) {
			return zero();
		}
		if (b == 0) {
			return null;
		}

		boolean isNegative = false;
		if (b * a < 0) {
			isNegative = true;
		}

		// check for convergence, RC = 1
		double ratio = a / b;
		if (ratio > 1 || ratio < -1) {
			return null;
		}

		HPN term = divide(new HPN(a), b);
		HPN sum = copy(term);

		sequence.add(term);

		int n = 0;
		int counter = 2;
		// stop when term gets very small
		while (!isZero(term)) {

			term = multiply(term, a);
			term = divide(term, b);

			if (!isNegative) {
				if (n % 2 == 0) {
					term.isNegative = true;

				} else {
					term.isNegative = false;
				}
			} else {
				term.isNegative = true;
			}
			HPN dterm = divide(term, counter);

			sum = add(sum, dterm);
			sequence.add(dterm);
			sums.add(sum);

			counter++;
			n++;

		}
		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);

		if (isNegative) {
			sum.isNegative = true;
		}
		
		System.out.println();
		System.out.println("The infinite sum approximation for log(" +a  +"/"+b+") " + sum.equalSign() + " " + sum);



		return sum;
	}

	/**
	 * Slow rate of Convergence
	 * 
	 * @return
	 */
	public static HPN ln2b() {
		// init

		HPN sum = zero();
		HPN term = one();
		int n = 1;

		while (!isZero(term)) {

			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);
			System.out.println("---");
			sum = add(sum, term);
			term = one();
			term = divide(term, n + 1);
			if (n % 2 != 0) {
				negate(term);
			}
			if (n == 2000) {
				break;
			}

			n++;
		}

		System.out.println("---------------");
		System.out.println("Sequence: " + sequence);
		System.out.println("Partial Sums: " + sums);
		System.out.println(n + " terms");
		System.out.println();
		System.out.println("The infinite sum approximation for ln(2) using a basic maclaurin series " + sum.equalSign() + " " + sum);
		return sum;
	}

	// These ln functions are inspired by machin-like formulas

	// Powers of 2 & 3
	public static HPN ln2() {
		HPN result = subtract(multiply(log(-5, 32), -2), multiply(log(-1, 9), 3));
		System.out.println();
		System.out.println("---------------");
		System.out.println("Using 2 & 3 powers");
		System.out.println("The infinite sum approximation for ln(2) using a machin-like formula " + result.equalSign() + " " + result);
		return result;
	}

	// Powers of 2 & 3
	public static HPN ln3() {
		HPN result = subtract(multiply(log(-5, 32), -3), multiply(log(-1, 9), 5));
		System.out.println();
		System.out.println("---------------");
		System.out.println("Using 2 & 3 powers");
		System.out.println("The infinite sum approximation for ln(3) using a machin-like formula " + result.equalSign() + " " + result);
		return result;
	}

	// Powers of 3 & 5
	public static HPN ln3b() {
		HPN result = subtract(multiply(log(-938, 3125), -2), multiply(log(-2, 27), 5));
		System.out.println();
		System.out.println("---------------");
		System.out.println("Using 3 & 5 powers");
		System.out.println("The infinite sum approximation for ln(3) using a machin-like formula " + result.equalSign() + " " + result);
		return result;
	}

	// Powers of 3 & 5
	public static HPN ln5() {
		HPN result = subtract(multiply(log(-938, 3125), -3), multiply(log(-2, 27), 7));
		System.out.println();
		System.out.println("---------------");
		System.out.println("Using 3 & 5 powers");
		System.out.println("The infinite sum approximation for ln(5) using a machin-like formula " + result.equalSign() + " " + result);
		return result;
	}

	/**
	 * Not so fast
	 * 
	 * @return
	 */
	public static HPN recipFibo() {

		HPN sum = zero();
		HPN term = one();
		int denom = 0;

		int n = 0;
		int a = 1;
		int b = 1;

		sequence.add(one());
		while (!isZero(term)) {
			sequence.add(term);
			// sums += sum + ", ";
			sums.add(sum);
			sum = add(sum, term);
			System.out.println("---");

			if (n % 2 == 0) {
				a += b;
				denom = a;

			} else {
				b += a;
				denom = b;
			}

			term = divide(one(), denom);

			if (n == 40) {
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

	public char equalSign() {
		char equalSign;
		if (this.isExact) {
			equalSign = '=';
		} else {
			equalSign = approx;
		}

		return equalSign;
	}

	public static HPN termAt(int a) {
		if (a == 0 || a < sequence.size()) {
			return null;
		}
		System.out.println();
		System.out.println("The number " + a + " term is: " + sequence.get(a - 1));
		return sequence.get(a - 1);
	}

	public static HPN sumAt(int a) {
		if (a == 0 || a < sums.size()) {
			return null;
		}
		System.out.println();
		System.out.println("The number " + a + " term is: " + sums.get(a - 1));
		return sums.get(a - 1);
	}

	public static int factorial(int a) {
		int product = 1;
		if (a < 1) {
			return 1;
		} else {
			for (int i = a; i > 0; i--) {
				System.out.println(i);
				product *= i;
			}

			return product;
		}
	}

	private static boolean isZero(HPN term) {
		HPN abs = copy(term);
		abs.isNegative = false;
		if (abs.intPart != 0) {
			return false;
		}
		for (int i = 0; i < abs.fracPart.length; i++) {
			if (abs.fracPart[i] != 0) {
				return false;
			}
		}
		return true;
	}

}