package com.jazart.simplecalculator;

import android.util.Log;

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

    private void onDecimal(String buttonText) {
        if(mCalc.isValidDecimal()) {
            mCalc.updateDecimal(false);
            mView.appendVal(buttonText);
        }
    }

    private void onClearOrDel(int buttonID) {
        if (mView.getLen() > 0 && buttonID == R.id.del_button) {
            String newVal = mView.getText().substring(0, mView.getText().length() - 1);
            mView.showOperationResult(newVal);
        } else {
            clearAndReset();
        }
    }

    //on equal checks to see if the calculator is in the listening state or calculating state
    //Listening state means it is waiting for an operator and another value.
    //calculating state means the calculator only needs another number then it will perform an operation.
    private void onEqual() {
        mCalc.updateState(false);
        String viewText = mView.getText();
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
    private void onOperator(String buttonText) {
        String empty = "";
        if(mView.getLen() < 1) {
            mView.showErrorToast(R.string.std_err);
            return;
        }

        try {
            if (!mCalc.getState()) {
                mCalc.addOp(buttonText);
                double valOne = Double.parseDouble(mView.getText());

                mCalc.updateState(true);
                mCalc.addValToQueue(valOne);
                mView.appendVal(buttonText);
                mCalc.updateDecimal(true);
            }
        } catch (NumberFormatException e) {
            return;
        }
        if(mCalc.getState()){
            double valTwo = 0;
            try {
                valTwo = parseTextView(mView.getText(), mCalc.peekOp());
            } catch(NumberFormatException e) {
                e.printStackTrace();
                return;
            } catch(ArithmeticException e) {
                e.printStackTrace();
                mView.showErrorToast(R.string.std_err);
            }
            mCalc.addValToQueue(valTwo);
            double result = mCalc.performOperation();
            mCalc.addOp(buttonText);

            mView.showOperationResult(String.valueOf(result) + "" + buttonText);
        }
    }

    private boolean checkDisplayLen(int textViewLen) {
        return textViewLen > Calculator.sMAX_DIGITS;
    }

    // TODO: 2/17/2018 allow second value to be negative
    private void onPlusMinus() {
        if(!mCalc.getState() && !mView.getText().contains("-")) {
            String currentText = mView.getText();
            mView.showOperationResult("-" + currentText);
        } else {
            String currentText = mView.getText().replace("-", "");
            mView.showOperationResult(currentText);
        }
    }

    @Override
    public void showUserInputOnDigitClick(String buttonText, int buttonId) {

        if(checkDisplayLen(mView.getLen())) {
            mView.showErrorToast(R.string.size_err);
            clearAndReset();
            return;
        }

        if (isNumeric(buttonText)) {
            mView.appendVal(buttonText);
        } else if(buttonId == R.id.plus_minus_button) {
            onPlusMinus();
        } else if(buttonId == R.id.deci_button) {
            onDecimal(buttonText);
        } else if(buttonId == R.id.del_button || buttonId == R.id.clear_all_button) {
            onClearOrDel(buttonId);
        } else if(buttonId == R.id.equal_button) {
           onEqual();
        } else {
            onOperator(buttonText);
        }
    }

    //checks to see if a given is numeric or not.
    private boolean isNumeric(String buttonText) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        formatter.parse(buttonText, position);
        return buttonText.length() == position.getIndex();
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
