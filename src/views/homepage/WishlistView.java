package views.homepage;

import controller.ItemController;
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
import views.auth.LoginView;

public class WishlistView {
	
	// buat testing user:
		// Username: user1
		// Password: @user_12345
		
		
		private Stage stage;
		private Scene scene;
		private VBox mainContainer;
		private TableView<Item> itemTable;
		private ItemController itemController;
		private int buyerId;

		public WishlistView(Stage stage, int buyerId) {
			this.stage = stage;
			this.buyerId = buyerId;
			this.itemController = ItemController.getInstance();
			initialize();
		}
		
		private void initialize() {
			mainContainer = new VBox(20);
			mainContainer.setPadding(new Insets(20));
			mainContainer.setAlignment(Pos.CENTER);

			Label headerLabel = new Label("Wishlist");
			headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

			setupItemTable();

			HBox buttonContainer = new HBox(10);
			buttonContainer.setAlignment(Pos.CENTER);

			Button removeItemButton = new Button("Remove Item");
			Button backButton = new Button("Back to Dashboard");

			buttonContainer.getChildren().addAll(removeItemButton, backButton);

			mainContainer.getChildren().addAll(headerLabel, itemTable, buttonContainer);

			removeItemButton.setOnAction(e -> handleRemoveItem());
			backButton.setOnAction(e -> handleBackToDashboard());

			scene = new Scene(mainContainer, 800, 600);
			stage.setTitle("CaLouselF - Buyer Wishlist");
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

//			TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
//			statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

			itemTable.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn);

			VBox.setVgrow(itemTable, Priority.ALWAYS);
			itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

			refreshItemList();
		}
		
		private void refreshItemList() {
			itemTable.getItems().clear();
			itemTable.getItems().addAll(itemController.viewItemsWishlist(this.buyerId));
		}

		private void handleRemoveItem() {
			// query input data ke table transactions yang memiliki column TransactionID (Auto Increment), ItemID, BuyerID, dan TotalPrice
			// Total price didapat dari harga item yang diselect
			// Setelah data masuk ke tabel transactions data item yang dibeli akan dihapus dari wishlist user

			Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
			if (selectedItem == null) {
				showAlert("Error", "Please select a wishlist item to remove.");
				return;
			}
			
			 // Tampilkan dialog konfirmasi
		    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmationAlert.setTitle("Confirmation");
		    confirmationAlert.setHeaderText("Are you sure?");
		    confirmationAlert.setContentText(
		        String.format("Do you want to remove the item: %s ?", 
		        selectedItem.getItemName()));
		    
		 // Tindak lanjuti berdasarkan pilihan user
		    confirmationAlert.showAndWait().ifPresent(response -> {
		        if (response.getText().equalsIgnoreCase("OK")) {
		            try {
		                // Hapus item dari tabel wishlist
		                String deleteWishlistQuery = String.format(
		                    "DELETE FROM Wishlist WHERE BuyerID = %d AND ItemID = %d",
		                    this.buyerId,
		                    selectedItem.getItemID()
		                );
		                DatabaseConnector.getInstance().execute(deleteWishlistQuery);

		                // Berikan feedback ke user
		                showAlert("Success", "Item successfully removed.");
		                refreshItemList(); // Refresh daftar item
		            } catch (Exception e) {
		                e.printStackTrace();
		                showAlert("Error", "An error occurred while processing your request.");
		            }
		        } else {
		            // Jika user memilih tidak
		            showAlert("Cancelled", "Item removal was cancelled.");
		        }
		    });
			
		}

		private void handleBackToDashboard() {
			BuyerView buyerView = new BuyerView(stage, this.buyerId);
			stage.setScene(buyerView.getScene());
			stage.show();
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
