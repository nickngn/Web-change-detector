package com.mpay.ccd.exception;

/**
 * The Class RenameException.
 */
public class RenameException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new rename exception.
	 */
	public RenameException() {
	}

	/**
	 * Instantiates a new rename exception.
	 *
	 * @param message the message
	 */
	public RenameException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new rename exception.
	 *
	 * @param cause the cause
	 */
	public RenameException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new rename exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public RenameException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new rename exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public RenameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
