package com.nickng.ccd.model;

import lombok.*;

import java.util.Set;

/**
 * The Class ConfigModel.
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ConfigModel {

	/** The sender. */
	private String sender;
	
	/** The sender password. */
	private String senderPassword;
	
	/** The receivers. */
	private Set<String> receivers;
	
	/** The links. */
	private Set<LinkModel> links;

}
