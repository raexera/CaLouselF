package views.auth;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class LoginView extends Application {

	private UserController userController;

	@Override
	public void start(Stage primaryStage) {

		userController = new UserController();

		Label title = new Label("CaLouselF");
		title.setStyle(
				"-fx-font-family: 'Arial'; -fx-font-size: 48px; -fx-font-weight: bolder; -fx-font-style: italic;");

		Label subtitle = new Label("Login Page");
		subtitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 20px; -fx-font-weight: bold;");

		Label usernameLbl = new Label("Username:");
		TextField usernameTf = new TextField();

		Label passwordLbl = new Label("Password:");
		PasswordField passwordPf = new PasswordField();

		Button loginBtn = new Button("Login");
		Label errorLbl = new Label();
		errorLbl.setStyle("-fx-text-fill: red;");

		Hyperlink registerLink = new Hyperlink("Register");
		registerLink.setStyle("-fx-text-fill: blue;");

		// Layout
		VBox layout = new VBox(14, title, subtitle, usernameLbl, usernameTf, passwordLbl, passwordPf, loginBtn,
				errorLbl, registerLink);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-padding: 40;");
		layout.setMaxWidth(400);

		loginBtn.setOnAction(e -> {
			String username = usernameTf.getText();
			String password = passwordPf.getText();

			if (username.isEmpty() || password.isEmpty()) {
				errorLbl.setText("Username or Password cannot be empty!");
				return;
			}

			User user = userController.loginUser(username, password);
			if (user == null) {
				errorLbl.setText("Invalid username or password!");
			} else {
				errorLbl.setText("Login Successful!");
				System.out.println("Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");
				// TODO : navigate ke halaman berikutnya (kalau perlu)
			}
		});

		
		registerLink.setOnAction(e -> {
			System.out.println("Redirect to Register Page!");
		});

		
		Scene scene = new Scene(layout, 600, 400); // adjust kl mau kecil / besar
		primaryStage.setTitle("Login Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
