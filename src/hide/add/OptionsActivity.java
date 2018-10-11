/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * @author the man-ager
 */
public class OptionsActivity extends Activity {

    int width, height;
    private TextView newGame, restartGame, back, cancel, exit;
    private TextView[] music = new TextView[4], sounds = new TextView[4], androidBar = new TextView[4], difficulty = new TextView[7], theme;
    private SlidingOptionsLayout musicContainer, soundsContainer, androidBarContainer, difficultyContainer, themeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        width = GameGlobals.width * 2 / 3;
        height = GameGlobals.height / 12;
//        GameGlobals.music.setVolume(.4f, .4f);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        findViewById(R.id.top_layout).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0, 1));
        findViewById(R.id.middle_layout).setLayoutParams(new LinearLayout.LayoutParams(width, height * 8, 0));
        ((LinearLayout.LayoutParams) findViewById(R.id.middle_layout).getLayoutParams()).gravity = Gravity.CENTER;
        LinearLayout options = (LinearLayout) findViewById(R.id.middle_layout);
        newGame = createOption(options, "New Game");
        newGame.setOnTouchListener(new Confirm(this, "new"));
        restartGame = createOption(options, "Restart Game");
        restartGame.setOnTouchListener(new Confirm(this, "restart"));
        musicContainer = new SlidingOptionsLayout(this, width, height, 0, new ValueChangeListener() {
            public void onValueChange() {
                if (GameGlobals.choises[0] == 1) {
//                    GameGlobals.music.start();
                } else {
//                    GameGlobals.music.pause();
                }
            }
        });
        musicContainer.setLayoutParams(params);
        music[0] = createOption(musicContainer, "Music Off");
        music[1] = createOption(musicContainer, "Music On");
        music[2] = createOption(musicContainer, "Music Off");
        music[3] = createOption(musicContainer, "Music On");
        options.addView(musicContainer);
        soundsContainer = new SlidingOptionsLayout(this, width, height, 1, null);
        soundsContainer.setLayoutParams(params);
        sounds[0] = createOption(soundsContainer, "Sounds Off");
        sounds[1] = createOption(soundsContainer, "Sounds On");
        sounds[2] = createOption(soundsContainer, "Sounds Off");
        sounds[3] = createOption(soundsContainer, "Sounds On");
        options.addView(soundsContainer);
        androidBarContainer = new SlidingOptionsLayout(this, width, height, 2, null);
        androidBarContainer.setLayoutParams(params);
        androidBar[0] = createOption(androidBarContainer, "Status Bar Off");
        androidBar[1] = createOption(androidBarContainer, "Status Bar On");
        androidBar[2] = createOption(androidBarContainer, "Status Bar Off");
        androidBar[3] = createOption(androidBarContainer, "Status Bar On");
        options.addView(androidBarContainer);
        difficultyContainer = new SlidingOptionsLayout(this, width, height, 3, null);
        difficultyContainer.setLayoutParams(params);
        difficulty[0] = createOption(difficultyContainer, "Very Hard");
        difficulty[1] = createOption(difficultyContainer, "Easy");
        difficulty[2] = createOption(difficultyContainer, "Medium");
        difficulty[3] = createOption(difficultyContainer, "Hard");
        difficulty[4] = createOption(difficultyContainer, "Very Hard");
        difficulty[5] = createOption(difficultyContainer, "Easy");
        options.addView(difficultyContainer);
        back = createOption(options, "Back To Game");
        back.setOnTouchListener(new Confirm(this, "back"));
        exit = createOption(options, "Exit Game");
        exit.setOnTouchListener(new Confirm(this, "exit"));
    }

    public void finish(String button) {
        Intent intent = new Intent();
        intent.putExtra("button", button);
        setResult(Activity.RESULT_OK, intent);
//        GameGlobals.music.setVolume(1, 1);
        super.finish();
    }

    private TextView createOption(ViewGroup container, String text) {
        TextView res = new TextView(this);
        res.setText(text);
        res.setTextSize(TypedValue.COMPLEX_UNIT_PX, width / 8);
        res.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height));
        res.setHeight(height);
        res.measure(LayoutParams.WRAP_CONTENT, height);
        ((LinearLayout.LayoutParams) res.getLayoutParams()).gravity = Gravity.CENTER;
        res.setHapticFeedbackEnabled(true);
        res.setBackgroundColor(Color.DKGRAY);
        res.setGravity(Gravity.CENTER);
        res.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Air Americana.ttf"));
        res.setPadding(width/12, 0, width/12, 0);
        container.addView(res);
        return res;
    }

    private class Confirm implements View.OnTouchListener {

        Context context;
        String button;

        public Confirm(Context context, final String button) {
            this.context = context;
            this.button = button;
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
            } else if (event.getAction() == MotionEvent.ACTION_UP && event.getX() >= 0 && event.getY() >= 0 && event.getX() <= v.getWidth() && event.getY() <= v.getHeight()) {
                if (button.equals("back")) {
                    finish(button);
                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to " + (button.equals("new") ? "start a new" : button.equals("exit") ? "exit" : "restart this") + " game?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(button);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
            return true;
        }
    }
}
