package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.Players.Premadonna;
import com.akiranagai.myapplication.collision.AABB;
import com.akiranagai.myapplication.collision.BoundingShape;
import com.akiranagai.myapplication.collision.Float3;
import com.akiranagai.myapplication.gamecontroller.GameManager;
import com.akiranagai.myapplication.gamecontroller.QuaterSpaceManager;

import java.nio.FloatBuffer;

public class Object3D{

    Shape3D model;
    int modelID;  //3D形状を表すモデルID
    protected float[] mMatrix=new float[16]; //モデル変換マトリックス
    int shaderSelectNumber = 1;  //シェーダー切換番号(デフォルトシェーダーは１)

    int rotateAngleValue;  //回転角度(degree)
    float[] rotateValues = new float[]{1f, 0f, 0f};
    protected float[] translateValues = new float[3];
    float[] scaleValues = new float[]{1f, 1f, 1f};
    float[] colorValues = new float[]{1f, 1f, 1f, 1f, 5f};
    float idcolor = 0f;  //パネル識別用のID　青要素へエンコードされている

    private boolean movable;  //可動オブジェクト
    private QuaterSpaceManager.QuaterySpace locationInSpace4;  //4分木空間での登録位置
    float[] barrierOrigin = new float[8];  //干渉チェック用(mMatrix を考慮しないModelMatrix上のオリジナルバウンディングボックス)
    protected BoundingShape barrier = null;  //mMatrix適用済み　バウンディングボックス
    protected float[] calcTranslate = new float[]{1,1,1};  //干渉チェック用一時　位置変数

    public Object3D(){

    }
    /**
     * This method is used to install into field3d
     */
    void putOn(){}

    /**
     * This method is used to get out from field3d
     */
    void getOut(){}

    /**
     * This method is called by field3D when premadonna incetance touched(collisioned) this object.
     * @param p
     */
    void beTouched(Premadonna p){}

    /**
     * This method is called by field3D when premadonna attacked by using its weapon and hit this object.
     * @param p
     */
    void beAttacked(Premadonna p){}

    public void setMovable(boolean bool){
        this.movable = bool;
    }
    public boolean isMovable(){
        return movable;
    }
    public void setLocationInSpace4(QuaterSpaceManager.QuaterySpace location){
        this.locationInSpace4 = location;
    }

    public void unRegistSpace4(){
        if(locationInSpace4 == null)return;
        locationInSpace4.unRegist(this);
    }

    public float[] getPosition(){
        return translateValues;
    }

    /**
     * 干渉チェック用　一時　位置変数を返す
     * @return
     */
    public float[] getCalcPosition(){
        return calcTranslate;
    }
    public void setCalcTranslate(float [] value){
        calcTranslate = value;
    }

    public float[] getMMatrix(){
        return mMatrix;
    }

    public FloatBuffer getVertexBuffer(){
        return model.vertexBuffer;
    }

    /**
     *
     * @param x 最小値側X
     * @param y 最小値側Y
     * @param z 最小値側Z
     * @param u 最大値側X
     * @param v 最大値側Y
     * @param w 最大値側Z
     */
    public void setAABB(float x, float y, float z, float u, float v, float w){
        barrier = new AABB(new Float3(x,y,z), new Float3(u,v,w));
        barrierOrigin[0] = x;
        barrierOrigin[1] = y;
        barrierOrigin[2] = z;
        barrierOrigin[3] = 1f;
        barrierOrigin[4] = u;
        barrierOrigin[5] = v;
        barrierOrigin[6] = w;
        barrierOrigin[7] = 1f;
    }
    public BoundingShape getBoundingShape(){
        return barrier;
    }

