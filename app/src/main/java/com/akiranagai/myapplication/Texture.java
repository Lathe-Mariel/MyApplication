package com.akiranagai.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class Texture {
    public static final HashMap<Integer, Integer> textureList = new HashMap<>();  //<R.drawable.Id, TextureId>

    static private int TextureUnitNumber=0;

    public void setUnitNumber(int unitNumber){
        TextureUnitNumber = unitNumber;
    }

    /**
     *
     * @param mContext
     * @param id リソースID(R.drawable.*)
     * @return
     */
    public int addTexture(Context mContext, int id) {
        int textureID;

        if(textureList.containsKey(id)){
            textureID = textureList.get(id);

            //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        }else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            //これをつけないとサイズが勝手に変更されてしまう
            //現時点でNexus7では正方形で一辺が2のべき乗サイズでなければならない
            //元のファイルの段階で大きさをそろえておく必要がある
            final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id, options);
            int FIRST_INDEX = 0;
            final int DEFAULT_OFFSET = 0;
            final int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, DEFAULT_OFFSET);
            textureID = textures[FIRST_INDEX];
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            textureList.put(id, textureID);
        }
        return textureID;
    }

    /**
     *
     * @param textureID 操作対象のtextureID
     * @param repeat Textureを繰り返し貼り付けするか
     */
    public static void setRepeartTexture(int textureID, boolean repeat){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
        if(repeat) {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        }else{
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    public static void setTexture(int tId) {
        // テクスチャの指定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + TextureUnitNumber);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tId);
        /*
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);*/
        GLES20.glUniform1i(GLES.textureHandle, TextureUnitNumber); //テクスチャユニット番号を指定する
    }

}
