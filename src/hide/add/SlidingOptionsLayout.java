/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * @author the man-ager
 */
public class SlidingOptionsLayout extends ViewGroup {

    private static int width, height;
    public int index, children;

    public SlidingOptionsLayout(final Context context, final int width, int height, final int index, final ValueChangeListener method) {
        super(context);
        SlidingOptionsLayout.width = width;
        SlidingOptionsLayout.height = height;
        this.index = index;
        this.setOnTouchListener(new View.OnTouchListener() {
            int x;

            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction(), hor = (int) event.getX() - x;
                if (action == MotionEvent.ACTION_DOWN) {
                    v.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
                    x = (int) event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    slideOptions(hor);
                } else if (action == MotionEvent.ACTION_UP) {
                    x = hor;
                    if (hor > width / 3 && GameGlobals.choises[index] > 0) {
                        if (GameGlobals.choises[index] == 1) {
                            GameGlobals.choises[index] = children - 2;
                        } else {
                            GameGlobals.choises[index]--;
                        }
                        x = hor > width ? 0 : hor - width;
                    } else if (hor < -width / 3 && GameGlobals.choises[index] < children - 1) {
                        if (GameGlobals.choises[index] == children - 2) {
                            GameGlobals.choises[index] = 1;
                        } else {
                            GameGlobals.choises[index]++;
                        }
                        x = hor < -width ? 0 : hor + width;
                    }
                    smoothingSlide(x);
                    if (method != null) {
                        method.onValueChange();
                    }
                }
                return true;
            }
        });
    }

    private void smoothingSlide(final int x) {
        final int N = 10;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int y = x > width / N ? x - width / N : x < -width / N ? x + width / N : 0;
                slideOptions(y);
                if (y != 0) {
                    smoothingSlide(y);
                }
            }
        }, 50);
    }

    private void slideOptions(int x) {
        x = x > width ? width : x < -width ? -width : x;
        for (int i = 0; i < children; i++) {
            View v = getChildAt(i);
            v.layout(width * (i - GameGlobals.choises[index]) + x, 0, width * (i - GameGlobals.choises[index] + 1) + x, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        children = getChildCount();
        slideOptions(0);
    }

}
