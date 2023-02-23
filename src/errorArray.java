
public class errorArray {
	
	private int errorVal;
	private boolean isTruncated;
	
	public errorArray(int error, boolean isTruncated) {
		this.errorVal = error;
		this.isTruncated = isTruncated;
	}

	public int getErrorVal() {
		return errorVal;
	}

	public boolean isTruncated() {
		return isTruncated;
	}

}
