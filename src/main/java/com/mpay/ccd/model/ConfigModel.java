package com.mpay.ccd.model;

import java.util.Set;

/**
 * The Class ConfigModel.
 */
public class ConfigModel {

	/** The sender. */
	private String sender;
	
	/** The sender password. */
	private String senderPassword;
	
	/** The receivers. */
	private Set<String> receivers;
	
	/** The links. */
	private Set<LinkModel> links;

	/**
	 * Instantiates a new config model.
	 */
	public ConfigModel() {
		super();
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param sender the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Gets the receivers.
	 *
	 * @return the receivers
	 */
	public Set<String> getReceivers() {
		return receivers;
	}

	/**
	 * Sets the receivers.
	 *
	 * @param receivers the new receivers
	 */
	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public Set<LinkModel> getLinks() {
		return links;
	}

	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(Set<LinkModel> links) {
		this.links = links;
	}

	/**
	 * Gets the sender password.
	 *
	 * @return the sender password
	 */
	public String getSenderPassword() {
		return senderPassword;
	}

	/**
	 * Sets the sender password.
	 *
	 * @param senderPassword the new sender password
	 */
	public void setSenderPassword(String senderPassword) {
		this.senderPassword = senderPassword;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{sender=");
		builder.append(sender);
		builder.append(", senderPassword=");
		builder.append(senderPassword);
		builder.append(", receivers=");
		builder.append(receivers);
		builder.append(", links=");
		builder.append(links);
		builder.append("}");
		return builder.toString();
	}


}
