package com.hansion.learnopengles.opengl.shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/12/29 14:17
 */
public class Square {

    /**
     * 顶点坐标数据缓冲区（FloatBuffer 类型）
     */
    private FloatBuffer vertexBuffer;

    /**
     * 绘制顺序数据缓冲区（ShortBuffer 类型）
     */
    private ShortBuffer drawListBuffer;

    /**
     * 顶点坐标数据的数组
     */
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // 左上
            -0.5f, -0.5f, 0.0f,   // 左下
            0.5f, -0.5f, 0.0f,   // 右下
            0.5f,  0.5f, 0.0f }; // 右上

    /**
     * 绘制顶点的顺序
     */
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    public Square() {
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // 初始化绘制顶点的顺序数据的字节缓冲区
        // 通过 allocateDirect 方法获取到 DirectByteBuffer 实例,参数是绘制顶点的顺序所占字节，每个short占2个字节
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}
