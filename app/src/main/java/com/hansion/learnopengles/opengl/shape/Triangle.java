package com.hansion.learnopengles.opengl.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.hansion.learnopengles.opengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/12/29 11:24
 */
public class Triangle {

    //顶点着色器
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // 用于访问和设置视图转换
    private int mMVPMatrixHandle;

    //片元着色器
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;

    // 坐标数组中的顶点坐标个数
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // 逆时针:
            0.0f, 0.622008459f, 0.0f, // 上
            -0.5f, -0.311004243f, 0.0f, // 左下
            0.5f, -0.311004243f, 0.0f  // 右下
    };

    private final int mProgram;

    // 设置颜色 RGBA
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};


    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex



    public Triangle() {
        // 初始化形状中顶点坐标数据的字节缓冲区
        // 通过 allocateDirect 方法获取到 DirectByteBuffer 实例,参数是坐标所占字节，每个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);

        // 设置ByteBuffer的字节序为当前硬件平台的字节序
        bb.order(ByteOrder.nativeOrder());

        // 通过ByteBuffer中获得一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        // 存储顶点坐标信息到FloatBuffer
        vertexBuffer.put(triangleCoords);
        // 设置缓冲区从第一个位置读取顶点坐标
        vertexBuffer.position(0);


        // 编译着色器代码
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // 创建一个OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // 添加顶点着色器到Program
        GLES20.glAttachShader(mProgram, vertexShader);

        // 添加片元着色器到Program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 链接创建OpenGL ES可执行文件
        GLES20.glLinkProgram(mProgram);
    }


    public void draw(float[] mvpMatrix) {
        // 将Program添加至OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器的vPosition成员
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用顶点属性数组。如果启用，那么当glDrawArrays或者glDrawElements被调用时，顶点属性数组会被使用。
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备形状坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取片元着色器的vColor成员
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 得到绘制对象的变换矩阵句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 将投影和视图转换传递给着色器
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // 设置绘制颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 绘制形状
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 禁用顶点属性数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
