package hide.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameActivity extends Activity {

    public int targetX = 0, targetY = 0, tempX = 0, tempY = 0, touching = 0, speed, cellSize, menuSize;
    public ImageView ddm;
    private static final int N = 5;
    private final int[][] data = new int[N][N], solutions = new int[4][11];
    private boolean once = true;
    public final TextView[][] tv = new TextView[N + 2][N + 2];
    private TextView time, target;
    private final GameMechanics gameMechanics = new GameMechanics(N, data);
    private GameLayout gameLayout;
    private long counter;
    private CountDownTimer timer;
    private List<Point> points;
//    private LinearLayout

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        gameMechanics.newGame(this, solutions);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5 + i * 2; j++) {
                System.out.print(solutions[i][j] + " ");
            }
            System.out.println("");
        }
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        GameGlobals.choises[0] = sharedPref.getInt("music", 1);
        GameGlobals.choises[1] = sharedPref.getInt("sounds", 1);
        GameGlobals.choises[2] = sharedPref.getInt("androidbar", 1);
        GameGlobals.choises[3] = sharedPref.getInt("difficulty", 1);
        GameGlobals.choises[4] = sharedPref.getInt("themes", 1);
        counter = sharedPref.getLong("counter", 3600000);
        GameGlobals.width = getWindowManager().getDefaultDisplay().getWidth();
        GameGlobals.height = getWindowManager().getDefaultDisplay().getHeight();
        Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/Air Americana.ttf");
        TopLayout topLayout = new TopLayout(this);
        time = new TextView(this);
        time.setTypeface(fonts);
        time.setText("Time");
        time.setGravity(Gravity.CENTER_VERTICAL);
        topLayout.addView(time);
        GameGlobals.movesText = new TextView(this);
        GameGlobals.movesText.setTypeface(fonts);
        GameGlobals.movesText.setText("Moves");
        GameGlobals.movesText.setGravity(Gravity.CENTER);
        topLayout.addView(GameGlobals.movesText);
        target = new TextView(this);
        target.setTypeface(fonts);
        target.setText("Target");
        target.setGravity(Gravity.CENTER);
        topLayout.addView(target);
        ((LinearLayout) findViewById(R.id.top_layout)).addView(topLayout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (once) {
            super.onWindowFocusChanged(hasFocus);
            Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/Air Americana.ttf");
//            GameGlobals.music = MediaPlayer.create(this, R.raw.soho);
//            GameGlobals.music.setLooping(true);
//            GameGlobals.click = MediaPlayer.create(this, R.raw.histicks);
            settings();
            cellSize = GameGlobals.width / N;
            menuSize = GameGlobals.width / 5;
            speed = cellSize / 3;
            gameLayout = new GameLayout(this, N, cellSize);
            gameLayout.setOnTouchListener(new MoveListener(this));
            gameLayout.setLayoutParams(new LinearLayout.LayoutParams(cellSize * N, cellSize * N, 0));

            GameGlobals.bottomLayout = new BottomLayout(this);
            GameGlobals.leftIndicator = new ImageView(this);
            GameGlobals.topIndicator = new ImageView(this);
            GameGlobals.rightIndicator = new ImageView(this);
            GameGlobals.bottomIndicator = new ImageView(this);
            TextView options = new TextView(this);
            GameGlobals.leftIndicator.setImageResource(R.drawable.left);
            GameGlobals.topIndicator.setImageResource(R.drawable.top);
            GameGlobals.rightIndicator.setImageResource(R.drawable.right);
            GameGlobals.bottomIndicator.setImageResource(R.drawable.bottom);
            options.setText("OPTIONS");
            options.setTypeface(fonts);
            options.setSingleLine();
            GameGlobals.bottomLayout.addView(GameGlobals.leftIndicator);
            GameGlobals.bottomLayout.addView(GameGlobals.topIndicator);
            GameGlobals.bottomLayout.addView(GameGlobals.rightIndicator);
            GameGlobals.bottomLayout.addView(GameGlobals.bottomIndicator);
            GameGlobals.bottomLayout.addView(options);
            options.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction(), x = Math.round(event.getX()), y = Math.round(event.getY());
                    if (action == MotionEvent.ACTION_DOWN) {
                        v.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
                    } else if (action == MotionEvent.ACTION_UP && x >= 0 && y >= 0 && x < v.getWidth() && y < v.getHeight()) {
                        timer.cancel();
                        Intent intent = new Intent(v.getContext(), OptionsActivity.class);
                        startActivityForResult(intent, 0);
                    }
                    return true;
                }
            });
            ((LinearLayout) findViewById(R.id.middle_layout)).addView(gameLayout);
            ((LinearLayout) findViewById(R.id.bottom_layout)).addView(GameGlobals.bottomLayout);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(getAssets().open("top.png"));
            } catch (IOException ex) {
                Logger.getLogger(GameActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    ImageView iv = new ImageView(this);
                    iv.setImageBitmap(bm);
                    iv.setAlpha(100);
                    gameLayout.addView(iv);
                }
            }
            for (int i = -1; i < N + 1; i++) {
                for (int j = -1; j < N + 1; j++) {
                    tv[i + 1][j + 1] = new TextView(this);
                    tv[i + 1][j + 1].setGravity(Gravity.CENTER);
                    tv[i + 1][j + 1].setHeight(cellSize);
                    tv[i + 1][j + 1].setWidth(cellSize);
                    tv[i + 1][j + 1].measure(cellSize, cellSize);
                    tv[i + 1][j + 1].setTextSize(TypedValue.COMPLEX_UNIT_PX, cellSize / 2);
                    tv[i + 1][j + 1].setTypeface(fonts);
                    gameLayout.addView(tv[i + 1][j + 1]);
                }
            }
            once = false;
            startTimer(true);
            move();
        }
    }

    private void startTimer(boolean flag) {
        if (flag) {
            counter = 3600099;
            if (timer != null) {
                timer.cancel();
            }
            GameGlobals.moves = 0;
            int targetScore = solutions[GameGlobals.choises[3] - 1][0];
            target.setText("Target: " + targetScore);
            GameGlobals.movesText.setText("Moves: " + GameGlobals.moves + " / " + (GameGlobals.choises[3] * 2 + 2));
            gameLayout.placeImages(data, tv);
        }
        timer = new CountDownTimer(counter, 100) {
            public void onTick(long ms) {
                counter = ms;
                ms = 3600099 - ms;
                time.setText("Time: " + String.format("%02d", ms / 60000) + ":" + String.format("%02d", ms % 60000 / 1000) + "." + ms % 1000 / 100);
            }

            public void onFinish() {
            }
        }.start();
    }

    private void settings() {
        if (GameGlobals.choises[0] == 1) {
//            GameGlobals.music.start();
        }
        if (GameGlobals.choises[2] == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (GameGlobals.choises[2] == 2) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startTimer(false);
        if (resultCode == Activity.RESULT_OK) {
            settings();
            String button = data.getStringExtra("button");
            if (button.equals("new")) {
                gameMechanics.newGame(this, solutions);
                startTimer(true);
            } else if (button.equals("restart")) {
                gameMechanics.restartGame();
                startTimer(true);
            } else if (button.equals("exit")) {
                finish();
            }
        }
    }

    @Override
    public void finish() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
//        GameGlobals.music.release();
        editor.putInt("music", GameGlobals.choises[0]);
        editor.putInt("sounds", GameGlobals.choises[1]);
        editor.putInt("androidbar", GameGlobals.choises[2]);
        editor.putInt("difficulty", GameGlobals.choises[3]);
        editor.putInt("themes", GameGlobals.choises[4]);
        editor.putLong("counter", counter);
        editor.commit();
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void openMenu(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void button(View view) {
        String set = view.getTag().toString();
        if (set.equals("sound_on")) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void move() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                move();
            }
        }, 50);
        if (touching == 0) {
            return;
        } else if (touching > 1) {
            TopLayout topLayout = (TopLayout) ((LinearLayout) findViewById(R.id.top_layout)).getChildAt(0);
            topLayout.removeAllViews();
            topLayout.addView(GameGlobals.movesText);
            topLayout.addView(time);
            topLayout.addView(target);
            topLayout.requestLayout();
            for (Point point : points) {
                int i = point.x, j = point.y, expand = (touching > 5 ? 8 - touching : touching > 4 ? 2 : touching - 2) * cellSize / 20;
                tv[i + 1][j + 1].layout(i * cellSize - expand, j * cellSize - expand, (i + 1) * cellSize + expand, (j + 1) * cellSize + expand);
                tv[i + 1][j + 1].setPadding(expand / 2, expand * 2, 0, 0);
                touching--;
                if (touching == 1) {
                    touching = 0;
                    points = null;
                    boolean[] checks = {false, false, false, false};
                    GameGlobals.bottomLayout.setAlphas(checks);
                }
            }
            return;
        } else if (touching == -1 && targetX == tempX && targetY == tempY) {
            if (Math.abs(tempX) > cellSize / 2 || Math.abs(tempY) > cellSize / 2) {
                points = gameMechanics.moveData(tempX != 0, tempX < 0 || tempY < 0);
                gameLayout.placeImages(data, tv);
                tempX = tempY = targetX = targetY = 0;
                if (points != null) {
                    touching = 7;
                } else {
                    touching = 0;
                    boolean[] checks = {false, false, false, false};
                    GameGlobals.bottomLayout.setAlphas(checks);
                }
            }
        } else if (targetX != 0) {
            if (Math.abs(tempY) >= speed) {
                tempY += tempY > 0 ? -speed : speed;
            } else if (tempY != 0) {
                int temp = speed - Math.abs(tempY);
                tempY = 0;
                tempX = targetX >= temp ? temp : targetX <= -temp ? -temp : targetX;
            } else {
                tempX += targetX >= tempX + speed ? speed : targetX <= tempX - speed ? -speed : targetX - tempX;
            }
        } else if (targetY != 0) {
            if (Math.abs(tempX) >= speed) {
                tempX += tempX > 0 ? -speed : speed;
            } else if (tempX != 0) {
                int temp = speed - Math.abs(tempX);
                tempX = 0;
                tempY = targetY >= temp ? temp : targetY <= -temp ? -temp : targetY;
            } else {
                tempY += targetY >= tempY + speed ? speed : targetY <= tempY - speed ? -speed : targetY - tempY;
            }
        } else {
            if (tempX != 0) {
                tempX += tempX > speed ? -speed : tempX < -speed ? speed : -tempX;
            } else {
                tempY += tempY > speed ? -speed : tempY < -speed ? speed : -tempY;
            }
        }
        gameLayout.moveTable(tempX, tempY, data, tv);
    }
}
