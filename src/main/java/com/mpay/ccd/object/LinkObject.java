package com.mpay.ccd.object;

public class LinkObject {

	private String title;
	private String link;
	
	public LinkObject() {
		super();
	}

	public LinkObject(String title, String link) {
		super();
		this.title = title;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "LinkObject [title=" + title + ", link=" + link + "]";
	}
	
	
}
