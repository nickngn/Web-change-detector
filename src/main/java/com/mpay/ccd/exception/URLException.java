package com.mpay.ccd.exception;

/**
 * The Class URLException.
 */
public class URLException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new URL exception.
	 */
	public URLException() {
	}

	/**
	 * Instantiates a new URL exception.
	 *
	 * @param message the message
	 */
	public URLException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new URL exception.
	 *
	 * @param cause the cause
	 */
	public URLException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new URL exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public URLException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new URL exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public URLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
