package com.akiranagai.myapplication.object3d;

import android.opengl.GLES20;

import com.akiranagai.myapplication.BufferUtil;
import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.gamecontroller.GameManager;

public class Rabbit extends Premadonna {
    @Override
    public void touched(Object3D o3) {

    }

    public Rabbit(GameManager manager){
        super(manager);

        translateValues[0] = 0f;
        translateValues[1] = -3.5f;
        translateValues[2] = -5;
        setModel(new StlModel(manager.activity, "rabbit.stl").createShape3D(0));

        model.texcoordBuffer = BufferUtil.makeFloatBuffer(new float[1]);
        setTranslate(translateValues[0], translateValues[1], translateValues[2]);

        setScale(0.05f, 0.05f, 0.05f);
        makeMatrix();
        setColor(0.3f,0.3f,0.3f,1);
        setShader(GLES.SP_SimpleObject);
    }

@Override
    public void init(){
        if(barrier == null){
            reCalcBoundingShape();
        }
        barrier.radius = 0.8f;
    }

}
