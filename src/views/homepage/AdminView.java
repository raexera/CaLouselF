package views.homepage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import controller.ItemController;
import controller.UserController;
import database.DatabaseConnector;
import model.Item;
import views.auth.LoginView;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AdminView {
	private Stage stage;
	private Scene scene;
	private VBox mainContainer;
	private TableView<Item> itemTable;
	private ItemController itemController;
	// panggil user controller buat pake method logout
	private UserController userController;
	

	public AdminView(Stage stage) {
		this.stage = stage;
		this.itemController = ItemController.getInstance();
		initialize();
	}

	private void initialize() {
		mainContainer = new VBox(20);
		mainContainer.setPadding(new Insets(20));
		mainContainer.setAlignment(Pos.CENTER);

		Label headerLabel = new Label("Admin Dashboard");
		headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		setupItemTable();

		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER);

		Button approveButton = new Button("Approve Item");
		Button declineButton = new Button("Decline Item");
		Button logoutButton = new Button("Logout");

		buttonContainer.getChildren().addAll(approveButton, declineButton, logoutButton);

		mainContainer.getChildren().addAll(headerLabel, itemTable, buttonContainer);

		approveButton.setOnAction(e -> handleApproveItem());
		declineButton.setOnAction(e -> handleDeclineItem());
		logoutButton.setOnAction(e -> handleLogout());

		scene = new Scene(mainContainer, 800, 600);
		stage.setTitle("CaLouselF - Admin Dashboard");
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

		TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

		itemTable.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn, statusColumn);

		VBox.setVgrow(itemTable, Priority.ALWAYS);
		itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		refreshItemList();
	}

	private void refreshItemList() {
		itemTable.getItems().clear();
		itemTable.getItems().addAll(itemController.viewPendingItems());
	}

	private void handleApproveItem() {
		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to approve.");
			return;
		}

		String query = String.format("UPDATE Items SET Status = 'Approved' WHERE ItemID = %d",
				selectedItem.getItemID());
		DatabaseConnector.getInstance().execute(query);

		showAlert("Success", "Item approved successfully.");
		refreshItemList();
	}

	private void handleDeclineItem() {
		Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			showAlert("Error", "Please select an item to decline.");
			return;
		}

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Decline Item");
		dialog.setHeaderText("Enter reason for declining:");
		dialog.setContentText("Reason:");

		dialog.showAndWait().ifPresent(reason -> {
			if (reason.trim().isEmpty()) {
				showAlert("Error", "Decline reason cannot be empty.");
				return;
			}

			String query = String.format("UPDATE Items SET Status = 'Declined', DeclineReason = '%s' WHERE ItemID = %d",
					reason, selectedItem.getItemID());
			DatabaseConnector.getInstance().execute(query);

			showAlert("Success", "Item declined successfully.");
			refreshItemList();
		});
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
