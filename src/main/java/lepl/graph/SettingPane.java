package lepl.graph;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import lepl.Constant;
import lepl.ShortcutHandler;

import java.util.List;

public class SettingPane extends AnchorPane {

    private final GraphPane graphPane;

    public RadioButton backgroundButton = new RadioButton();

    public final ShortcutHandler focusRequest;

    private final VBox box = new VBox();

    public SettingPane(GraphPane graphPane) {
        this.graphPane = graphPane;

        setLayoutX(1080);
        setPrefSize(1500, 2400);
        setBackground(new Background(new BackgroundFill(
                Color.rgb(190, 190, 190),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        Label functionLabel = new Label("함수 추가");
        functionLabel.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 70));
        functionLabel.setLayoutX(20);
        functionLabel.setLayoutY(5);
        getChildren().add(functionLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFocusTraversable(false);
        scrollPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(220, 220, 220),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setLayoutX(20);
        scrollPane.setLayoutY(300);
        scrollPane.setPrefSize(1460, 1970);
        scrollPane.setHmax(0);
        scrollPane.setBorder(new Border(new BorderStroke(Color.rgb(100, 100, 100), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        box.setBackground(new Background(new BackgroundFill(
                Color.rgb(220, 220, 220),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        box.setPrefSize(1460, 1969);
        scrollPane.setContent(box);
        getChildren().add(scrollPane);

        TextField functionText = new TextField();
        functionText.setLayoutX(20);
        functionText.setLayoutY(110);
        functionText.setPrefSize(1460, 120);
        functionText.setFocusTraversable(false);
        functionText.setFont(Font.loadFont(Constant.getResource("fonts/cambria.ttc"), 70));
        functionText.setOnAction(actionEvent -> {
            if (!functionText.getText().isBlank()) {
                try {
                    functionText.setBorder(null);
                    box.getChildren().add(new FunctionPane(functionText.getText(), graphPane, this));
                    functionText.setText("");
                } catch (Exception e) {
                    functionText.setBorder(new Border(new BorderStroke(Color.rgb(200, 0, 0), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                }
            }
        });
        getChildren().add(functionText);
        focusRequest = functionText::requestFocus;

        Label backgroundButtonLabel = new Label("배경화면 함께 내보내기");
        backgroundButtonLabel.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 40));
        backgroundButtonLabel.setPrefHeight(110);
        backgroundButtonLabel.setLayoutY(2280);
        backgroundButtonLabel.setLayoutX(30);
        getChildren().add(backgroundButtonLabel);

        backgroundButton.getTransforms().add(new Scale(3, 3));
        backgroundButton.setLayoutX(445);
        backgroundButton.setLayoutY(2312);
        backgroundButton.setFocusTraversable(false);
        backgroundButton.setSelected(true);
        backgroundButton.setOnAction(actionEvent -> graphPane.isBackgroundExport = backgroundButton.isSelected());
        getChildren().add(backgroundButton);

        Label paneSizeLabel = new Label("좌표평면 크기 설정");
        paneSizeLabel.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 40));
        paneSizeLabel.setPrefHeight(110);
        paneSizeLabel.setLayoutY(2280);
        paneSizeLabel.setLayoutX(550);
        getChildren().add(paneSizeLabel);

        int x = 900, t = 180;
        IntOnlyTextField widthText = new IntOnlyTextField(0, 1081, "1080");
        IntOnlyTextField heightText = new IntOnlyTextField(0, 2401, "2400");

        widthText.setPrefSize(150, 90);
        widthText.setLayoutX(x);
        widthText.setLayoutY(2290);
        widthText.setFont(new Font(40));
        widthText.setFocusTraversable(false);
        widthText.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        widthText.setOnAction(actionEvent -> resizePane(widthText.getTextInt(), heightText.getTextInt()));
        getChildren().add(widthText);

        heightText.setPrefSize(150, 90);
        heightText.setLayoutX(x + t);
        heightText.setLayoutY(2290);
        heightText.setFont(new Font(40));
        heightText.setFocusTraversable(false);
        heightText.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        heightText.setOnAction(actionEvent -> resizePane(widthText.getTextInt(), heightText.getTextInt()));
        getChildren().add(heightText);

        Button sizeApplyButton = new Button("적용");
        sizeApplyButton.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 40));
        sizeApplyButton.setPrefSize(140, 80);
        sizeApplyButton.setLayoutX(x + t * 2);
        sizeApplyButton.setLayoutY(2295);
        sizeApplyButton.setFocusTraversable(false);
        sizeApplyButton.setOnAction(actionEvent -> resizePane(widthText.getTextInt(), heightText.getTextInt()));
        getChildren().add(sizeApplyButton);
    }

    public void resizePane(double width, double height) {
        width = Math.min(width, 1080);
        height = Math.min(height, 2400);
        graphPane.setCoordinatePaneWidth(width);
        graphPane.setCoordinatePaneHeight(height);

        for (Node node : box.getChildren()) {
            FunctionPane functionPane = (FunctionPane) node;
            functionPane.resize();
        }

        graphPane.sortElements();
    }

    public boolean remove(FunctionPane functionPane) {
        return box.getChildren().remove(functionPane);
    }
    public List<Node> getFunctionList() {
        return box.getChildren();
    }
}
