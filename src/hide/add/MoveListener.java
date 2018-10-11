/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author the man-ager
 */
public class MoveListener implements OnTouchListener {

    GameActivity main;
    static int touchX, touchY;

    public MoveListener(GameActivity main) {
        this.main = main;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int sw = event.getAction();
        if (sw == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getX();
            touchY = (int) event.getY();
            if (main.touching < 1) {
                main.touching = 1;
            }
        } else if (main.touching < 2 && sw == MotionEvent.ACTION_MOVE) {
            main.targetX = (int) event.getX() - touchX;
            main.targetY = (int) event.getY() - touchY;
            if (Math.abs(main.targetX) > Math.abs(main.targetY)) {
                main.targetY = 0;
                main.targetX = main.targetX > main.cellSize ? main.cellSize : main.targetX < -main.cellSize ? -main.cellSize : main.targetX;
            } else {
                main.targetX = 0;
                main.targetY = main.targetY > main.cellSize ? main.cellSize : main.targetY < -main.cellSize ? -main.cellSize : main.targetY;
            }
            boolean[] checks = {main.targetX < -main.cellSize / 2, main.targetY < -main.cellSize / 2, main.targetX > main.cellSize / 2, main.targetY > main.cellSize / 2};
            GameGlobals.bottomLayout.setAlphas(checks);
            main.touching = 1;
        } else if (main.touching == 1 && sw == MotionEvent.ACTION_UP) {
            main.targetX = main.targetX > main.cellSize / 2 ? main.cellSize : main.targetX < -main.cellSize / 2 ? -main.cellSize : 0;
            main.targetY = main.targetY > main.cellSize / 2 ? main.cellSize : main.targetY < -main.cellSize / 2 ? -main.cellSize : 0;
            main.touching = -1;
        }
        return true;
    }
}
