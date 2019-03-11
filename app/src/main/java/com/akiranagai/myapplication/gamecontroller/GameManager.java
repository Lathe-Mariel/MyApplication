package com.akiranagai.myapplication.gamecontroller;

import android.app.Activity;
import android.util.Log;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.Players.Cat;
import com.akiranagai.myapplication.Players.Premadonna;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.StageFileReader;
import com.akiranagai.myapplication.object3d.StlModel;

import java.util.ArrayList;

public class GameManager {
    public Activity activity;  //呼び出し元Activityを保持
    public GLStageRenderer renderer;
    Field3D2 field;  //GameManager生成
    ScreenInput sInput;  //GameManager生成
    public static MyGLSurfaceView surfaceView;  //GLActivity生成
    Premadonna prema;  //GameManager生成
    QuaterSpaceManager qsManager;  //GameManager生成

    private Object3D questionObject;
    int questionObjectKey;
    private StageConstructions stageConstructions = null;

    private StageRoopTimer timer;
    private int currentStage = 1;  //プレイ中の面(ステージ)
    static int answerAlphabet = -1;  //ゲームの答えのアルファベット

    boolean alwaysDrawCross;

    public static int CLEAR_ID;

    GameManager(Activity activity) {
        this.activity = activity;
        sInput = new ScreenInput(this);
    }

    public void setCurrentStage(int stageNumber){
        this.currentStage = stageNumber;
    }

    public void setRenderer(GLStageRenderer renderer) {
        this.renderer = renderer;
    }

    public QuaterSpaceManager getQuaterSpaceManager() {
        return qsManager;
    }

    public static void setAnswer(int alphabet) {
        answerAlphabet = alphabet;
    }

    public static int getSnaswer() {
        return answerAlphabet;
    }

    void init() {
        sInput.setDrawCrossState(alwaysDrawCross);
        Log.d("messagee", "ALWAYS_DRAW_CROSS: " + alwaysDrawCross);
    }

    /**
     * GLSurfaceView.Renderer#onSurfaceCreatedからコールバックされる
     */
    void callBackByRenderer() {
        makeStage(currentStage);
    }

    void makeStage(int stage) {
        CLEAR_ID = new Texture().addTexture(activity.getBaseContext(), R.drawable.stageclear);

        field = new Field3D2(this);
        sInput.setField(field);  //このメソッドでmanager.rendererも渡す

        qsManager = new QuaterSpaceManager();  //干渉チェックマネージャー
        qsManager.setSize(100, 100);
        field.qsManager = qsManager;

        prema = new Cat(this);  //Prema を生成、　StageConstructionの中でPremaの位置設定の可能性があるため、その前に生成する必要あり
        ((Cat) prema).init();
        prema.setRenderer(renderer);
        generateStageFactors(stage);  //ステージデータ読み込み
        makeNewQuestion();  //クイズの問題を生成(Alphabet 1個)

        field.sendObjectsApparenceData(renderer);  //field 経由で rendererへも表示オブジェクト
        field.prema = prema;

        renderer.setStageDataReady(true);  //Renderer#onDrawFrame OpenGL描画ループスタート

        stageConstructions.startStage();  //各ステージの独自スタート処理
        timer = new StageRoopTimer(25);
        timer.start();
    }

    /**
     * 指定されたファイルからオブジェクトを読み込み
     *
     * @param fileName
     */
    private void readObjectFromFile(String fileName) {
        // new Thread(new Runnable() {
        //          public void run() {
        ArrayList<Object3D> list0;
        list0 = StageFileReader.makeObjects(activity.getBaseContext(), surfaceView, fileName);  //ステージに追加するオブジェクト
        field.putAllObjects(list0);
        //        }
        //  }).start();
    }

    private GLStageRenderer generateStageFactors(int stageNumber) {
        if (stageNumber == 0) {
            stageConstructions = new ExplainStageConstructions(this);
        } else if (stageNumber == 1) {
            readObjectFromFile("stagea.txt");
            stageConstructions = new DefaultStageConstructions(this);
        } else if (stageNumber == 2) {
            readObjectFromFile("stagemayu.txt");
            stageConstructions = new MayuStageConstructions(this);
        } else if (stageNumber == 3) {
            readObjectFromFile("stagea.txt");
            stageConstructions = new AqualiumStageConstructions(this);
        } else if (stageNumber == 4) {
            //readObjectFormFile("");
            stageConstructions = new BritishStageConstructions(this);
        } else if (stageNumber == 5){
            stageConstructions = new SpaceStageConstructions(this);
        }else if(stageNumber==6){
        }
        field.putAllObjects(stageConstructions.getObjectList());
        return null;
    }

    public void stageClear(){
        Log.d("clear", "clear");
        renderer.clear();
        new Thread(new Runnable(){
            public void run() {
                try {
                    synchronized (sInput) {
                        field.removeObject(questionObjectKey);
                        makeNewQuestion();
                    }
                    Thread.sleep(4000);

                    field.sendObjectsApparenceData(renderer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}).start();
    }
    public void makeNewQuestion(){
        questionObject = new Object3D();
        StlModel stlModel = new StlModel(activity.getBaseContext());
        int shapeID = stlModel.createShape3D(0);
        questionObject.setModel(shapeID);
        stageConstructions.setQuestion(questionObject);

        questionObjectKey = field.putObject(questionObject);
        //aabb -2 -3 -2 2 3 2
    }

    public void destroyStage(){
        synchronized (sInput) {
            timer.isRoop = false;
        }
        field.removeAllObjects();
        qsManager = null;
        activity.finish();
    }

    class StageRoopTimer extends Thread {
        int intervalTime = 50;
        boolean isRoop = true;

        StageRoopTimer(int time) {
            intervalTime = time;
        }

        public void run() {
            while (isRoop) {

                try {
                    Thread.sleep(intervalTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sInput.screenInputFetch();
            }
        }
    }
}
