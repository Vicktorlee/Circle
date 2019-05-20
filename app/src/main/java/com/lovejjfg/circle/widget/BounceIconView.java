package com.lovejjfg.circle.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;


import com.lovejjfg.circle.R;
import com.lovejjfg.circle.listener.SimpleAnimatorListener;

import java.util.ArrayList;


//文字


public class BounceIconView extends View {

// TODO: 2019/5/19 0019 根据Duration/每次按顺序对一个字进行PathText操作


    //音符
// TODO: 2019/5/19 0019 旋转/向下/向上/平移 四个Value Animator

    //    时长
    private float duration;
    private static final long shichang=10000;
    //    文字
    private String text;
    private ArrayList<Character> charList;
    private static final String TEST="祝大家520快乐哈";
    private int startChar=0;
    private Path path;
    private Path path2;
    private TextPaint textPaint;
    private float defaultTextX=0;
    private float defaultTextY=0;
    private float perTextWidth;
    private float totalTextWidth;
    private float textHeight;
    private float density;

    private ObjectAnimator charAnimator;
    private int currentChar = 0;//文字偏移量
    public int getCurrentChar() { return currentChar; }
    public void setCurrentChar(int currentChar) { this.currentChar = currentChar; }


    private ObjectAnimator vibrateAnimator;
    private float vibrate=0;
    public float getAmplitude() { return amplitude; }
    public void setAmplitude(float amplitude) { this.amplitude = amplitude; }
    private float amplitude=50.0f;
    public float getVibrate() { return vibrate; }
    public void setVibrate(float vibrate) { this.vibrate = vibrate;}

    private ObjectAnimator timeAnimator;
    public float getCurrentTime() { return currentTime; }
    public void setCurrentTime(float currentTime) { this.currentTime = currentTime; }
    private float currentTime=0;

//    音符
    private Bitmap note;
    private Matrix noteMatrix;
    private ObjectAnimator rotateAnimator;
    private ObjectAnimator downAnimator;
    private ObjectAnimator upAnimator;
    private Path downPath;
    private Path upPath;

    public Point getNowPoint() {
        return nowPoint;
    }

    public void setNowPoint(int x, int y) {
        this.nowPoint = new Point(x,y);
    }

    private Point nowPoint;
    private Bitmap tempBitmap;
    private Canvas tempCanvas;
    private Paint notePaint;
    private int noteStartX=0;
    private int noteStartY=0;
    private int noteStartR=-90;
   SimpleAnimatorListener listener;

    private int offsetX=-1;
    public int getOffsetX() {return offsetX; }
    public void setOffsetX(int offsetX) { this.offsetX = offsetX;}
    public int getOffsetY() { return offsetY; }
    public void setOffsetY(int offsetY) { this.offsetY = offsetY; }
    private int offsetY=-1;
    private int offsetR=-1;
    public int getOffsetR() { return offsetR; }
    public void setOffsetR(int offsetR) { this.offsetR = offsetR; }
//    位置坐标相关
    private int currentHeight;
    private int currentWidth;
//    构造方法
    public BounceIconView(Context context) { this(context, null); }

    public BounceIconView(Context context, AttributeSet attrs) { this(context, attrs, 0); }

