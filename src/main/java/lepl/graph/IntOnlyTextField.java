package lepl.graph;


import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import lepl.Constant;

public class IntOnlyTextField extends TextField {
    String textSave;

    public IntOnlyTextField(int min, int max, String s) {
        super(s);
        textSave = s;
        textProperty().addListener(e -> {
            try {
                int value = Integer.parseInt(getText());
                if (value < max) {
                    if (value >= min) {
                        if (value != 0 && getText().charAt(0) == '0') {
                            setText(String.valueOf(value));
                        }
                    } else {
                        setText(String.valueOf(min));
                    }
                } else {
                    setText(String.valueOf(max - 1));
                }
                textSave = getText();
            } catch (NumberFormatException n) {
                if (getText().isBlank()) {
                    setText("0");
                    textSave = "0";
                } else {
                    setText(textSave);
                }
            }
        });
    }

    public int getTextInt() {
        return Integer.parseInt(getText());
    }
}