package com.akiranagai.myapplication.object3d;



import android.graphics.Color;
import android.util.Log;

import com.akiranagai.myapplication.GLES;
import com.akiranagai.myapplication.StringTextureGenerator;
import com.akiranagai.myapplication.gamecontroller.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyPanelSet {
    public GameManager manager;

    //private ArrayList<Object3D> alphabetPanels = new ArrayList<>();
    private int panelShapeID;  //キー入力オブジェクトの形状
    private int foreColor = Color.rgb(255,0,0);
    private int backColor = Color.rgb(255,255,0);
    private int alphaCharTextureID =0;
    public static Map<Integer, TexObject3D> keyPanelList = new HashMap<Integer, TexObject3D>();
    private static int selectedKeyPanel = -1;
    private static int selectedKeyPanel6 = -1;

    public void createAlphabets(){
        createAlphabets(0,25);
    }
    public TexObject3D createAlphabets(int startAlphabet, int endAlphabet){
        int count = endAlphabet - startAlphabet + 1;
        float[] position = new float[count*3];
        for(int i = 0; i < count; i++){
            position[i*3] = (float)i*2; //X
            position[i*3+1] = -3.5f;   //Y
            position[i*3+2] = 4f;  //Z位置
        }
        return createAlphabets(startAlphabet, endAlphabet, position);
    }

    /**
     *
     * @param startAlphabet
     * @param endAlphabet そのアルファベットを含むパネルセットを生成(A=0, Z=25)
     * @param position 各アルファベットのX,Y,Z位置を示すfloat配列  position.length=生成するアルファベット数*3
     * @return
     */

    public TexObject3D createAlphabets(int startAlphabet, int endAlphabet, float[] position) {
        if(panelShapeID ==0)return null;  //入力オブジェクト形状の設定がなければ

        for (int i = startAlphabet; i < endAlphabet+1; i++) {
            final TexObject3D currentObject = new TexObject3D();
            keyPanelList.put(i, currentObject);
            final String string =  String.valueOf((char)('A' + i));  //a-z

            manager.surfaceView.queueEvent(new Runnable(){
                public void run(){
                    StringTextureGenerator st = new StringTextureGenerator();
                    alphaCharTextureID = st.makeStringTexture(string, 50.f, foreColor, backColor);
                    ((TexObject3D)currentObject).setTexture(alphaCharTextureID);
                }
            });

            float blueElement = ((float)i/255);  //キーオブジェクト識別用のID 青要素へエンコード
            currentObject.setIDColor(blueElement);
            currentObject.setModel(panelShapeID);
            currentObject.setTranslate(position[i*3], position[i*3+1], position[i*3+2]);
            currentObject.setShader(GLES.SP_TextureWithLight);
            currentObject.immediateMakeMatrix();
            //currentObject.makeMatrix();
            currentObject.reCalcBoundingShape();
        }
        return keyPanelList.get(endAlphabet);
    }

    public ArrayList<TexObject3D> getPanels(){
        return new ArrayList(keyPanelList.values());
    }

    public void setPanelShapeID(int id){
        this.panelShapeID = id;
    }

    public void setForeColor(int fore){
        foreColor = fore;
    }
    public void setBackColor(int back){
        backColor = back;
    }

    /**
     * タップされたパネルにエフェクトをかける
     * @param alphabetNumber A=0で表現　通常０～２６.
     */
    public static boolean tapKeyPanel(int alphabetNumber) {
        if(alphabetNumber < 0)return false;
        //singleCubePanelの場合
        if(alphabetNumber < keyPanelList.size()) {
            TexObject3D object;
            if (selectedKeyPanel != -1) {
                object = keyPanelList.get(selectedKeyPanel);
                object.setScale(1f, 1f, 1f)
                        .makeMatrix();
            }
            if (alphabetNumber == selectedKeyPanel) {
                selectedKeyPanel = -1;
                return true;
            }
            object = keyPanelList.get(alphabetNumber);
            object.setScale(1.4f, 1.4f, 1.4f)
                    .makeMatrix();
            selectedKeyPanel = alphabetNumber;
            return false;
        }
        //6面体の場合
        if(alphabetNumber >= keyPanelList.size() && alphabetNumber < 26){
            int selectedNumber = alphabetNumber -(keyPanelList.size()-1);
            Log.d("message", "key6Panel number: " + selectedNumber);

            Input6Cube tappedCube = (Input6Cube)keyPanelList.get(keyPanelList.size()-1);
            if(alphabetNumber == selectedKeyPanel6){
                tappedCube.selectFace(-1);
                selectedKeyPanel6 = -1;
                return true;
            }else{
                tappedCube.selectFace(selectedNumber);
            }
            selectedKeyPanel6 = alphabetNumber;
        }

        return false;
    }

    /**
     * ６面　別文字パネルを１つ作る.
     * null を返す可能性あり.
     * This method may return null.
     * @param panelString 6文字のString
     * @param indexs 各文字のアルファベット上のインデックス(A=0).
     */
    public Input6Cube make6Cube(String panelString, int[] indexs) {
        if(indexs.length < 6) return null;
        Input6Cube cube6textureTest = new Input6Cube();
        TexCubeShapeGenerator tcsg = new TexCubeShapeGenerator();
        tcsg.setTextureMode(6
        );  //6面　別Texture に設定
        cube6textureTest.setModel(tcsg.createShape3D(0));
        cube6textureTest.setTranslate(3f, 0f, 18f)
                .setShader(GLES.SP_SimpleTexture)
                .setScale(1,1,1);
        cube6textureTest.makeMatrix();
        int[] frontAndBack = new StringTextureGenerator().makeString6Texture(panelString, 36, Color.BLUE, Color.WHITE, indexs[0]);
        cube6textureTest.setTexture(frontAndBack[0]);
        cube6textureTest.setBackTexture(frontAndBack[1]);
        keyPanelList.put(indexs[0], cube6textureTest);
        return cube6textureTest;
    }
}
