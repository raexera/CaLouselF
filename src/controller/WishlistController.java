package controller;

import database.DatabaseConnector;
import model.Wishlist;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistController {
	private static WishlistController instance;
	private final DatabaseConnector db;

	private WishlistController() {
		db = DatabaseConnector.getInstance();
	}

	public static WishlistController getInstance() {
		if (instance == null) {
			instance = new WishlistController();
		}
		return instance;
	}

	public void addItemToWishlist(Wishlist wishlist) {
		String query = String.format("INSERT INTO Wishlist (BuyerID, ItemID) VALUES (%d, %d)", wishlist.getBuyerID(),
				wishlist.getItemID());
		db.execute(query);
		System.out.println("Item added to wishlist successfully.");
	}

	public List<Item> viewWishlist(int buyerID) {
		String query = String
				.format("SELECT i.* FROM Wishlist w JOIN Items i ON w.ItemID = i.ItemID WHERE w.BuyerID = %d", buyerID);
		ResultSet rs = db.execQuery(query);
		List<Item> wishlistItems = new ArrayList<>();
		try {
			while (rs.next()) {
				wishlistItems.add(new Item(rs.getInt("ItemID"), 0, rs.getString("ItemName"), rs.getString("Category"),
						rs.getString("Size"), rs.getDouble("Price"), rs.getString("Status"),
						rs.getString("DeclineReason")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishlistItems;
	}

	public void removeItemFromWishlist(Wishlist wishlist) {
		String query = String.format("DELETE FROM Wishlist WHERE WishlistID = %d AND BuyerID = %d",
				wishlist.getWishlistID(), wishlist.getBuyerID());
		db.execute(query);
		System.out.println("Item removed from wishlist successfully.");
	}
}
