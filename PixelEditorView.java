package org.aseprite.androidproto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class PixelEditorView extends View {
    private static final int GRID_W = 32;
    private static final int GRID_H = 32;

    private final int[][] pixels = new int[GRID_H][GRID_W];
    private final Paint paint = new Paint();
    private int selectedColor = Color.WHITE;
    private boolean eraser = false;

    private final int[] palette = new int[] {
            Color.WHITE, Color.BLACK, Color.RED, Color.GREEN,
            Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA,
            0xffff8800, 0xff8844ff
    };

    public PixelEditorView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        for (int y = 0; y < GRID_H; y++) {
            for (int x = 0; x < GRID_W; x++) {
                pixels[y][x] = Color.TRANSPARENT;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0xff202020);

        float toolbarH = 90f;
        float margin = 24f;
        float usableW = getWidth() - margin * 2f;
        float usableH = getHeight() - toolbarH - margin * 2f;
        float cell = Math.min(usableW / GRID_W, usableH / GRID_H);

        float gridW = cell * GRID_W;
        float gridH = cell * GRID_H;
        float startX = (getWidth() - gridW) / 2f;
        float startY = toolbarH + ((getHeight() - toolbarH - gridH) / 2f);

        paint.setStyle(Paint.Style.FILL);

        // checkerboard background
        for (int y = 0; y < GRID_H; y++) {
            for (int x = 0; x < GRID_W; x++) {
                boolean even = ((x + y) % 2 == 0);
                paint.setColor(even ? 0xff505050 : 0xff606060);
                canvas.drawRect(startX + x * cell, startY + y * cell,
                        startX + (x + 1) * cell, startY + (y + 1) * cell, paint);

                if (pixels[y][x] != Color.TRANSPARENT) {
                    paint.setColor(pixels[y][x]);
                    canvas.drawRect(startX + x * cell, startY + y * cell,
                            startX + (x + 1) * cell, startY + (y + 1) * cell, paint);
                }
            }
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setColor(0xff888888);
        for (int x = 0; x <= GRID_W; x++) {
            canvas.drawLine(startX + x * cell, startY, startX + x * cell, startY + gridH, paint);
        }
        for (int y = 0; y <= GRID_H; y++) {
            canvas.drawLine(startX, startY + y * cell, startX + gridW, startY + y * cell, paint);
        }

        // top toolbar
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff111111);
        canvas.drawRect(0, 0, getWidth(), toolbarH, paint);

        float swatchSize = 54f;
        float xPos = 16f;
        for (int i = 0; i < palette.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(palette[i]);
            RectF r = new RectF(xPos, 18f, xPos + swatchSize, 18f + swatchSize);
            canvas.drawRoundRect(r, 8f, 8f, paint);

            if (!eraser && selectedColor == palette[i]) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5f);
                paint.setColor(0xffffffff);
                canvas.drawRoundRect(r, 8f, 8f, paint);
            }
            xPos += swatchSize + 12f;
        }

        // eraser button
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(eraser ? 0xffffffff : 0xff777777);
        RectF er = new RectF(xPos + 12f, 18f, xPos + 120f, 72f);
        canvas.drawRoundRect(er, 10f, 10f, paint);
        paint.setColor(eraser ? 0xff000000 : 0xffffffff);
        paint.setTextSize(28f);
        paint.setFakeBoldText(true);
        canvas.drawText("ERASE", xPos + 22f, 55f, paint);
        paint.setFakeBoldText(false);

        // title
        paint.setColor(0xffffffff);
        paint.setTextSize(20f);
        canvas.drawText("Aseprite Android Proto - touch canvas 32x32", getWidth() - 420f, 55f, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float toolbarH = 90f;
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (y < toolbarH && event.getAction() == MotionEvent.ACTION_DOWN) {
                handleToolbar(x, y);
                invalidate();
                return true;
            }

            paintPixel(x, y);
            invalidate();
            return true;
        }

        return true;
    }

    private void handleToolbar(float x, float y) {
        float swatchSize = 54f;
        float xPos = 16f;

        for (int i = 0; i < palette.length; i++) {
            if (x >= xPos && x <= xPos + swatchSize && y >= 18f && y <= 72f) {
                selectedColor = palette[i];
                eraser = false;
                return;
            }
            xPos += swatchSize + 12f;
        }

        if (x >= xPos + 12f && x <= xPos + 120f && y >= 18f && y <= 72f) {
            eraser = !eraser;
        }
    }

    private void paintPixel(float touchX, float touchY) {
        float toolbarH = 90f;
        float margin = 24f;
        float usableW = getWidth() - margin * 2f;
        float usableH = getHeight() - toolbarH - margin * 2f;
        float cell = Math.min(usableW / GRID_W, usableH / GRID_H);

        float gridW = cell * GRID_W;
        float gridH = cell * GRID_H;
        float startX = (getWidth() - gridW) / 2f;
        float startY = toolbarH + ((getHeight() - toolbarH - gridH) / 2f);

        int px = (int)((touchX - startX) / cell);
        int py = (int)((touchY - startY) / cell);

        if (px >= 0 && px < GRID_W && py >= 0 && py < GRID_H) {
            pixels[py][px] = eraser ? Color.TRANSPARENT : selectedColor;
        }
    }
}
