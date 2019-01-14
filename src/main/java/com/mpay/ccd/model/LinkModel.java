package com.mpay.ccd.model;

/**
 * The Class LinkModel.
 */
public class LinkModel {

	/** The title. */
	private String title;
	
	/** The link. */
	private String link;
	
	/**
	 * Instantiates a new link model.
	 */
	public LinkModel() {
		super();
	}

	/**
	 * Instantiates a new link model.
	 *
	 * @param title the title
	 * @param link the link
	 */
	public LinkModel(String title, String link) {
		super();
		this.title = title;
		this.link = link;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the new link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{title=" + title + ", link=" + link + "}";
	}
	
	
}