    public BounceIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init(); }

    private void init(){
        density=getResources().getDisplayMetrics().density;
//        文字
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(sp2px(getContext(), 20));
        textPaint.setColor(Color.RED);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textHeight = textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top;
        totalTextWidth=textPaint.measureText(TEST);
        perTextWidth=totalTextWidth/TEST.length();

        defaultTextX=currentWidth/2==0?300:currentWidth/2;
        defaultTextY=currentHeight/2==0?700:currentHeight/2;
        charList=new ArrayList<>();
        for (int i=0;i<TEST.length();i++){ charList.add(TEST.charAt(i)); }

//        音符
        note= BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        notePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        notePaint.setStyle(Paint.Style.STROKE);
        notePaint.setStrokeWidth(5);
        notePaint.setColor(Color.GREEN);
    }
    private void initAnim(int width,int height){
        rotateAnimator=ObjectAnimator.ofInt(this,mCurrentRotate,0);
        rotateAnimator.setDuration(shichang/(TEST.length()));
        rotateAnimator.setIntValues(0,360);
        rotateAnimator.setRepeatCount(TEST.length()*2);

        downPath=new Path();
        downPath.moveTo(0,0);
        setPath(downPath,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
        downAnimator=ObjectAnimator.ofInt(note,mOffsetX,mOffsetY,downPath);
        downAnimator.setDuration(shichang/(TEST.length())/2);
        downAnimator.setInterpolator(new AccelerateInterpolator());
        listener=new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                vibrateAnimator.setDuration(200);
                setAmplitude(50);
                vibrateAnimator.setFloatValues(0,amplitude,0);
                vibrateAnimator.start();
                vibrateAnimator.setAutoCancel(true);

                setNowPoint(mOffsetX.get(note),mOffsetY.get(note));
                upPath.reset();
                upPath.moveTo(nowPoint.x,nowPoint.y);
                setPath(upPath,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                upAnimator.cancel();
                upAnimator=ObjectAnimator.ofInt(note,mOffsetX,mOffsetY,upPath);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        downPath.reset();
                        setNowPoint(mOffsetX.get(note),mOffsetY.get(note));
                        downPath.reset();
                        downPath.moveTo(getNowPoint().x,getNowPoint().y);
                        setPath(downPath,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
                        downAnimator.cancel();
                        downAnimator=ObjectAnimator.ofInt(note,mOffsetX,mOffsetY,downPath);
                        downAnimator.setDuration(shichang/(TEST.length())/2);
                        downAnimator.setInterpolator(new AccelerateInterpolator());
                        downAnimator.addListener(listener);
                        downAnimator.start();
                    }
                });
                upAnimator.start();
            }
        };
        downAnimator.addListener(listener);
        upPath=new Path();
        upPath.moveTo(0,0);
        setPath(upPath,100,100,50,50);
        upAnimator=ObjectAnimator.ofInt(note,mOffsetX,mOffsetY,upPath);
        upAnimator.setDuration(shichang/(TEST.length())/2);
        upAnimator.setInterpolator(new DecelerateInterpolator());
        upAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setPath(upPath,100,100,50,50);
                downAnimator.start();
            }
        });

        vibrateAnimator=ObjectAnimator.ofFloat(this,mCurVibrate,0);
        vibrateAnimator.setInterpolator(new BounceInterpolator());
        path = new Path();path2=new Path();
        charAnimator=ObjectAnimator.ofInt(this,mCurChar,0);
        charAnimator.setDuration(shichang);
        charAnimator.setIntValues(startChar,TEST.length());
        charAnimator.setInterpolator(new LinearInterpolator());
        charAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });

        timeAnimator=ObjectAnimator.ofFloat(this,mCurrentTime,0);
        timeAnimator.setDuration(shichang);
        timeAnimator.setFloatValues(0,shichang);
        timeAnimator.setInterpolator(new LinearInterpolator());
        timeAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                charAnimator.start();
                rotateAnimator.start();
                downAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }
        });
        timeAnimator.start();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(defaultTextX,defaultTextY);
        for (int i=0;i<charList.size();i++){
            if (mCurChar.get(BounceIconView.this)==i){
                path.reset();path.moveTo(0,0);path.quadTo(perTextWidth/2,vibrate,perTextWidth,0);
                canvas.drawTextOnPath(charList.get(i).toString(),path,0,0,textPaint);
            }else {
                path2.reset();path2.moveTo(0,0);path2.lineTo(perTextWidth,0);
                canvas.drawTextOnPath(charList.get(i).toString(),path2,0,0,textPaint);
            }
            canvas.translate(perTextWidth,0);
        }
        invalidate();
        canvas.restore();

        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(offsetX,offsetY);
        canvas.drawBitmap(tempBitmap,noteMatrix,notePaint);
        invalidate();
        canvas.restore();

    }

    //    工具方法
    private Path setPath(Path path,int eX, int eY, int cX, int cY){
        path.rQuadTo(cX,cY,eX,eY);
        return path;
    }
    public static float sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentHeight=h;
        currentWidth=w;
        Log.d("currentHeight",String.valueOf(currentHeight));
        Log.d("currentWidth",String.valueOf(currentWidth));
        initAnim(currentWidth,currentHeight); }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); }

    private Property<BounceIconView,Integer> mCurChar=new Property<BounceIconView, Integer>(Integer.class,
            "currentChar") {
        @Override
        public Integer get(BounceIconView object) {
            return object.getCurrentChar();
        }

        @Override
        public void set(BounceIconView object, Integer value) {
            object.setCurrentChar(value);
        }
    };
    private Property<BounceIconView,Float> mCurVibrate=new Property<BounceIconView, Float>(Float.class,
            "vibrate") {
        @Override
        public Float get(BounceIconView object) {
            return object.getVibrate();
        }

        @Override
        public void set(BounceIconView object, Float value) {
            object.setVibrate(value);
        }
    };
    private Property<BounceIconView,Float> mCurrentTime=new Property<BounceIconView, Float>(Float.class,
            "time") {
        @Override
        public Float get(BounceIconView object) {
            return object.getCurrentTime();
        }

        @Override
        public void set(BounceIconView object, Float value) {
            object.setCurrentTime(value);
        }
    };
    private Property<BounceIconView,Integer> mCurrentRotate=new Property<BounceIconView, Integer>(Integer.class,
            "picRotate") {
        @Override
        public Integer get(BounceIconView object) {
            return object.getOffsetR();
        }

        @Override
        public void set(BounceIconView object, Integer value) {
            object.setOffsetR(value);
        }
    };
    private Property<Bitmap,Integer> mOffsetX=new Property<Bitmap, Integer>(Integer.class,
            "offsetX") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getOffsetX(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setOffsetX(value); }
    };
    private Property<Bitmap,Integer> mOffsetY=new Property<Bitmap, Integer>(Integer.class,
            "offsetY") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getOffsetY(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setOffsetY(value); }
    };
}
