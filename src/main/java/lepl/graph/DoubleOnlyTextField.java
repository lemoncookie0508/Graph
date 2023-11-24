package lepl.graph;

import javafx.scene.control.TextField;

public class DoubleOnlyTextField extends TextField {
    private String textSave;
    private final double min, max;

    public DoubleOnlyTextField(double min, double max, String s) {
        super(s);
        textSave = s;
        this.max = max;
        this.min = min;
        setOnAction(e -> action());
    }
    public void action() {
        if (!getText().equals(textSave)) {
            try {
                double val = Double.parseDouble(getText());
                if (val <= max) {
                    if (val >= min) {
                        textSave = String.valueOf(val);
                    } else {
                        textSave = String.valueOf(min);
                        setText(String.valueOf(min));
                    }
                } else {
                    textSave = String.valueOf(max);
                    setText(String.valueOf(max));
                }
            } catch (Exception exception) {
                setText(textSave);
            }
        }
    }

    public double getTextDouble() {
        return Double.parseDouble(getText());
    }
}
