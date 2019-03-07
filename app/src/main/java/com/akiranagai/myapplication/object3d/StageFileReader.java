package com.akiranagai.myapplication.object3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.Toast;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.gamecontroller.MainActivity;
import com.akiranagai.myapplication.Texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.text.TextUtils.split;

public class StageFileReader {

    public static ArrayList<Object3D> makeObjects(Context context, GLSurfaceView surfaceView, String stageFileName){
        ArrayList<Object3D> objectList = new ArrayList();

        context = MainActivity.ct;

        InputStream is=null;
        BufferedReader reader;

        try {
            is = context.getResources().getAssets().open(stageFileName);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

        reader = new BufferedReader(new InputStreamReader(is));

        CreatableShape newShapeClass = null;
        int state = 0;
        Object3D newObject=null;

        String line;
        int rowCount=1;
        int classCount = 0;
        boolean diplicatable = false;

        try {
            while ((line = reader.readLine()) != null) {
                String stl_line[] = split(line, " ");

                if(stl_line[0].equals("class")){  //クラスを生成
                    try {
                        Class readClass = Class.forName("com.akiranagai.myapplication.object3d." + stl_line[1]);
                        newObject = (Object3D) readClass.newInstance();
                        state = 1;  //クラス生成完了
                        diplicatable = false;
                    }catch(ClassNotFoundException e){
                        e.printStackTrace();
                    }catch(IllegalAccessException e2){
                    e2.printStackTrace();
                    Log.d("error", rowCount + "行目");
                    Toast.makeText(context, rowCount + "行目: 不正にファイルにアクセスしました<110>", Toast.LENGTH_LONG).show();
                    return null;
                    }catch(InstantiationException e3){
                    e3.printStackTrace();
                    Log.d("error", rowCount + "行目");
                    Toast.makeText(context, rowCount + "行目: クラスをインスタンス化できません<111>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state == 0 && diplicatable && stl_line[0].equals("diplicateclass")){  //直前に生成したオブジェクトを複製
                    newObject = newObject.clone();
                    diplicatable = false;
                    state = 1;
                }
                else if (state > 0 && stl_line[0].equals("shape")) {  //形状クラス生成
                    if(stl_line.length > 2){
                        try{
                            newObject.setModel(Integer.parseInt(stl_line[2]));
                        }catch(NumberFormatException e){
                            e.printStackTrace();
                            Log.d("error", rowCount + "行目");
                            Toast.makeText(context, "モデルIDに数字以外が含まれています<120>", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        try {
                            Class readShapeClass = Class.forName("com.akiranagai.myapplication.object3d." + stl_line[1]);
                            newShapeClass = (CreatableShape) readShapeClass.newInstance();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                            Log.d("error", rowCount + "行目");
                            Toast.makeText(context, "クラス名が間違っています<101>", Toast.LENGTH_LONG).show();
                            return null;
                        } catch (IllegalAccessException e2) {
                            e2.printStackTrace();
                            Log.d("error", rowCount + "行目");
                            Toast.makeText(context, rowCount + "行目: 不正にファイルにアクセスしました<110>", Toast.LENGTH_LONG).show();
                            return null;
                        } catch (InstantiationException e3) {
                            e3.printStackTrace();
                            Log.d("error", rowCount + "行目");
                            Toast.makeText(context, rowCount + "行目: クラスをインスタンス化できません<111>", Toast.LENGTH_LONG).show();
                        }
                    }
                    state = 3;
                }
                else if(state > 1 && stl_line[0].equals("makeshape")){  //makeshapeで始まる行の引数リストを元に形状オブジェクトを制作し新Object3Dに代入(CreatableShapeの場合のみ使用)
                    try {
                        if(stl_line.length == 1){
                            newObject.setModel(newShapeClass.createShape3D(0));
                        }
                        else if (stl_line.length == 2) {
                            newObject.setModel(newShapeClass.createShape3D(Integer.parseInt(stl_line[1])));
                        } else if (stl_line.length == 3) {
                            newObject.setModel(newShapeClass.createShape3D(Integer.parseInt(stl_line[1]), Float.parseFloat(stl_line[2])));
                        } else if (stl_line.length == 4) {
                            newObject.setModel(newShapeClass.createShape3D(Integer.parseInt(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3])));
                        } else if (stl_line.length == 7) {
                            newObject.setModel(newShapeClass.createShape3D(Integer.parseInt(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]), Float.parseFloat(stl_line[4]), Integer.parseInt(stl_line[5]), Integer.parseInt(stl_line[6])));
                        }
                    }catch(NumberFormatException e7){
                        e7.printStackTrace();
                        Log.d("error", rowCount + "行目");
                        Toast.makeText(context, rowCount + "行目: 形状製作の引数に数値以外の文字があります<107>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state > 1 && stl_line[0].equals("readshape")){  //readshapeで始まる行の引数リストを元に形状オブジェクトを制作し新Object3Dに代入(StlModelのみ使用)
                    int shapeId = 0;
                        StlModel tmp = ((StlModel)newShapeClass);
                        tmp.setContext(context);
                        if(stl_line.length ==1) {
                            tmp.makeRandomModel();  //ファイル名指定がない場合はランダム生成
                        }else if(stl_line.length == 3) {
                            try {
                                shapeId = Integer.parseInt(stl_line[1]);
                            } catch (NumberFormatException e) {
                                shapeId =0;
                                e.printStackTrace();
                                Log.d("error", rowCount + "行目");
                                Toast.makeText(context, rowCount + "行目: 形状ID指令中に数値以外の文字があります<113>", Toast.LENGTH_LONG).show();
                            }
                            tmp.makeModel(context, stl_line[2]);  //ファイル名指定がある場合
                        }

                        newObject.setModel(tmp.createShape3D(shapeId));
                        newObject.model.setCcwState(GLES20.GL_TRIANGLES);
                }
                else if(state > 0 && stl_line[0].equals("aabb")){
                    try {
                        newObject.setAABB(Float.parseFloat(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]), Float.parseFloat(stl_line[4]), Float.parseFloat(stl_line[5]), Float.parseFloat(stl_line[6]));
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                        Toast.makeText(context, rowCount + "行目: AABB指令中に数値以外の文字があります<123>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state > 0 && newObject instanceof TexObject3D && stl_line[0].equals("texture")){  //テキスチャ生成&オフジェクトにセット
                    final int id = context.getResources().getIdentifier("@drawable/" + stl_line[1], null, context.getPackageName());
                    final Context con = context;
                    final Object3D obj = newObject;
                    final String[] str = stl_line;
                    surfaceView.queueEvent(new Runnable() {  //Texture の生成のみGLスレッドで実行する必要がある
                        public void run() {
                            int textureID = new Texture().addTexture(con, id); //Textureのstatic ArrayList へtextureを登録
                            ((TexObject3D) obj).setTexture(textureID);
                            if (str.length > 2 && str[2].equals("repeat")) {
                                Texture.setRepeartTexture(textureID, true);
                            }
                        }
                    });
                }
                else if(state > 0 && stl_line[0].equals("shader")){  //シェーダー読み込み
                    int programNumber =0;

                    for (String str : GLES.programNumber.keySet()) {
                        if(str.equals(stl_line[1])){
                            programNumber = GLES.programNumber.get(str);
                            break;
                        }
                    }

                    if(programNumber != 0) {
                        newObject.setShader(programNumber);
                        Log.d("message", "selected shaderNumber: " + programNumber);
                    }else{
                        Log.d("error", rowCount + "行目: ファイルから読み込めないシェーダーがあります<103>    stl_line[1]: " + stl_line[1]);
                        Toast.makeText(context , rowCount + "行目: ファイルから読み込めないシェーダーがあります<103>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state > 0 && stl_line[0].equals("rotate")){  //回転を設定
                    try {
                        newObject.setRotate(Integer.parseInt(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]), Float.parseFloat(stl_line[4]));
                    }catch(NumberFormatException e4){
                        e4.printStackTrace();
                        Log.d("error", rowCount + "行目");
                        Toast.makeText(context, rowCount + "行目: 回転指令中に数値以外の文字があります<104>", Toast.LENGTH_LONG).show();
                    }
                }
                if(state > 0 && stl_line[0].equals("translate")){  //移動を設定
                    try{
                        newObject.setTranslate(Float.parseFloat(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]));
                    }catch(NumberFormatException e5){
                        e5.printStackTrace();
                        Log.d("error", rowCount + "行目");
                        Toast.makeText(context, rowCount + "行目: 位置変更指令中に数値以外の文字があります<105>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state > 0 && stl_line[0].equals("scale")){  //スケールを設定
                    try{
                        newObject.setScale(Float.parseFloat(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]));
                    }catch(NumberFormatException e6){
                        e6.printStackTrace();
                        Log.d("error", rowCount + "行目");
                        Toast.makeText(context, rowCount + "行目: スケール指令中に数値以外の文字があります<106>", Toast.LENGTH_LONG).show();
                    }
                }
                if(state > 0 && stl_line[0].equals("color")){  //カラーを設定
                    try{
                        newObject.setColor(Float.parseFloat(stl_line[1]), Float.parseFloat(stl_line[2]), Float.parseFloat(stl_line[3]), Float.parseFloat(stl_line[4]));
                    }catch(NumberFormatException e7){
                        e7.printStackTrace();
                        Log.d("load error", rowCount + "行目");
                        Toast.makeText(context, rowCount + "行目: カラー指令中に数値以外の文字があります<107>", Toast.LENGTH_LONG).show();
                    }
                }
                else if(state > 0 && stl_line[0].equals("classend")){  //クラス読み込み完了＆リストにアッド
                    newObject.makeMatrix();
                    objectList.add(newObject);
                    classCount++;  //読み込み済みオブジェクト数カウントアップ
                    state = 0;
                    diplicatable = true;
                }
                rowCount++;  //読み込み行カウントアップ
            }
        }catch(IOException e){
            Log.d("error", rowCount + "行目");
            Toast.makeText(context, rowCount + "行目: IOException発生<109>", Toast.LENGTH_LONG).show();
        }
        try {
            reader.close();
        }catch(IOException e){
            if(reader != null)
                try {
                    reader.close();
                }catch(Exception ee){}
            e.printStackTrace();
            Toast.makeText(context, "ファイルクローズ時エラー<112>", Toast.LENGTH_LONG).show();
        }

        surfaceView = null;
        context = null;
        return objectList;
    }
}
