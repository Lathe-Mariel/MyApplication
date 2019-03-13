package com.akiranagai.myapplication.gamecontroller;

import android.content.Context;
import android.graphics.Color;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.object3d.Premadonna;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.StringTextureGenerator;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.StlModel;
import com.akiranagai.myapplication.object3d.TexCubeShapeGenerator;
import com.akiranagai.myapplication.object3d.TexObject3D;
import com.akiranagai.myapplication.object3d.TilePanelShapeGenerator;
import com.akiranagai.myapplication.object3d.Toast;

import java.util.ArrayList;

public class ExplainStageConstructions extends StageConstructions {
    Object3D question;  //クエスションObject3D
    GameManager manager;
    private int fingerID;  //finger TextureID

    ExplainStageConstructions(GameManager manager){
        super();

        this.manager = manager;

        Context context = manager.activity.getBaseContext();

        int roadPictureID3 = new Texture().addTexture(context, R.drawable.marble);  //地面タイル3
        fingerID = new Texture().addTexture(context, R.drawable.finger);  //finger

        //センターロード　下部パネル
        TexObject3D road = new TexObject3D();
        int roadModelID = new TilePanelShapeGenerator().createShape3D(0,21f,43f,0f,43,43);
        road.setModel(roadModelID);
        Texture.setRepeartTexture(roadPictureID3, true);
        road.setTexture(roadPictureID3);
        road.setTranslate(0f, -3.5f, 0f);
        road.setScale(3f,3f,3f);
        road.makeMatrix();
        road.setShader(GLES.SP_SimpleTexture);
        objectList.add(road);

//方角　指示器
        TexObject3D directionPanel = new TexObject3D();
        directionPanel.setModel(new Toast().createShape3D(0, 7.85f, 0.1f));
        directionPanel.setShader(GLES.SP_SimpleTextureSimpleZ);
        directionPanel.setTexture(new StringTextureGenerator().makeStringTextureWithSpace("ESWNE", 30f, Color.BLACK));
        manager.prema.setDirectionPanel(directionPanel);

        //キー入力パネル
        KeyPanelSet kps = new KeyPanelSet();
        final float y = -2f;
        final float x1 = -30;
        final float x2 = -25;
        float[] keyPositionArray = new float[]{
                10,0, 2,  //0A
                25, -1, -6,
                -40, y, 34,
                x1, 1, 16,
                -35, -2f, -50,  //E
                -38, -2, 20,  //5
                -45, -3.5f, -22,
                x1,-2f,39,  //H
                31,1,-36,
                10,-2,-42,  //J
                x2, y, 10,  //10K
                -38, 0, -5,  //L
                -5, y, -3,  //12M
                32, y, -18,
                12, -2f, 3f,  //O
                31, 10, 8,  //P
                20, 0.5f, 36,  //Q
                18,3,24,  //17R
                5,y,26,  //S
                x2,y,28,  //T
        };

        kps.setPanelShapeID(new TexCubeShapeGenerator().createShape3D(0, 0.8f));
        kps.createAlphabets(0, 19, keyPositionArray);
        TexObject3D cubeAlpha = kps.make6Cube("UVWXYZ", new int[]{20,21,22,23,24,25});
        cubeAlpha.setTranslate(18, 0, 8);
        ArrayList<TexObject3D> list;
        list = kps.getPanels();
        objectList.addAll(list);
        manager.sInput.setBuckBufferObjectList(list);  //バックバッファに設定する表示オブジェクトを登録(Alphabet Boxes)

        manager.prema.setTranslate(0,-3,-20);

        //A を生成
        Object3D questionObject = new Object3D();
        StlModel stlModel = new StlModel(manager.activity.getBaseContext(), "a.stl");

        int shapeID = stlModel.createShape3D(0);
        questionObject.setModel(shapeID);
        questionObject.setTranslate(0, 15,0);  //とりあえず、見えない所へ置く
        questionObject.setScale(0.01f,0.01f,0.02f);
        questionObject.setColor(0.55f,1,0.6f,1);
        questionObject.fullCalcBoundingShape();
        questionObject.setShader(GLES.SP_ObjectWithLight2);
        //objectList.add(questionObject);


        manager.questionObjectKey = manager.field.putObject(questionObject);
        this.question = questionObject;
    }

    @Override
    public void setQuestion(Object3D questionObject) {
        //this.question = questionObject;
/*
        questionObject.setScale(0.01f,0.01f,0.02f);
        questionObject.setColor(0.55f,1,0.6f,1);
        questionObject.setTranslate(0,0,50f);
        questionObject.fullCalcBoundingShape();
        //questionObject.makeMatrix();
        questionObject.setShader(GLES.SP_ObjectWithLight2);
        */
    }

    @Override
    public void startStage(){
        new MoveQuestion().start();
    }

    class MoveQuestion extends Thread {
        GLStageRenderer renderer = manager.renderer;
        Premadonna prema = manager.prema;

