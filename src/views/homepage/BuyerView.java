package views.homepage;

import controller.ItemController;
import controller.OfferController;
import controller.UserController;
import database.DatabaseConnector;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import model.Offer;
import views.auth.LoginView;

public class BuyerView {
	
	// buat testing user:
	// Username: user1
	// Password: @user_12345
	
	
	private Stage stage;
	private Scene scene;
	private VBox mainContainer;
	private TableView<Item> itemTable;
	private ItemController itemController;
	private int buyerId;

	public BuyerView(Stage stage, int buyerId) {
		this.stage = stage;
		this.buyerId = buyerId;
		this.itemController = ItemController.getInstance();
		initialize();
	}
	
	private void initialize() {
		mainContainer = new VBox(20);
		mainContainer.setPadding(new Insets(20));
		mainContainer.setAlignment(Pos.CENTER);

		Label headerLabel = new Label("Buyer Dashboard");
		headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		setupItemTable();

		HBox buttonContainer = new HBox(10);
		HBox logoutContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER);
		logoutContainer.setAlignment(Pos.CENTER);

		Button buyButton = new Button("Buy Item");
		Button wishlistButton = new Button("Add to Wishlist");
		Button makeOfferButton = new Button("Offer Price");
		Button viewWishlistButton = new Button("View Wishlist");
		Button viewTransactionsHitoryButton = new Button("View Transactions History");
		Button logoutButton = new Button("Logout");

		buttonContainer.getChildren().addAll(buyButton, wishlistButton, makeOfferButton, viewWishlistButton, viewTransactionsHitoryButton);
		logoutContainer.getChildren().addAll(logoutButton);
		mainContainer.getChildren().addAll(headerLabel, itemTable, buttonContainer, logoutContainer);

		buyButton.setOnAction(e -> handleBuyItem());
		wishlistButton.setOnAction(e -> handleWishlistItem());
		makeOfferButton.setOnAction(e -> handleMakeOfferItem());
		viewWishlistButton.setOnAction(e -> handleViewWishlist());
		viewTransactionsHitoryButton.setOnAction(e -> handleViewTrxHistory());
		logoutButton.setOnAction(e -> handleLogout());

		scene = new Scene(mainContainer, 800, 600);
		stage.setTitle("CaLouselF - Buyer Dashboard");
	}
	
	private void setupItemTable() {
		itemTable = new TableView<>();

		TableColumn<Item, Integer> idColumn = new TableColumn<>("Item ID");
		idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getItemID()));

		TableColumn<Item, String> nameColumn = new TableColumn<>("Item Name");
		nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemName()));

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));

		TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
		sizeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSize()));

		TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));

//		TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
//		statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

		itemTable.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn);

		VBox.setVgrow(itemTable, Priority.ALWAYS);
		itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		refreshItemList();
	}
	
	private void refreshItemList() {
		itemTable.getItems().clear();
		itemTable.getItems().addAll(itemController.viewItemsToBuy());
	}

	private void handleBuyItem() {
		// query input data ke table transactions yang memiliki column TransactionID (Auto Increment), ItemID, BuyerID, dan TotalPrice
		// Total price didapat dari harga item yang diselect
		// Setelah data masuk ke tabel transactions data item yang dibeli akan dihapus dari wishlist user

		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to buy.");
			return;
		}
		
		 // Tampilkan dialog konfirmasi
	    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmationAlert.setTitle("Confirmation");
	    confirmationAlert.setHeaderText("Are you sure?");
	    confirmationAlert.setContentText(
	        String.format("Do you want to buy the item: %s for $%.2f?", 
	        selectedItem.getItemName(),  
	        selectedItem.getPrice()));
	    
	 // Tindak lanjuti berdasarkan pilihan user
	    confirmationAlert.showAndWait().ifPresent(response -> {
	        if (response.getText().equalsIgnoreCase("OK")) {
	            try {
	                // Insert item ke tabel transactions
	                String insertTransactionQuery = String.format(
	                    "INSERT INTO Transactions (BuyerID, ItemID, TotalPrice) VALUES (%d, %d, %.2f)",
	                    this.buyerId,
	                    selectedItem.getItemID(),
	                    selectedItem.getPrice()
	                );
	                DatabaseConnector.getInstance().execute(insertTransactionQuery);

	                // Hapus item dari tabel wishlist
	                String deleteWishlistQuery = String.format(
	                    "DELETE FROM Wishlist WHERE BuyerID = %d AND ItemID = %d",
	                    this.buyerId,
	                    selectedItem.getItemID()
	                );
	                DatabaseConnector.getInstance().execute(deleteWishlistQuery);

	                // Berikan feedback ke user
	                showAlert("Success", "Item successfully purchased.");
	                refreshItemList(); // Refresh daftar item
	            } catch (Exception e) {
	                e.printStackTrace();
	                showAlert("Error", "An error occurred while processing your purchase.");
	            }
	        } else {
	            // Jika user memilih tidak
	            showAlert("Cancelled", "Item purchase was cancelled.");
	        }
	    });
		
	    // Dibawah ini code cadangan kalau code diatas ini error
	    // Bedanya cuma 1 code diatas bakal muncul pop up confirmasi yakin mau beli apa g,
	    // kalo code dibawah begitu tombol beli dipencet datanya lgsg masuk ke database g ada pop up dl.
	    
