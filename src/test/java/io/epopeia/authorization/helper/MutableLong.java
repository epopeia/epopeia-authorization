package io.epopeia.authorization.helper;

/**
 * Type definition of a not final Long
 * 
 * @author Fernando Amaral
 */
public class MutableLong {

	private long val;

	public MutableLong(long val) {
		this.val = val;
	}

	public long get() {
		return val;
	}

	public void set(int val) {
		this.val = val;
	}

	public void increment() {
		this.val = val + 1;
	}

	// used to print value convinently
	public String toString() {
		return Long.toString(val);
	}
}
