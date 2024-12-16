package views.homepage;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import controller.ItemController;
import controller.OfferController;
import model.Item;
import model.Offer;
import views.auth.LoginView;

public class SellerView {
	private Stage stage;
	private Scene scene;
	private VBox mainContainer;
	private TableView<Item> itemTable;
	private TableView<Offer> offerTable;
	private ItemController itemController;
	private OfferController offerController;
	private int sellerId;

	public SellerView(Stage stage, int sellerId) {
		this.stage = stage;
		this.sellerId = sellerId;
		this.itemController = ItemController.getInstance();
		this.offerController = OfferController.getInstance();
		initialize();
	}

	private void initialize() {
		mainContainer = new VBox(20);
		mainContainer.setPadding(new Insets(20));
		mainContainer.setAlignment(Pos.CENTER);

		Label headerLabel = new Label("Seller Dashboard");
		headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		TabPane tabPane = new TabPane();

		Tab itemsTab = new Tab("My Items");
		itemsTab.setClosable(false);
		setupItemsTab(itemsTab);

		Tab offersTab = new Tab("Offers");
		offersTab.setClosable(false);
		setupOffersTab(offersTab);

		tabPane.getTabs().addAll(itemsTab, offersTab);

		Button logoutButton = new Button("Logout");
		logoutButton.setOnAction(e -> handleLogout());

		mainContainer.getChildren().addAll(headerLabel, tabPane, logoutButton);

		scene = new Scene(mainContainer, 800, 600);
		stage.setTitle("CaLouselF - Seller Dashboard");
	}

