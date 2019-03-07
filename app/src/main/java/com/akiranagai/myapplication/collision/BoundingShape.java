package com.akiranagai.myapplication.collision;

import com.akiranagai.myapplication.object3d.Object3D;

public abstract class BoundingShape {
    public float radius;

    public void collideVsAABB(Object3D object){

    }
    public void collideVsSphere(Object3D object){

    }

    public void acquire2DLocation(float[] p1, float[] p2){

    }
    public abstract void acquire3DLocation(float[] p1, float[] p2);
    public abstract void set3DLocation(float[] p1, float[] p2);
}
