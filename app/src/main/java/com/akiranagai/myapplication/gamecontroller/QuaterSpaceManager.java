package com.akiranagai.myapplication.gamecontroller;

import android.graphics.Point;
import android.util.Log;

import com.akiranagai.myapplication.collision.AABB;
import com.akiranagai.myapplication.collision.Float3;
import com.akiranagai.myapplication.object3d.Object3D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class QuaterSpaceManager {
    //L0 = 1, L1 = 4, L2 = 16, L3 = 64, L4 = 256
    private final float RESOLUTION = 16f;  //16 * 16 = 256
    //配列番号 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37 ...
    //レベル別 0| 0, 1, 2, 3| 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15| 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16 ...
    //         0|----L1-----|-------------------------L2--------------------|------------------------------------L3------------
    private final static int[] LEVEL_FIRST_NUMBER = {0, 1, 5, 21, 85};  //各空間レベルの配列バイアス値
    private QuaterySpace[] quateryTree = new QuaterySpace[341];  //85+256
    private float divX, divY;
    private int biasX, biasY;  //フィールド全長距離の半分

    /**
     * 可動オブジェクトのリスト(下位のスペースに配置されたオブジェクトと総当たりで干渉チェックする)
     * 上位のスペースに戻る際にカレントスペースのオブジェクトは削除される
     */
    private Stack<Object3D>  movableObjectStack = new Stack<>();
    private Stack<Object3D> staticObjectStack = new Stack<>();

    public QuaterSpaceManager(){
        quateryTree[0] = new QuaterySpace();
    }  //ルートスペースのみ生成

    /**
     * 各軸(x,y)の分解能を設定 < 実距離 / 分割数 >
     * @param x x軸方向フィールド距離（全長)
     * @param y y軸方向フィールド距離（全長)
     */
    public void setSize(int x, int y){
        divX = x / RESOLUTION;
        divY = y / RESOLUTION;
        biasX = x /2;
        biasY = y /2;
        //Log.d("message", "setSize( " + x +" , " + y + " )");
    }

    public void registObject3D(Object3D newObject){
        //Log.d("message colli", "registObject3D");
        newObject.unRegistSpace4();
        float[] p1 = new float[2];
        float[] p2 = new float[2];
        newObject.getBoundingShape().acquire2DLocation(p1,p2);  //p1 p2 に位置を取得
        p1[0] = (int)((p1[0]+biasX) / divX);  //グリッド内での現在位置を算出
        p1[1] = (int)((p1[1]+biasY) / divY);
        p2[0] = (int)((p2[0]+biasX) / divX);
        p2[1] = (int)((p2[1]+biasY) / divY);
        int location_P1 = get2DMortonNumber((int)p1[0], (int)p1[1]);
        //Log.d("message colli", "registObject3D    MortonNumber x: " + p1[0] + "  y: " + p1[1]);
        //Log.d("messagea", "location p1: " + location_P1);
        if(p1[0] == p2[0] && p1[1] == p2[1]){  //最もレベルの深いグリッドに収まるなら
            registLocation(location_P1, 4, newObject);
            return;
        }
        int location_P2 = get2DMortonNumber((int)p2[0], (int)p2[1]);
        //Log.d("messagea", "location p1: " + location_P1 + "    location p2: " + location_P2);
        int xor = location_P1 ^ location_P2;
        int shift = 1;
        for(int i = 1; i< 5; i++){
            if((xor & 3) > 0) shift=i;
            xor >>= 2;
        }
        shift *= 2;
        location_P2 >>= shift;  //レベル内の空間位置
        switch(shift){  //空間レベル取得
            case 2:
                location_P1 = 3;
                break;
            case 4:
                location_P1 = 2;
                break;
            case 6:
                location_P1 = 1;
                break;
            case 8:
                location_P1 = 0;
                break;
            default:
                location_P1 = 4;
        }
        registLocation(location_P2, location_P1, newObject);
        //printAllArray();
    }

    public void printAllArray(){
        for(int i =0; i< quateryTree.length; i++){
            if(quateryTree[i] != null)
                Log.d("message", "quateryTree[" + i + "]: " + quateryTree[i].movingObjectList+quateryTree[i].obstacleList);
        }
    }
    /**
     * オブジェクトを空間へ登録
     * @param location 空間レベル内の通し空間番号
     * @param spaceLevel 空間レベル L0=ルート
     * @param object 登録する3Dオブジェクト
     */
    private void registLocation(int location, int spaceLevel, Object3D object){
        //Log.d("messageb", "registLocation    location: " + location + "  in current level    object:" + object);
        int arrayLocation = location + LEVEL_FIRST_NUMBER[spaceLevel];  //配列上の位置
        //Log.d("messagea", "arrayLocation: " + arrayLocation);
        if(quateryTree[arrayLocation] == null){
            quateryTree[arrayLocation] = new QuaterySpace(arrayLocation);
        }
        if(object.isMovable())
            quateryTree[arrayLocation].movingObjectList.add(object);
        else
            quateryTree[arrayLocation].obstacleList.add(object);
        object.setLocationInSpace4(quateryTree[arrayLocation]);
    }
    private int get2DMortonNumber(int x, int y){
        return bitSeparate32(x)| (bitSeparate32(y) << 1);
    }
    private int bitSeparate32(int number){
        number = (number|(number<<8)) & 0x00ff00ff;
        number = (number|(number<<4)) & 0x0f0f0f0f;
        number = (number|(number<<2)) & 0x33333333;
        return (number|(number<<1)) & 0x55555555;
    }

    public void checkCollision(){
        //Log.d("message", "checkCollision()");
        checkCollision(quateryTree[0], 0);
    }

    public void checkCollision(QuaterySpace currentSpace, final int arrayIndex){  //レベル空間の総オブジェクトリスト（obstacle + movable + stack)を作る
        //Log.d("messageb", "<checkCollision>    currentSpace: " + arrayIndex + "    obstacles: " + currentSpace.obstacleList.size());

        //カレント動 vs カレント動  &  カレント動 vs カレント静  &  カレント動 vs スタック静  &  カレント動 vs スタック動
        for(int i = 0; i < currentSpace.movingObjectList.size(); i++) {
            Object3D movingObj = currentSpace.movingObjectList.get(i);
            for (int k = i; k < currentSpace.movingObjectList.size(); k++) {  //カレント動 vs カレント動
                currentSpace.movingObjectList.get(k).getBoundingShape().collideVsSphere(movingObj);
            }
            for(int k = 0; k < currentSpace.obstacleList.size(); k++){  //カレント動 vs カレント静
                currentSpace.obstacleList.get(k).getBoundingShape().collideVsSphere(movingObj);
            }
            for (int k = 0; k < staticObjectStack.size(); k++) {  //カレント動 vs スタック静
                staticObjectStack.get(k).getBoundingShape().collideVsSphere(movingObj);
            }
            for(int k = 0; k < movableObjectStack.size(); k++){  //カレント動 vs スタック動
                movableObjectStack.get(k).getBoundingShape().collideVsSphere(movingObj);
            }
        }
        //スタック動 vs カレント静
        for(int i = 0; i < movableObjectStack.size(); i++){
            Object3D movingObj = movableObjectStack.get(i);
            for(int k = 0; k < currentSpace.obstacleList.size(); k++){
                currentSpace.obstacleList.get(k).getBoundingShape().collideVsSphere(movingObj);
            }
        }
        //スタックへカレントオブジェクトを格納
        for(int i =0; i < currentSpace.obstacleList.size(); i++){
            staticObjectStack.push(currentSpace.obstacleList.get(i));
        }
        for(int i =0; i< currentSpace.movingObjectList.size(); i++){
            movableObjectStack.push(currentSpace.movingObjectList.get(i));
        }

        //Log.d("messageb", "checkCollision    obstacles: " + currentSpace.obstacleList.size());
        //Log.d("messageb", "checkCollision    movingObj: " + currentSpace.movingObjectList.size());
        //Log.d("messageb", "checkCollision    objStack : " + movableObjectStack.size());

        int currentIndex = arrayIndex*4 +1;
        if(currentIndex < 338){
        for(int i = 0; i < 4; i++){  //子　がある場合は再起呼び出し
            if(quateryTree[currentIndex+i] != null){
                checkCollision(quateryTree[currentIndex+i], currentIndex+i);
            }
        }}
        for(int i = 0; i < currentSpace.movingObjectList.size(); i++){  //スタックから このレベルのmovableオブジェクト取り出し
            movableObjectStack.pop();
        }
        for(int i = 0; i < currentSpace.obstacleList.size(); i++){
            staticObjectStack.pop();
        }
        //Log.d("messageb", "return from: " + currentIndex);
    }

    public class QuaterySpace{
        //QuaterySpace[] childHolder;
        ArrayList<Object3D> obstacleList;  //動かない 3D Object List
        ArrayList<Object3D> movingObjectList;  //可動 3D Object List

        QuaterySpace(){
            //childHolder = new QuaterySpace[4];
            obstacleList = new ArrayList<>();
            movingObjectList = new ArrayList<>();
        }
        QuaterySpace(int arrayNumber){
            this();
            if(quateryTree[(arrayNumber-1)/4] == null){
                quateryTree[(arrayNumber-1)/4] = new QuaterySpace((arrayNumber-1)/4);
            }
        }
        public synchronized void unRegist(Object3D deleteObject){
            if(deleteObject.isMovable())
                movingObjectList.remove(deleteObject);
            else
                obstacleList.remove(deleteObject);
        }
    }
}