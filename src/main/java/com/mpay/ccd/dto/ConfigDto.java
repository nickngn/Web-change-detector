package com.mpay.ccd.dto;

import java.util.Arrays;
import java.util.HashSet;

import com.mpay.ccd.model.ConfigModel;

/**
 * The Class ConfigDto.
 */
public class ConfigDto {

	/** The port. */
	private int port;
	
	/** The looping time. */
	private long loopingTime;
	
	/** The sender email. */
	private String senderEmail;
	
	/** The sender email password. */
	private String senderEmailPassword;
	
	/** The receiver emails. */
	private String receiverEmails;
	
	/**
	 * To config object.
	 *
	 * @return the config model
	 */
	public ConfigModel toConfigObject() {
		ConfigModel object = new ConfigModel();
		object.setSender(senderEmail);
		object.setSenderPassword(senderEmailPassword);
		object.setReceivers(new HashSet<String>(
				Arrays.asList(receiverEmails
						.replaceAll(" ", "")
						.replaceAll("\n", "")
						.split(";"))));
		return object;
	}
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the looping time.
	 *
	 * @return the looping time
	 */
	public long getLoopingTime() {
		return loopingTime;
	}
	
	/**
	 * Sets the looping time.
	 *
	 * @param loopingTime the new looping time
	 */
	public void setLoopingTime(long loopingTime) {
		this.loopingTime = loopingTime;
	}
	
	/**
	 * Gets the sender email.
	 *
	 * @return the sender email
	 */
	public String getSenderEmail() {
		return senderEmail;
	}
	
	/**
	 * Sets the sender email.
	 *
	 * @param senderEmail the new sender email
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	/**
	 * Gets the sender email password.
	 *
	 * @return the sender email password
	 */
	public String getSenderEmailPassword() {
		return senderEmailPassword;
	}
	
	/**
	 * Sets the sender email password.
	 *
	 * @param senderEmailPassword the new sender email password
	 */
	public void setSenderEmailPassword(String senderEmailPassword) {
		this.senderEmailPassword = senderEmailPassword;
	}
	
	/**
	 * Gets the receiver emails.
	 *
	 * @return the receiver emails
	 */
	public String getReceiverEmails() {
		return receiverEmails;
	}
	
	/**
	 * Sets the receiver emails.
	 *
	 * @param receiverEmails the new receiver emails
	 */
	public void setReceiverEmails(String receiverEmails) {
		this.receiverEmails = receiverEmails;
	}
	
}
