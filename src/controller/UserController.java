	package controller;
	
	import database.DatabaseConnector;
	import model.User;
	
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.HashMap;
	import java.util.Map;
	
	public class UserController {
		private static UserController instance;
		private final DatabaseConnector db;
	
		private UserController() {
			db = DatabaseConnector.getInstance();
		}
	
		public static UserController getInstance() {
			if (instance == null) {
				instance = new UserController();
			}
			return instance;
		}
	
		public Map<String, String> registerUser(User user, String password) {
			Map<String, String> validationErrors = validateRegistration(user, password);
	
			if (!validationErrors.isEmpty()) {
				return validationErrors;
			}
	
			try {
				String hashedPassword = hashPassword(password);
				String query = String.format(
						"INSERT INTO Users (Username, PasswordHash, PhoneNumber, Address, Role) "
								+ "VALUES ('%s', '%s', '%s', '%s', '%s')",
						user.getUsername(), hashedPassword, user.getPhoneNumber(), user.getAddress(), user.getRole());
				db.execute(query);
				System.out.println("User registered successfully.");
			} catch (Exception e) {
				validationErrors.put("database", "An error occurred during registration.");
				e.printStackTrace();
			}
	
			return validationErrors;
		}
	
		public User loginUser(String username, String password) {
			if (username.equals("admin") && password.equals("admin")) {
				return new User(0, "admin", "", "", "", "Admin");
			}
			
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
	
		private Map<String, String> validateRegistration(User user, String password) {
			Map<String, String> errors = new HashMap<>();
	
			if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
				errors.put("username", "Username cannot be empty.");
			} else if (user.getUsername().length() < 3) {
				errors.put("username", "Username must be at least 3 characters long.");
			} else if (!isUsernameUnique(user.getUsername())) {
				errors.put("username", "Username is already taken.");
			}
	
			if (password == null || password.trim().isEmpty()) {
				errors.put("password", "Password cannot be empty.");
			} else if (password.length() < 8 || !containsSpecialCharacter(password)) {
				errors.put("password", "Password must be at least 8 characters long and include special characters.");
			}
	
			if (user.getPhoneNumber() == null || !user.getPhoneNumber().startsWith("+62")
					|| user.getPhoneNumber().length() != 13) {
				errors.put("phoneNumber", "Phone number must start with +62 and be 13 digits long.");
			}
	
			if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
				errors.put("address", "Address cannot be empty.");
			}
	
			if (!user.getRole().equalsIgnoreCase("Seller") && !user.getRole().equalsIgnoreCase("Buyer")) {
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