    private void generateBoundingBox(){
        float[] tempValue;
        float[] checkMaxValue = new float[4];
        float[] checkMinValue = new float[4];
        Shape3D shape = Shape3D.getShape3D(modelID);
        int counterMAX = shape.vertexBuffer.capacity()/3;
        tempValue = new float[3];
        shape.vertexBuffer.position(0);
        shape.vertexBuffer.get(checkMaxValue, 0,3);
        for(int i = 0; i<4; i++)
            checkMinValue[i] = checkMaxValue[i];
        float temp;

        shape.vertexBuffer.position(0);
        for(int i =0; i < counterMAX; i++){
            shape.vertexBuffer.get(tempValue, 0, 3);
            checkMaxValue[0] = checkMaxValue[0]<tempValue[0]?tempValue[0]:checkMaxValue[0];
            checkMaxValue[1] = checkMaxValue[1]<tempValue[1]?tempValue[1]:checkMaxValue[1];
            checkMaxValue[2] = checkMaxValue[2]<tempValue[2]?tempValue[2]:checkMaxValue[2];
            checkMinValue[0] = checkMinValue[0]>tempValue[0]?tempValue[0]:checkMinValue[0];
            checkMinValue[1] = checkMinValue[1]>tempValue[1]?tempValue[1]:checkMinValue[1];
            checkMinValue[2] = checkMinValue[2]>tempValue[2]?tempValue[2]:checkMinValue[2];
        }
        shape.vertexBuffer.position(0);
        setAABB(checkMinValue[0], checkMinValue[1], checkMinValue[2], checkMaxValue[0], checkMaxValue[1], checkMaxValue[2]);

        Log.d("message", "makeBoundignShapge: ");
    }
    /*
    mMatrixを考慮してBoundingBoxを生成
    barrier.set3DlocationまでしてしまうためmakeBarrier()呼び出し不要(makeBarrier()呼び出すとmMatrixが2重にかかる)
     */
    private void calcGenerateBoundingBox(){
        float[] tempValue;
        float[] checkMaxValue = new float[4];
        float[] checkMinValue = new float[4];
        Shape3D shape = Shape3D.getShape3D(modelID);
        int counterMAX = shape.vertexBuffer.capacity()/3;
        tempValue = new float[4];
        shape.vertexBuffer.position(0);
        shape.vertexBuffer.get(checkMaxValue, 0,3);
        checkMaxValue[3] = 1;
        Matrix.multiplyMV(checkMaxValue, 0, mMatrix, 0, checkMaxValue,0);
        for(int i = 0; i<3; i++)
            checkMinValue[i] = checkMaxValue[i];

        shape.vertexBuffer.position(0);
        for(int i =0; i < counterMAX; i++){
            shape.vertexBuffer.get(tempValue, 0, 3);
            tempValue[3] = 1;
            Matrix.multiplyMV(tempValue, 0, mMatrix, 0, tempValue, 0);
            checkMaxValue[0] = checkMaxValue[0]<tempValue[0]?tempValue[0]:checkMaxValue[0];
            checkMaxValue[1] = checkMaxValue[1]<tempValue[1]?tempValue[1]:checkMaxValue[1];
            checkMaxValue[2] = checkMaxValue[2]<tempValue[2]?tempValue[2]:checkMaxValue[2];
            checkMinValue[0] = checkMinValue[0]>tempValue[0]?tempValue[0]:checkMinValue[0];
            checkMinValue[1] = checkMinValue[1]>tempValue[1]?tempValue[1]:checkMinValue[1];
            checkMinValue[2] = checkMinValue[2]>tempValue[2]?tempValue[2]:checkMinValue[2];
        }
        shape.vertexBuffer.position(0);
        setAABB(checkMinValue[0], checkMinValue[1], checkMinValue[2], checkMaxValue[0], checkMaxValue[1], checkMaxValue[2]);

        //barrier.set3DLocation(checkMinValue, checkMaxValue);
        Log.d("message", "makeBoundignShapge: ");
    }
    /**
     * mMatrixを反映してオブジェクトのバウンディングボックスを生成
     * Bボックス未設定の場合、vertexBufferから自動生成
     */
    public void reCalcBoundingShape(){
        if(barrier == null){  //OriginalBoundingBoxがまだ設定されていなければShape3D#VertexBufferを元に各軸最大値・最小値でAABBを作る
            generateBoundingBox();
        }
        Matrix.setIdentityM(mMatrix, 0);
        Matrix.translateM(mMatrix, 0, calcTranslate[0], calcTranslate[1], calcTranslate[2]);
        makeBarrier();
    }

