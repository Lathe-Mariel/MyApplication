package com.akiranagai.myapplication.object3d;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.Texture;
import com.akiranagai.myapplication.gamecontroller.GameManager;

public class Cat extends Premadonna {
    @Override
    public void touched(Object3D o3) {

    }

    public Cat(GameManager manager){
        super(manager);
        hp = 5000;
        maxHp = 5000;
        translateValues[0] = 0f;
        translateValues[1] = -3.5f;
        translateValues[2] = -5f;
        setModel(new TexCubeShapeGenerator().createShape3D(0, 0.5f));
        setTexture(new Texture().addTexture(manager.activity.getBaseContext(), R.drawable.usaa));
        setTranslate(translateValues[0], translateValues[1], translateValues[2]);
        makeMatrix();
        setShader(GLES.SP_SimpleTexture);
    }

@Override
    public void init(){
        if(barrier == null){
            reCalcBoundingShape();
        }
        barrier.radius = 0.8f;
    }
}
