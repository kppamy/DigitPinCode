package com.cucumbersw.digitpincode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cucumbersw.digitpincodeview.DigitPinCodeView;

public class MainActivity extends AppCompatActivity implements DigitPinCodeView.PinCodeCompleteListener, View.OnClickListener {
    private DigitPinCodeView mPinCodeView;
    private Button mBtnConfirm;
    private Button mBtnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPinCodeView = (DigitPinCodeView) findViewById(R.id.pincodes);
        mPinCodeView.setPinCodeCompleteListener(this);
        mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
        mBtnReset = (Button)findViewById(R.id.btn_reset);
        mBtnConfirm.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPinCodeView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInputComplete(int[] pinCodes, int length) {
        mPinCodeView.clearFocus();
        mBtnConfirm.requestFocus();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            int[] pinCodes = mPinCodeView.getPinCodes();
            int length = pinCodes.length;
            String PIN = "";
            for (int i = 0; i < length; i ++) {
                if (pinCodes[i] < 0 || pinCodes[i] > 9) {
                    Toast.makeText(this, String.format("Invalid input, PIN[%d] code is %d", i, pinCodes[i]), Toast.LENGTH_SHORT).show();
                    return;
                }
                PIN += String.valueOf(pinCodes[i]);
            }
            Toast.makeText(this, String.format("PIN code is %s", PIN), Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_reset) {
            mPinCodeView.reset();
        }
    }
}
