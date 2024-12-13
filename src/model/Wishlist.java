package model;

import java.sql.Timestamp;

public class Wishlist {
	private int wishlistID;
	private int buyerID;
	private int itemID;
	private Timestamp createdAt;

	public Wishlist(int wishlistID, int buyerID, int itemID, Timestamp createdAt) {
		super();
		this.wishlistID = wishlistID;
		this.buyerID = buyerID;
		this.itemID = itemID;
		this.createdAt = createdAt;
	}

	public int getWishlistID() {
		return wishlistID;
	}

	public void setWishlistID(int wishlistID) {
		this.wishlistID = wishlistID;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
