import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SnifferController {
    @FXML
    private ListView<HBox> packetsList;

    private ImageView createImageView(String path) {
        ImageView imageView = new ImageView();
        Image image = new Image(SnifferController.class.getResourceAsStream(path), 22, 22, false, false);
        imageView.setImage(image);
        return imageView;
    }

    public void addPacket(String sourceIP, String destinationIP, Integer packetSize, String details) {
        Platform.runLater(() -> {
            ImageView compSrc = createImageView("/icons/device.png");
            Text t = new Text(sourceIP);
            ImageView arrow = createImageView("/icons/arrow.png");
            Text t2 = new Text(destinationIP);
            ImageView compDest = createImageView("icons/device.png");
            Text t3 = new Text("Caplen: " + packetSize + "\n" + details);

            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.getChildren().addAll(compSrc, t, arrow, t2, compDest, t3);
            packetsList.getItems().add(hbox);
        });
    }
}