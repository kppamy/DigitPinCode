package com.cucumbersw.digitpincodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 */
public class PinCodeTextView extends TextView {
    private static final long PIN_TXT_AUTO_HIDE_DELAY = 300;
    private boolean mHideText;

    public PinCodeTextView(Context context) {
        super(context);
    }

    public PinCodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinCodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPin(String pin) {
        if (pin.length() > 1) {
            throw new RuntimeException("Pin code can only be 1 char long");
        }
        mHideText = false;
        setText(pin);
        postDelayed(hideTextRunnable, PIN_TXT_AUTO_HIDE_DELAY);
    }

    // Trick, use this function to clear the circle
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.equals("")) {
            mHideText = false;
        }
    }

    public void hidePinImmediately() {
        removeCallbacks(hideTextRunnable);
        mHideText = true;
        invalidate();
    }

    private Runnable hideTextRunnable = new Runnable() {
        @Override
        public void run() {
            mHideText = true;
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if (mHideText) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getCurrentTextColor());
            int w = getWidth();
            int h = getHeight();
            int r = (w > h ? h : w) / 5;
            canvas.drawCircle(w/2, h/2, r, paint);
            return;
        }
        super.onDraw(canvas);
    }
}
