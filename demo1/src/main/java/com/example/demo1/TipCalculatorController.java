package com.example.demo1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class TipCalculatorController {
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

    @FXML
    private TextField amountTextField;

    @FXML
    private Label tipPercentageLabel;

    @FXML
    private Slider tipPercentageSlider;

    @FXML
    private TextField tipTextField;

    @FXML
    private TextField totalTextField;

    private ObjectProperty<BigDecimal> tipPercentage = new SimpleObjectProperty<>(BigDecimal.valueOf(0.15));

    @FXML
    public void initialize() {
        tipPercentageLabel.textProperty().bind(
                Bindings.createStringBinding(() -> PERCENT_FORMAT.format(tipPercentage.get()), tipPercentage));

        tipPercentageSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            tipPercentage.set(BigDecimal.valueOf(newValue.intValue() / 100.0));
        });

        amountTextField.textProperty().addListener((observable, oldValue, newValue) -> calculateTipAndTotal());
        tipPercentage.addListener((observable, oldValue, newValue) -> calculateTipAndTotal());
    }

    private void calculateTipAndTotal() {
        try {
            BigDecimal amount = new BigDecimal(amountTextField.getText());
            BigDecimal tip = amount.multiply(tipPercentage.get());
            BigDecimal total = amount.add(tip);

            tipTextField.setText(CURRENCY_FORMAT.format(tip));
            totalTextField.setText(CURRENCY_FORMAT.format(total));
        } catch (NumberFormatException ex) {
            amountTextField.setText(" ");
            amountTextField.selectAll();
            amountTextField.requestFocus();
        }
    }
}
