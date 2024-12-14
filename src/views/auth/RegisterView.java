package views.auth;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class RegisterView extends Application {

    private UserController userController;

    @Override
    public void start(Stage primaryStage) {

        userController = new UserController();

        Label title = new Label("CaLouselF");
        title.setStyle(
                "-fx-font-family: 'Arial'; -fx-font-size: 48px; -fx-font-weight: bolder; -fx-font-style: italic;");

        Label subtitle = new Label("Register Page");
        subtitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Form Fields
        Label usernameLbl = new Label("Username:");
        TextField usernameTf = new TextField();

        Label passwordLbl = new Label("Password:");
        PasswordField passwordPf = new PasswordField();

        Label phoneLbl = new Label("Phone Number (+62):");
        TextField phoneTf = new TextField();

        Label addressLbl = new Label("Address:");
        TextField addressTf = new TextField();

        Label roleLbl = new Label("Role:");
        ComboBox<String> roleCb = new ComboBox<>();
        roleCb.getItems().addAll("Buyer", "Seller");
        roleCb.setPromptText("Select Role");

        Button registerBtn = new Button("Register");
        Label errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        Hyperlink loginLink = new Hyperlink("Back to Login");
        loginLink.setStyle("-fx-text-fill: blue;");

        // Layout
        VBox layout = new VBox(14, title, subtitle, usernameLbl, usernameTf, passwordLbl, passwordPf,
                phoneLbl, phoneTf, addressLbl, addressTf, roleLbl, roleCb, registerBtn, errorLbl, loginLink);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 40;");
        layout.setMaxWidth(400);

        registerBtn.setOnAction(e -> {
            String username = usernameTf.getText();
            String password = passwordPf.getText();
            String phoneNumber = phoneTf.getText();
            String address = addressTf.getText();
            String role = roleCb.getValue();

            Map<String, String> errors = userController.registerUser(username, password, phoneNumber, address, role);

            if (errors.isEmpty()) {
                errorLbl.setText("Registration Successful!");
                System.out.println("User registered: " + username);
            } else {
                StringBuilder errorMsg = new StringBuilder();
                errors.forEach((key, value) -> errorMsg.append(value).append("\n"));
                errorLbl.setText(errorMsg.toString());
            }
        });

        loginLink.setOnAction(e -> {
            try {
                new LoginView().start(primaryStage); // Navigasi kembali ke halaman login
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(layout, 600, 400); // Ukuran sama dengan LoginView
        primaryStage.setTitle("Register Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
