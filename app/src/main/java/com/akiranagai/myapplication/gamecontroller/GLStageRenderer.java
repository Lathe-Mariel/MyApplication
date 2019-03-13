package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Display;
import android.view.WindowManager;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.object3d.InnerHalfSphere;
import com.akiranagai.myapplication.object3d.Premadonna;
import com.akiranagai.myapplication.PointSprite;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.StringTextureGenerator;
import com.akiranagai.myapplication.object3d.TextPanel;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.TimeZone;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLStageRenderer implements GLSurfaceView.Renderer {
    GameManager manager;

    private final Context mContext;
    private Premadonna prema;
    static final int PREMA_STEPBACK = -10;

    private boolean validProgram=false; //シェーダプログラムが有効
    private float aspect;//アスペクト比

    float viewlength0 = 5.0f; //視点距離
    float viewlength = 5.0f; //視点距離
    float viewingangle0 = 45f; //視野角（画面長手方向に対応する視野角[deg]）
    float viewingangle = 45f; //視野角（画面長手方向に対応する視野角[deg]）
    float RotAngle=0f;       //2本指回転量
    float AngleHor=0f;       //横移動角（2本指）[deg]
    float AngleVer=0f;       //縦移動角（2本指）[deg]

    //-------------------------------------------------------------------------------------------------------------------
    //float cameraCenterX=0, cameraCenterZ=-8;//  カメラ中心座標
    //float viewArrowX= 0, viewArrowZ=-4f;//視線方向（ベクトル）

    //視点変更テスト変数
    float alph=-2.f,beta=0f;
    final float MAX_alph = 0;
    final float MIN_alph = -3.5f;

    //光源の座標　x,y,z
    private  float[] LightPos={65f,40f,0f,1f};//x,y,z,1
    //光源の環境光
    private float[] LightAmb={.4f,.4f,.4f,1f}; //r,g,b,a
    //光源の乱反射光
    private float[] LightDif={0.5f,0.5f,0.5f,1f}; //r,g,b,a
    //光源の鏡面反射反射光
    private float[] LightSpc={.6f,.6f,.6f,1f}; //r,g,b,a

    private  float[] LightPos2={0f,50f,0f,1f};//x,y,z,1
    private float[] LightAmb2={.1f,.1f,.1f,1f}; //r,g,b,a
    private float[] LightDif2={0.1f,0.1f,0.1f,1f}; //r,g,b,a
    private float[] LightSpc2={.1f,.1f,.1f,1f}; //r,g,b,a
    private float light2Angle = 0;

    //変換マトリックス
    private  float[] pMatrix=new float[16]; //プロジェクション変換マトリックス
    private  float[] mMatrix=new float[16]; //モデル変換マトリックス
    private  float[] cMatrix=new float[16]; //カメラビュー変換マトリックス

    //モデル座標系の原点

    private StringTextureGenerator dashBoardText;
    private TexObject3D timerPanel;
    private boolean stageDataReady;

    private int outerTextureID;

    //オブジェクトリスト
    private ArrayList<Object3D> objectList;  //通常の表示オブジェクトリスト
    private ArrayList<Object3D> frontObjectList;  //最前面表示のオブジェクトリスト

    //Outer Sphere 外壁半球
    private InnerHalfSphere outerSphere;

    public GLStageRenderer(final Context context, GameManager manager) {
        mContext = context;
        this.manager = manager;
        Point point = new Point();
        Display display = ((WindowManager)(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        display.getSize(point);
        objectList = new ArrayList<Object3D>();
        frontObjectList = new ArrayList<Object3D>();
    }

//表示するオブジェクトを追加
    public void addAllObjects(final ArrayList<Object3D> objects){
        manager.surfaceView.queueEvent(new Runnable() {
            public void run(){
                objectList.clear();
                objectList.addAll(objects);
            }
        });
    }
    public void removeAllObjects(){
        manager.surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                objectList.clear();
            }
        });
    }

    public void addFrontObject(final Object3D object) {
        manager.surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                frontObjectList.add(object);
            }
        });
    }
    public void removeFrontObject(final Object3D object) {
        manager.surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                frontObjectList.remove(object);
            }
        });
    }

    public void setBackTexture(int texture){
        outerTextureID = texture;
    }

    public Object3D getObject(int number){
        return objectList.get(number);
    }

    //サーフェイス生成時に呼ばれる
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //プログラムの生成
        outerTextureID = new Texture().addTexture(mContext,R.drawable.clouds);
        validProgram = GLES.makeProgram();
        defaultFrameInit();
        manager.callBackByRenderer();
        prema = manager.prema;

        dashBoardText = new StringTextureGenerator("", 50f,Color.parseColor("#0066BBFF"), Color.WHITE);
        dashBoardText.setAlfa(210,180);
        timerPanel = new TexObject3D();
        timerPanel.setShader(GLES.SP_SimpleTexture);
        timerPanel.setModel(new TextPanel().createShape3D(0, 0.7f, 0.2f));
        timerPanel.setRotate(90, 1,0,0);
        timerPanel.setTranslate(-0.55f, 0.99f, -0.95f);
        timerPanel.makeMatrix();
        timerPanel.setTexture(dashBoardText.getTextureId());

        outerSphere = new InnerHalfSphere(360f,0f,90f,72,36,false); //zx平面より+y側にある半球を定義, 内向きが表面の半球 innerface of the half sphere is a front face
    }

    public void defaultFrameInit(){  //画面用フレームバッファ初期化
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glFrontFace(GLES20.GL_CCW); //表面のvertexのindex番号はCCWで登録
        GLES20.glCullFace(GLES20.GL_BACK); //裏面は表示しない
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        //光源の設定
        GLES.putLightAttribute(LightAmb, LightDif, LightSpc);
        GLES.putLightAttribute2(LightAmb2, LightDif2, LightSpc2);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }
    public void screenViewInit(){
        //Screen Clear
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT |
                GLES20.GL_DEPTH_BUFFER_BIT);

        GLES.gluPerspective(pMatrix,
                viewingangle,  //Y方向の画角
                aspect, //アスペクト比
                0.001f,   //ニアクリップ　　　z=-0.001から
                600.0f);//ファークリップ
        GLES.setPMatrix(pMatrix);

        float[] c1Matrix=new float[16]; //カメラ視点変換マトリックス作成用
        float cameraDeltaX = (float)(PREMA_STEPBACK * Math.sin(prema.getViewDirection()));  //prema の視線方向反対位置にカメラを変位
        float cameraDeltaZ = (float)(PREMA_STEPBACK * Math.cos(prema.getViewDirection()));

        Matrix.setLookAtM(c1Matrix, 0,
                (prema.getX()+cameraDeltaX),  //カメラの視点 x
                -2f, //カメラの視点 y
                (prema.getZ() + cameraDeltaZ),  //カメラの視点 z
                prema.getViewArrowX()+cameraDeltaX+beta, alph, prema.getViewArrowZ()+cameraDeltaZ, //カメラの視線方向の代表点
                0.0f, 1.0f, 0.0f);//カメラの上方向

        GLES.setCMatrix(c1Matrix);
    }

    public void setStageDataReady(boolean ready){
        this.stageDataReady = ready;
    }

    //This method will be called when screen is changed.
    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        //view port translation
        GLES20.glViewport(0,0,w,h);
        aspect=(float)w/(float)h;
    }
    //毎フレーム描画時
    @Override
    public synchronized void onDrawFrame(GL10 glUnused) {
        if (!stageDataReady) return;

        screenViewInit();

        //cMatrixをセットしてから光源位置をセット1&2
        GLES.setLightPosition(LightPos);
        light2Angle = (light2Angle+0.02f) % 6.28f;
        LightPos2[0] =(float)(20 * Math.cos(light2Angle));
        LightPos2[2] = (float)(20 * Math.sin(light2Angle));
        GLES.setLightPosition2(LightPos2);

        /*
        if(ps != null) {
            GLES.selectProgram(GLES.SP_PointSprite);
            Matrix.setIdentityM(mMatrix, 0);
            GLES.updateMatrix(mMatrix);
            ps.draw(1f,0f,0f,1f,10);
        }*/

        prema.render();

        //登録されたオブジェクト群を描画
        try {
            for (Iterator<Object3D> i = objectList.iterator(); i.hasNext(); ) {
                Object3D currentObject = i.next();
                currentObject.render();
            }
        }catch(ConcurrentModificationException e){
            e.printStackTrace();
        }

        //外壁半球を表示 show the floor panel
        GLES20.glFrontFace(GLES20.GL_CW);
        GLES.selectProgram(GLES.SP_SimpleTexture);
        Matrix.setIdentityM(mMatrix, 0);
        Matrix.translateM(mMatrix, 0, 0f, -15f, 0f);
        Matrix.scaleM(mMatrix, 0, 300f, 120f, 300f);
        GLES.updateMatrix(mMatrix);//現在の変換行列をシェーダに指定2
        Texture.setTexture(outerTextureID);
        outerSphere.draw(1f, 1f, 1f, 0.8f, 5f);
        GLES20.glFrontFace(GLES20.GL_CCW);

        //ダッシュボードパネルを表示 show dashboard panel(常時最前面表示のため無変換)
        Matrix.setIdentityM(pMatrix, 0);
        GLES.setPMatrix(pMatrix);
        Matrix.setIdentityM(cMatrix, 0);
        GLES.setCMatrix(cMatrix);
        //現在の時間でテキスチャー更新

        String time = manager.getCurrentTime();
        timerPanel.setTexture(dashBoardText.makeStringTexture(time, 50f,Color.parseColor("#3399DDFF"), Color.WHITE));
        timerPanel.render();

        for(int i = 0; i< frontObjectList.size(); i++){  //最前面表示オブジェクトリスト
            frontObjectList.get(i).render();
        }
    }

    public void putToast(int textureID, final int time){
        final TexObject3D newMessage = new TexObject3D();
        newMessage.setTexture(textureID);
        newMessage.setShader(GLES.SP_SimpleTexture);
        newMessage.setModel(new Toast().createShape3D(0, 1.2f, 0.6f));
        //newMessage.setRotate(90, 1,0,0);
        newMessage.setTranslate(0f, 0f, -0.95f);
        newMessage.makeMatrix();
        Thread removeThread = new Thread(new Runnable(){
            public void run(){
                addFrontObject(newMessage);
                try{
                    Thread.sleep(time);
                }catch(Exception e){
                    e.printStackTrace();
                }
                removeFrontObject(newMessage);
            }
        });
        removeThread.start();
    }

    public void putToast2(int textureID, final int time){
        final TexObject3D newMessage = new TexObject3D();
        newMessage.setTexture(textureID);
        newMessage.setShader(GLES.SP_SimpleTexture);
        newMessage.setModel(new Toast().createShape3D(0, 1.4f, 0.4f));
        //newMessage.setRotate(90, 1,0,0);
        newMessage.setTranslate(0f, -0.8f, -0.95f);
        newMessage.makeMatrix();
        Thread removeThread = new Thread(new Runnable() {
            public void run() {
                addFrontObject(newMessage);
                try {
                    Thread.sleep(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removeFrontObject(newMessage);
            }
        });
        removeThread.start();
    }

    PointSprite ps;
    public void testSprite(FloatBuffer fb){
        ByteBuffer bb = ByteBuffer.allocateDirect(fb.capacity()*4);       //!!!!!!!!!!
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer bufferPoints = bb.asFloatBuffer();
        fb.position(0);
        bufferPoints.position(0);
        while(fb.hasRemaining()){
            bufferPoints.put(fb.get());
        }
        bb.position(0);
        bufferPoints.position(0);
        //float[] f = new float[fb.capacity()];
        //fb.get(f, 0, f.length);
          //      ps.setVertexs(f1);
        ps = new PointSprite(bufferPoints);
    }

    //一本指でのスクロール
    public void setScrollValue(float DeltaX, float DeltaY) {
        beta += DeltaX * 0.005;
        if (6.28f < beta) beta = 6.28f;
        if (beta < -6.28) beta = -6.28f;
        alph -= DeltaY * 0.002;
        if (MAX_alph < alph) alph = MAX_alph;
        if (alph < MIN_alph) alph = MIN_alph;
    }

    //２本指でのスクロール
    private float Scroll2fg[] = {0f, 0f}; //２本指のドラッグ[deg]
    public void setScroll2Value(float Delta2X, float Delta2Y) {
        Scroll2fg[0] += Delta2X * 0.05; //[deg]
        Scroll2fg[1] += Delta2Y * 0.05; //[deg]
        AngleHor = Scroll2fg[0];
        AngleVer = Scroll2fg[1];
    }

    public void setScroll3Value(float Delta2X, float Delta2Y) {

    }

    //２本指でのピンチイン・ピンチアウト
    private float My_ScaleFactor = 1.0f;
    public void setPinch2(float factor) {
        My_ScaleFactor *= factor;
        viewlength = viewlength0 / My_ScaleFactor;
    }

    //３本指でのピンチイン・ピンチアウト
    private float My_ScaleFactor3 = 1.0f;
    public void setPinch3(float factor) {
        My_ScaleFactor3 *= factor;
        viewingangle = viewingangle0 / My_ScaleFactor3;
    }

    //２本指でのローテート
    private float My_RotAngle =0f;
    public void setRot2Angle(float Rotation) {
        My_RotAngle += Rotation;
        RotAngle = My_RotAngle;
    }

    public void setRot3Angle(float Rotation) {

    }
}