    public void reCalcBoundingShapeWithScale(){
        if(barrier == null){  //OriginalBoundingBoxがまだ設定されていなければShape3D#VertexBufferを元に各軸最大値・最小値でAABBを作る
            generateBoundingBox();
        }
        Matrix.setIdentityM(mMatrix,0);
        Matrix.translateM(mMatrix, 0, calcTranslate[0], calcTranslate[1], calcTranslate[2]);
        Matrix.scaleM(mMatrix, 0, scaleValues[0], scaleValues[1], scaleValues[2]);
        makeBarrier();
    }

    /*
    mMatrixを考慮してBoundingBoxを生成
     */
    public void fullCalcBoundingShape(){
        Matrix.setIdentityM(mMatrix,0);
        Matrix.rotateM(mMatrix,0,rotateAngleValue, rotateValues[0],rotateValues[1],rotateValues[2]);
        Matrix.translateM(mMatrix, 0, calcTranslate[0], calcTranslate[1], calcTranslate[2]);
        Matrix.scaleM(mMatrix, 0, scaleValues[0], scaleValues[1], scaleValues[2]);
        if(barrier == null) {  //OriginalBoundingBoxがまだ設定されていなければShape3D#VertexBufferを元に各軸最大値・最小値でAABBを作る
            calcGenerateBoundingBox();
        }
    }

    private void makeBarrier(){
        float[] checkMaxValue = new float[4];
        float[] checkMinValue = new float[4];
        Matrix.multiplyMV(checkMinValue, 0, mMatrix, 0, barrierOrigin, 0);  //AABB(barrier)位置更新 barrier v1
        Matrix.multiplyMV(checkMaxValue, 0, mMatrix, 0, barrierOrigin, 4);  //barrier v2
        Log.d("messagea", "barrierOrigin: 0: " + barrierOrigin[0] + "  1: " + barrierOrigin[1] + "  2: " + barrierOrigin[2] + "  3: " + barrierOrigin[3] + "  4: " + barrierOrigin[4] + "  5: " + barrierOrigin[5] + "  6: " + barrierOrigin[6]);
        barrier.set3DLocation(checkMinValue, checkMaxValue);
        translateValues[0] = calcTranslate[0];
        translateValues[1] = calcTranslate[1];
        translateValues[2] = calcTranslate[2];
    }

    void immediateMakeMatrix(){
        Matrix.setIdentityM(mMatrix, 0);
        Matrix.translateM(mMatrix, 0, translateValues[0], translateValues[1], translateValues[2]);
    }

    /**
     * 事前にマトリックスを合成
     */
    public void makeMatrix(){
        GameManager.surfaceView.queueEvent(new Runnable() {
            public void run() {
                Matrix.setIdentityM(mMatrix, 0);
                Matrix.rotateM(mMatrix, 0, rotateAngleValue, rotateValues[0], rotateValues[1], rotateValues[2]);
                Matrix.translateM(mMatrix, 0, translateValues[0], translateValues[1], translateValues[2]);
                Matrix.scaleM(mMatrix, 0, scaleValues[0], scaleValues[1], scaleValues[2]);
            }
        });
    }

    public void setMatrix(float[] matrix){
        mMatrix = matrix;
    }

    public void calcBoundingShapeByMatrix(float[] matrix){
        mMatrix = matrix;
        if(barrier == null){  //OriginalBoundingBoxがまだ設定されていなければShape3D#VertexBufferを元に各軸最大値・最小値でAABBを作る
            calcGenerateBoundingBox();
        }
    }

