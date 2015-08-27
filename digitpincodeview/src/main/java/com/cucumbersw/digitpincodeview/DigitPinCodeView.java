package com.cucumbersw.digitpincodeview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;


/**
 */
public class DigitPinCodeView extends LinearLayout{
    public static final int INVALID_PIN = -1;
    private static final int NUM_OF_PINS = 4;
    private PinCodeTextView[] mPinCodes = new PinCodeTextView[NUM_OF_PINS];
    private int[] mPinValues = new int[4];
    private int mPos = 0; //current input

    private PinCodeCompleteListener mInputListener;

    private boolean mKeyboardDisplaying;

    public DigitPinCodeView(Context context) {
        this(context, null);
    }

    public DigitPinCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitPinCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        for (int i = 0; i < NUM_OF_PINS; i ++) {
            mPinValues[i] = INVALID_PIN;
            mPinCodes[i] = (PinCodeTextView)inflater.inflate(R.layout.digitpin, null, false);
            int l = (int)(mPinCodes[i].getTextSize() * 2);
            this.addView(mPinCodes[i], new ViewGroup.LayoutParams(l, l));
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            mPinCodes[mPos].setBackgroundResource(R.drawable.bg_highlight_with_border_dark);
        } else {
            mPinCodes[mPos].setBackgroundResource(R.drawable.bg_normal_with_border_dark);
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (!mKeyboardDisplaying) {
            ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            mKeyboardDisplaying = true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (mPos > 0 && mPinValues[mPos] == INVALID_PIN) {
                    //current position has no input yet, directly move backward
                    mPos --;
                }
                mPinValues[mPos] = INVALID_PIN;
                //high light current position
                mPinCodes[mPos].setBackgroundResource(R.drawable.bg_highlight_with_border_dark);
                mPinCodes[mPos].setText(""); //Clear the pin code at current position
                if (mPos < NUM_OF_PINS - 1) { // clear the high light for former position
                    mPinCodes[mPos + 1].setBackgroundResource(R.drawable.bg_normal_with_border_dark);
                }
                mPos--; //move the position backward
                if (mPos < 0) { //no backspace when nothing has been input
                    mPos = 0;
                }
            } else {
                int value;
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_0:
                    case KeyEvent.KEYCODE_NUMPAD_0:
                        value = 0;
                        break;
                    case KeyEvent.KEYCODE_1:
                    case KeyEvent.KEYCODE_NUMPAD_1:
                        value = 1;
                        break;
                    case KeyEvent.KEYCODE_2:
                    case KeyEvent.KEYCODE_NUMPAD_2:
                        value = 2;
                        break;
                    case KeyEvent.KEYCODE_3:
                    case KeyEvent.KEYCODE_NUMPAD_3:
                        value = 3;
                        break;
                    case KeyEvent.KEYCODE_4:
                    case KeyEvent.KEYCODE_NUMPAD_4:
                        value = 4;
                        break;
                    case KeyEvent.KEYCODE_5:
                    case KeyEvent.KEYCODE_NUMPAD_5:
                        value = 5;
                        break;
                    case KeyEvent.KEYCODE_6:
                    case KeyEvent.KEYCODE_NUMPAD_6:
                        value = 6;
                        break;
                    case KeyEvent.KEYCODE_7:
                    case KeyEvent.KEYCODE_NUMPAD_7:
                        value = 7;
                        break;
                    case KeyEvent.KEYCODE_8:
                    case KeyEvent.KEYCODE_NUMPAD_8:
                        value = 8;
                        break;
                    case KeyEvent.KEYCODE_9:
                    case KeyEvent.KEYCODE_NUMPAD_9:
                        value = 9;
                        break;
                    default:
                        return false;
                }
                mPinValues[mPos] = value;
                mPinCodes[mPos].setPin(String.format("%d", value));
                if (mPos > 0) {
                    // once user input on current position, hider former one immediately
                    mPinCodes[mPos - 1].hidePinImmediately();
                }
                // clear high light on current position
                mPinCodes[mPos].setBackgroundResource(R.drawable.bg_normal_with_border_dark);
                mPos ++; //move pos forward
                if (mPos > NUM_OF_PINS - 1) { //all pin codes are input
                    mPos = NUM_OF_PINS - 1; //stay at the last position
                    onInputComplete();
                } else { //high light the next position
                    mPinCodes[mPos].setBackgroundResource(R.drawable.bg_highlight_with_border_dark);
                }
                return true;
            }
        }
        return false;
    }


    private void onInputComplete() {
        if (mInputListener != null) {
            mInputListener.onInputComplete(mPinValues, NUM_OF_PINS);
        }
    }

    public void setPinCodeCompleteListener(PinCodeCompleteListener l) {
        mInputListener = l;
    }

    public interface PinCodeCompleteListener {
        void onInputComplete(int[] pinCodes, int length);
    }

    public int[] getPinCodes() {
        return mPinValues;
    }
}
