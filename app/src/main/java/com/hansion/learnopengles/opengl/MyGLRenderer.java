package com.hansion.learnopengles.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.hansion.learnopengles.opengl.shape.Square;
import com.hansion.learnopengles.opengl.shape.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/12/29 10:28
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {


    private Triangle mTriangle;
    private Square mSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    //用于旋转的变换矩阵
    private float[] mRotationMatrix = new float[16];

    public volatile float mAngle;


    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }


    /**
     * 加载并编译着色器代码
     * @param type 渲染器类型 {GLES20.GL_VERTEX_SHADER, GLES20.GL_FRAGMENT_SHADER}
     * @param shaderCode  GLSL渲染器代码
     * @return
     */
    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //指定刷新颜色缓冲区时所用的颜色
        //需要注意的是glClearColor只起到Set的作用，并不Clear。
        //glClearColor更类似与初始化，如果不做，新的绘制就会绘制在以前的上面，类似于混合，而不是覆盖
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // 初始化三角形
        mTriangle = new Triangle();
        // 初始化矩形
        mSquare = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //glViewport用于告诉OpenGL应把渲染之后的图形绘制在窗体的哪个部位、大小
        GLES20.glViewport(0, 0, width, height);

        //计算宽高比
        float ratio = (float) width / height;

        //填充投影矩阵 mProjectionMatrix
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // glClear表示清除缓冲  传入GL_COLOR_BUFFER_BIT指要清除颜色缓冲
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 设置相机视角
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // 计算投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //用于存储最终合成的矩阵
        float[] scratch = new float[16];

//        // 创建一个旋转变换矩阵 mRotationMatrix
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);


        //根据传入的角度mAngle,进行旋转
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);


        // 将旋转矩阵与投影和相机视图组合在一起
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // 绘制形状
        mTriangle.draw(scratch);
    }
}
