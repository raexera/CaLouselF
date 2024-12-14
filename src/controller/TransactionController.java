package controller;

import database.DatabaseConnector;
import model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionController {
	private static TransactionController instance;
	private final DatabaseConnector db;

	private TransactionController() {
		db = DatabaseConnector.getInstance();
	}

	public static TransactionController getInstance() {
		if (instance == null) {
			instance = new TransactionController();
		}
		return instance;
	}

	public void createTransaction(Transaction transaction) {
		String query = String.format("INSERT INTO Transactions (BuyerID, ItemID, TotalPrice) VALUES (%d, %d, %.2f)",
				transaction.getBuyerID(), transaction.getItemID(), transaction.getTotalPrice());
		db.execute(query);

		String wishlistQuery = String.format("DELETE FROM Wishlist WHERE BuyerID = %d AND ItemID = %d",
				transaction.getBuyerID(), transaction.getItemID());
		db.execute(wishlistQuery);

		System.out.println("Transaction created successfully. Item removed from wishlist if it existed.");
	}

	public List<Transaction> viewTransactionHistory(int buyerID) {
		String query = String.format("SELECT t.TransactionID, t.BuyerID, t.ItemID, t.TotalPrice, t.CreatedAt, "
				+ "i.ItemName, i.Category, i.Size " + "FROM Transactions t " + "JOIN Items i ON t.ItemID = i.ItemID "
				+ "WHERE t.BuyerID = %d", buyerID);
		ResultSet rs = db.execQuery(query);
		List<Transaction> transactions = new ArrayList<>();
		try {
			while (rs.next()) {
				Transaction transaction = new Transaction(rs.getInt("TransactionID"), rs.getInt("BuyerID"),
						rs.getInt("ItemID"), rs.getDouble("TotalPrice"));
				System.out.printf("Transaction ID: %d, Item Name: %s, Category: %s, Size: %s, Price: %.2f, Date: %s%n",
						transaction.getTransactionID(), rs.getString("ItemName"), rs.getString("Category"),
						rs.getString("Size"), rs.getDouble("TotalPrice"), rs.getTimestamp("CreatedAt"));
				transactions.add(transaction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
}
