package controller;

import database.DatabaseConnector;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public void uploadItem(Item item) {
		if (validateItemDetails(item)) {
			String query = String.format(
					"INSERT INTO Items (SellerID, ItemName, Category, Size, Price, Status) VALUES (%d, '%s', '%s', '%s', %.2f, 'Pending')",
					item.getSellerID(), item.getItemName(), item.getCategory(), item.getSize(), item.getPrice());
			db.execute(query);
			System.out.println("Item uploaded successfully. Pending admin approval.");
		}
	}

	public void editItem(Item item) {
		if (validateItemDetails(item)) {
			String query = String.format(
					"UPDATE Items SET ItemName = '%s', Category = '%s', Size = '%s', Price = %.2f WHERE ItemID = %d AND Status = 'Approved'",
					item.getItemName(), item.getCategory(), item.getSize(), item.getPrice(), item.getItemID());
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
	
	public List<Item> viewPendingItems() {
	    String query = "SELECT * FROM Items WHERE Status = 'Pending'";
	    ResultSet rs = db.execQuery(query);
	    List<Item> items = new ArrayList<>();
	    try {
	        while (rs.next()) {
	            items.add(new Item(
	                rs.getInt("ItemID"),
	                rs.getInt("SellerID"),
	                rs.getString("ItemName"),
	                rs.getString("Category"),
	                rs.getString("Size"),
	                rs.getDouble("Price"),
	                rs.getString("Status"),
	                rs.getString("DeclineReason")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return items;
	}
	
	public List<Item> viewItems(int sellerID) {
	    String query = String.format("SELECT * FROM Items WHERE Status = 'Approved' AND SellerID = %d", sellerID);
	    ResultSet rs = db.execQuery(query);
	    List<Item> items = new ArrayList<>();
	    try {
	        while (rs.next()) {
	            items.add(new Item(rs.getInt("ItemID"), rs.getInt("SellerID"), rs.getString("ItemName"),
	                    rs.getString("Category"), rs.getString("Size"), rs.getDouble("Price"), 
	                    rs.getString("Status"), rs.getString("DeclineReason")));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return items;
	}

	private boolean validateItemDetails(Item item) {
		if (item.getItemName() == null || item.getItemName().trim().length() < 3) {
			System.out.println("Item name must be at least 3 characters long.");
			return false;
		}
		if (item.getCategory() == null || item.getCategory().trim().length() < 3) {
			System.out.println("Item category must be at least 3 characters long.");
			return false;
		}
		if (item.getSize() == null || item.getSize().trim().isEmpty()) {
			System.out.println("Item size cannot be empty.");
			return false;
		}
		if (item.getPrice() <= 0) {
			System.out.println("Item price must be greater than zero.");
			return false;
		}
		return true;
	}
}
