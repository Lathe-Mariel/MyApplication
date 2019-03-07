package com.akiranagai.myapplication.gamecontroller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.akiranagai.myapplication.BufferUtil;
import com.akiranagai.myapplication.R;
import com.akiranagai.myapplication.object3d.KeyPanelSet;
import com.akiranagai.myapplication.object3d.TexObject3D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class ScreenInput extends View implements View.OnTouchListener {
    private GameManager manager;

    Rect[] rect = new Rect[2];
    int displayWidth, displayHeight;
    private MotionEvent oldMotionEvent=null;
    float accelerateX, accelerateZ;
    float degreeBeta;//Y軸周りの回転量
    private int threashold;
    private int damper;

    private GLStageRenderer renderer;
    private GestureDetector mGestureDetector;
    Thread thread;
    private Field field; //ゲームステージのメインコントローラ
    //int interrvalTime = 50;  //キーインプットのポーリングインターバル porling intervale for keyinput
    private static final String TAG = "MyGLSurfaceView";

    private ArrayList<TexObject3D> backBufferObjects;

    private Paint mPaint = new Paint();

    public ScreenInput(GameManager manager){
        super(manager.activity);
        this.manager = manager;

        mPaint.setStrokeWidth(6);  //入力エリア　描画設定
        mPaint.setColor(getResources().getColor(R.color.whiteblue));
        mPaint.setAlpha(170);

        setOnTouchListener(this);
        rect[0] = new Rect();
        rect[1] = new Rect();
        threashold = 51;  //default threashold出力値がこの値以下なら、０出力
        damper = 30        ;//onTouchEventが無い場合の出力減衰値

        oldMotionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, MotionEvent.ACTION_UP, 0, 0, 0);

        mGestureDetector = new GestureDetector(mOnGestureListener);
    }

    public void setField(Field field){
        renderer = manager.renderer;
        this.field = field;
    }

    public void setThreashold(int s){
        this.threashold = s;
    }

    //float[] pointX = new float[2];//current coordinate on the Device screen.
    //float[] pointY = new float[2];
    float[] zeroX = new float[2];//Input Panel Sensor Center Coordinate on the Device Screen.
    float[] zeroY = new float[2];
    boolean validTouched;
    //private MotionEvent processingMotionEvent;

    boolean moveReady = true;

    float[] mouseTouched = new float[14];

    public void screenInputFetch(){

            postInvalidate();
            if(moveReady) {
                synchronized(this) {
                    if (accelerateX != 0 || accelerateZ != 0 || degreeBeta != 0) {    //sending data to registered listeners.登録されたリスナにデータを送信
                        field.setInputState(accelerateX, accelerateZ, degreeBeta);
                        //Log.d("receiveSensorValues", "accelerateX: " + accelerateX + "   , accelerateZ: " + accelerateZ + "   , degreeBeta: " + degreeBeta);
                    }
                }
            }

            if(validTouched) {
                //validTouched = false;
                accelerateX = mouseTouched[0] - zeroX[0] + (mouseTouched[2] - zeroX[1]);
                accelerateZ = mouseTouched[1] - zeroY[0] + (mouseTouched[3] - zeroY[1]);
                accelerateZ = calc(accelerateZ);
                degreeBeta = (mouseTouched[3] - zeroY[1]) - (mouseTouched[1] - zeroY[0]);
            }else{
                dampOutputValue();
            }
    }

    /**
     * if there are any inputs, this method will be called and the output value is decreased
     * the method calc(float) always works with this method.
     */
    private void dampOutputValue(){
        accelerateX = calc(accelerateX);
        accelerateZ = calc(accelerateZ);
        degreeBeta = calc(degreeBeta);
    }

    private float calc(float value){
        if (value > 0) {
            if (value < threashold) {
                value = 0;
            } else {
                value -= damper;
            }
        } else if (value < 0) {
            if (value > -threashold) {
                value = 0;
            } else {
                value += damper;
            }
        }
        return value;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        Log.d("message", "display Width: " + w + "    Height: " + h);
        //centerX = (float)(getX() + w/2);
        //centerY = (float)(getY() + w/2);
        displayWidth = w;
        displayHeight = h;
        rect[0].left = 2;
        rect[0].top = (int)(h/1.5);
        rect[0].bottom = h-2;
        rect[0].right = w/7;

        rect[1].left = w-rect[0].right;
        rect[1].top = rect[0].top;
        rect[1].bottom = rect[0].bottom;
        rect[1].right = w-2;
        invalidate();
        //Log.d("message", "(onSizeChanged) rect[0]: " + rect[0] +"");

        zeroX[0] = rect[0].centerX();
        zeroY[0] = rect[0].centerY();
        zeroX[1] = rect[1].centerX();
        zeroY[1] = rect[1].centerY();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void keyInputZoneCheck(MotionEvent event){
        validTouched = false;
        int actionCount = event.getPointerCount();
        int actionMask = event.getActionMasked();
        if(actionMask != MotionEvent.ACTION_UP){
        for(int i =0; i < actionCount; i++) {

            float x = event.getX(i);
            float y = event.getY(i);
            if (y > rect[0].top) {
                if (x < rect[0].right) {
                    if(actionMask == MotionEvent.ACTION_POINTER_UP){
                        mouseTouched[0] = zeroX[0];
                        mouseTouched[1] =zeroY[0];
                    }else {
                        validTouched = true;
                        mouseTouched[0] = x;
                        mouseTouched[1] = y;
                    }
                } else if(x > rect[1].left){
                    if(actionMask == MotionEvent.ACTION_POINTER_UP){
                        mouseTouched[2] = zeroX[1];
                        mouseTouched[3] =zeroY[1];
                    }else {
                        validTouched = true;
                        mouseTouched[2] = x;
                        mouseTouched[3] = y;
                    }
                }
            }
        }
        }
        if(validTouched)return;
        mouseTouched[0] = zeroX[0];
        mouseTouched[1] =zeroY[0];
        mouseTouched[2] = zeroX[1];
        mouseTouched[3] =zeroY[1];

        mGestureDetector.onTouchEvent(event);

    }
    public boolean onTouch(View v, final MotionEvent event){
        keyInputZoneCheck(event);
        return true;
    }

    protected void onDraw(Canvas canvas) {

        if(!validTouched) {
            mPaint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < 2; i++) {
                RectF rectf = new RectF(rect[i]);
                canvas.drawRoundRect(rectf, 50, 50, mPaint);
                canvas.drawLine(rectf.centerX(), rectf.centerY() - 70, rectf.centerX(), rectf.centerY() + 70, mPaint);
                canvas.drawLine(rectf.centerX() - 70, rectf.centerY(), rectf.centerX() + 70, rectf.centerY(), mPaint);
            }
        }
        super.onDraw(canvas);
    }

    private GestureListener mOnGestureListener
            = new GestureListener() {

        @Override
        public void onFirstTouch(GestureDetector detector) {
            Log.i(TAG, "onFirstTouch:" + detector.getX1() + ", " + detector.getY1());
        }

        @Override
        public void onSecondTouch(GestureDetector detector) {
            Log.i(TAG, "onSecondTouch:" + detector.getX2() + ", " + detector.getY2());
        }

        @Override
        public void onSecondTouchEnd(GestureDetector detector) {
            Log.i(TAG, "onSecondTouchEnd:");
        }

        @Override
        public void onFirstTouchEnd(GestureDetector detector) {
            Log.i(TAG, "onFirstTouchEnd:");
        }

        @Override
        public void onShortTap(GestureDetector detector) {
            //Toast.makeText(mContext, "detected short tap", Toast.LENGTH_SHORT).show();
            Log.d("message", "onShortTap");
            float DeltaX = detector.getAccumulatedDeltaX();
            float DeltaY = detector.getAccumulatedDeltaY();
            renderer.setScrollValue(-DeltaX, -DeltaY);//shorttapなのに動いてしまったのを修正
            //UpperLayout.setVisibility(View.VISIBLE);
            getColorByScreen( (int)detector.getX1(), (int)detector.getY1());
        }

        @Override
        public void onLongTap(GestureDetector detector) {
            Log.d("message", "onLongTap");
            quitStage();
        }

        public void onFling(GestureDetector detector){
            Log.d("message", "onFling");

        }

        @Override
        public void onOneFingerMove(GestureDetector detector) {
            float DeltaX = detector.getDeltaX();
            float DeltaY = detector.getDeltaY();
            Log.i(TAG, "onOneFingerMove: DeltaX, DeltaY = " + DeltaX + "," + DeltaY);
            renderer.setScrollValue(DeltaX, DeltaY);
        }

        @Override
        public void onTwoFingerMove(GestureDetector detector) {
            float factor = detector.getFactor();
            float Delta2X = detector.getDelta2X();
            float Delta2Y = detector.getDelta2Y();
            float Rotation = detector.getRotation();
            Log.i(TAG, "onTwoFingerMove: factor = " + factor);
            Log.i(TAG, "onTwoFingerMove: Delta2X, Delta2Y = " + Delta2X + "," + Delta2Y);
            Log.i(TAG, "onTwoFingerMove: Rotation = " + Rotation);
            renderer.setPinch2(factor);
            renderer.setRot2Angle(Rotation);
            renderer.setScroll2Value(Delta2X, Delta2Y);
        }

        @Override
        public void onThreeFingerMove(GestureDetector detector) {
            float factor = detector.getFactor();
            float Delta2X = detector.getDelta2X();
            float Delta2Y = detector.getDelta2Y();
            float Rotation = detector.getRotation();
            Log.i(TAG, "onThreeFingerMove: factor = " + factor);
            Log.i(TAG, "onThreeFingerMove: Delta2X, Delta2Y = " + Delta2X + "," + Delta2Y);
            Log.i(TAG, "onThreeFingerMove: Rotation = " + Rotation);
            renderer.setPinch3(factor);
            renderer.setRot3Angle(Rotation);
            renderer.setScroll3Value(Delta2X, Delta2Y);
        }
    };

    ByteBuffer btbPixel;

    private void getColorByScreen(final int pickX, final int y){

        byte[] abtPixel;
        final int pickY = displayHeight - y;  //高さ方向反転
        abtPixel = new byte[8];
        btbPixel = BufferUtil.makeByteBuffer(abtPixel );
        btbPixel.position(0);

        manager.surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {

                int[] colorBuffer = new int[1];
                int[] depthBuffer = new int[1];
                int[] frameBuffer = new int[1];
                int width = getWidth();
                int height = getHeight();

                GLES20.glGenTextures(1, colorBuffer, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, colorBuffer[0]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

/*
                GLES20.glGenTextures(1, depthBuffer, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthBuffer[0]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                */
/*
                GLES20.glGenRenderbuffers(1, colorBuffer, 0);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, colorBuffer[0]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_RGB, width, height);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
*/
                GLES20.glGenRenderbuffers(1, depthBuffer, 0);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthBuffer[0]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);

                int error4 = GLES20.glGetError();
                Log.d("message", "error4: " + error4);
                GLES20.glGenFramebuffers(1, frameBuffer, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);

                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, colorBuffer[0],0);
                //GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthBuffer[0],0);

               // GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_RENDERBUFFER, colorBuffer[0]);
                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthBuffer[0]);

                int errorcode = 0;
                switch(GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)){
                    case GLES20.GL_FRAMEBUFFER_COMPLETE:
                        Log.d("message", "GL_FRAMEBUFFER_COMPLETE");
                        break;
                    case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                        errorcode = 1;
                        break;
                    case GLES20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                        errorcode = 2;
                        break;
                    case GLES20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
                        errorcode = 3;
                        break;
                    case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
                        errorcode = 4;
                        break;
                }
                Log.d("message", "GL_FRAMEBUFFER error: " + errorcode);
                GLES20.glViewport(0,0,width,height);
                float aspect=(float)width/(float)height;

               renderer.defaultFrameInit();
               renderer.screenViewInit();

                for (Iterator<TexObject3D> i = backBufferObjects.iterator(); i.hasNext(); ) {
                    i.next().backRender();
                }

                int error5 = GLES20.glGetError();
                Log.d("message", "error5: " + error5  + "--------------------------------------------------------------------------------------");
                GLES20.glReadPixels(pickX, pickY, 1,1,GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, btbPixel);
                int error6 = GLES20.glGetError();
                Log.d("message", "error6 code: " + error6);
                /*
                btbPixel.position(0);
                byte[] abtRGB = new byte[3];
                short readPixel = btbPixel.get(0);
                abtRGB[0] = (byte)( ( readPixel & 0xF800 ) >> 11 << 3 );// 赤は、5ビット、32階調
                abtRGB[1] = (byte)( ( readPixel & 0x7E0 ) >> 5 << 2 );// 緑は、6ビット、64階調
                abtRGB[2] = (byte)( ( readPixel & 0x1F ) << 3 );// 青は、5ビット、32階調
                btbPixel.position(0);
                Log.d("message", "pickupColor:  r: " + abtRGB[0] + "  g: " + abtRGB[1] + "  b: " + abtRGB[2]);
                btbPixel.position(0);
                */
                btbPixel.position(0);
                int pickedAlphabet = btbPixel.get(2);  //タップしたキーのブルーエレメントを取得
                Log.d("messageb", "tappedPanel: " + btbPixel.get(2));
                if(KeyPanelSet.tapKeyPanel(pickedAlphabet)) {
                    if (manager.answerAlphabet - 97 == pickedAlphabet) {
                        manager.stageClear();
                        Log.d("message", "Stage Clear!!");
                    } else {
                        btbPixel.position(0);
                        Log.d("message", "btbPixel.get(2): " + btbPixel.get(2) + "     field.getAnswer(): " + (field.getAnswer() - 97));
                    }
                    btbPixel.position(0);
                    Log.d("message", "pickupColor: r: " + btbPixel.get(0) + "    g: " + btbPixel.get(1) + "    b: " + btbPixel.get(2));
                }

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);  //アンバインド
                GLES20.glDeleteTextures(1, colorBuffer, 0);
                //GLES20.glDeleteTextures(1, depthBuffer, 0);
        //        GLES20.glDeleteRenderbuffers(1,colorBuffer, 0);
                GLES20.glDeleteRenderbuffers(1, depthBuffer, 0);
                GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
            }
        });
    }

    /**
     * バックバッファへ描画するオブジェクトを登録
     * @param list
     */
    public void setBuckBufferObjectList(ArrayList<TexObject3D> list){
        backBufferObjects = list;
    }

    private void quitStage(){
        new AlertDialog.Builder(manager.surfaceView.getContext())
                .setTitle("Confirmation")
                .setMessage("Do you really want to quit this stage?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.destroyStage();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
