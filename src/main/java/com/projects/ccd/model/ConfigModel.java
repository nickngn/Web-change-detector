package com.projects.ccd.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * The Class ConfigModel.
 */
@Getter
@Setter
@NoArgsConstructor
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
