package model;

public class Transaction {
	private int transactionID;
	private int buyerID;
	private int itemID;
	private double totalPrice;

	public Transaction(int transactionID, int buyerID, int itemID, double totalPrice) {
		super();
		this.transactionID = transactionID;
		this.buyerID = buyerID;
		this.itemID = itemID;
		this.totalPrice = totalPrice;
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
}
