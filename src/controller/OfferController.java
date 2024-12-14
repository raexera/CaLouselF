package controller;

import database.DatabaseConnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfferController {
	private static OfferController instance;
	private final DatabaseConnector db;

	private OfferController() {
		db = DatabaseConnector.getInstance();
	}

	public static OfferController getInstance() {
		if (instance == null) {
			instance = new OfferController();
		}
		return instance;
	}

	public void makeOffer(int itemID, int buyerID, double offerPrice) {
		double highestOffer = getHighestOffer(itemID);
		if (offerPrice <= highestOffer) {
			System.out.println("Offer price must be higher than the current highest offer.");
			return;
		}
		String query = String.format("INSERT INTO Offers (ItemID, BuyerID, OfferPrice) VALUES (%d, %d, %.2f)", itemID,
				buyerID, offerPrice);
		db.execute(query);
		System.out.println("Offer submitted successfully.");
	}

	public void viewOffers(int sellerID) {
		String query = String
				.format("SELECT o.OfferID, i.ItemName, i.Category, i.Size, i.Price AS InitialPrice, o.OfferPrice "
						+ "FROM Offers o JOIN Items i ON o.ItemID = i.ItemID "
						+ "WHERE i.SellerID = %d AND o.Status = 'Pending'", sellerID);
		ResultSet rs = db.execQuery(query);
		try {
			while (rs.next()) {
				System.out.printf(
						"OfferID: %d, Item: %s, Category: %s, Size: %s, Initial Price: %.2f, Offer Price: %.2f%n",
						rs.getInt("OfferID"), rs.getString("ItemName"), rs.getString("Category"), rs.getString("Size"),
						rs.getDouble("InitialPrice"), rs.getDouble("OfferPrice"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void acceptOffer(int offerID) {
		String query = String.format("UPDATE Offers SET Status = 'Accepted' WHERE OfferID = %d", offerID);
		db.execute(query);
		removeItemAfterAcceptance(offerID);
		System.out.println("Offer accepted successfully.");
	}

	public void declineOffer(int offerID, String reason) {
		if (reason == null || reason.trim().isEmpty()) {
			System.out.println("Reason for declining cannot be empty.");
			return;
		}
		String query = String.format("UPDATE Offers SET Status = 'Declined', DeclineReason = '%s' WHERE OfferID = %d",
				reason, offerID);
		db.execute(query);
		System.out.println("Offer declined successfully.");
	}

	private double getHighestOffer(int itemID) {
		String query = String.format("SELECT MAX(OfferPrice) AS HighestOffer FROM Offers WHERE ItemID = %d", itemID);
		ResultSet rs = db.execQuery(query);
		try {
			if (rs.next())
				return rs.getDouble("HighestOffer");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void removeItemAfterAcceptance(int offerID) {
		String query = String.format("DELETE FROM Items WHERE ItemID = (SELECT ItemID FROM Offers WHERE OfferID = %d)",
				offerID);
		db.execute(query);
	}
}
