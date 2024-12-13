package model;

public class Offer {
	private int offerID;
	private int itemID;
	private int buyerID;
	private double offerPrice;
	private String status;
	private String declineReason;

	public Offer(int offerID, int itemID, int buyerID, double offerPrice, String status, String declineReason) {
		super();
		this.offerID = offerID;
		this.itemID = itemID;
		this.buyerID = buyerID;
		this.offerPrice = offerPrice;
		this.status = status;
		this.declineReason = declineReason;
	}

	public int getOfferID() {
		return offerID;
	}

	public void setOfferID(int offerID) {
		this.offerID = offerID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getBuyerID() {
		return buyerID;
	}

	public void setBuyerID(int buyerID) {
		this.buyerID = buyerID;
	}

	public double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeclineReason() {
		return declineReason;
	}

	public void setDeclineReason(String declineReason) {
		this.declineReason = declineReason;
	}
}
