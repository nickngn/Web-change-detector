package com.nickng.ccd.dto;

import com.nickng.ccd.model.ConfigModel;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The Class ConfigDto.
 */
@Data
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
		List<String> receivers = Arrays.asList(
				receiverEmails
						.replaceAll(" ", "")
						.replaceAll("\n", "")
						.split(";"));
		ConfigModel object = ConfigModel.builder()
			.sender(senderEmail)
			.senderPassword(senderEmailPassword)
			.receivers(new HashSet<>(receivers))
			.build();
		return object;
	}

}
