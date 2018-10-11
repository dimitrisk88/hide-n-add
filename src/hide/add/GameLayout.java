/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * @author the man-ager
 */
public class GameLayout extends ViewGroup {

    private static int N, cell;
    private boolean once = false;

    public GameLayout(Context context, int N, int cell) {
        super(context);
        GameLayout.N = N;
        GameLayout.cell = cell;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (once) {
            return;
        }
        int children = getChildCount(), iv = 0, tv = 0, N2 = N + 2;
        for (int i = 0; i < children; i++) {
            View v = getChildAt(i);
            if (v.getClass() == ImageView.class) {
                v.layout(iv / N * cell, iv % N * cell, iv / N * cell + cell, iv % N * cell + cell);
                iv++;
            } else if (v.getClass() == TextView.class) {
                v.layout(tv / N2 * cell - cell, tv % N2 * cell - cell, tv / N2 * cell, tv % N2 * cell);
                tv++;
            }
        }
        once = true;
    }

    public void moveTable(int hor, int ver, int data[][], TextView tv[][]) {
        int x1, x2, y1, y2;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (data[i][j] < 1 && (hor != 0 || ver != 0)) {
                    continue;
                }
                x1 = x2 = hor;
                y1 = y2 = ver;
                if (hor < 0) {
                    if (data[(i - 1 + N) % N][j] < 0) {
                        x1 = 0;
                        x2 = hor / 4;
                    } else if (data[((i - 1 + N) % N)][j] > 0 && data[((i - 2 + N) % N)][j] < 0) {
                        x1 = hor * 3 / 4;
                        x2 = hor;
                    }
                } else if (hor > 0) {
                    if (data[(i + 1) % N][j] < 0) {
                        x1 = hor / 4;
                        x2 = 0;
                    } else if (data[(i + 1) % N][j] > 0 && data[(i + 2) % N][j] < 0) {
                        x1 = hor;
                        x2 = hor * 3 / 4;
                    }
                } else if (ver < 0) {
                    if (data[i][(j - 1 + N) % N] < 0) {
                        y1 = 0;
                        y2 = ver / 4;
                    } else if (data[i][(j - 1 + N) % N] > 0 && data[i][(j - 2 + N) % N] < 0) {
                        y1 = ver * 3 / 4;
                        y2 = ver;
                    }
                } else if (ver > 0) {
                    if (data[i][(j + 1) % N] < 0) {
                        y1 = ver / 4;
                        y2 = 0;
                    } else if (data[i][(j + 1) % N] > 0 && data[i][(j + 2) % N] < 0) {
                        y1 = ver;
                        y2 = ver * 3 / 4;
                    }
                }
                tv[i + 1][j + 1].layout(i * cell + x1, j * cell + y1, (i + 1) * cell + x2, (j + 1) * cell + y2);
                tv[i + 1][j + 1].setPadding((x2 - x1) / 4, y2 - y1, (x2 - x1) / 4, 0);
                if (i == 0) {
                    tv[N + 1][j + 1].layout(N * cell + x1, j * cell, (N + 1) * cell + x2, (j + 1) * cell);
                    tv[N + 1][j + 1].setPadding((x2 - x1) / 4, 0, (x2 - x1) / 4, 0);
                } else if (i == N - 1) {
                    tv[0][j + 1].layout(x1 - cell, j * cell, x2, (j + 1) * cell);
                    tv[0][j + 1].setPadding((x2 - x1) / 4, 0, (x2 - x1) / 4, 0);
                }
                if (j == 0) {
                    tv[i + 1][N + 1].layout(i * cell, N * cell + y1, (i + 1) * cell, (N + 1) * cell + y2);
                    tv[i + 1][N + 1].setPadding(0, y2 - y1, 0, 0);
                } else if (j == N - 1) {
                    tv[i + 1][0].layout(i * cell, y1 - cell, (i + 1) * cell, y2);
                    tv[i + 1][0].setPadding(0, y2 - y1, 0, 0);
                }
            }
        }
    }

    public void placeImages(int[][] data, TextView[][] tv) {
//                GameGlobals.click.start();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Integer t = data[i][j] > 0 ? R.drawable.numbers : data[i][j] == 0 ? 0 : R.drawable.obstacle;
                String s = "" + (data[i][j] > 0 ? data[i][j] : "");
                tv[i + 1][j + 1].setBackgroundResource(t);
                tv[i + 1][j + 1].setText(s);
                if (i == 0) {
                    tv[N + 1][j + 1].setBackgroundResource(t);
                    tv[N + 1][j + 1].setText(s);
                }
                if (j == 0) {
                    tv[i + 1][N + 1].setBackgroundResource(t);
                    tv[i + 1][N + 1].setText(s);
                }
                if (i == N - 1) {
                    tv[0][j + 1].setBackgroundResource(t);
                    tv[0][j + 1].setText(s);
                }
                if (j == N - 1) {
                    tv[i + 1][0].setBackgroundResource(t);
                    tv[i + 1][0].setText(s);
                }
            }
        }
    }
}
