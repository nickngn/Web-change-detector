package com.mpay.changedwebsitecontentdetector.object;

import java.util.Set;

public class ConfigObject {

	private String sender;
	private String senderPassword;
	private Set<String> receivers;
	private Set<LinkObject> links;

	public ConfigObject() {
		super();
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Set<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}

	public Set<LinkObject> getLinks() {
		return links;
	}

	public void setLinks(Set<LinkObject> links) {
		this.links = links;
	}

	public String getSenderPassword() {
		return senderPassword;
	}

	public void setSenderPassword(String senderPassword) {
		this.senderPassword = senderPassword;
	}

	@Override
	public String toString() {
		return "ConfigObject [sender=" + sender + ", senderPassword=" + senderPassword + ", receivers=" + receivers
				+ ", links=" + links + "]";
	}

}
