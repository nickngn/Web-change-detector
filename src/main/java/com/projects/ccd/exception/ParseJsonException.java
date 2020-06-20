package com.projects.ccd.exception;

/**
 * The Class ParseJsonException.
 */
public class ParseJsonException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new parses the json exception.
	 */
	public ParseJsonException() {
		super();
	}

	/**
	 * Instantiates a new parses the json exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public ParseJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new parses the json exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ParseJsonException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new parses the json exception.
	 *
	 * @param message the message
	 */
	public ParseJsonException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new parses the json exception.
	 *
	 * @param cause the cause
	 */
	public ParseJsonException(Throwable cause) {
		super(cause);
	}

	
}
