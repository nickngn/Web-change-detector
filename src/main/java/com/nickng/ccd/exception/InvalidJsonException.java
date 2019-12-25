package com.nickng.ccd.exception;

/**
 * The Class InvalidJsonException.
 */
public class InvalidJsonException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid json exception.
	 */
	public InvalidJsonException() {
		super();
	}

	/**
	 * Instantiates a new invalid json exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public InvalidJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new invalid json exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidJsonException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new invalid json exception.
	 *
	 * @param message the message
	 */
	public InvalidJsonException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid json exception.
	 *
	 * @param cause the cause
	 */
	public InvalidJsonException(Throwable cause) {
		super(cause);
	}

	
}
