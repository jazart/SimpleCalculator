package com.jazart.simplecalculator;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/*
Main Activity that implements my MVP.main Interface. I used a Model View Presenter architecture
to sepearate my button listening, ui updates, and business logic into 3 classes all that implement
different interfaces which hold basic public methods for them.
 */

//Main activity implements View.OnClickListener so I can create a single onclick method instead of implementing it for each button individually.
public class MainActivity extends AppCompatActivity implements MainMVP.MainViewOps, View.OnClickListener {
    private Button mOneButton;
    private Button mTwoButton;
    private Button mThreeButton;
    private Button mFourButton;
    private Button mFiveButton;
    private Button mSixButton;
    private Button mSevenButton;
    private Button mEightButton;
    private Button mNineButton;
    private Button mAddButton;
    private Button mMultButton;
    private Button mSubButton;
    private Button mDivButton;
    private Button mDelButton;
    private Button mZeroButton;
    private Button mEqualButton;
    private Button mDeciButton;
    private Button mPercentButton;
    private Button mClearButton;
    private Button mPlusMinusButton;
    private TextView mResultView;
    private MainMVP.PresenterOps mMainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOneButton = findViewById(R.id.one_button);
        mOneButton.setOnClickListener(this);

        mTwoButton = findViewById(R.id.two_button);
        mTwoButton.setOnClickListener(this);

        mThreeButton = findViewById(R.id.three_button);
        mThreeButton.setOnClickListener(this);

        mFourButton = findViewById(R.id.four_button);
        mFourButton.setOnClickListener(this);

        mFiveButton = findViewById(R.id.five_button);
        mFiveButton.setOnClickListener(this);

        mSixButton = findViewById(R.id.six_button);
        mSixButton.setOnClickListener(this);

        mSevenButton = findViewById(R.id.seven_button);
        mSevenButton.setOnClickListener(this);

        mEightButton = findViewById(R.id.eight_button);
        mEightButton.setOnClickListener(this);

        mNineButton = findViewById(R.id.nine_button);
        mNineButton.setOnClickListener(this);

        mZeroButton = findViewById(R.id.zero_button);
        mZeroButton.setOnClickListener(this);

        mEqualButton = findViewById(R.id.equal_button);
        mEqualButton.setOnClickListener(this);

        mDivButton = findViewById(R.id.div_button);
        mDivButton.setOnClickListener(this);

        mMultButton = findViewById(R.id.mult_button);
        mMultButton.setOnClickListener(this);

        mSubButton = findViewById(R.id.sub_button);
        mSubButton.setOnClickListener(this);

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(this);

        mDelButton = findViewById(R.id.del_button);
        mDelButton.setOnClickListener(this);

        mDeciButton = findViewById(R.id.deci_button);
        mDeciButton.setOnClickListener(this);

        mPercentButton = findViewById(R.id.percent_button);
        mPercentButton.setOnClickListener(this);

        mClearButton = findViewById(R.id.clear_all_button);
        mClearButton.setOnClickListener(this);

        mPlusMinusButton = findViewById(R.id.plus_minus_button);
        mPlusMinusButton.setOnClickListener(this);

        mResultView = findViewById(R.id.result_view);
        if(savedInstanceState != null) {
            mResultView.setText((String)savedInstanceState.get("EXTRA_TEXT"));
        }

        try {
            init(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    //initialize the presenter by creating a new instance of presenter and passing this activity as the view for that presenter.
    private void init(MainMVP.MainViewOps view) throws InstantiationException, IllegalAccessException {
        mMainPresenter = new MainPresenter(view, new Calculator());
    }

    @Override
    public void showOperationResult(String result) {
        mResultView.setText(result);
    }

    @Override
    public void showErrorToast(int msgID) {
        Toast.makeText(this, msgID, Toast.LENGTH_SHORT)
                .show();
    }


    /*All buttons use this onClick method. The main activity(view) only listens for input. Once a button is pressed
    the presenter will be called to handle further details
    */
    @Override
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        AlphaAnimation buttonAnim = new AlphaAnimation(0.3F, 0.7F);
        buttonAnim.setDuration(25);
        v = (Button) v;
        v.startAnimation(buttonAnim);
        vibe.vibrate(10);
        mMainPresenter.showUserInputOnDigitClick(((Button) v).getText().toString(), v.getId());
    }



    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("EXTRA_TEXT", mResultView.getText().toString());
    }

    @Override
    public void appendVal(String val) {
        mResultView.append(val);
    }

    @Override
    public String getText() {
        return mResultView.getText().toString();
    }

    @Override
    public int getLen() {
        return mResultView.length();
    }
}
