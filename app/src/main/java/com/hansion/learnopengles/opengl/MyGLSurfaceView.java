package com.hansion.learnopengles.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/12/29 10:26
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private MyGLRenderer mRenderer;


    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        // 设置EGLContext客户端使用OpenGL ES 2.0 版本
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // 设置渲染器到GLSurfaceView上
        setRenderer(mRenderer);

        //触摸时请求渲染
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //获取触摸点在以View左上角为坐标原点的坐标系中的X/Y坐标
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                //获取两次触摸的横向差值
                float dx = x - mPreviousX;
                //获取两次触摸的纵向差值
                float dy = y - mPreviousY;

                //当触摸点位于View的水平中线以下时,要反向旋转，所以dx要取相反数
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                //当触摸点位于View的竖直中线以左时,要反向旋转，所以dy要取相反数
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                //配合插值比例,计算出一个符合手指移动距离的角度
                float angle = mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR);

                //设置角度到渲染器中
                mRenderer.setAngle(angle);

                // 请求渲染
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
