
public class errorData {
	
	private int errorVal;
	private boolean isTruncated;
	
	public errorData(int error, boolean isTruncated) {
		this.errorVal = error;
		this.isTruncated = isTruncated;
	}

	public int getErrorVal() {
		return errorVal;
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	/**
	 * Takes two errorData objects and the result HPN
	 * @param a
	 * @param b
	 * @param operation
	 * @return
	 */
	public static errorData addError(HPN a, HPN b, String operation) {
		int error = 0;
		boolean isTruncated = false; 
		
		int aLength = a.getFracPart().length;
		int bLength = b.getFracPart().length;

		double placeDiff;
		int firstError = 0;
		int secondError = 0;
		
		if(aLength == bLength) {
			error = a.getError() + b.getError();

		}
		else if(aLength > bLength){
			placeDiff = aLength - bLength;
			if(placeDiff > 9) {
				placeDiff = 9;
				isTruncated = true;
			}
			firstError = (int)(a.getError() * java.lang.Math.pow(10,placeDiff));
			secondError = b.getError();
			error = firstError + secondError;
		}
		else {
			placeDiff = bLength - aLength;
			if(placeDiff > 9) {
				placeDiff = 9;
				isTruncated = true;
				
			}

			firstError = (b.getError() * (int)java.lang.Math.pow(10,placeDiff));
			secondError = a.getError();
			error = firstError + secondError;
		}
		
		errorData Error = new errorData(error, isTruncated);
		return Error;
	}

}