package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(1524);
        primaryStage.setHeight(1000);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("Logo.png"));
        
        Pane rootPane = new Pane();
        UIManager uiManager = new UIManager(rootPane);
        uiManager.createUI(58,29,25);
        Scene scene = new Scene(uiManager.getRootPane());        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Conway's Game of Life DEMO");
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
