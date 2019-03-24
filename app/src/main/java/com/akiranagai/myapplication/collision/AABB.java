package com.akiranagai.myapplication.collision;

import android.util.Log;

import com.akiranagai.myapplication.object3d.Object3D;
import com.akiranagai.myapplication.object3d.Premadonna;

public class AABB extends BoundingShape{
    Float3 point;  //-側　座標
    Float3 halfLength;  //各軸の辺の長さの半分
    Float3 point2;  //+側　座標

    public AABB(){}
    public AABB(Float3 point, Float3 point2){
        this.point = point;
        this.point2 = point2;
        halfLength = new Float3();
        this.halfLength.point[0] = (point2.point[0] - point.point[0]) /2;
        this.halfLength.point[1] = (point2.point[1] - point.point[1]) /2;
        this.halfLength.point[2] = (point2.point[2] - point.point[2]) /2;
    }
    public float lenX(){
        return halfLength.point[0]*2f;
    }
    public float lenY(){
        return halfLength.point[1]*2f;
    }
    public float lenZ(){
        return halfLength.point[2]*2f;
    }

    public float len(int i){
        return halfLength.point[i] *2;
    }

    public Float3 getPoint(){
        return point;
    }

    public float getPoint(int i){
        return point.point[i];
    }

    @Override
    public void acquire2DLocation(float[] p1, float[] p2){
        p1[0] = point.point[0];  //1点目X
        p1[1] = point.point[2];  //1点目Z
        p2[0] = point2.point[0];  //2点目X
        p2[1] = point2.point[2];  //2点目Z
    }

    @Override
    public void acquire3DLocation(float[] p1, float[] p2){
        p1[0] = point.point[0];  //1点目(-側)X
        p1[1] = point.point[1];
        p1[2] = point.point[2];  //1点目Z
        p2[0] = point2.point[0];  //2点目(+側)X
        p2[1] = point2.point[1];  //2点目Y
        p2[2] = point2.point[2];  //2点目Z
    }

    @Override
    public void set3DLocation(float[] p1, float[] p2){
        point.point[0] = p1[0];
        point.point[1] = p1[1];
        point.point[2] = p1[2];
        point2.point[0] = p2[0];
        point2.point[1] = p2[1];
        point2.point[2] = p2[2];
    }

@Override
    public void collideVsSphere(final Object3D object){
        Log.d("messager", "AABB####collideVsSphere");
        float[] v2 = object.getCalcPosition();
        //Log.d("messageb", "translateValues    [0]: " + v2[0] + "  [1]: " + v2[1] + "  [2]: " + v2[2]);
        final float r = object.getBoundingShape().radius;
        float[] backDirection = new float[3];
        double sqLen = 0;
        for (int i = 0; i < 3; i++) {
            if (v2[i] < point.point[i]){
                sqLen += Math.pow(v2[i] - point.point[i], 2);
                backDirection[i] = -1f;
            }
            else if(v2[i] > point2.point[i]){
                sqLen += Math.pow(v2[i] - point2.point[i], 2);
                backDirection[i] = 1f;
            }else{
            }
        }

        //double distance = sqLen - (r*r);

        if(sqLen < r*r) {
            final double distance = Math.sqrt(sqLen) - r;
            //distance = Math.sqrt(distance);
            Float3 returnVector = new Float3(backDirection);
            returnVector.norm();
            returnVector = returnVector.multi((float)-distance);
            for(int i =0; i < 3; i++) {
                v2[i] = v2[i] + returnVector.get(i);
            }
            if(object instanceof Premadonna){
                ((Premadonna)object).crash(-(distance*8)*(distance*8));
            }

            object.setTranslate(v2[0], v2[1],v2[2]);
        }
    }

    public void collideVsAABB(float[] v1, float[] v2){}

}
