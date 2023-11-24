package lepl.graph;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lepl.Constant;
import lepl.CustomFunction;
import lepl.Function;
import lepl.InvalidFunctionException;
import lepl.function.ParametricCurve;

import java.util.List;

public class FunctionPane extends AnchorPane {

    private final GraphPane graphPane;
    private final SettingPane settingPane;
    private CustomFunction function = null;
    private ParametricCurve curve = null;
    private Function curveStart, curveEnd;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private static final Background
            basicBackground = new Background(new BackgroundFill(
                    Color.rgb(240, 240, 240),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )),
            errorBackground = new Background(new BackgroundFill(
                    Color.rgb(220, 100, 100),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )),
            deleteButtonBackground = new Background(new BackgroundFill(
                    Color.rgb(0, 0, 0, 0),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            ));
    private static final Border
            basicBorder = new Border(
                    new BorderStroke(
                            Color.rgb(100, 100, 100),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(2)
                    )),
            swapBorder = new Border(
                    new BorderStroke(
                            Color.rgb(139, 0, 255),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(2)
                    ));

    private final TextField functionText;
    private final IntOnlyTextField colorR = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorG = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorB = new IntOnlyTextField(0, 256, "255");
    private final DoubleOnlyTextField colorA = new DoubleOnlyTextField(0, 1, "1");

    private final IntOnlyTextField lineWidthText = new IntOnlyTextField(0, 20, "7");

    private static int selectedIndex = -1;

