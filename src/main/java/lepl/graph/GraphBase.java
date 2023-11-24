package lepl.graph;

import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lepl.BaseType;
import lepl.Constant;
import lepl.LBase;

import java.io.File;

public class GraphBase extends LBase {
    public GraphPane graphPane;

    public GraphBase(Stage primaryStage) {
        super(1080 + 1500, 2400, 20, new BaseType(BaseType.DEFAULT), primaryStage);
        setTitleBar(new GraphTitleBar(this));
        setSmallestWidth(270);
        setBackground(new Background(new BackgroundFill(Color.rgb(100, 100, 100), CornerRadii.EMPTY, Insets.EMPTY)));
        setPaneBackground(new Background(new BackgroundFill(Color.rgb(100, 100, 100), CornerRadii.EMPTY, Insets.EMPTY)));
        scale((Constant.SCREEN_SIZE.getHeight() - Constant.SCREEN_INSETS.bottom - getTitleHeight()) / 2400D);


        add(graphPane = new GraphPane());
        SettingPane s = new SettingPane(graphPane);
        add(s);

        KeyCodeCombination command = new KeyCodeCombination(KeyCode.SLASH);
        keyboardShortcuts.put(command, s.focusRequest);
    }

    public void doSave(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imgType = new FileChooser.ExtensionFilter("PNG file", "*.png");
        fileChooser.getExtensionFilters().add(imgType);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/desktop"));
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            graphPane.saveFile(file);
        }
    }
}