//		// Input data ke table transactions
//		int loggedInUserID = UserController.getInstance().getLoggedInUserID();
//		
//		String queryBeli = String.format("INSERT INTO transactions (BuyerID, ItemID, TotalPrice) VALUES(%d, %d, %lf);",
//				loggedInUserID, selectedItem.getItemID(), selectedItem.getPrice());
//		DatabaseConnector.getInstance().execute(queryBeli);
//		
//		// Hapus data item yang dibeli dari table wishlist
//		String queryWishlist = String.format("DELETE FROM wishlist WHERE ItemID = %d AND BuyerID = %d;",
//				selectedItem.getItemID(), loggedInUserID);
//		DatabaseConnector.getInstance().execute(queryWishlist);
//		
//		showAlert("Success", "Item bought successfully.");
//		refreshItemList();
	}

	private void handleWishlistItem() {
		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to add to wishlist.");
			return;
		}
		
		String queryWishlist = String.format("INSERT INTO wishlist (BuyerID, ItemID) VALUES(%d, %d);",
				this.buyerId, selectedItem.getItemID());
		DatabaseConnector.getInstance().execute(queryWishlist);
		
		showAlert("Success", "Item successfully added to wishlist.");
		refreshItemList();
	}
	
	private void handleMakeOfferItem() {
	    // Ambil item yang dipilih
	    Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
	    if (selectedItem == null) {
	        showAlert("Error", "Please select an item to make an offer.");
	        return;
	    }

	    // Membuat pop-up dialog untuk input harga
	    TextInputDialog offerDialog = new TextInputDialog();
	    offerDialog.setTitle("Make an Offer");
	    offerDialog.setHeaderText(String.format("Make an offer for item: %s (ID: %d)", 
	        selectedItem.getItemName(), 
	        selectedItem.getItemID()));
	    offerDialog.setContentText("Enter your offer price:");

	    // Handle input dari user
	    offerDialog.showAndWait().ifPresent(offerPriceStr -> {
	        if (offerPriceStr.trim().isEmpty()) {
	            showAlert("Error", "Offer price cannot be empty.");
	            return;
	        }

	        try {
	            // Parse input menjadi double
	            double offerPrice = Double.parseDouble(offerPriceStr);
	            if (offerPrice <= 0) {
	                showAlert("Error", "Offer price must be greater than zero.");
	                return;
	            }

	            // Buat objek Offer
	            Offer newOffer = new Offer(0, selectedItem.getItemID(), this.buyerId, offerPrice, "Pending", "N/A");

	            // Cek dengan OfferController
	            OfferController offerController = OfferController.getInstance();
//	            double currentHighestOffer = offerController.getHighestOffer(selectedItem.getItemID());
//	            if (offerPrice <= currentHighestOffer) {
//	                showAlert("Error", String.format("Your offer must be higher than the current highest offer of %.2f.", currentHighestOffer));
//	                return;
//	            }

	            // Jika valid, masukkan ke database
	            offerController.makeOffer(newOffer);
	            showAlert("Success", "Your offer has been submitted. Status: Pending.");

	        } catch (NumberFormatException e) {
	            showAlert("Error", "Please enter a valid numeric offer price.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert("Error", "An error occurred while submitting your offer.");
	        }
	    });
	}

	
	private void handleViewWishlist() {
		/*
		 1. redirect ke wishlist page
		 2. di wishlist page butuh user id buat access data wishlist user tersebut di table wishlist
		 * 
		*/
		
		WishlistView wishlistView = new WishlistView(stage, this.buyerId);
		stage.setScene(wishlistView.getScene());
		stage.show();
	}
	
	private void handleViewTrxHistory() {
		PurchaseHistoryView purchaseHistoryView = new PurchaseHistoryView(stage, this.buyerId);
		stage.setScene(purchaseHistoryView.getScene());
		stage.show();
	}

	private void handleLogout() {
		try {
			new LoginView().start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public Scene getScene() {
		return scene;
	}

}
