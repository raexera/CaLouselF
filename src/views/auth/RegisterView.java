package views.auth;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.util.Map;

public class RegisterView extends Application {

	private UserController userController;

	@Override
	public void start(Stage primaryStage) {

		userController = UserController.getInstance();

		Label title = new Label("CaLouselF");
		title.setStyle(
				"-fx-font-family: 'Arial'; -fx-font-size: 48px; -fx-font-weight: bolder; -fx-font-style: italic;");

		Label subtitle = new Label("Register Page");
		subtitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 20px; -fx-font-weight: bold;");

		Label usernameLbl = new Label("Username:");
		TextField usernameTf = new TextField();
		usernameTf.setPrefWidth(300);

		Label passwordLbl = new Label("Password:");
		PasswordField passwordPf = new PasswordField();
		passwordPf.setPrefWidth(300);

		Label phoneLbl = new Label("Phone Number (+62):");

		TextField phoneTf = new TextField();
		phoneTf.setPromptText("+62xxxxxxxxxxx");
		Label phoneHelper = new Label("Format: +62 followed by 10 digits");
		phoneHelper.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");
		phoneTf.setPrefWidth(300);

		Label addressLbl = new Label("Address:");
		TextField addressTf = new TextField();
		addressTf.setPrefWidth(300);

		Label roleLbl = new Label("Role:");
		ComboBox<String> roleCb = new ComboBox<>();
		roleCb.getItems().addAll("Buyer", "Seller");
		roleCb.setPromptText("Select Role");
		roleCb.setPrefWidth(300);

		Button registerBtn = new Button("Register");
		Label errorLbl = new Label();
		errorLbl.setStyle("-fx-text-fill: red;");

		Hyperlink loginLink = new Hyperlink("Back to Login");
		loginLink.setStyle("-fx-text-fill: blue;");

		VBox layout = new VBox(14, title, subtitle, usernameLbl, usernameTf, passwordLbl, passwordPf, phoneLbl, phoneTf,
				addressLbl, addressTf, roleLbl, roleCb, registerBtn, errorLbl, loginLink);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-padding: 40; -fx-spacing: 10;");

		registerBtn.setOnAction(e -> {

			errorLbl.setStyle("-fx-text-fill: red;");
			errorLbl.setText("");

			// avoid extra space di input
			String username = usernameTf.getText().trim();
			String password = passwordPf.getText().trim();
			String phoneNumber = phoneTf.getText().trim();
			String address = addressTf.getText().trim();
			String role = roleCb.getValue();

			if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
				errorLbl.setText("All fields must be filled.");
				return;
			}

			if (role == null) {
				errorLbl.setText("Role must be selected.");
				return;
			}

			User newUser = new User(0, username, null, phoneNumber, address, role);

			Map<String, String> errors = userController.registerUser(newUser, password);

			if (errors.isEmpty()) {
				errorLbl.setStyle("-fx-text-fill: green;");
				errorLbl.setText("Registration Successful!");
				System.out.println("User registered: " + username);

				// optional: clear form setelah sukses
				usernameTf.clear();
				passwordPf.clear();
				phoneTf.clear();
				addressTf.clear();
				roleCb.setValue(null);
			} else {
				errorLbl.setStyle("-fx-text-fill: red;");
				StringBuilder errorMsg = new StringBuilder();
				errors.forEach((key, value) -> errorMsg.append(value).append("\n"));
				errorLbl.setText(errorMsg.toString());
			}
		});

		loginLink.setOnAction(e -> {
			try {
				new LoginView().start(primaryStage);
			} catch (Exception ex) {
				ex.printStackTrace();
				errorLbl.setText("Failed to navigate to login page.");
			}
		});

		Scene scene = new Scene(layout, 600, 500);
		primaryStage.setTitle("Register Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
