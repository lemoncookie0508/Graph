package lepl.graph;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import lepl.LTitleBar;

public class GraphTitleBar extends LTitleBar {
    private final Background basic = new Background(new BackgroundFill(
            Color.rgb(82, 82, 82),
            CornerRadii.EMPTY,
            Insets.EMPTY
    ));
    private final Background entered = new Background(new BackgroundFill(
            Color.rgb(100, 100, 100),
            CornerRadii.EMPTY,
            Insets.EMPTY
    ));

    public GraphTitleBar(GraphBase defend) {
        super(defend);
        Button exportButton = new Button("내보내기");
        exportButton.setFocusTraversable(false);
        exportButton.setLayoutX(142);
        exportButton.setBackground(basic);
        exportButton.setTextFill(Color.WHITE);
        exportButton.setOnAction(actionEvent -> defend.doSave());
        exportButton.setOnMouseEntered(e -> exportButton.setBackground(entered));
        exportButton.setOnMouseExited(e -> exportButton.setBackground(basic));
        add(exportButton);
    }
}
