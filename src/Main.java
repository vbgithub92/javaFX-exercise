import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage PrimaryStage) {
 		
		Scene scene = new Scene(new FramePain(),1000,600);
		PrimaryStage.setScene(scene);
		PrimaryStage.setResizable(false);
		PrimaryStage.setAlwaysOnTop(false);
		PrimaryStage.show();
	}
	public static void main(String[] args) {

		launch(args);
	}

}
