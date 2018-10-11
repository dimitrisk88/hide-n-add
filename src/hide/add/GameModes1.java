/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hide.add;

/**
 *
 * @author qbsstation7
 */
public class GameModes1 {

    private static int N;
    private final int[] moves = new int[15];
    private final int[] tempmax = new int[15];
    private int maxmax=0;

    public GameModes1(int N) {
        GameModes1.N = N;
    }
    
    private int calcMax(int[][] data) {
        int max = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                }
            }
        }
        return max;
    }

    public int max(int[][] data, int now, int lim) {
        int[][] temp = new int[N][N];
        int res, max = 0;
        for (int i = 0; i < 4; i++) {
            moves[now] = i;
            if (now >= 2 && Math.abs(moves[now] - moves[now - 1]) == 2 && moves[now] == moves[now - 2]) {
                i++;
            }
            for (int k = 0; k < N; k++) {
                System.arraycopy(data[k], 0, temp[k], 0, N);
            }
            GameMechanics gm = new GameMechanics(N, temp);
            gm.moveData(i%2==0, i<2);
//            if (i == 0) {
//                gm.moveData(true, true);    //left
//            } else if (i == 1) {
//                gm.moveData(false, true);   //up
//            } else if (i == 2) {
//                gm.moveData(true, false);   //right
//            } else if (i == 3) {
//                gm.moveData(false, false);  //down
//            }
            if (now < lim) {
                res = max(temp, now + 1, lim);
            } else {
                res = calcMax(temp);
                if(res>maxmax){
                    maxmax=res;
                    for(int j=0;j<8;j++) {
                        tempmax[j]=moves[j];
                    }
                }
            }
            if (res > max) {
                max = res;
            }
        }
        if(now==0){
            for(int i=0;i<8;i++){
                System.out.print(tempmax[i]+" ");
            }
            System.out.println();
        }
        return max;
    }
}
