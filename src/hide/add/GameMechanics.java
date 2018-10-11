/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author qbsstation7
 */
public class GameMechanics {

    private static int N;
    private final int[][] data, restartData;

    public GameMechanics(int N, int[][] data) {
        GameMechanics.N = N;
        this.data = data;
        restartData = new int[N][N];
    }

    public void newGame(Activity main, int[][] solutions) {
        int stage = new Random().nextInt(10000), place = 0, number;
        InputStream raw;
        try {
            raw = main.getAssets().open("stages");
            System.out.println(raw.skip(stage * 20));
            byte[] buffer = new byte[20];
            raw.read(buffer);
            raw.close();
            for (int i = 0; i < 20; i++) {
                System.out.print(buffer[i] + " ");
            }
            System.out.println("");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    number = convert(buffer, place, 3);
                    data[i][j] = number == 4 ? -1 : number == 6 ? 8 : number;
                    restartData[i][j] = data[i][j];
                    place += 3;
                    System.out.print(data[i][j] + " ");
                }
                System.out.println("");
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4 + i * 2; j++) {
                    solutions[i][j + 1] = convert(buffer, place, 2);
                    place += 2;
                }
                solutions[i][0] = convert(buffer, place, 7);
                place += 7;
            }
        } catch (IOException ex) {
            Logger.getLogger(GameMechanics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int convert(byte[] array, int place, int size) {
        int i = place / 8, j = 8 - place % 8 - size, data, temp;
        if (j < 0) {
            temp = 255 & array[i];
            data = temp % (int) Math.pow(2, size + j);
            data = data << -j;
            temp = 255 & array[i + 1];
            data += temp >> 8 + j;
        } else {
            temp = 255 & array[i];
            data = (temp >> j) % (int) Math.pow(2, size);
        }
        return data;
    }

    public void restartGame() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                data[i][j] = restartData[i][j];
            }
        }
    }

    public List<Point> moveData(boolean hor, boolean ul) {
        int[] temp = new int[N];
        int add = ul ? -1 : 1;
        int[][] dataTemp = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dataTemp[i][j] = data[i][j];
            }
        }
        List<Point> res = new ArrayList<Point>();
        for (int ci = 0; ci < N; ci++) {
            int i = ul ? ci : N - 1 - ci;
            for (int cj = 0; cj < N; cj++) {
                int j = ul ? cj : N - 1 - cj;
                if ((hor ? i : j) == (ul ? 0 : N - 1)) {
                    temp[hor ? j : i] = data[i][j];
                    if (data[hor ? (ul ? N - 1 : 0) : i][!hor ? (ul ? N - 1 : 0) : j] != -1 && data[i][j] != -1) {
                        data[i][j] = 0;
                    }
                } else if (data[i + (hor ? add : 0)][j + (!hor ? add : 0)] != -1 && data[i][j] != -1) {
                    if (data[(i + (hor ? add * 2 : 0) + N) % N][(j + (!hor ? add * 2 : 0) + N) % N] != -1) {
                        data[i + (hor ? add : 0)][j + (!hor ? add : 0)] = data[i][j];
                    } else {
                        if (data[i + (hor ? add : 0)][j + (!hor ? add : 0)] != 0 && 0 != data[i][j]) {
                            res.add(new Point(i + (hor ? add : 0), j + (!hor ? add : 0)));
                        }
                        data[i + (hor ? add : 0)][j + (!hor ? add : 0)] += data[i][j];
                    }
                    data[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            if (data[hor ? (ul ? N - 1 : 0) : i][!hor ? (ul ? N - 1 : 0) : i] != -1 && temp[i] != -1) {
                if (data[hor ? (ul ? N - 2 : 1) : i][!hor ? (ul ? N - 2 : 1) : i] != -1) {
                    data[hor ? (ul ? N - 1 : 0) : i][!hor ? (ul ? N - 1 : 0) : i] = temp[i];
                } else {
                    if (data[hor ? (ul ? N - 1 : 0) : i][!hor ? (ul ? N - 1 : 0) : i] != 0 && 0 != temp[i]) {
                        res.add(new Point(hor ? (ul ? N - 1 : 0) : i, !hor ? (ul ? N - 1 : 0) : i));
                    }
                    data[hor ? (ul ? N - 1 : 0) : i][!hor ? (ul ? N - 1 : 0) : i] += temp[i];
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (dataTemp[i][j] != data[i][j]) {
                    GameGlobals.moves++;
                    GameGlobals.movesText.setText("Moves: " + GameGlobals.moves + " / " + (GameGlobals.choises[3] * 2 + 2));
                    return res.isEmpty() ? null : res;
                }
            }
        }
        return res.isEmpty() ? null : res;
    }
}
