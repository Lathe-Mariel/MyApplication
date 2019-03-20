package com.akiranagai.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class BarGraphTextureGenerator {

    final int textSize = 20;
    private int foreAlfa=180;
    private int backAlfa=160;
    private int TextureId=-1;
    private int TextureUnitNumber=0;
    private int barColor;  //バー色
    private int backColor;  //バー背景色
    private int charColor;  //文字色
    private int backWidth;
    private int backHeight;
    private int maxValue;  //バー最大値
    private int charPositionY;  //描画文字　Y方向位置

    public BarGraphTextureGenerator(){}

    public BarGraphTextureGenerator(int barColor, int charColor, int backColor, int textureidnumber) {
        TextureUnitNumber = textureidnumber;
        this.barColor = barColor;
        this.backColor = backColor;
        this.charColor = charColor;
    }

    public void setMaxValue(int max){
        this.maxValue = max;
    }
    public void setWidth(int value){
        this.backWidth = value;
    }
    public void setHeight(int value){
        this.backHeight = value;
        charPositionY = value - 33;

    }

    /**
     *
     * @param value
     * @return
     */
    public int refleshBar(int value) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        String text = value +" / " + maxValue;
        //paint.getTextBounds(text, 0, text.length(), new Rect(0, 0, (int) textSize * text.length(), (int) textSize));

        //int textWidth = (int) paint.measureText(text);
        //int textHeight = (int) (Math.abs(fontMetrics.top) + fontMetrics.bottom);

        //if (textWidth == 0) textWidth = 10;
        //if (textHeight == 0) textHeight = 10;

        final Bitmap bitmap = Bitmap.createBitmap(backWidth, backHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        paint.setColor(backColor);
        paint.setAlpha(backAlfa);
        canvas.drawRect(new Rect(0, 0, backWidth, backHeight), paint);  //バーグラフ背景描画

        paint.setColor(barColor);
        paint.setAlpha(foreAlfa);
        canvas.drawRect(new Rect(0,0, ((int)(backWidth * value/maxValue)) , backHeight), paint);  //バーグラフ描画

        Log.d("messager", "backWidth: " + backWidth + "    backHeight: " + backHeight);

        paint.setColor(charColor);
        canvas.drawText(text, 5, charPositionY, paint);

        int FIRST_INDEX = 0;
        final int DEFAULT_OFFSET = 0;
        final int[] textures = new int[1];
        if (TextureId != -1) {
            textures[FIRST_INDEX] = TextureId;
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glDeleteTextures(1, textures, DEFAULT_OFFSET);
        }
        GLES20.glGenTextures(1, textures, DEFAULT_OFFSET);
        TextureId = textures[FIRST_INDEX];
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+TextureUnitNumber);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        return TextureId;
    }
}
