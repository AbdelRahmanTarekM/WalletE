package com.abdelrahman.wallete.Widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abdelrahman.wallete.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AbdelRahman on 9/10/2016.
 */
public class ExpenseDatePicker extends LinearLayout implements View.OnTouchListener {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    public static final int DELAY = 250;

    private Calendar mCalendar;
    private TextView mDate;
    private TextView mMonth;
    private TextView mYear;
    private SimpleDateFormat format;

    private boolean mIcrement;
    private boolean mDecrement;

    private int MESSAGE_WHAT = 123;
    private int mActiveID;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mIcrement) {
                increment(mActiveID);
            }
            if (mDecrement) {
                decrement(mActiveID);
            }
            if (mIcrement || mDecrement) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            }
            return true;
        }
    });

    public ExpenseDatePicker(Context context) {
        super(context);
        init(context);
    }

    public ExpenseDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpenseDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_picker_view, this);
        mCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDate = (TextView) this.findViewById(R.id.tv_day);
        mMonth = (TextView) this.findViewById(R.id.tv_month);
        mYear = (TextView) this.findViewById(R.id.tv_year);

        mDate.setOnTouchListener(this);
        mMonth.setOnTouchListener(this);
        mYear.setOnTouchListener(this);

        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);
    }

    public void update(int date, int month, int year, int hour, int minute, int second) {
        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mDate.setText(date + "");
        mMonth.setText(format.format(mCalendar.getTime()));
        mYear.setText(year + "");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_day:
                processEventsFor(mDate, event);
                break;
            case R.id.tv_month:
                processEventsFor(mMonth, event);
                break;
            case R.id.tv_year:
                processEventsFor(mYear, event);
                break;
        }
        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event) {
        Drawable[] drawables = textView.getCompoundDrawables();
        if (hasDrawableTop(drawables) && hasDrawableBottom(drawables)) {
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            mActiveID = textView.getId();
            if (topDrawableHIT(textView, topBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mIcrement = true;
                    increment(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView, true);
                }
                if (isActionUpOrCancel(event)) {
                    mIcrement = false;
                    toggleDrawable(textView, false);
                }
            } else if (bottomDrawableHIT(textView, bottomBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mDecrement = true;
                    decrement(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView, true);
                }
                if (isActionUpOrCancel(event)) {
                    mDecrement = false;
                    toggleDrawable(textView, false);
                }
            } else {
                mIcrement = false;
                mDecrement = false;
                toggleDrawable(textView, false);
            }
        }
    }

    private void set(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        mDate.setText(date + "");
        mYear.setText(year + "");
        mMonth.setText(format.format(mCalendar.getTime()));
    }

    private void increment(int id) {
        switch (id) {
            case R.id.tv_day:
                mCalendar.add(Calendar.DATE, 1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }
        set(mCalendar);
    }

    private void decrement(int id) {
        switch (id) {
            case R.id.tv_day:
                mCalendar.add(Calendar.DATE, -1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }
        set(mCalendar);
    }

    private boolean hasDrawableTop(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    private boolean hasDrawableBottom(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }

    private boolean topDrawableHIT(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;
        return x < xmax && x > xmin && y > ymin && y < ymax;
    }

    private boolean bottomDrawableHIT(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getHeight() - textView.getPaddingBottom() - drawableHeight;
        int ymax = textView.getHeight() - textView.getPaddingBottom();
        return x < xmax && x > xmin && y > ymin && y < ymax;
    }

    private boolean isActionDown(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    private boolean isActionUpOrCancel(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    private void toggleDrawable(TextView textView, boolean pressed) {
        if (pressed) {
            if (mIcrement) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_pressed, 0, R.drawable.down_normal);
            }
            if (mDecrement) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_pressed);
            }
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_normal);
        }
    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }
}
