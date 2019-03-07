package com.akiranagai.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.text.TextUtils.split;

public class StlModel {

    //bufferの定義
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer indexBuffer;
    protected FloatBuffer normalBuffer;

    protected int nIndexs;
    protected int nPoints;


    public StlModel(Context context, String fileName){
        InputStream is=null;
        BufferedReader reader;
        int[] aiCountTriangle = new int[1];

        try {
            is = context.getResources().getAssets().open("b.stl");
        }catch(IOException e){e.printStackTrace();}
        reader = new BufferedReader(new InputStreamReader(is));
        parse_first(reader, aiCountTriangle);
        reader =null;
        is = null;

        try {
            is = context.getAssets().open("b.stl");
        }catch(IOException e){e.printStackTrace();}
        reader = new BufferedReader(new InputStreamReader(is));

        nIndexs = aiCountTriangle[0]*3;

Log.d("StlModel", "aiCountTriangle[0]: " + aiCountTriangle[0]);

        float[] vertexs= new float[aiCountTriangle[0] *9];
        float[] normals= new float[aiCountTriangle[0] *9];
        short[] indexs = new short[aiCountTriangle[0] *3];

        //ArrayList Polygons = new ArrayList();
        float Scale = 500;

            int FacetFlag = 0;
            int VertexFlag = 0;

            //float[] Polygon = new float[12];

            String line;
            int count=0;
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll("\\s{2,}", " ").trim();

                    //Log.d("secondParse0", line);
                    //空白文字除去
                    //line = trim(line);
                    //要素分解
                    String stl_line[] = split(line, " ");
                    /*
                    Log.d("secondParse 2","stl_line[0]: " + stl_line[0]);
                    if(stl_line.length > 2)
                        Log.d("secondParse 3","stl_line[1]: " + stl_line[1]);
                    if(stl_line.length > 3)
                        Log.d("secondParse 4","stl_line[2]: " + stl_line[2]);
                        */
                    //要素検索
                    if (stl_line[0].equals("facet") && stl_line[1].equals("normal")) {  //法線ベクトル
                        normals[count *9 ] = Float.valueOf(stl_line[2]).floatValue();
                        normals[count *9 +1] = Float.valueOf(stl_line[3]).floatValue();
                        normals[count *9 +2] = Float.valueOf(stl_line[4]).floatValue();

                        normals[count *9 +3] = Float.valueOf(stl_line[2]).floatValue();
                        normals[count *9 +4] = Float.valueOf(stl_line[3]).floatValue();
                        normals[count *9 +5] = Float.valueOf(stl_line[4]).floatValue();

                        normals[count *9 +6] = Float.valueOf(stl_line[2]).floatValue();
                        normals[count *9 +7] = Float.valueOf(stl_line[3]).floatValue();
                        normals[count *9 +8] = Float.valueOf(stl_line[4]).floatValue();
                        FacetFlag = 1;
                    }
                    if (stl_line[0].equals("outer") && stl_line[1].equals("loop")) {  //頂点データ開始
                        VertexFlag = 1;
                    }

                    if (stl_line[0].equals("vertex")) {  //頂点1
                        if(VertexFlag == 1){
                            vertexs[count *9] = Float.valueOf(stl_line[1]).floatValue();
                            vertexs[count*9 +1] = Float.valueOf(stl_line[2]).floatValue();
                            vertexs[count*9 +2] = Float.valueOf(stl_line[3]).floatValue();
                            VertexFlag = 2;
                        }
                        else if(VertexFlag == 2){
                            vertexs[count*9 +3] = Float.valueOf(stl_line[1]).floatValue();
                            vertexs[count*9 +4] = Float.valueOf(stl_line[2]).floatValue();
                            vertexs[count*9 +5] = Float.valueOf(stl_line[3]).floatValue();
                            VertexFlag = 3;
                        }else if(VertexFlag == 3){
                            vertexs[count*9 +6] = Float.valueOf(stl_line[1]).floatValue();
                            vertexs[count*9 +7] = Float.valueOf(stl_line[2]).floatValue();
                            vertexs[count*9 +8] = Float.valueOf(stl_line[3]).floatValue();
                        }
//Log.d("vertexs", "vertexs[1]: " + vertexs[count*9] + "vertexs[9]: " + vertexs[count*9+8]);
                    }

                    if (stl_line[0].equals("endloop")) {  //頂点データここまで
                        VertexFlag = 0;
                    }

                    if (stl_line[0].equals("endfacet")) {  //法線ベクトルここまで
                        indexs[count*3] = (short)(count*3);
                        indexs[count*3+1] = (short)(count*3+1);
                        indexs[count*3+2] =(short)(count*3+2);
                        count++;
                        FacetFlag = 0;
                    }
                }
                reader.close();
            }
            catch (IOException e) {

            }
        vertexBuffer=BufferUtil.makeFloatBuffer(vertexs);
        indexBuffer=BufferUtil.makeShortBuffer(indexs);
        normalBuffer=BufferUtil.makeFloatBuffer(normals);
        //for(int i =0; i < vertexs.length; i++)
            //Log.d("vertexBuffer", "vertexs[" + i + "]: " + vertexs[i]);

    }

    private static boolean parse_first(BufferedReader br, int[] aiCountTriangle ) {
        // インプットのチェック
        if (null == aiCountTriangle) {
            return false;
        }
        // アウトプットの初期化
        aiCountTriangle[0] = 0;

        try {
            int iIndexTriangle = 0;
            while (true) {
                String strReadString = br.readLine();
                //Log.d("parse_First", strReadString);
                if (null == strReadString) {
                    break;
                }
                StringTokenizer stReadString = new StringTokenizer(strReadString, ", \t\r\n");
                if (!stReadString.hasMoreTokens()) {
                    continue;
                }
                String token = stReadString.nextToken();
                if (token.equalsIgnoreCase("endfacet")) {
                    ++iIndexTriangle;
                    continue;
                }
            }
            br.close();
            aiCountTriangle[0] = iIndexTriangle;

            return true;
        } catch (Exception e) {
            Log.e("StlFileLoader", "parse_first error : " + e);
            return false;
        }
    }


     //   vertexBuffer=BufferUtil.makeFloatBuffer(vertexs);
      //  indexBuffer=BufferUtil.makeShortBuffer(indexs);
      //  normalBuffer=vertexBuffer; //同じでよいので，同じ実体を用いる

    public void draw(float r,float g,float b,float a, float shininess){
        //頂点点列
        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);

        if (GLES.checkLiting()) {
            //頂点での法線ベクトル
            GLES20.glVertexAttribPointer(GLES.normalHandle, 3,
                    GLES20.GL_FLOAT, false, 0, normalBuffer);

            //周辺光反射
            GLES20.glUniform4f(GLES.materialAmbientHandle, r, g, b, a);

            //拡散反射
            GLES20.glUniform4f(GLES.materialDiffuseHandle, r, g, b, a);

            //鏡面反射
            GLES20.glUniform4f(GLES.materialSpecularHandle, 1f, 1f, 1f, a);
            GLES20.glUniform1f(GLES.materialShininessHandle, shininess);

        } else {
            //shadingを使わない時に使う単色の設定 (r, g, b,a)
            GLES20.glUniform4f(GLES.objectColorHandle, r, g, b, a);
        }

        //
        indexBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                nIndexs, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

    }
}
