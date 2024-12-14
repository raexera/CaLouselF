package main;

import javafx.application.Application;
import javafx.stage.Stage;
import views.auth.LoginView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			LoginView loginView = new LoginView();
			loginView.start(primaryStage); 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}