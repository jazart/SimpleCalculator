package com.jazart.simplecalculator;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.EmptyStackException;


/**
 * Created by jazart on 2/2/2018.
 */
/*
The job of main presenter is to interpret the input from the view and pass it to the calculator
the calculator will perform the given operation then return the result back to the presenter
then the presenter will then update the view by passing the result and displaying it.

 */
public class MainPresenter implements MainMVP.PresenterOps {
    private MainMVP.MainViewOps mView;
    private MainMVP.Calc mCalc;

    public MainPresenter(MainMVP.MainViewOps mView, MainMVP.Calc calc) {
        this.mView = mView;
        this.mCalc = calc;

    }

    private void onDecimal(TextView textView, Button button) {
        if(mCalc.isValidDecimal()) {
            mCalc.updateDecimal(false);
            textView.append(button.getText().toString());
        }
    }

    private void onClearOrDel(TextView textView, Button button) {
        if (textView.length() > 0 && button.getId() == R.id.del_button) {
            String newVal = textView.getText().toString().substring(0, textView.getText().length() - 1);
            textView.setText(newVal);
        } else {
            clearAndReset();
        }
    }

    //on equal checks to see if the calculator is in the listening state or calculating state
    //Listening state means it is waiting for an operator and another value.
    //calculating state means the calculator only needs another number then it will perform an operation.
    private void onEqual(TextView textView) {
        mCalc.updateState(false);
        String viewText = textView.getText().toString();
        double newNum;
        try {
            newNum = parseTextView(viewText, mCalc.peekOp());
        } catch (EmptyStackException e) {
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mCalc.updateState(true);
            return;
        }
        mCalc.addValToQueue(newNum);
        try {
            double result = mCalc.performOperation();
            mCalc.resetContainers();
            mView.showOperationResult(String.valueOf(result));
        }  catch (ArithmeticException e) {
            e.printStackTrace();
            mView.showErrorToast(R.string.std_err);
            clearAndReset();
        }
    }

    //works similarly to onEqual except with more error checks for invalid inputs and operations
    //Also checks the state, if it's in the listening state then the operator will be displayed on the screen
    //on the calculating state the calculator will perform the pending operation then display the operator
    private void onOperator(TextView textView, Button button) {
        if(textView.length() < 1) {
            mView.showErrorToast(R.string.std_err);
            return;
        }

        try {
            if (!mCalc.getState()) {
                mCalc.addOp(button.getText().toString());
                double valOne = Double.parseDouble(textView.getText().toString());

                mCalc.updateState(true);
                mCalc.addValToQueue(valOne);
                textView.append(button.getText().toString());
                mCalc.updateDecimal(true);
            }
        } catch (NumberFormatException e) {
            return;
        }
        if(mCalc.getState()){
            double valTwo = 0;
            try {
                valTwo = parseTextView(textView.getText().toString(), mCalc.peekOp());
            } catch(NumberFormatException e) {
                e.printStackTrace();
                return;
            } catch(ArithmeticException e) {
                e.printStackTrace();
                mView.showErrorToast(R.string.std_err);
            }
            mCalc.addValToQueue(valTwo);
            double result = mCalc.performOperation();
            mCalc.addOp(button.getText().toString());

            mView.showOperationResult(String.valueOf(result) + "" + button.getText().toString());
        }
    }

    private boolean checkDisplayLen(TextView textView) {
        return textView.length() > Calculator.sMAX_DIGITS;
    }

    // TODO: 2/17/2018 allow second value to be negative
    private void onPlusMinus(TextView textView) {
        if(!mCalc.getState() && !textView.getText().toString().contains("-")) {
            CharSequence currentText = textView.getText();
            textView.setText("-" + currentText);
        } else {
            CharSequence currentText = textView.getText().toString().replace("-", "");
            textView.setText(currentText);
        }
    }

    @Override
    public void showUserInputOnDigitClick(Button button, TextView textView) {
        if(checkDisplayLen(textView)) {
            mView.showErrorToast(R.string.size_err);
            clearAndReset();
            return;
        }

        if (isNumeric(button)) {
            textView.append(button.getText());
        } else if(button.getId() == R.id.plus_minus_button) {
            onPlusMinus(textView);
        } else if(button.getId() == R.id.deci_button) {
            onDecimal(textView, button);
        } else if(button.getId() == R.id.del_button || button.getId() == R.id.clear_all_button) {
            onClearOrDel(textView, button);
        } else if(button.getId() == R.id.equal_button) {
           onEqual(textView);
        } else {
            onOperator(textView, button);
        }
    }

    //checks to see if a given is numeric or not.
    private boolean isNumeric(Button button) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        formatter.parse((String)button.getText(),position);
        return button.getText().length() == position.getIndex();
    }

    private double parseTextView(String text, String opSymbol) {
        int starLocation = text.indexOf(opSymbol) + 1;
        Log.d("PARSE TEXT", opSymbol);
        Log.d("PARSE TEXT", text);
        double parsedVal = Double.parseDouble(text.substring(starLocation, text.length()));
        return parsedVal;
    }


    private void clearAndReset() {
        mCalc.updateState(false);
        mCalc.resetContainers();
        mView.showOperationResult("");
        mCalc.updateDecimal(true);
    }

}
