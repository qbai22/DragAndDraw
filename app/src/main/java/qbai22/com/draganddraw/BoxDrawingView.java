package qbai22.com.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * Created by qbai on 16.11.2016.
 */

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private static final String PARENT_STATE = "Parent State";
    private static final String BOXEN_STATE = "Boxen State";
    //используется при создании View в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }


    //Used when inflating view on the markup of XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i(TAG, "onSaveInstanceState: invoked");
        Parcelable parent = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATE, parent);
        bundle.putSerializable(BOXEN_STATE, (Serializable) mBoxen);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i(TAG, "onRestoreInstanceState: invoooooled");
        Bundle b = (Bundle) state;
        ArrayList<Box> list = (ArrayList<Box>) b.getSerializable(BOXEN_STATE);
        mBoxen = list;
        super.onRestoreInstanceState(b.getParcelable(PARENT_STATE));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }

        Log.e(TAG, "onDraw: invalidated");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                //сброс текущего состояния
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                int index = mBoxen.indexOf(mCurrentBox);
                Log.i(TAG, "current box"+ mBoxen.get(index).getCurrent().x);
                mCurrentBox = null;
                Log.i(TAG, mBoxen.contains(mCurrentBox)+"");
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        return true;
    }

    static class SavedState extends BaseSavedState {

        private ArrayList<Box> mBoxArrayList;

        public SavedState(Parcel source) {
            super(source);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
