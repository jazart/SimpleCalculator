package com.jazart.simplecalculator;


import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by jazart on 2/10/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculatorTest {

    private Calculator calc;
    private MainMVP.MainViewOps mockView;
    private MainMVP.PresenterOps mockPresenter;

    @Mock
    Log log;


    @Before
    public void start() {
        MockitoAnnotations.initMocks(this);
        mockPresenter = mock(MainMVP.PresenterOps.class);
        mockView = mock(MainMVP.MainViewOps.class);
        calc = new Calculator();
        calc.addValToQueue(4);
        calc.addOp("+");

    }

    @Test
    public void returnValueOnCalculation() {
        calc.performOperation();
    }

}
