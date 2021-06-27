import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pcap4j.core.NotOpenException;

public class Sniffer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        SnifferController snifferController = new SnifferController();
        loader.setController(snifferController);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Simple Sniffer");
        primaryStage.show();

        String deviceAddress = getParameters().getUnnamed().get(0);
        SnifferThread snifferThread = new SnifferThread(snifferController, deviceAddress);
        snifferThread.start();

        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                snifferThread.getHandle().breakLoop();
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
            snifferThread.getHandle().close();
            snifferThread.interrupt();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
