package com.akiranagai.myapplication.collision;

public class Float3 {
    float[] point = new float[3];

    public Float3(){}
    public Float3(float x, float y, float z){
        point[0] = x;
        point[1] = y;
        point[2] = z;
    }
    public Float3(float[] values){
        point = values;
    }

    public float get(int i){
        return point[i];
    }

    public Float3 add(Float3 operand){
        return new Float3(point[0] + operand.point[0], point[1] + operand.point[1], point[2]+ operand.point[2]);
    }
    public Float3 sub(Float3 operand){
        return new Float3(point[0] - operand.point[0], point[1]- operand.point[1], point[2]-operand.point[2]);
    }
    public Float3 invert(){
        return new Float3(-point[0], -point[1], -point[2]);
    }
    public Float3 multi(float operand){
        return new Float3(point[0]*operand, point[1]*operand, point[2]*operand);
    }
    public Float3 multi(Float3 operand){
        return new Float3(point[0] *operand.point[0], point[1]*operand.point[1], point[2]*operand.point[2]);
    }
    public Float3 divide(float operand){
        return new Float3(point[0]/operand, point[1]/operand, point[2]/operand);
    }
    public Float3 divide(Float3 operand){
        return new Float3(point[0]/operand.point[0], point[1]/operand.point[1], point[2]/operand.point[2]);
    }
    public float dot(Float3 operand){
        return point[0]*operand.point[0] + point[1]*operand.point[1] + point[2]*operand.point[2];
    }
    public Float3 cross(Float3 operand){
        return new Float3(point[1] * operand.point[2] - point[2] * operand.point[1], point[2] * operand.point[0] - point[0] *operand.point[2], point[0] * operand.point[1] - point[1] * operand.point[0]);
    }
    public float length(){
        return ((float)Math.sqrt(lengthSq()));
    }
    public float lengthSq(){
        return point[0]*point[0] + point[1]*point[1] + point[2]*point[2];
    }
    public void norm(){
        float len = length();
        if(len > 0f){
            point[0] /= len;
            point[1] /= len;
            point[2] /= len;
        }
    }
    public Float3 getNorm(){
        float len = length();
        if(len > 0f) {
            return new Float3(point[0] / len, point[1] / len, point[2] / len);
        }
        return new Float3(0f,0f,0f);
    }
}
