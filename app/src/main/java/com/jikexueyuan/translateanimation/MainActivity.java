package com.jikexueyuan.translateanimation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private AnimationSet as;
    private TranslateAnimation taX,taY;
    private Button btnXMLView,btnJavaView,btnXMLProperties,btnJavaProperties;
    private ImageView iv3DRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        XML动画View
        btnXMLView = (Button) findViewById(R.id.btnXMLView);
        btnXMLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.view_animation));
            }
        });
//      java动画View
        as = new AnimationSet(true);

        taX = new TranslateAnimation(0,200,0,0);
        taX.setDuration(500);
        as.addAnimation(taX);

        taY = new TranslateAnimation(0,0,0,100);
        taY.setDuration(500);
        taY.setStartOffset(500);
        as.addAnimation(taY);

        btnJavaView = (Button) findViewById(R.id.btnJavaView);

        btnJavaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(as);
            }
        });
//      通过配置文件实现属性动画
        btnXMLProperties = (Button) findViewById(R.id.btnXMLProperties);

        btnXMLProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,R.animator.properties_animation);
                animatorSet.setTarget(v);
                animatorSet.start();
            }
        });

//        通过java代码实现属性动画翻转
        btnJavaProperties = (Button) findViewById(R.id.btnJavaProperties);

        btnJavaProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator translateX = ObjectAnimator.ofFloat(v,"translationX",0,200).setDuration(500);
                translateX.start();
                ObjectAnimator translateY = ObjectAnimator.ofFloat(v,"translationY",0,100).setDuration(500);
                translateY.setStartDelay(500);
                translateY.start();
                ObjectAnimator backY = ObjectAnimator.ofFloat(v,"translationY",100,0).setDuration(500);
                backY.setStartDelay(1000);
                backY.start();
                ObjectAnimator backX = ObjectAnimator.ofFloat(v,"translationX",200,0).setDuration(500);
                backX.setStartDelay(1500);
                backX.start();
            }
        });

        iv3DRotate = (ImageView) findViewById(R.id.iv3DRotate);

//        设置图片点击翻转事件
        iv3DRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rotate3d rotate = new Rotate3d();
                rotate.setDuration(1000);
                iv3DRotate.measure(0,0);
//                测定中心点
                rotate.setCenter(iv3DRotate.getMeasuredWidth() / 2, iv3DRotate.getMeasuredHeight() / 2);
                rotate.setFillAfter(true); // 使动画结束后定在最终画面，如果不设置为true，则将会回到初始画面
                iv3DRotate.startAnimation(rotate);
            }
        });
    }

//    3D翻转实现方法
    class Rotate3d extends Animation {
        private float mCenterX = 0;
        private float mCenterY = 0;

        public void setCenter(float centerX, float centerY) {
            mCenterX = centerX;
            mCenterY = centerY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix matrix = t.getMatrix();
            Camera camera = new Camera();
            camera.save();
            camera.rotateY(180 * interpolatedTime);
            camera.getMatrix(matrix);
            camera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
        }
    }

}
