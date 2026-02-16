package nusgpt;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for NUSGPT using FXML.
 */
public class Main extends Application {

    private NUSGPT nusgpt = new NUSGPT("data/nusgpt.NUSGPT.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();

            assert ap != null : "AnchorPane must not be null";
            assert fxmlLoader.getController() != null : "MainWindow controller must not be null";

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setNUSGPT(nusgpt);  // inject the NUSGPT instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}