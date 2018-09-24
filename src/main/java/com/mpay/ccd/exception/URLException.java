package com.mpay.ccd.exception;

public class URLException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public URLException() {
	}

	public URLException(String message) {
		super(message);
	}

	public URLException(Throwable cause) {
		super(cause);
	}

	public URLException(String message, Throwable cause) {
		super(message, cause);
	}

	public URLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
