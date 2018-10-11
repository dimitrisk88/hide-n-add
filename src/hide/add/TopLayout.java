/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * @author qbsstation7
 */
public class TopLayout extends ViewGroup {

    public TopLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed == false) {
            return;
        }
        int children = getChildCount();
        for (int i = 0; i < children; i++) {
            TextView v = (TextView) getChildAt(i);
            v.setSingleLine();
//            v.setBackgroundColor(Color.BLUE);
            int width=0,height=0;
            if (i == 0) {
                width=r*50/100;
                height=b*30/100;
                v.layout(r * 25 / 100, b * 10 / 100, r * 75 / 100, b * 40 / 100);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, b*25/100);
                v.setTextColor(Color.BLACK);
            } else if (i == 1) {
                width=r*30/100;
                height=b*25/100;
                v.layout(r * 10 / 100, b * 45 / 100, r * 40 / 100, b * 70 / 100);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, b*18/100);
            } else if (i == 2) {
                width=r*30/100;
                height=b*25/100;
                v.layout(r * 60 / 100, b * 60 / 100, r * 90 / 100, b * 85 / 100);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, b*18/100);
            }
            v.setWidth(width);
            v.setHeight(height);
            v.measure(width, height);
            v.setTextScaleX(1);
            String text = v.getText().toString();
            text = text.contains("Time")?"Time: 00:00.0 ":text.contains("Moves")?"Moves: 00 / 00 " : "Target: 00 ";
            v.setTextScaleX((float) v.getWidth() / v.getPaint().measureText(text));
        }
    }
}
