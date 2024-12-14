package controller;

import database.DatabaseConnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemController {
	private static ItemController instance;
	private final DatabaseConnector db;

	private ItemController() {
		db = DatabaseConnector.getInstance();
	}

	public static ItemController getInstance() {
		if (instance == null) {
			instance = new ItemController();
		}
		return instance;
	}

	public void uploadItem(int sellerID, String itemName, String category, String size, double price) {
		if (validateItemDetails(itemName, category, size, price)) {
			String query = String.format(
					"INSERT INTO Items (SellerID, ItemName, Category, Size, Price, Status) "
							+ "VALUES (%d, '%s', '%s', '%s', %.2f, 'Pending')",
					sellerID, itemName, category, size, price);
			db.execute(query);
			System.out.println("Item uploaded successfully. Pending admin approval.");
		}
	}

	public void editItem(int itemID, String itemName, String category, String size, double price) {
		if (validateItemDetails(itemName, category, size, price)) {
			String query = String.format("UPDATE Items SET ItemName = '%s', Category = '%s', Size = '%s', Price = %.2f "
					+ "WHERE ItemID = %d AND Status = 'Approved'", itemName, category, size, price, itemID);
			db.execute(query);
			System.out.println("Item details updated successfully.");
		}
	}

	public void deleteItem(int itemID, int sellerID) {
		String query = String.format("DELETE FROM Items WHERE ItemID = %d AND SellerID = %d AND Status = 'Approved'",
				itemID, sellerID);
		db.execute(query);
		System.out.println("Item deleted successfully.");
	}

	public void viewItems() {
		String query = "SELECT ItemName, Category, Size, Price FROM Items WHERE Status = 'Approved'";
		ResultSet rs = db.execQuery(query);
		try {
			while (rs.next()) {
				System.out.printf("Name: %s, Category: %s, Size: %s, Price: %.2f%n", rs.getString("ItemName"),
						rs.getString("Category"), rs.getString("Size"), rs.getDouble("Price"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean validateItemDetails(String name, String category, String size, double price) {
		if (name == null || name.trim().length() < 3) {
			System.out.println("Item name must be at least 3 characters long.");
			return false;
		}
		if (category == null || category.trim().length() < 3) {
			System.out.println("Item category must be at least 3 characters long.");
			return false;
		}
		if (size == null || size.trim().isEmpty()) {
			System.out.println("Item size cannot be empty.");
			return false;
		}
		if (price <= 0) {
			System.out.println("Item price must be greater than zero.");
			return false;
		}
		return true;
	}
}