    /**
     * This method may return null.
     * @param modelID
     * @return
     */
    public Object3D setModel(int modelID){
        if(modelID == 0)return null;
        this.modelID = modelID;
        model = Shape3D.getShape3D(modelID);
        if(model !=null)
        return this;
        return null;
    }
    public Object3D setRotate(int r, float x, float y, float z){
        rotateAngleValue = r;
        rotateValues[0] = x;
        rotateValues[1] = y;
        rotateValues[2] = z;
        return this;
    }
    public Object3D setTranslate(float x, float y, float z){
        translateValues[0] = x;
        translateValues[1] = y;
        translateValues[2] = z;
        calcTranslate[0] = x;
        calcTranslate[1] =y;
        calcTranslate[2] =z;
        return this;
    }

    public Object3D setScale(float x, float y, float z){
        scaleValues[0] = x;
        scaleValues[1] = y;
        scaleValues[2] = z;
        return this;
    }

    public Object3D setColor(float r, float g, float b, float a){
        colorValues[0] = r;
        colorValues[1] = g;
        colorValues[2] = b;
        colorValues[3] = a;
        return this;
    }

    public Object3D setColor(float r, float g, float b, float a, float s){
        setColor(r,g,b,a);
        colorValues[4] = s;
        return this;
    }

    public Object3D setShininess(float s){
        colorValues[4] = s;
        return this;
    }

    public Object3D setShader(int number){
        shaderSelectNumber = number;
        return this;
    }

    public Object3D setIDColor(float color){
        this.idcolor = color;
        return this;
    }

    public void render() {
            GLES.selectProgram(shaderSelectNumber);
            GLES.updateMatrix(mMatrix);
            draw(colorValues[0], colorValues[1], colorValues[2], colorValues[3], colorValues[4]);
    }

    public void backRender(){
        GLES.selectProgram(GLES.SP_SimpleTexture);
        GLES.updateMatrix(mMatrix);
        draw(1f,1f,idcolor,1f,1f);
    }

    public void wireRender(){
        GLES.selectProgram(shaderSelectNumber);
        GLES.updateMatrix(mMatrix);
        drawline(colorValues[0], colorValues[1], colorValues[2], colorValues[3]);
    }

    public void draw(float r,float g,float b,float a, float shininess){
        //頂点点列

        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, model.vertexBuffer);
        int k = 0;

        if (GLES.checkLiting()) {
            //頂点での法線ベクトル
            GLES20.glVertexAttribPointer(GLES.normalHandle, 3,
                    GLES20.GL_FLOAT, false, 0, model.normalBuffer);
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
        model.indexBuffer.position(0);
        GLES20.glDrawElements(model.stripOrder, model.nIndexs, GLES20.GL_UNSIGNED_SHORT, model.indexBuffer);
    }

    public void drawline(float r,float g,float b,float a){
        //頂点点列
        GLES20.glVertexAttribPointer(GLES.positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, model.vertexBuffer);

        //shadingを使わない時に使う単色の設定 (r, g, b,a)
        GLES20.glUniform4f(GLES.objectColorHandle, r, g, b, a);

        model.indexBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_LINE_STRIP, model.nIndexs, GLES20.GL_UNSIGNED_SHORT, model.indexBuffer);
    }

    /**
     * 3Dオブジェクト　ディープコピー用メソッド
     * modelはIDのコピーなのでシャロー
     * stripOrder のコピーは無し
     * @param object
     * @return
     */
    Object3D copyAttributes(Object3D object){
        return object
                .setModel(this.modelID)
                .setColor(this.colorValues[0], this.colorValues[1], this.colorValues[2], this.colorValues[3], this.colorValues[4])
                .setRotate(this.rotateAngleValue, this.rotateValues[0], this.rotateValues[1], this.rotateValues[2])
                .setShader(this.shaderSelectNumber)
                .setTranslate(this.translateValues[0], this.translateValues[1], this.translateValues[2])
                .setScale(this.scaleValues[0], this.scaleValues[1], this.scaleValues[2]);
    }
    @Override
    public Object3D clone(){
        return copyAttributes(new Object3D());
    }

}
