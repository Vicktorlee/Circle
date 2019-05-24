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
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
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
    private static final String TEST="你再骂？有本事你去找物管啊！";
    private int startChar=0;//文字偏移量
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
    private int currentChar;
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

    private ObjectAnimator shadowAnimator1;
    private ObjectAnimator shadowAnimator2;
    private ObjectAnimator shadowAnimator3;
    private ObjectAnimator shadowAnimator4;
    private SimpleAnimatorListener shadowListener1;
    private SimpleAnimatorListener shadowListener2;
    private SimpleAnimatorListener shadowListener3;
    private SimpleAnimatorListener shadowListener4;
    private Path dPath1,dPath2,dPath3,dPath4;
    private Path uPath1,uPath2,uPath3,uPath4;

    private Bitmap tempBitmap;
    private Canvas tempCanvas;
    private Paint notePaint;
    private ArrayList<Paint> shadowPaints=new ArrayList<>();
    private int noteStartX=0;
    private int noteStartY=0;
    private int noteStartR=-90;
    private SimpleAnimatorListener listener;

    private int offsetX=-1;
    private int offsetY=-1;
    public int getOffsetX() {return offsetX; }
    public void setOffsetX(int offsetX) { this.offsetX = offsetX;}
    public int getOffsetY() { return offsetY; }
    public void setOffsetY(int offsetY) { this.offsetY = offsetY; }

    private int shadowX1;
    private int shadowX2;
    private int shadowX3;
    private int shadowX4;
    private int shadowY1;
    private int shadowY2;
    private int shadowY3;
    private int shadowY4;
    public int getShadowX1() { return shadowX1; }
    public int getShadowX2() { return shadowX2; }
    public int getShadowX3() { return shadowX3; }
    public int getShadowX4() { return shadowX4; }
    public void setShadowX1(int shadowX1) { this.shadowX1 = shadowX1; }
    public void setShadowX2(int shadowX2) { this.shadowX2 = shadowX2; }
    public void setShadowX3(int shadowX3) { this.shadowX3 = shadowX3; }
    public void setShadowX4(int shadowX4) { this.shadowX4 = shadowX4; }
    public int getShadowY1() { return shadowY1; }
    public int getShadowY2() { return shadowY2; }
    public int getShadowY3() { return shadowY3; }
    public int getShadowY4() { return shadowY4; }
    public void setShadowY1(int shadowY1) { this.shadowY1 = shadowY1; }
    public void setShadowY2(int shadowY2) { this.shadowY2 = shadowY2; }
    public void setShadowY3(int shadowY3) { this.shadowY3 = shadowY3; }
    public void setShadowY4(int shadowY4) { this.shadowY4 = shadowY4; }



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
        notePaint.setStrokeWidth(2);
        notePaint.setColor(Color.RED);

        Paint shadowPaint1=new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint1.setStyle(Paint.Style.STROKE);
        shadowPaint1.setStrokeWidth(2);
        shadowPaint1.setColor(Color.RED);
        shadowPaint1.setAlpha(150);
        shadowPaints.add(shadowPaint1);

        Paint shadowPaint2=new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint2.setStyle(Paint.Style.STROKE);
        shadowPaint2.setStrokeWidth(2);
        shadowPaint2.setColor(Color.RED);
        shadowPaint2.setAlpha(150);
        shadowPaints.add(shadowPaint2);

        Paint shadowPaint3=new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint3.setStyle(Paint.Style.STROKE);
        shadowPaint3.setStrokeWidth(2);
        shadowPaint3.setColor(Color.RED);
        shadowPaint3.setAlpha(100);
        shadowPaints.add(shadowPaint3);

        Paint shadowPaint4=new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint4.setStyle(Paint.Style.STROKE);
        shadowPaint4.setStrokeWidth(2);
        shadowPaint4.setColor(Color.RED);
        shadowPaint4.setAlpha(80);
        shadowPaints.add(shadowPaint4);

        Paint shadowPaint5=new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint5.setStyle(Paint.Style.STROKE);
        shadowPaint5.setStrokeWidth(2);
        shadowPaint5.setColor(Color.RED);
        shadowPaint5.setAlpha(50);
        shadowPaints.add(shadowPaint5);
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
                vibrateAnimator.setDuration(100);
                setAmplitude(80);
                vibrateAnimator.setFloatValues(0,amplitude,0);
                vibrateAnimator.setAutoCancel(true);
                vibrateAnimator.start();
                upPath.reset();
                upPath.moveTo(mOffsetX.get(note),mOffsetY.get(note));
                setPath(upPath,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                upAnimator.cancel();
                upAnimator=ObjectAnimator.ofInt(note,mOffsetX,mOffsetY,upPath);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        downPath.reset();
                        downPath.moveTo(mOffsetX.get(note),mOffsetY.get(note));
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

        uPath1=new Path();
        dPath1=new Path();
        shadowListener1=new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                uPath1.reset();
                uPath1.moveTo(curShadowX1.get(note),curShadowY1.get(note));
                setPath(uPath1,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                ObjectAnimator upAnimator=ObjectAnimator.ofInt(note,curShadowX1,curShadowY1,uPath1);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dPath1.reset();
                        dPath1.moveTo(curShadowX1.get(note),curShadowY1.get(note));
                        setPath(dPath1,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
                        shadowAnimator1.cancel();
                        shadowAnimator1=ObjectAnimator.ofInt(note,curShadowX1,curShadowY1,dPath1);
                        shadowAnimator1.setDuration(shichang/(TEST.length())/2);
                        shadowAnimator1.setInterpolator(new AccelerateInterpolator());
                        shadowAnimator1.addListener(shadowListener1);
                        shadowAnimator1.start();
                    }
                });
                upAnimator.start();
            }
        };
        dPath2=new Path();
        uPath2=new Path();
        shadowListener2=new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                uPath2.reset();
                uPath2.moveTo(curShadowX2.get(note),curShadowY2.get(note));
                setPath(uPath2,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                ObjectAnimator upAnimator=ObjectAnimator.ofInt(note,curShadowX2,curShadowY2,uPath2);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dPath2.reset();
                        dPath2.moveTo(curShadowX2.get(note),curShadowY2.get(note));
                        setPath(dPath2,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
                        shadowAnimator2.cancel();
                        shadowAnimator2=ObjectAnimator.ofInt(note,curShadowX2,curShadowY2,dPath2);
                        shadowAnimator2.setDuration(shichang/(TEST.length())/2);
                        shadowAnimator2.setInterpolator(new AccelerateInterpolator());
                        shadowAnimator2.addListener(shadowListener2);
                        shadowAnimator2.start();
                    }
                });
                upAnimator.start();
            }
        };
        uPath3=new Path();
        dPath3=new Path();
        shadowListener3=new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                uPath3.reset();
                uPath3.moveTo(curShadowX3.get(note),curShadowY3.get(note));
                setPath(uPath3,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                ObjectAnimator upAnimator=ObjectAnimator.ofInt(note,curShadowX3,curShadowY3,uPath3);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dPath3.reset();
                        dPath3.moveTo(curShadowX3.get(note),curShadowY3.get(note));
                        setPath(dPath3,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
                        shadowAnimator3.cancel();
                        shadowAnimator3=ObjectAnimator.ofInt(note,curShadowX3,curShadowY3,dPath3);
                        shadowAnimator3.setDuration(shichang/(TEST.length())/2);
                        shadowAnimator3.setInterpolator(new AccelerateInterpolator());
                        shadowAnimator3.addListener(shadowListener3);
                        shadowAnimator3.start();
                    }
                });
                upAnimator.start();
            }
        };
        uPath4=new Path();
        dPath4=new Path();
        shadowListener4=new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                uPath4.reset();
                uPath4.moveTo(curShadowX4.get(note),curShadowY4.get(note));
                setPath(uPath4,(int)perTextWidth/2,-100,(int)perTextWidth/2+10,-60);
                ObjectAnimator upAnimator=ObjectAnimator.ofInt(note,curShadowX4,curShadowY4,uPath4);
                upAnimator.setDuration(shichang/(TEST.length())/2);
                upAnimator.setInterpolator(new DecelerateInterpolator());
                upAnimator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dPath4.reset();
                        dPath4.moveTo(curShadowX4.get(note),curShadowY4.get(note));
                        setPath(dPath4,(int)perTextWidth/2,100,(int)perTextWidth/2+10,40);
                        shadowAnimator4.cancel();
                        shadowAnimator4=ObjectAnimator.ofInt(note,curShadowX4,curShadowY4,dPath4);
                        shadowAnimator4.setDuration(shichang/(TEST.length())/2);
                        shadowAnimator4.setInterpolator(new AccelerateInterpolator());
                        shadowAnimator4.addListener(shadowListener4);
                        shadowAnimator4.start();
                    }
                });
                upAnimator.start();
            }
        };
        shadowAnimator1=ObjectAnimator.ofInt(note,curShadowX1,curShadowY1,downPath);
        shadowAnimator1.setDuration(shichang/(TEST.length())/2);
        shadowAnimator1.setInterpolator(new AccelerateInterpolator());
        shadowAnimator1.setStartDelay(100);
        shadowAnimator1.addListener(shadowListener1);

        shadowAnimator2=ObjectAnimator.ofInt(note,curShadowX2,curShadowY2,downPath);
        shadowAnimator2.setDuration(shichang/(TEST.length())/2);
        shadowAnimator2.setInterpolator(new AccelerateInterpolator());
        shadowAnimator2.setStartDelay(200);
        shadowAnimator2.addListener(shadowListener2);

        shadowAnimator3=ObjectAnimator.ofInt(note,curShadowX3,curShadowY3,downPath);
        shadowAnimator3.setDuration(shichang/(TEST.length())/2);
        shadowAnimator3.setInterpolator(new AccelerateInterpolator());
        shadowAnimator3.setStartDelay(300);
        shadowAnimator3.addListener(shadowListener3);

        shadowAnimator4=ObjectAnimator.ofInt(note,curShadowX4,curShadowY4,downPath);
        shadowAnimator4.setDuration(shichang/(TEST.length())/2);
        shadowAnimator4.setInterpolator(new AccelerateInterpolator());
        shadowAnimator4.setStartDelay(400);
        shadowAnimator4.addListener(shadowListener4);

        vibrateAnimator=ObjectAnimator.ofFloat(this,mCurVibrate,0);
        vibrateAnimator.setInterpolator(new BounceInterpolator());
        path = new Path();path2=new Path();
        charAnimator=ObjectAnimator.ofInt(this,mCurChar,0);
        charAnimator.setDuration(shichang);
        charAnimator.setIntValues(startChar,TEST.length());
        charAnimator.setInterpolator(new LinearInterpolator());
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
                shadowAnimator1.start();
                shadowAnimator2.start();
                shadowAnimator3.start();
                shadowAnimator4.start();
            }

        });
        timeAnimator.start();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(defaultTextX,defaultTextY);
        for (int i=0;i<charList.size()-1;i++){
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


//本体
        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(noteStartR+offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(offsetX,offsetY);
        canvas.drawBitmap(tempBitmap,noteMatrix,notePaint);
        canvas.restore();

        //残影1
        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(-5+noteStartR+offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(shadowX1,shadowY1);
        canvas.drawBitmap(tempBitmap,noteMatrix,shadowPaints.get(0));
        canvas.restore();

        //残影2
        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(-10+noteStartR+offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(shadowX2,shadowY2);
        canvas.drawBitmap(tempBitmap,noteMatrix,shadowPaints.get(1));
        canvas.restore();

        //残影3
        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(-15+noteStartR+offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(shadowX3,shadowY3);
        canvas.drawBitmap(tempBitmap,noteMatrix,shadowPaints.get(2));
        canvas.restore();

        //残影4
        canvas.save();
        canvas.translate(noteStartX+defaultTextX-perTextWidth,noteStartY+defaultTextY-textHeight*3);
        noteMatrix=new Matrix();
        tempBitmap=Bitmap.createBitmap(note.getWidth()+1,note.getHeight()+1, Bitmap.Config.ARGB_8888);
        tempCanvas=new Canvas(tempBitmap);
        noteMatrix.postRotate(-20+noteStartR+offsetR,note.getWidth()/2,note.getHeight()/2);
        tempCanvas.drawBitmap(note,noteMatrix,notePaint);
        noteMatrix=new Matrix();
        noteMatrix.postTranslate(shadowX4,shadowY4);
        canvas.drawBitmap(tempBitmap,noteMatrix,shadowPaints.get(3));
        canvas.restore();


        invalidate();
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

    private Property<Bitmap,Integer> curShadowX1=new Property<Bitmap, Integer>(Integer.class,
            "shadowX1") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowX1(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowX1(value); }
    };
    private Property<Bitmap,Integer> curShadowX2=new Property<Bitmap, Integer>(Integer.class,
            "shadowX2") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowX2(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowX2(value); }
    };
    private Property<Bitmap,Integer> curShadowX3=new Property<Bitmap, Integer>(Integer.class,
            "shadowX3") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowX3(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowX3(value); }
    };
    private Property<Bitmap,Integer> curShadowX4=new Property<Bitmap, Integer>(Integer.class,
            "shadowX4") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowX4(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowX4(value); }
    };
    private Property<Bitmap,Integer> curShadowY1=new Property<Bitmap, Integer>(Integer.class,
            "shadowY1") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowY1(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowY1(value); }
    };
    private Property<Bitmap,Integer> curShadowY2=new Property<Bitmap, Integer>(Integer.class,
            "shadowY2") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowY2(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowY2(value); }
    };
    private Property<Bitmap,Integer> curShadowY3=new Property<Bitmap, Integer>(Integer.class,
            "shadowY3") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowY3(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowY3(value); }
    };
    private Property<Bitmap,Integer> curShadowY4=new Property<Bitmap, Integer>(Integer.class,
            "shadowY4") {
        @Override
        public Integer get(Bitmap object) { return BounceIconView.this.getShadowY4(); }

        @Override
        public void set(Bitmap object, Integer value) { BounceIconView.this.setShadowY4(value); }
    };
}
