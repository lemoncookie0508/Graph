package lepl.graph;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
                            new BorderWidths(4)
                    ));
    private static final Background
            gradationButtonBasic = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonBasic.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            )),
            gradationButtonBasicEntered = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonBasicEntered.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            )),
            gradationButtonActive = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonActive.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            )),
            gradationButtonActiveEntered = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonActiveEntered.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            ));
    private static final Background
            gradationVectorButtonH = new Background(new BackgroundImage(
            new Image(Constant.getImageResource("buttonH.png")),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(45, 45, false, false, false, false)
    )),
            gradationVectorButtonHEntered = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonHEntered.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            )),
            gradationVectorButtonV = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonV.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            )),
            gradationVectorButtonVEntered = new Background(new BackgroundImage(
                    new Image(Constant.getImageResource("buttonVEntered.png")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(45, 45, false, false, false, false)
            ));

    private final TextField functionText;
    private final IntOnlyTextField colorR = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorG = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorB = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorR2 = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorG2 = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorB2 = new IntOnlyTextField(0, 256, "255");
    private final IntOnlyTextField colorA = new IntOnlyTextField(0, 101, "0");

    private final IntOnlyTextField lineWidthText = new IntOnlyTextField(0, 20, "10");

    private static int selectedIndex = -1;
    private boolean isGradationActive = false, isGradationVectorH = true;

    public FunctionPane(String functionString, GraphPane graphPane, SettingPane settingPane) throws  InvalidFunctionException {
        this.graphPane = graphPane;
        this.settingPane = settingPane;

        functionString = functionString.replaceAll("\\s", "");
        if (!isCurve(functionString)) this.function = new CustomFunction(functionString);
        else setCurve(functionString);

        this.canvas = new Canvas(graphPane.getCoordinatePaneWidth(), graphPane.getCoordinatePaneHeight());
        this.gc = canvas.getGraphicsContext2D();

        setPrefSize(1450, 260);
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
        colorLabel.setLayoutX(20);
        colorLabel.setLayoutY(146);
        colorLabel.setFont(new Font(40));
        getChildren().add(colorLabel);

        colorR.setPrefSize(110, 40);
        colorR.setLayoutX(135);
        colorR.setLayoutY(155);
        colorR.setFont(new Font(40));
        colorR.setFocusTraversable(false);
        colorR.setOnAction(e -> refreshFunction());
        getChildren().add(colorR);

        colorG.setPrefSize(110, 40);
        colorG.setLayoutX(250);
        colorG.setLayoutY(155);
        colorG.setFont(new Font(40));
        colorG.setFocusTraversable(false);
        colorG.setOnAction(e -> refreshFunction());
        getChildren().add(colorG);

        colorB.setPrefSize(110, 40);
        colorB.setLayoutX(365);
        colorB.setLayoutY(155);
        colorB.setFont(new Font(40));
        colorB.setFocusTraversable(false);
        colorB.setOnAction(e -> refreshFunction());
        getChildren().add(colorB);

        Button gradationButton = new Button();
        Button gradationVectorButton = new Button();

        gradationButton.setPrefSize(45, 45);
        gradationButton.setLayoutX(480);
        gradationButton.setLayoutY(146);
        gradationButton.setBackground(gradationButtonBasic);
        gradationButton.setOnMouseEntered(mouseEvent -> {
            if (isGradationActive) gradationButton.setBackground(gradationButtonActiveEntered);
            else gradationButton.setBackground(gradationButtonBasicEntered);
        });
        gradationButton.setOnMouseExited(mouseEvent -> {
            if (isGradationActive) gradationButton.setBackground(gradationButtonActive);
            else gradationButton.setBackground(gradationButtonBasic);
        });
        gradationButton.setOnAction(actionEvent -> {
            if (isGradationActive) {
                gradationButton.setBackground(gradationButtonBasicEntered);

                colorR2.setDisable(true);
                colorG2.setDisable(true);
                colorB2.setDisable(true);
                gradationVectorButton.setDisable(true);
            } else {
                gradationButton.setBackground(gradationButtonActiveEntered);

                colorR2.setDisable(false);
                colorG2.setDisable(false);
                colorB2.setDisable(false);
                gradationVectorButton.setDisable(false);
            }
            isGradationActive = !isGradationActive;
            refreshFunction();
        });
        getChildren().add(gradationButton);

        gradationVectorButton.setPrefSize(45, 45);
        gradationVectorButton.setLayoutX(480);
        gradationVectorButton.setLayoutY(195);
        gradationVectorButton.setBackground(gradationVectorButtonH);
        gradationVectorButton.setOnMouseEntered(mouseEvent -> {
            if (isGradationVectorH) gradationVectorButton.setBackground(gradationVectorButtonHEntered);
            else gradationVectorButton.setBackground(gradationVectorButtonVEntered);
        });
        gradationVectorButton.setOnMouseExited(mouseEvent -> {
            if (isGradationVectorH) gradationVectorButton.setBackground(gradationVectorButtonH);
            else gradationVectorButton.setBackground(gradationVectorButtonV);
        });
        gradationVectorButton.setOnAction(actionEvent -> {
            if (isGradationVectorH) {
                gradationVectorButton.setBackground(gradationVectorButtonVEntered);
            } else {
                gradationVectorButton.setBackground(gradationVectorButtonHEntered);
            }
            isGradationVectorH = !isGradationVectorH;
            refreshFunction();
        });
        gradationVectorButton.setDisable(true);
        getChildren().add(gradationVectorButton);

        colorR2.setPrefSize(110, 40);
        colorR2.setLayoutX(530);
        colorR2.setLayoutY(155);
        colorR2.setFont(new Font(40));
        colorR2.setFocusTraversable(false);
        colorR2.setOnAction(e -> refreshFunction());
        colorR2.setDisable(true);
        getChildren().add(colorR2);

        colorG2.setPrefSize(110, 40);
        colorG2.setLayoutX(645);
        colorG2.setLayoutY(155);
        colorG2.setFont(new Font(40));
        colorG2.setFocusTraversable(false);
        colorG2.setOnAction(e -> refreshFunction());
        colorG2.setDisable(true);
        getChildren().add(colorG2);

        colorB2.setPrefSize(110, 40);
        colorB2.setLayoutX(760);
        colorB2.setLayoutY(155);
        colorB2.setFont(new Font(40));
        colorB2.setFocusTraversable(false);
        colorB2.setOnAction(e -> refreshFunction());
        colorB2.setDisable(true);
        getChildren().add(colorB2);

        Label lineWidthLabel = new Label("폭 :");
        lineWidthLabel.setPrefSize(120, 90);
        lineWidthLabel.setLayoutX(895);
        lineWidthLabel.setLayoutY(146);
        lineWidthLabel.setFont(new Font(40));
        getChildren().add(lineWidthLabel);

        lineWidthText.setPrefSize(90, 40);
        lineWidthText.setLayoutX(975);//730
        lineWidthText.setLayoutY(155);
        lineWidthText.setFont(new Font(40));
        lineWidthText.setFocusTraversable(false);
        lineWidthText.setOnAction(e -> refreshFunction());
        getChildren().add(lineWidthText);

        Label AlphaLabel = new Label("투명 :");
        AlphaLabel.setPrefSize(120, 90);
        AlphaLabel.setLayoutX(1090);
        AlphaLabel.setLayoutY(146);
        AlphaLabel.setFont(new Font(40));
        getChildren().add(AlphaLabel);

        colorA.setPrefSize(110, 40);
        colorA.setLayoutX(1210);
        colorA.setLayoutY(158);
        colorA.setFont(new Font(40));
        colorA.setFocusTraversable(false);
        colorA.setOnAction(e -> refreshFunction());
        getChildren().add(colorA);

        graphPane.add(canvas);
        drawFunction();

        Button deleteButton = new Button("×");
        deleteButton.setPrefSize(60, 60);
        deleteButton.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 50));
        deleteButton.setLayoutX(1335);
        deleteButton.setLayoutY(10);
        deleteButton.setBackground(deleteButtonBackground);
        deleteButton.setOnMouseEntered(mouseEvent -> deleteButton.setTextFill(Color.rgb(200, 0, 0)));
        deleteButton.setOnMouseExited(mouseEvent -> deleteButton.setTextFill(Color.BLACK));
        deleteButton.setOnAction(actionEvent -> {
            if (selectedIndex >= 0) {
                ((FunctionPane) settingPane.getFunctionList().get(selectedIndex)).setBorder(basicBorder);
                selectedIndex = -1;
            }
            graphPane.remove(canvas);
            settingPane.remove(this);
        });
        getChildren().add(deleteButton);

        Button swapButton = new Button("≡");
        swapButton.setPrefSize(115, 40);
        swapButton.setFont(Font.loadFont(Constant.getResource("fonts/HanSantteutDotum-Regular.TTF"), 50));
        swapButton.setLayoutX(1330);
        swapButton.setLayoutY(158);
        swapButton.setBackground(deleteButtonBackground);
        swapButton.setOnMouseEntered(mouseEvent -> swapButton.setTextFill(Color.rgb(139, 0, 255)));
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

        if (selectedIndex >= 0) {
            ((FunctionPane) settingPane.getFunctionList().get(selectedIndex)).setBorder(basicBorder);
            selectedIndex = -1;
        }
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
                1 - colorA.getTextInt() / 100D
        );
        double centerX = graphPane.getCoordinatePaneWidth() / 2;

        if (isGradationActive) {
            Color color2 = Color.rgb(
                    colorR2.getTextInt(),
                    colorG2.getTextInt(),
                    colorB2.getTextInt(),
                    1 - colorA.getTextInt() / 100D
            );
            LinearGradient gradient = new LinearGradient(0, 0, isGradationVectorH ? 1 : 0, isGradationVectorH ? 0 : 1, true, CycleMethod.NO_CYCLE,
                    List.of(new Stop(0, color), new Stop(1, color2))
            );
            gc.setStroke(gradient);

        } else gc.setStroke(color);

        gc.setLineWidth(lineWidthText.getTextInt());

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
