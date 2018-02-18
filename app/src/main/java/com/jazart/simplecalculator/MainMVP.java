package com.jazart.simplecalculator;

/**
 * Created by jazart on 2/1/2018.
 */

public interface MainMVP {

    interface MainViewOps {
        void showOperationResult(String result);

        void showErrorToast(int msgId);
        void appendVal(String val);
        String getText();
        int getLen();
    }


    interface PresenterOps {

        void showUserInputOnDigitClick(String buttonText, int buttonId);

    }

    interface Calc {
        void addValToQueue(double a);
        void addOp(String opID);
        double performOperation();
        String peekOp();
        boolean getState();
        void updateState(boolean newState);
        void resetContainers();
        boolean isValidDecimal();
        void updateDecimal(boolean validDecimal);
    }



}
