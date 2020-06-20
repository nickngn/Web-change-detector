package com.projects.ccd.exception;

/**
 * The Class UnavailablePortException.
 */
public class UnavailablePortException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unavailable port exception.
	 */
	public UnavailablePortException() {
		super();
	}

	/**
	 * Instantiates a new unavailable port exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public UnavailablePortException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new unavailable port exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public UnavailablePortException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new unavailable port exception.
	 *
	 * @param message the message
	 */
	public UnavailablePortException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new unavailable port exception.
	 *
	 * @param cause the cause
	 */
	public UnavailablePortException(Throwable cause) {
		super(cause);
	}

}
