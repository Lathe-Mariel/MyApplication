package com.akiranagai.myapplication.gamecontroller;

import android.opengl.Matrix;
import android.util.Log;

import com.akiranagai.myapplication.Players.Premadonna;
import com.akiranagai.myapplication.collision.AABB;
import com.akiranagai.myapplication.object3d.Object3D;

import java.nio.BufferUnderflowException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Field3D2 implements Field{
	GameManager manager;
	QuaterSpaceManager qsManager;
	Premadonna prema;

	private int objectKeyNumber = 0;  //一意なオブジェクトID
	HashMap<Integer, Object3D> objectMap = new HashMap<>();  //表示させるオブジェクトリスト

	//private ArrayList<Object3D> obstacles = new ArrayList<>();  //移動時　衝突判定対象とするオブジェクトリスト（8ツリーにより動的生成)

	private int answer =-1;

		private float viewAngle;  //視線方向
		private double tmpX, tmpZ; //位置変位

		synchronized void putAllObjects(List<Object3D> list){
			for(Iterator<Object3D> i = list.iterator(); i.hasNext();)
				putObject(i.next());
		}

		int putObject(Object3D object){
			objectMap.put(++objectKeyNumber, object);
			if(object.getBoundingShape() != null){
				//obstacles.add(object);
				//Log.d("message", "Field3D2#putObject");
				qsManager.registObject3D(object);
			}
			return objectKeyNumber;
		}

		void removeAllObjects(){
			objectKeyNumber = 0;
			objectMap.clear();
			manager.renderer.removeAllObjects();
		}

		void removeObject(int objectKey){
			objectMap.get(objectKey).unRegistSpace4();  //当たり判定4分木から削除
			objectMap.remove(objectKey);
		}

		void sendObjectsApparenceData(GLStageRenderer renderer){
			renderer.addAllObjects(new ArrayList<>(objectMap.values()));
		}

		/*
	public void putObstacle(Object3D obstacle){
			obstacles.add(obstacle);
		}
*/

		public Field3D2(GameManager manager){
			this.manager = manager;
		}

		public boolean setObject(Object3D addObject) {
			final float[] effectMatrix = addObject.getMMatrix();
			FloatBuffer source = addObject.getVertexBuffer();
			Log.d("check", "source vertexes: " + source.capacity() / 3 + "    source.capacity(): " + source.remaining());
			FloatBuffer fb = FloatBuffer.allocate(source.capacity());
			fb.position(0);
			source.position(0);

			float[] result = {0f, 0f, 0f, 0f};
			float[] affectedMatrix = {0f, 0f, 0f, 1f};

			while (3 <= source.remaining()) {
				try {
					source.get(affectedMatrix, 0, 3);
				} catch (BufferUnderflowException e) {
					e.printStackTrace();
					Log.d("error", "BufferUnderflow");
				}

				Matrix.multiplyMV(result, 0, effectMatrix, 0, affectedMatrix, 0);
				fb.put(result, 0, 3);
			}
				//Log.d("check", "remaining: " + source.remaining());

				source.position(0);
				fb.position(0);
				return true;
			}

		/**
		 * This method recieves state from ScreenInput object.
		 */
		@Override
		public void setInputState(float x, float z, float b){

			float accelB=0;
			float accelZ=0;
			float accelX=0;
			accelB = b/5000;
			int sign =1;
			if(x < 0)sign = -1;
			accelX = sign * x * x/80000f;
			sign =1;
			if(z < 0)sign = -1;
			accelZ = sign * z * z /80000f;

			//Log.d("receiveSensor", "x: " + accelX + "    z: " + accelZ + "    b: " + accelB);

			accelB = accelB>0.2f?0.2f:accelB;
			accelB = accelB<-0.2f?-0.2f:accelB;

			accelZ = accelZ>0.6f?0.6f:accelZ;
			accelZ = accelZ<-0.6f?-0.6f:accelZ;

			accelX = accelX>0.6f?0.6f:accelX;
			accelX = accelX<-0.6f?-0.6f:accelX;

			viewAngle -= accelB;

			//Log.d("math", "Math.cos(viewAngle)  " + Math.cos(viewAngle));
			//自機位置変位量
			tmpX = -accelZ * Math.sin(viewAngle) - accelX * Math.cos(viewAngle);
			tmpZ  = -accelZ * Math.cos(viewAngle) + accelX * Math.sin(viewAngle);
			//視線方向更新
			float[] v2 = {prema.getX() + (float)tmpX, prema.getY(), prema.getZ() + (float)tmpZ};

			prema.translate(v2[0], v2[1], v2[2], viewAngle, accelB);

		}

		void stageClear(){
			manager.renderer.clear();
		}

		@Override
		public void setAnswer(int answer){
			this.answer = answer;
		}
		@Override
	public int getAnswer(){
			return answer;
		}

}

