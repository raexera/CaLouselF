package views.homepage;

import controller.ItemController;
import controller.TransactionController;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;

public class PurchaseHistoryView {
	
	private Stage stage;
	private Scene scene;
	private VBox mainContainer;
	private TableView<Item> itemTable;
	private ItemController itemController;
	private TransactionController trxController;
	private int buyerId;

	public PurchaseHistoryView(Stage stage, int buyerId) {
		this.stage = stage;
		this.buyerId = buyerId;
		this.itemController = ItemController.getInstance();
		this.trxController = TransactionController.getInstance();
		initialize();
	}
	
	private void initialize() {
		mainContainer = new VBox(20);
		mainContainer.setPadding(new Insets(20));
		mainContainer.setAlignment(Pos.CENTER);

		Label headerLabel = new Label("Purchase History");
		headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		setupItemTable();

		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER);

		Button backButton = new Button("Back to Dashboard");

		buttonContainer.getChildren().addAll(backButton);

		mainContainer.getChildren().addAll(headerLabel, itemTable, buttonContainer);

		backButton.setOnAction(e -> handleBackToDashboard());

		scene = new Scene(mainContainer, 800, 600);
		stage.setTitle("CaLouselF - Buyer Purchase History");
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

		itemTable.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn);

		VBox.setVgrow(itemTable, Priority.ALWAYS);
		itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		refreshItemList();
	}
	
	private void refreshItemList() {
		itemTable.getItems().clear();
		itemTable.getItems().addAll(trxController.viewTransactionHistory(this.buyerId));
	}

	private void handleBackToDashboard() {
		BuyerView buyerView = new BuyerView(stage, this.buyerId);
		stage.setScene(buyerView.getScene());
		stage.show();
	}

	public Scene getScene() {
		return scene;
	}

}
