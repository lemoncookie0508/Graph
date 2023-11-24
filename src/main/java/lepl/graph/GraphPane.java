package lepl.graph;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lepl.Constant;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GraphPane extends AnchorPane {

    private double coordinatePaneWidth = 1080, coordinatePaneHeight = 2400;
    public double getCoordinatePaneWidth() {
        return coordinatePaneWidth;
    }
    public double getCoordinatePaneHeight() {
        return coordinatePaneHeight;
    }
    public void setCoordinatePaneWidth(double coordinatePaneWidth) {
        this.coordinatePaneWidth = coordinatePaneWidth;
        setLayoutX((1080 - coordinatePaneWidth) / 2);
    }
    public void setCoordinatePaneHeight(double coordinatePaneHeight) {
        this.coordinatePaneHeight = coordinatePaneHeight;
        setLayoutY((2400 - coordinatePaneHeight) / 2);
    }

    public boolean isBackgroundExport = true;

    private final ImageView xAxis = new ImageView(new Image(Constant.getImageResource("white.png")));
    private final ImageView yAxis = new ImageView(new Image(Constant.getImageResource("white.png")));
    private final ImageView xArrow = new ImageView(new Image(Constant.getImageResource("arrow_x.png")));
    private final ImageView yArrow = new ImageView(new Image(Constant.getImageResource("arrow_y.png")));
    private final Label x = new Label("\uD835\uDC65");
    private final Label y = new Label("\uD835\uDC66");

    private final AnchorPane functionCanvasPane = new AnchorPane();


    public GraphPane() {
        setPrefSize(1080, 2400);
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        xAxis.setFitHeight(10);
        xAxis.setLayoutX(0);

        yAxis.setFitWidth(10);
        yAxis.setLayoutY(30);

        xArrow.setSmooth(true);
        xArrow.setFitWidth(44);
        xArrow.setFitHeight(44);

        yArrow.setSmooth(true);
        yArrow.setFitWidth(44);
        yArrow.setFitHeight(44);
        yArrow.setLayoutY(0);

        x.setTextFill(Color.WHITE);
        x.setFont(new Font(120));

        y.setTextFill(Color.WHITE);
        y.setFont(new Font(120));
        y.setLayoutY(-20);

        sortElements();

        getChildren().add(xAxis);
        getChildren().add(yAxis);
        getChildren().add(xArrow);
        getChildren().add(yArrow);
        getChildren().add(x);
        getChildren().add(y);
        getChildren().add(functionCanvasPane);
    }

    public boolean add(Canvas canvas) {
        return functionCanvasPane.getChildren().add(canvas);
    }
    public List<Node> getCanvasList() {
        return functionCanvasPane.getChildren();
    }
    public boolean remove(Canvas canvas) {
        return functionCanvasPane.getChildren().remove(canvas);
    }

    public void sortElements() {
        xAxis.setLayoutY(coordinatePaneHeight / 2 - 5);
        xAxis.setFitWidth(coordinatePaneWidth - 30);

        yAxis.setLayoutX(coordinatePaneWidth / 2 - 5);
        yAxis.setFitHeight(coordinatePaneHeight - 30);

        xArrow.setLayoutX(coordinatePaneWidth - 44);
        xArrow.setLayoutY(coordinatePaneHeight / 2 - 22);

        yArrow.setLayoutX(coordinatePaneWidth / 2 - 22);

        x.setLayoutX(coordinatePaneWidth - 84);
        x.setLayoutY(coordinatePaneHeight / 2 - 8);

        y.setLayoutX(coordinatePaneWidth / 2 - 112);

        functionCanvasPane.setPrefSize(coordinatePaneWidth, coordinatePaneHeight);
        setPrefSize(coordinatePaneWidth, coordinatePaneHeight);
    }

    public void saveFile(File file) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image;
        if (isBackgroundExport) {
            image = new WritableImage(snapshot(parameters, null).getPixelReader(), 0, 20, (int) coordinatePaneWidth, (int) coordinatePaneHeight);
        } else {
            image = new WritableImage(functionCanvasPane.snapshot(parameters, null).getPixelReader(), 0, 0, (int) coordinatePaneWidth, (int) coordinatePaneHeight);
        }

        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
