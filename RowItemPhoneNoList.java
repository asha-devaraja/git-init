package com.android_examples.Connect2ControlHome;

/**
 * Encapsulates information about a news entry
 */
public final class RowItemPhoneNoList {
	
	private final String title;
	private final String author;
	private final int icon;
	private final boolean isAdmin;

	public RowItemPhoneNoList(final String title, final String author, final int icon, final boolean isAdmin) {
		this.title = title;
		this.author = author;
		this.icon = icon;
		this.isAdmin = isAdmin;
	}

	/**
	 * @return Title of news entry
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Author of news entry
	 */
	public String getAuthor() {
		return author;
	}

    /**
	 * @return Icon of this news entry
	 */
	public int getIcon() {
		return icon;
	}

	public Boolean getAdminFlag() {
		return isAdmin;
	}
}