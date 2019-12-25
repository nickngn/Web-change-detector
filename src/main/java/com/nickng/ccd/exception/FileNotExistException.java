package com.nickng.ccd.exception;

public class FileNotExistException extends Exception {

  private static final long serialVersionUID = 1L;

  public FileNotExistException() {}

  public FileNotExistException(String message) {
    super(message);
  }

  public FileNotExistException(Throwable cause) {
    super(cause);
  }

  public FileNotExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileNotExistException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
