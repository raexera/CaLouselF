package controller;

import database.DatabaseConnector;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserController {

	private DatabaseConnector db;

	public UserController() {
		db = DatabaseConnector.getInstance();
	}

	public Map<String, String> registerUser(String username, String password, String phoneNumber, String address,
			String role) {
		Map<String, String> validationErrors = validateRegistration(username, password, phoneNumber, address, role);

		if (!validationErrors.isEmpty()) {
			return validationErrors;
		}

		try {
			String hashedPassword = hashPassword(password);
			String query = String.format(
					"INSERT INTO Users (Username, PasswordHash, PhoneNumber, Address, Role) "
							+ "VALUES ('%s', '%s', '%s', '%s', '%s')",
					username, hashedPassword, phoneNumber, address, role);
			db.execute(query);
		} catch (Exception e) {
			validationErrors.put("database", "An error occurred during registration.");
			e.printStackTrace();
		}

		return validationErrors;
	}

	public User loginUser(String username, String password) {
		try {
			String query = String.format("SELECT * FROM Users WHERE Username = '%s'", username);
			ResultSet rs = db.execQuery(query);

			if (rs.next()) {
				String passwordHash = rs.getString("PasswordHash");
				if (checkPassword(password, passwordHash)) {
					return new User(rs.getInt("UserID"), rs.getString("Username"), rs.getString("PasswordHash"),
							rs.getString("PhoneNumber"), rs.getString("Address"), rs.getString("Role"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Map<String, String> validateRegistration(String username, String password, String phoneNumber,
			String address, String role) {
		Map<String, String> errors = new HashMap<>();

		// Username validation
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "Username cannot be empty.");
		} else if (username.length() < 3) {
			errors.put("username", "Username must be at least 3 characters long.");
		} else if (!isUsernameUnique(username)) {
			errors.put("username", "Username is already taken.");
		}

		// Password validation
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "Password cannot be empty.");
		} else if (password.length() < 8 || !containsSpecialCharacter(password)) {
			errors.put("password", "Password must be at least 8 characters long and include special characters.");
		}

		// Phone number validation
		if (phoneNumber == null || !phoneNumber.startsWith("+62") || phoneNumber.length() != 13) {
			errors.put("phoneNumber", "Phone number must start with +62 and be 10 digits long.");
		}

		// Address validation
		if (address == null || address.trim().isEmpty()) {
			errors.put("address", "Address cannot be empty.");
		}

		// Role validation
		if (!role.equalsIgnoreCase("Seller") && !role.equalsIgnoreCase("Buyer")) {
			errors.put("role", "Role must be either 'Seller' or 'Buyer'.");
		}

		return errors;
	}

	private boolean isUsernameUnique(String username) {
		try {
			String query = String.format("SELECT * FROM Users WHERE Username = '%s'", username);
			ResultSet rs = db.execQuery(query);
			return !rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean containsSpecialCharacter(String password) {
		String specialChars = "!@#$%^&*";
		for (char c : password.toCharArray()) {
			if (specialChars.indexOf(c) != -1) {
				return true;
			}
		}
		return false;
	}

	private String hashPassword(String password) {
		return Integer.toHexString(password.hashCode());
	}

	private boolean checkPassword(String password, String passwordHash) {
		return hashPassword(password).equals(passwordHash);
	}
}
