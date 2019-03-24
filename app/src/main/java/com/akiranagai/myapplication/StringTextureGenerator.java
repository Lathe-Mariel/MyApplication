package com.akiranagai.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import com.akiranagai.myapplication.object3d.TexObject3D;

import javax.microedition.khronos.opengles.GL10;


public class StringTextureGenerator {
    private int foreAlfa=255;
    private int backAlfa=255;
    private int TextureId=-1;
    private int TextureUnitNumber=0;

    public StringTextureGenerator(){}

    public StringTextureGenerator(String text, float textSize, int txtcolor, int bkcolor, int textureidnumber) {
        TextureUnitNumber = textureidnumber;
        makeStringTexture(text, textSize, txtcolor, bkcolor);
    }
    public StringTextureGenerator(String text, float textSize, int txtcolor, int bkcolor) {
        makeStringTexture(text, textSize, txtcolor, bkcolor);
    }

    /*
    Canvas のサイズは常に２乗の正方形
     */
    public int makeStringTexture(String text, float textSize, int txtcolor, int bkcolor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.getTextBounds(text, 0, text.length(), new Rect(0, 0, (int) textSize * text.length(), (int) textSize));

        int textWidth = (int) paint.measureText(text);
        int textHeight = (int) (Math.abs(fontMetrics.top) + fontMetrics.bottom);

        if (textWidth == 0) textWidth = 10;
        if (textHeight == 0) textHeight = 10;

        int bitmapsize = 2; //現時点でNexus7ではビットマップは正方形で一辺の長さは2のべき乗でなければならない
        while (bitmapsize < textWidth) bitmapsize *= 2;
        while (bitmapsize < textHeight) bitmapsize *= 2;

        final Bitmap bitmap = Bitmap.createBitmap(bitmapsize, bitmapsize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        paint.setColor(bkcolor);
        paint.setAlpha(backAlfa);
        canvas.drawRect(new Rect(0, 0, bitmapsize, bitmapsize), paint);
        paint.setColor(txtcolor);
        paint.setAlpha(foreAlfa);
        canvas.drawText(text, bitmapsize / 2 - textWidth / 2, bitmapsize / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, paint);

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

    /**
     * Canvasのサイズを正方形にしないバージョン
     * @param text
     * @param textSize
     * @param txtcolor
     * @param bkcolor
     * @return
     */
    public int makeStringTexture2(String text, float textSize, int txtcolor, int bkcolor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.getTextBounds(text, 0, text.length(), new Rect(0, 0, (int) textSize * text.length(), (int) textSize));

        int textWidth = (int) paint.measureText(text);
        int textHeight = (int) (Math.abs(fontMetrics.top) + fontMetrics.bottom);

        if (textWidth == 0) textWidth = 10;
        if (textHeight == 0) textHeight = 10;

        final Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        paint.setColor(bkcolor);
        paint.setAlpha(backAlfa);
        canvas.drawRect(new Rect(0, 0, textWidth, textHeight), paint);
        paint.setColor(txtcolor);
        paint.setAlpha(foreAlfa);
        canvas.drawText(text, 0, textHeight / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, paint);

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

    /**
     * ６面別文字テキスチャーを作る(バックバッファー用BlueElement付き)
     * @param text
     * @param textSize
     * @param txtcolor
     * @param bkcolor
     * @return 表示用＆バックバッファ用テキスチャーID配列(0:表示用、1:バックバッファBlueElementでエンコード).
     */
    public int[] makeString6Texture(String text, float textSize, int txtcolor, int bkcolor, int startIndex) {
        String firstChar = text.substring(0,1);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.getTextBounds(firstChar, 0, 1, new Rect(0, 0, (int) textSize, (int) textSize));

        int textWidth = (int) paint.measureText(firstChar);
        int textHeight = (int) (Math.abs(fontMetrics.top) + fontMetrics.bottom);

        if (textWidth == 0) textWidth = 10;
        if (textHeight == 0) textHeight = 10;

        int bitmapsize = 2; //現時点でNexus7ではビットマップは正方形で一辺の長さは2のべき乗でなければならない
        while (bitmapsize < textWidth) bitmapsize *= 2;
        while (bitmapsize < textHeight) bitmapsize *= 2;

        final Bitmap bitmap = Bitmap.createBitmap(bitmapsize*8, bitmapsize, Bitmap.Config.ARGB_8888);
        final Bitmap bitmapBack = Bitmap.createBitmap(bitmapsize*8, bitmapsize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Canvas backBuffer = new Canvas(bitmapBack);
        paint.setColor(bkcolor);
        paint.setAlpha(backAlfa);
        canvas.drawRect(new Rect(0, 0, bitmapsize*8, bitmapsize), paint);
        paint.setColor(txtcolor);
        paint.setAlpha(foreAlfa);
        //表バッファ用画像
        for(int i = 0; i< text.length(); i++){
            canvas.drawText(text.substring(i, i+1), bitmapsize / 2 - textWidth / 2 + i*bitmapsize, bitmapsize / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, paint);
        }
        //裏バッファ用画像
        for(int i =0; i< 6; i++){
            paint.setColor(Color.argb(255,0,0,startIndex+i));
            backBuffer.drawRect(new Rect(bitmapsize*i,0,bitmapsize*(i+1), bitmapsize), paint);
        }
                final int[] textures = new int[2];
                GLES20.glGenTextures(2, textures, 0);

                //表示用６面別　テキスチャー生成
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        //バックバッファー用テキスチャー生成
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapBack, 0);
        bitmapBack.recycle();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textures;
    }

    /**
     * スペース付きの文字テクスチャー生成
     * @param text
     * @param textSize
     * @param txtcolor
     * @return
     */
    public int makeStringTextureWithSpace(String text, float textSize, int txtcolor) {
        String firstChar = text.substring(0,1);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.getTextBounds(firstChar, 0, 1, new Rect(0, 0, (int) textSize, (int) textSize));

        int textWidth = (int) paint.measureText(firstChar);
        int textHeight = (int) (Math.abs(fontMetrics.top) + fontMetrics.bottom);

        if (textWidth == 0) textWidth = 10;
        if (textHeight == 0) textHeight = 10;

        int bitmapsize = 2; //現時点でNexus7ではビットマップは正方形で一辺の長さは2のべき乗でなければならない
        while (bitmapsize < textWidth) bitmapsize *= 2;
        while (bitmapsize < textHeight) bitmapsize *= 2;

        final Bitmap bitmap = Bitmap.createBitmap(bitmapsize*46, bitmapsize, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        paint.setColor(txtcolor);
        paint.setAlpha(180);

        float canvasLength = bitmapsize*46;
        float pitch = canvasLength / text.length();
        for(int i = 0; i< text.length(); i++){
            canvas.drawText(text.substring(i, i+1), pitch * i + pitch/2,bitmapsize / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, paint);
        }

        final int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        //表示用６面別　テキスチャー生成
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textures[0];
    }

    public void setTexture() {
        // テクスチャの指定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+TextureUnitNumber);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureId);
        GLES20.glUniform1i(GLES.textureHandle, TextureUnitNumber); //テクスチャユニット番号を指定する
    }

    public int getTextureId(){
        return TextureId;
    }
    public void setAlfa(int fore, int back){
        this.foreAlfa = fore;
        this.backAlfa = back;
    }

    public static void deleteTexture(int textureId){
        final int[] textures = new int[1];
        if (textureId != -1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
           // GLES20.glDeleteTextures(1, textures, 0);
            textures[0] = textureId;
            GLES20.glDeleteTextures(1, textures, 0);
        }
    }
}
