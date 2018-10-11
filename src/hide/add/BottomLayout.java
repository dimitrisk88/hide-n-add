/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * @author the man-ager
 */
public class BottomLayout extends ViewGroup {
    
    public BottomLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed==false) {
            return;
        }
        int children = getChildCount();
        for (int i = 0; i < children; i++) {
            View v = getChildAt(i);
            if (i < 4) {
                ((ImageView) v).setScaleType(ImageView.ScaleType.FIT_XY);
                ((ImageView) v).setAlpha(0);
            }
            if (i == 0) {
                v.layout(0, 0, r * 2 / 9, b);
            } else if (i == 1) {
                v.layout(r / 9 + 1, 0, r * 8 / 9, b / 3);
            } else if (i == 2) {
                v.layout(r * 7 / 9 + 1, 0, r, b);
            } else if (i == 3) {
                v.layout(r / 9 + 1, b * 2 / 3 + 1, r * 8 / 9, b);
            } else if (i == 4) {
                ((TextView) v).setWidth(r * 5 / 9);
                ((TextView) v).setHeight(b / 3);
                ((TextView) v).measure(r * 5 / 9, b / 3);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, b*28/100);
                v.layout(r * 2 / 9 + 1, b / 3 + 1, r * 7 / 9, b * 2 / 3);
                ((TextView) v).setTextScaleX(1);
                ((TextView) v).setTextScaleX((v.getWidth()) / ((TextView) v).getPaint().measureText("OPTIONS "));
            }
        }
    }
    
    public void setAlphas(boolean[] checks) {
        for (int i=0;i<4;i++) {
            ((ImageView)getChildAt(i)).setAlpha(checks[i]?100:0);
        }
    }

}
