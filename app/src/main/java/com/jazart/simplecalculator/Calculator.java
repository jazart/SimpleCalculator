package com.jazart.simplecalculator;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by jazart on 2/5/2018.
 */


/*
Calculator takes values passed from the presenter class. Numbers go into the queue and operators go into the stack.
To complete an operation the two values will be dequeued and the last operation will be popped from the stack to perform the op.
Then the result value will be formatted and added to the queue. Then that value will be returned to the presenter class.
 */
public class Calculator implements MainMVP.Calc {
    private Queue<Double> mEnteredValues;
    private Stack<String> mOperations;

    //mState tells the presenter class if it's in the operation state(true) or input state(false)
    private boolean mState= false, validDecimal = true;
    static final int sMAX_DIGITS = 14;

    Calculator() {
        mEnteredValues = new LinkedList<>();
        mOperations = new Stack<>();
    }



    private double add(double a, double b) {
        return a + b;
    }


    private double subtract(double a, double b) {
        double diff = a - b;
        return diff;
    }


    private double divide(double a, double b) {
        if(b == 0) throw new ArithmeticException();
        double quotient = a / b;
        return quotient;
    }


    private double multiply(double a, double b) {
        double product = a * b;
        return product;
    }

    // TODO: 2/17/2018 implement percent operation
    private double percent(double a, double b) {
        return a + (a * b/100);
    }


    @Override
    public void addValToQueue(double a) {
        mEnteredValues.add(a);
    }

    @Override
    public void addOp(String op) {
        mOperations.add(op);
    }

    @Override
    public String peekOp() {
        return mOperations.peek();
    }

    @Override
    public boolean getState() {
        return mState;
    }

    @Override
    public void updateState(boolean newState) {
        mState= newState;
    }

    @Override
    public boolean isValidDecimal() {
        return validDecimal;
    }

    @Override
    public void updateDecimal(boolean isValid) {
        validDecimal = isValid;
    }

    @Override
    public double performOperation() {
            double result = 0;
            double firstVal = mEnteredValues.poll();
            double secondVal;
            try {
                secondVal = mEnteredValues.remove();
            } catch(NoSuchElementException e) {
                secondVal = Double.MAX_VALUE;
            }
            String op = mOperations.pop();
            switch(op) {
                case "/":
                    try {
                        result = divide(firstVal, secondVal);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    resetContainers();
                    result = formatValue(result);
                    mEnteredValues.add(result);
                    break;
                case "x":
                    result = (secondVal == Double.MAX_VALUE) ? firstVal : multiply(firstVal, secondVal);
                    Log.d("Result op:", String.valueOf(firstVal) + " " + String.valueOf(secondVal));
                    resetContainers();
                    result = formatValue(result);
                    mEnteredValues.add(result);
                    break;
                case "\u2212":
                    Log.d("SUB Result", String.valueOf(firstVal +" "+ secondVal));

                    result = formatValue(subtract(firstVal, secondVal));
                    resetContainers();
                    mEnteredValues.add(result);
                    break;
                case "+":
                    Log.d("ADD Result", String.valueOf(firstVal +" "+ secondVal));

                    result = (secondVal == Double.MAX_VALUE) ? firstVal : add(firstVal, secondVal);
                    resetContainers();
                    result = formatValue(result);
                    mEnteredValues.add(result);
                    break;
                case "%":
                    result = (secondVal == Double.MAX_VALUE) ? firstVal / 100 : percent(firstVal, secondVal);
                    resetContainers();
                    result = formatValue(result);
                    mEnteredValues.add(result);
                    break;

            }
            return result;

    }

    @Override
    public void resetContainers() {
        mOperations.clear();
        mEnteredValues.clear();
    }

    private double formatValue(double val) {
        DecimalFormat df = new DecimalFormat("0.000");
        return Double.parseDouble(df.format(val));
    }
}
