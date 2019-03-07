package com.akiranagai.myapplication;

import java.util.ArrayList;

public abstract class Rendarable {
    ArrayList<Object3DProperty> objects;

    float[] mMatrix=new float[16]; //モデル変換マトリックス
    int shaderSelectNumber = 1;  //シェーダー切換番号(デフォルトシェーダーは１)

    Texture texture;

    public abstract void render();

    public Rendarable(){
        objects = new ArrayList<Object3DProperty>();
        objects.add(new Object3DProperty());
    }

    public Rendarable setRotate(int r, float x, float y, float z){
        objects.get(0).setRotate(r, x, y, z);
        return this;
    }
    public Rendarable setTranslate(float x, float y, float z){
        objects.get(0).setTranslate(x, y, z);
        return this;
    }
    public Rendarable setScale(float x, float y, float z){
        objects.get(0).setScale(x, y, z);
        return this;
    }
    public Rendarable setColor(float x, float y, float z, float a){
        objects.get(0).setColor(x, y, z, a);
        return this;
    }

    public Rendarable setShader(int number){
        shaderSelectNumber = number;
        return this;
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void addObject(Rendarable.Object3DProperty object){
        objects.add(object);
    }

    public Object3DProperty getNewObject(){
        return new Object3DProperty();
    }

    public class Object3DProperty{
        int rotateValue;
        float[] rotateValues = new float[]{1f, 0f, 0f};
        float[] translateValues = new float[3];
        float[] scaleValues = new float[]{1f, 1f, 1f};
        float[] colorValues = new float[]{1f, 1f, 1f, 1f};

        /**
         * It is possible to set 1 or 2 or 3 bit simultaneously
         * @param r rotate angle
         * @param x reffering rotate axisX(0 or 1)
         * @param y reffering rotate axisY(0 or 1)
         * @param z reffering rotate axisZ(0 or 1)
         * @return
         */
        public Object3DProperty setRotate(int r, float x, float y, float z){
            rotateValue = r;
            rotateValues = new float[]{x, y, z};
            return this;
        }

        /**
         *
         * @param x to move in X direction
         * @param y to move object in Y direction
         * @param z to move object in Z direction
         * @return
         */
        public Object3DProperty setTranslate(float x, float y, float z){
            translateValues = new float[]{x, y, z};
            return this;
        }

        /**
         *
         * @param x x-direction
         * @param y y-direction
         * @param z z-direction
         * @return
         */
        public Object3DProperty setScale(float x, float y, float z){
            scaleValues = new float[]{x, y, z};
            return this;
        }

        /**
         *
         * @param x red
         * @param y blue
         * @param z green
         * @param a alpha
         * @return this object oneself
         */
        public Object3DProperty setColor(float x, float y, float z, float a){
            colorValues = new float[]{x, y, z, a};
            return this;
        }
    }
}
