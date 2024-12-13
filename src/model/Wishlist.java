package model;

public class Wishlist {
	private int wishlistID;
	private int buyerID;
	private int itemID;

	public Wishlist(int wishlistID, int buyerID, int itemID) {
		super();
		this.wishlistID = wishlistID;
		this.buyerID = buyerID;
		this.itemID = itemID;
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
}
