package com.example.colortiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

public class TilesView extends View {
    int[][] tiles = new int[4][4];
    int brightColor = Color.rgb(255, 153, 204);
    int darkColor = Color.rgb(102, 0, 51);
    private MainActivity mainActivity;



    int width, height;

    public TilesView(Context context) {
        super(context);

    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity) context;

        Random rn = new Random();
        int color;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                color = rn.nextInt(2);
                if (color == 0)
                    tiles[i][j] = brightColor;
                else
                    tiles[i][j] = darkColor;
            }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        Paint p = new Paint();
        Paint strokePaint = new Paint();

        float tileWidth = width / 4f;
        float tileHeight = height / 4f;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                p.setColor(tiles[i][j]);
                p.setShadowLayer(5, 5, 10, Color.BLUE);
                canvas.drawRect(j * tileWidth, i * tileHeight,
                        (j + 2) * tileWidth, (i+1) * tileHeight, p);
                strokePaint.setStyle(Paint.Style.STROKE);
                strokePaint.setColor(Color.WHITE);
                strokePaint.setStrokeWidth(10);
                strokePaint.setShadowLayer(5, 5, 10, Color.BLACK);
                canvas.drawRect(j * tileWidth, i * tileHeight,
                        (j + 2) * tileWidth, (i+1) * tileHeight, strokePaint);

            }

        countingDarkTiles();
    }

    public int changeColor(int v) {
        if (v == brightColor)
            v = darkColor;
        else
            v = brightColor;
        return v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        int tileWidth = width / 4;
        int tileHeight = height / 4;

        int col = x / tileWidth;
        int row = y / tileHeight;

        Log.d("tag", "x = "+ col + "y = " + row);

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            for (int i = 0; i < 4; i++) {
                tiles[row][i] = changeColor(tiles[row][i]);
                tiles[i][col] = changeColor(tiles[i][col]);
            }
            tiles[row][col] = changeColor(tiles[row][col]);
            new AnimationTask(tiles, row, col).execute();
            invalidate();
            countingDarkTiles();
            checkVictory();

        }

        invalidate();
        return true;
    }

    public void checkVictory() {
        int firstColor = tiles[0][0];
        boolean allSame = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tiles[i][j] != firstColor) {
                    allSame = false;
                    break;
                }
            }
            if (!allSame) break;
        }

        /*if (allSame) {
            Toast.makeText(mainActivity, "Вы выиграли", Toast.LENGTH_SHORT).show();
        }*/

    }

    class AnimationTask extends AsyncTask<Void, int[][], Void> {

        private int x, y;
        int[][] localityTiles = new int[4][4];

        public AnimationTask(int[][] tiles, int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 4; i++) {
                localityTiles[x][i] = tiles[x][i];
                localityTiles[i][y] = tiles[i][y];
            }

            for (int i = 0; i < 4; i++) {
                tiles[x][i] = Color.WHITE;
                tiles[i][y] = Color.WHITE;
            }

            publishProgress(localityTiles); // Отправка обновленных значений
            try {
                Thread.sleep(20); // Задержка между кадрами
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            for (int[] x : localityTiles)
                for (int y : x)
                    Log.d("tilesss", "" + y);

            Log.d("tilesss", "tile after after" + localityTiles[x][y]);

            for (int i = 0; i < 4; i++) {
                tiles[x][i] = localityTiles[x][i];
                tiles[i][y] = localityTiles[i][y];
            }


        }
    }

    public void countingDarkTiles() {
        int darkCount = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (tiles[i][j] == darkColor)
                    darkCount++;
            }

        mainActivity.setTextViews(darkCount);

    }
}
