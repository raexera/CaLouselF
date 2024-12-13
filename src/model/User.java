package model;

public class User {
	private int userID;
	private String username;
	private String passwordHash;
	private String phoneNumber;
	private String address;
	private String role;

	public User(int userID, String username, String passwordHash, String phoneNumber, String address, String role) {
		super();
		this.userID = userID;
		this.username = username;
		this.passwordHash = passwordHash;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.role = role;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