	private void setupItemsTab(Tab tab) {
		VBox container = new VBox(10);
		container.setPadding(new Insets(10));

		itemTable = new TableView<>();

		TableColumn<Item, Integer> idColumn = new TableColumn<>("Item ID");
		idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getItemID()).asObject());

		TableColumn<Item, String> nameColumn = new TableColumn<>("Item Name");
		nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));

		TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
		sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));

		TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
		priceColumn
				.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

		TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

		itemTable.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn, statusColumn);

		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER);

		Button addButton = new Button("Add Item");
		Button editButton = new Button("Edit Item");
		Button deleteButton = new Button("Delete Item");

		buttonContainer.getChildren().addAll(addButton, editButton, deleteButton);

		addButton.setOnAction(e -> handleAddItem());
		editButton.setOnAction(e -> handleEditItem());
		deleteButton.setOnAction(e -> handleDeleteItem());

		container.getChildren().addAll(itemTable, buttonContainer);
		tab.setContent(container);

		refreshItemList();
	}

	private void setupOffersTab(Tab tab) {
		VBox container = new VBox(10);
		container.setPadding(new Insets(10));

		offerTable = new TableView<>();

		TableColumn<Offer, Integer> offerIdColumn = new TableColumn<>("Offer ID");
		TableColumn<Offer, Integer> itemIdColumn = new TableColumn<>("Item ID");
		TableColumn<Offer, Integer> buyerIdColumn = new TableColumn<>("Buyer ID");
		TableColumn<Offer, Double> priceColumn = new TableColumn<>("Offer Price");
		TableColumn<Offer, String> statusColumn = new TableColumn<>("Status");

		offerTable.getColumns().addAll(offerIdColumn, itemIdColumn, buyerIdColumn, priceColumn, statusColumn);

		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER);

		Button acceptButton = new Button("Accept Offer");
		Button declineButton = new Button("Decline Offer");

		buttonContainer.getChildren().addAll(acceptButton, declineButton);

		acceptButton.setOnAction(e -> handleAcceptOffer());
		declineButton.setOnAction(e -> handleDeclineOffer());

		container.getChildren().addAll(offerTable, buttonContainer);
		tab.setContent(container);

		refreshOfferList();
	}

	private void handleAddItem() {
		Dialog<Item> dialog = new Dialog<>();
		dialog.setTitle("Add New Item");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField nameField = new TextField();
		TextField categoryField = new TextField();
		TextField sizeField = new TextField();
		TextField priceField = new TextField();

		grid.add(new Label("Item Name:"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Category:"), 0, 1);
		grid.add(categoryField, 1, 1);
		grid.add(new Label("Size:"), 0, 2);
		grid.add(sizeField, 1, 2);
		grid.add(new Label("Price:"), 0, 3);
		grid.add(priceField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				try {
					double price = Double.parseDouble(priceField.getText());
					return new Item(0, sellerId, nameField.getText(), categoryField.getText(), sizeField.getText(),
							price, "Pending", null);
				} catch (NumberFormatException e) {
					showAlert("Error", "Price must be a valid number.");
					return null;
				}
			}
			return null;
		});

		dialog.showAndWait().ifPresent(item -> {
			itemController.uploadItem(item);
			refreshItemList();
		});
	}

	private void handleEditItem() {
		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to edit.");
			return;
		}

		Dialog<Item> dialog = new Dialog<>();
		dialog.setTitle("Edit Item");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField nameField = new TextField(selectedItem.getItemName());
		TextField categoryField = new TextField(selectedItem.getCategory());
		TextField sizeField = new TextField(selectedItem.getSize());
		TextField priceField = new TextField(String.valueOf(selectedItem.getPrice()));

		grid.add(new Label("Item Name:"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Category:"), 0, 1);
		grid.add(categoryField, 1, 1);
		grid.add(new Label("Size:"), 0, 2);
		grid.add(sizeField, 1, 2);
		grid.add(new Label("Price:"), 0, 3);
		grid.add(priceField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				try {
					double price = Double.parseDouble(priceField.getText());
					return new Item(selectedItem.getItemID(), sellerId, nameField.getText(), categoryField.getText(),
							sizeField.getText(), price, selectedItem.getStatus(), selectedItem.getDeclineReason());
				} catch (NumberFormatException e) {
					showAlert("Error", "Price must be a valid number.");
					return null;
				}
			}
			return null;
		});

		dialog.showAndWait().ifPresent(item -> {
			itemController.editItem(item);
			refreshItemList();
		});
	}

	private void handleDeleteItem() {
		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to delete.");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Item");
		alert.setContentText("Are you sure you want to delete this item?");

		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				itemController.deleteItem(selectedItem.getItemID(), sellerId);
				refreshItemList();
			}
		});
	}

	private void handleAcceptOffer() {
		Offer selectedOffer = offerTable.getSelectionModel().getSelectedItem();
		if (selectedOffer == null) {
			showAlert("Error", "Please select an offer to accept.");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Accept Offer");
		alert.setContentText("Are you sure you want to accept this offer?");

		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				offerController.acceptOffer(selectedOffer.getOfferID());
				refreshOfferList();
				refreshItemList();
			}
		});
	}

	private void handleDeclineOffer() {
		Offer selectedOffer = offerTable.getSelectionModel().getSelectedItem();
		if (selectedOffer == null) {
			showAlert("Error", "Please select an offer to decline.");
			return;
		}

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Decline Offer");
		dialog.setHeaderText("Enter reason for declining:");
		dialog.setContentText("Reason:");

		dialog.showAndWait().ifPresent(reason -> {
			if (reason.trim().isEmpty()) {
				showAlert("Error", "Decline reason cannot be empty.");
				return;
			}
			offerController.declineOffer(selectedOffer.getOfferID(), reason);
			refreshOfferList();
		});
	}

	private void handleLogout() {

		try {
			new LoginView().start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshItemList() {
		itemTable.getItems().clear();
		List<Item> items = itemController.viewItems(sellerId);
		// ini buat mastiin udh masuk aja
//	    System.out.println("Jumlah item: " + items.size());
		itemTable.getItems().addAll(items);
	}

	private void refreshOfferList() {
		offerTable.getItems().clear();
		offerTable.getItems().addAll(offerController.viewOffers(sellerId));
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