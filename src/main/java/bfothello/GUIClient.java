package bfothello;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUIClient extends Application {

    private Parent createContent() {
        Rectangle underColor = new Rectangle(800, 800, Color.GREEN);
        center_box(underColor);
        return new Pane(underColor);
    }

    private void center_box(Rectangle box) {
        box.setTranslateX(50);
        box.setTranslateY(50);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), 900, 900) );
        stage.show();
    }

}
