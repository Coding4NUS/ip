package nusgpt;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for NUSGPT using FXML.
 */
public class Main extends Application {

    private final NUSGPT nusgpt = new NUSGPT("data/nusgpt.NUSGPT.txt");

    /**
     * Starts the JavaFX UI.
     *
     * @param stage Primary stage provided by JavaFX.
     */
    // generated JavaDoc comment using ChatGPT
    @Override
    public void start(Stage stage) {
        stage.setTitle("NUSGPT");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/app.png")));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            assert root != null : "AnchorPane must not be null";
            assert fxmlLoader.getController() != null : "MainWindow controller must not be null";
            stage.setScene(new Scene(root));
            // inject the NUSGPT instance
            fxmlLoader.<MainWindow>getController().setNusGpt(nusgpt);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}