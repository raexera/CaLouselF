package model;

public class Item {
	private int itemID;
	private int sellerID;
	private String itemName;
	private String category;
	private String size;
	private double price;
	private String status;
	private String declineReason;

	public Item(int itemID, int sellerID, String itemName, String category, String size, double price, String status,
			String declineReason) {
		super();
		this.itemID = itemID;
		this.sellerID = sellerID;
		this.itemName = itemName;
		this.category = category;
		this.size = size;
		this.price = price;
		this.status = status;
		this.declineReason = declineReason;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getSellerID() {
		return sellerID;
	}

	public void setSellerID(int sellerID) {
		this.sellerID = sellerID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
