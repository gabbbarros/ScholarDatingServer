package com.main.java.crawler;

public class CoAuthor {
	private String userID = null;
	private String name = null;
	public static String photoURL_START = "https://scholar.google.com/citations?view_op=medium_photo&user=";
	public static String photoURL_END = "&citpid=2";
	
	public CoAuthor(String userId, String name) {
		this.userID = userId;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhotoURL() {
		return photoURL_START + userID + photoURL_END;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	@Override
	public String toString() {
		return userID + ": " + name;
	}
}