    public FunctionPane(String functionString, GraphPane graphPane, SettingPane settingPane) throws  InvalidFunctionException {
        this.graphPane = graphPane;
        this.settingPane = settingPane;

        functionString = functionString.replaceAll("\\s", "");
        if (!isCurve(functionString)) this.function = new CustomFunction(functionString);
        else setCurve(functionString);

        this.canvas = new Canvas(graphPane.getCoordinatePaneWidth(), graphPane.getCoordinatePaneHeight());
        this.gc = canvas.getGraphicsContext2D();

        setPrefSize(1450, 290);
        setBackground(basicBackground);
        setBorder(basicBorder);

        functionText = new TextField(functionString);
        functionText.setLayoutX(10);
        functionText.setLayoutY(15);
        functionText.setPrefSize(1305, 120);
        functionText.setFocusTraversable(false);
        functionText.setFont(Font.loadFont(Constant.getResource("fonts/cambria.ttc"), 70));
        functionText.setBackground(basicBackground);
        functionText.setOnAction(actionEvent -> refreshFunction());
        getChildren().add(functionText);

        Label colorLabel = new Label("RGB :");
        colorLabel.setPrefSize(130, 90);
        colorLabel.setLayoutX(40);
        colorLabel.setLayoutY(160);
        colorLabel.setFont(new Font(40));
        getChildren().add(colorLabel);

        colorR.setPrefSize(120, 90);
        colorR.setLayoutX(170);
        colorR.setLayoutY(160);
        colorR.setFont(new Font(40));
        colorR.setFocusTraversable(false);
        colorR.setOnAction(e -> refreshFunction());
        getChildren().add(colorR);

        colorG.setPrefSize(120, 90);
        colorG.setLayoutX(320);
        colorG.setLayoutY(160);
        colorG.setFont(new Font(40));
        colorG.setFocusTraversable(false);
        colorG.setOnAction(e -> refreshFunction());
        getChildren().add(colorG);

        colorB.setPrefSize(120, 90);
        colorB.setLayoutX(470);
        colorB.setLayoutY(160);
        colorB.setFont(new Font(40));
        colorB.setFocusTraversable(false);
        colorB.setOnAction(e -> refreshFunction());
        getChildren().add(colorB);

        Label lineWidthLabel = new Label("두께 :");
        lineWidthLabel.setPrefSize(120, 90);
        lineWidthLabel.setLayoutX(630);
        lineWidthLabel.setLayoutY(160);
        lineWidthLabel.setFont(new Font(40));
        getChildren().add(lineWidthLabel);

        lineWidthText.setPrefSize(110, 90);
        lineWidthText.setLayoutX(760);
        lineWidthText.setLayoutY(160);
        lineWidthText.setFont(new Font(40));
        lineWidthText.setFocusTraversable(false);
        lineWidthText.setOnAction(e -> refreshFunction());
        getChildren().add(lineWidthText);

        Label AlphaLabel = new Label("불투명도 :");
        AlphaLabel.setPrefSize(200, 90);
        AlphaLabel.setLayoutX(900);
        AlphaLabel.setLayoutY(160);
        AlphaLabel.setFont(new Font(40));
        getChildren().add(AlphaLabel);

        colorA.setPrefSize(120, 90);
        colorA.setLayoutX(1100);
        colorA.setLayoutY(160);
        colorA.setFont(new Font(40));
        colorA.setFocusTraversable(false);
        colorA.setOnAction(e -> {
            colorA.action();
            refreshFunction();
        });
        getChildren().add(colorA);

        graphPane.add(canvas);
        drawFunction();

        Button deleteButton = new Button("×");
        deleteButton.setPrefSize(60, 60);
        deleteButton.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 50));
        deleteButton.setLayoutX(1335);
        deleteButton.setLayoutY(170);
        deleteButton.setBackground(deleteButtonBackground);
        deleteButton.setOnMouseEntered(mouseEvent -> deleteButton.setTextFill(Color.rgb(100, 100, 100)));
        deleteButton.setOnMouseExited(mouseEvent -> deleteButton.setTextFill(Color.BLACK));
        deleteButton.setOnAction(actionEvent -> {
            graphPane.remove(canvas);
            settingPane.remove(this);
        });
        getChildren().add(deleteButton);

        Button swapButton = new Button("≡");
        swapButton.setPrefSize(115, 60);
        swapButton.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 50));
        swapButton.setLayoutX(1325);
        swapButton.setLayoutY(10);
        swapButton.setBackground(deleteButtonBackground);
        swapButton.setOnMouseEntered(mouseEvent -> swapButton.setTextFill(Color.rgb(100, 100, 100)));
        swapButton.setOnMouseExited(mouseEvent -> swapButton.setTextFill(Color.BLACK));
        swapButton.setOnAction(actionEvent -> {
            int i = settingPane.getFunctionList().indexOf(this);
            if (selectedIndex < 0) {
                selectedIndex = i;
                setBorder(swapBorder);
            } else {
                if (selectedIndex != i) {
                    swapGraph(selectedIndex, i);
                }
                ((FunctionPane) settingPane.getFunctionList().get(i)).setBorder(basicBorder);
                selectedIndex = -1;
            }
        });
        getChildren().add(swapButton);
    }

    public boolean isCurve(String fs) {
        return
                CustomFunction.getPairParenthesis(new StringBuilder(fs), 0) == fs.length() - 1
                && fs.split(",").length == 4;
    }
    public void setCurve(String functionString) throws InvalidFunctionException {
        String curveString = functionString.substring(1, functionString.length() - 1);
        String[] curveParameter = curveString.split(",");
        this.curve = new ParametricCurve(curveParameter);
        CustomFunction start = new CustomFunction(curveParameter[2]);
        CustomFunction end = new CustomFunction(curveParameter[3]);
        if (!(start.isConstant() && end.isConstant())) throw new InvalidFunctionException("start and end must be constant");
        curveStart = start;
        curveEnd = end;
    }

    public void drawFunction() {
        Color color = Color.rgb(
                colorR.getTextInt(),
                colorG.getTextInt(),
                colorB.getTextInt(),
                colorA.getTextDouble()
        );
        gc.setStroke(color);
        gc.setLineWidth(lineWidthText.getTextInt());

        double centerX = graphPane.getCoordinatePaneWidth() / 2;
        if (function != null) {
            for (double x = -centerX; x <= centerX; x++) {
                double x1 = centerX + x, y1 = graphPane.getCoordinatePaneHeight() / 2 - function.get(x);
                if (Double.isFinite(y1)) {
                    gc.lineTo(x1, y1);
                } else {
                    gc.stroke();
                    gc.beginPath();
                }
            }
        } else {
            double dt = (curveEnd.get(0) - curveStart.get(0)) / 1080;
            for (double t = curveStart.get(0); t <= curveEnd.get(0); t += dt) {
                double[] d = curve.get(t);
                gc.lineTo(centerX + d[0], graphPane.getCoordinatePaneHeight() / 2 - d[1]);
            }
        }
        gc.stroke();
        gc.beginPath();
    }
    public void clearFunction() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void refreshFunction() {
        clearFunction();
        if (!functionText.getText().isBlank()) {
            try {
                if (isCurve(functionText.getText())) {
                    setCurve(functionText.getText());
                    function = null;
                }
                else {
                    function = new CustomFunction(functionText.getText());
                    curve = null;
                }
                drawFunction();
                setBackground(basicBackground);
                functionText.setBackground(basicBackground);
            } catch (Exception e) {
                setBackground(errorBackground);
                functionText.setBackground(errorBackground);
            }
        } else {
            setBackground(errorBackground);
            functionText.setBackground(errorBackground);
        }
    }

    public void resize() {
        clearFunction();
        canvas.setWidth(graphPane.getCoordinatePaneWidth());
        canvas.setHeight(graphPane.getCoordinatePaneHeight());
        drawFunction();
    }

    public void swapGraph(int i, int j) {
        List<Node> l = graphPane.getCanvasList(), l2 = settingPane.getFunctionList();
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        l.add(i, l.remove(j));
        l.add(j, l.remove(i + 1));
        l2.add(i, l2.remove(j));
        l2.add(j, l2.remove(i + 1));
    }
}