        @Override
        public void run() {
            MessageTextureGenerator mtg = new MessageTextureGenerator();
            mtg.start();

            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }

            float movingValue = 13;
            //垂直移動
            for (int i = 0; i < 140; i++) {
                movingValue -= 0.1f;
                question.setTranslate(0, movingValue, 0);
                question.makeMatrix();
                try {
                    Thread.sleep(40);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //A回転
            for (int i = 0; i < 90; i++) {
                question.setRotate(i, 1, 0, 0);
                question.makeMatrix();
                try {
                    Thread.sleep(45);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //位置回転＆視点中心
            float valueX = 0, valueZ = 0;
            int armLength = 20;

            for (int i = 0; i < 360; i++) {
                float direction = (float) (i * Math.PI * 2 / 360);
                float tempX = (float) Math.sin(direction);
                float tempY = (float) -Math.cos(direction);
                valueX = (float) tempX * armLength;
                valueZ = (float) tempY * armLength;
                //prema.setViewDirection(-direction);
                synchronized (manager.sInput) {
                    prema.translate(valueX, prema.getY(), valueZ, -direction, 0);
                }
                prema.makeMatrix();
                try {
                    Thread.sleep(30);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //位置回転＆視点中心
            valueX = 0;
            valueZ = 0;

            float direction=0;
            for (int i = 0; i < 25; i++) {
                direction = (float) (i * Math.PI * 2 / 360);
                prema.setViewDirection(direction);

                prema.makeMatrix();
                try {
                    Thread.sleep(55);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try{
                Thread.sleep(1000);
            }catch(Exception e){e.printStackTrace();}


            //Aへ前進
            float tmpX, tmpZ;
            for(int i = 0; i < 110; i++) {
                tmpX = (float)(0.15 * Math.sin(direction));
                tmpZ = (float)(0.15 * Math.cos(direction));
                //視線方向更新
                float[] v2 = {prema.getX() + (float) tmpX, prema.getY(), prema.getZ() + (float) tmpZ};
                synchronized (manager.sInput) {
                    prema.translate(v2[0], v2[1], v2[2], direction, 0);
                }

                try {
                    Thread.sleep(45);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            KeyPanelSet.tapKeyPanel(0);  //Aをタップ
            try{
                Thread.sleep(3000);
            }catch(Exception e){e.printStackTrace();}
            KeyPanelSet.tapKeyPanel(0);  //Aをタップ
            try{
                Thread.sleep(3000);
            }catch(Exception e){e.printStackTrace();}
            manager.correctAnswer();
            try{
                Thread.sleep(4000);
            }catch(Exception e){e.printStackTrace();}

            //-----------------------------------------------------------------------------------

            final float fingerX=0.85f;
            final float fingerY=-0.65f;
            final TexObject3D fingerR = new TexObject3D();
            fingerR.setModel(new Toast().createShape3D(0,0.3f,0.3f));
            fingerR.setTexture(fingerID);
            fingerR.setTranslate(fingerX, fingerY, -0.95f);
            fingerR.makeMatrix();
            fingerR.setShader(GLES.SP_SimpleTexture);
            manager.surfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    renderer.addFrontObject(fingerR);
                }
            });
            final TexObject3D fingerL = new TexObject3D();
            fingerL.setModel(new Toast().createShape3D(0,0.3f,0.3f));
            fingerL.setTexture(fingerID);
            fingerL.setTranslate(-fingerX, fingerY, -0.95f);
            fingerL.makeMatrix();
            fingerL.setShader(GLES.SP_SimpleTexture);
            manager.surfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    renderer.addFrontObject(fingerL);
                }
            });
            new MessageTextureGenerator2().start();

            try{
                Thread.sleep(8000);
            }catch(Exception e){e.printStackTrace();}

            //finger 前進 教示
            fingerR.setTranslate(fingerX, fingerY+0.2f, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX, fingerY+0.2f, -0.95f);
            fingerL.makeMatrix();
            for(int i =0;i<30;i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(0, -200, 0);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try{
                    Thread.sleep(1000);
            }catch(Exception e){e.printStackTrace();}


            //後退 教示
            fingerR.setTranslate(fingerX, fingerY-0.3f, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX, fingerY-0.3f, -0.95f);
            fingerL.makeMatrix();
            for(int i =0; i<30; i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(0, 200, 0);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try{
                Thread.sleep(1000);
            }catch(Exception e){e.printStackTrace();}

            //右
            fingerR.setTranslate(fingerX+0.1f, fingerY, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX+0.1f, fingerY, -0.95f);
            fingerL.makeMatrix();
            for(int i =0; i < 30;i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(200, 0, 0);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try{
                Thread.sleep(1000);
            }catch(Exception e){e.printStackTrace();}

            //左
            fingerR.setTranslate(fingerX-0.1f, fingerY, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX-0.1f, fingerY, -0.95f);
            fingerL.makeMatrix();
            for(int i = 0; i<30;i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(-200, 0, 0);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try{
                Thread.sleep(1000);
            }catch(Exception e){e.printStackTrace();}

            //finger 回転 教示
            fingerR.setTranslate(fingerX, fingerY+0.2f, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX, fingerY-0.2f, -0.95f);
            fingerL.makeMatrix();
            for(int i = 0; i<60;i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(0, 0, -200);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //finger 逆回転 教示
            fingerR.setTranslate(fingerX, fingerY-0.2f, -0.95f);
            fingerR.makeMatrix();
            fingerL.setTranslate(-fingerX, fingerY+0.2f, -0.95f);
            fingerL.makeMatrix();
            for(int i = 0; i<60;i++) {
                synchronized (manager.sInput) {
                    manager.field.setInputState(0, 0, 200);
                }
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //finger Object3D削除
            manager.surfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    renderer.removeFrontObject(fingerR);
                    renderer.removeFrontObject(fingerL);
                }
            });
        }

    }

        class MessageTextureGenerator extends Thread{
            String[] messages;
            private final int TIME_GAP = 600;

            int textureID = -1;
            int count = 0;
            int[] interval;

            private GLStageRenderer renderer;

            MessageTextureGenerator() {
                messages = new String[10];
                messages[0] = "There's an alphabet character.";
                messages[1] = "But you can't look it from high place.";
                messages[2] = "You need to guess by looking only horizontally.";
                messages[3] = "When you understand what's the alphabet.";
                messages[4] = "You can input the answer by using a Yellow Box.";
                messages[5] = "First tap means selecting.";
                messages[6] = "And second is deciding.";
                messages[7] = "If the answer is correct...";
                messages[8] = "You'll get a point!!!";
                messages[9] = "";

                interval = new int[messages.length];
                interval[0]=4000;
                interval[1]=4000;
                interval[2]=5500;
                interval[3]=5000;
                interval[4]=4500;
                interval[5]=2500;
                interval[6]=2500;
                interval[7]=2500;
                interval[8]=2500;
                interval[9]=300;

                renderer = manager.renderer;

            }

            public void run(){
                for(int count = 0; count < messages.length; count++) {
                    final String message = messages[count];
                    synchronized (this) {
                        manager.surfaceView.queueEvent(new Runnable() {
                            public void run() {
                                StringTextureGenerator.deleteTexture(textureID);
                                synchronized (MessageTextureGenerator.this) {
                                    StringTextureGenerator stg = new StringTextureGenerator();
                                    stg.setAlfa(200, 0);
                                    textureID = stg.makeStringTexture2(message, 50, Color.parseColor("#006666FF"), Color.parseColor("#00FFFFFF"));
                                    MessageTextureGenerator.this.notify();
                                }
                            }
                        });


                        try {
                            wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    renderer.putToast2(textureID, interval[count]);
                    try {
                        Thread.sleep(interval[count] + TIME_GAP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    class MessageTextureGenerator2 extends Thread{
        String[] messages;
        private final int TIME_GAP = 600;

        int textureID = -1;
        int count = 0;
        int[] interval;

        private GLStageRenderer renderer;

        MessageTextureGenerator2() {
            messages = new String[10];
            messages[0] = "Touching screen, you can move in a map.";
            messages[1] = "Fingers up simultaneously makes you step forward.";
            messages[2] = "Fingers down makes you step backward.";
            messages[3] = "Moving right makes you walk sideways.";
            messages[4] = "Moving left ofcourse makes you go left.";
            messages[5] = "Moving fingers opposite direction, you just turn. ";
            messages[6] = "";
            messages[7] = "";
            messages[8] = "";
            messages[9] = "";

            interval = new int[messages.length];
            interval[0]=5000;
            interval[1]=3000;
            interval[2]=3500;
            interval[3]=3500;
            interval[4]=3500;
            interval[5]=4000;
            interval[6]=3000;
            interval[7]=3000;
            interval[8]=3000;
            interval[9]=3000;

            renderer = manager.renderer;

        }

        public void run(){
            for(int count = 0; count < messages.length; count++) {
                final String message = messages[count];
                synchronized (this) {
                    manager.surfaceView.queueEvent(new Runnable() {
                        public void run() {
                            StringTextureGenerator.deleteTexture(textureID);
                            synchronized (MessageTextureGenerator2.this) {
                                StringTextureGenerator stg = new StringTextureGenerator();
                                stg.setAlfa(200, 0);
                                textureID = stg.makeStringTexture2(message, 50, Color.parseColor("#006666FF"), Color.parseColor("#00FFFFFF"));
                                MessageTextureGenerator2.this.notify();
                            }
                        }
                    });


                    try {
                        wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                renderer.putToast2(textureID, interval[count]);
                try {
                    Thread.sleep(interval[count] + TIME_GAP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }