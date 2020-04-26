package com.mpay.ccd.dto;

import java.util.Arrays;
import java.util.HashSet;

import com.mpay.ccd.model.ConfigModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class ConfigDto.
 */
@Getter
@Setter
@NoArgsConstructor
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

}
