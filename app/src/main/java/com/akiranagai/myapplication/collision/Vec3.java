package com.akiranagai.myapplication.collision;

import com.akiranagai.myapplication.collision.Float3;

public class Vec3 extends Float3 {
    public Vec3(){}
    public Vec3(float x, float y, float z){
        super(x,y,z);
    }
    public Vec3(Float3 r){
        point[0] = r.point[0];
        point[1] = r.point[1];
        point[2] = r.point[2];
    }

    /**
     *  OX_EPSILON の誤差範囲内なら垂直としてtrueを返す
     * @param operand 比較対象のベクトル
     * @return
     */
    public boolean isVertical(Vec3 operand){
        float d= dot(operand);
        return (- Util.OX_EPSILON < d && d < Util.OX_EPSILON);
    }

    /**
     *  OX_EPSILON の誤差範囲内なら平行としてtrueを返す
     * @param operand 比較対象のベクトル
     * @return
     */
    public boolean isParallel(Vec3 operand){
        float d = cross(operand).lengthSq();
        return (-Util.OX_EPSILON < d && d < Util.OX_EPSILON);
    }

    /**
     *  対象ベクトルとのなす角が鋭角ならtrueを返す
     * @param operand 対象のベクトル
     * @return
     */
    public boolean isSharpAngle(Vec3 operand){
        return(dot(operand) >= 0.0f);
    }

}
