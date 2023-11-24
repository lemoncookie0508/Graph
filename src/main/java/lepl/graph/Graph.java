package lepl.graph;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lepl.Constant;
import lepl.LApplication;

public class Graph extends LApplication {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);

        GraphBase graphBase = new GraphBase(primaryStage);
        Scene graphScene = graphBase.makeScene();
        primaryStage.setScene(graphScene);
        setIcon(primaryStage, graphBase, new Image(Constant.getImageResource("icon.png")));
        setTitle(primaryStage, graphBase, "그래프 이미지 생성기");

        primaryStage.show();
    }
}
