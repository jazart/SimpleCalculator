package com.jazart.simplecalculator;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jazart on 2/1/2018.
 */

public interface MainMVP {

    interface MainViewOps {
        void showOperationResult(String result);

        void showErrorToast(int msgId);

    }


    interface PresenterOps {

        void showUserInputOnDigitClick(Button button, TextView textView);

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
