/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * @author the man-ager
 */
public class GameGlobals {

    public static MediaPlayer music;
    public static MediaPlayer click;
    public static int[] choises = new int[5];
    public static int moves, width, height;
    public static TextView movesText;
    public static ImageView leftIndicator, topIndicator, rightIndicator, bottomIndicator;
    public static BottomLayout bottomLayout;
}
