package model;

import java.sql.Timestamp;

public class Transaction {
	private int transactionID;
	private int buyerID;
	private int itemID;
	private double totalPrice;
	private Timestamp createdAt;

	public Transaction(int transactionID, int buyerID, int itemID, double totalPrice, Timestamp createdAt) {
		super();
		this.transactionID = transactionID;
		this.buyerID = buyerID;
		this.itemID = itemID;
		this.totalPrice = totalPrice;
		this.createdAt = createdAt;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getBuyerID() {
		return buyerID;
	}

	public void setBuyerID(int buyerID) {
		this.buyerID = buyerID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
