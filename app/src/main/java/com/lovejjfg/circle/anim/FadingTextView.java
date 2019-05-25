package com.lovejjfg.circle.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lovejjfg.circle.R;
import com.lovejjfg.circle.anim.factory.FlyawayFactory;
public class FadingTextView extends View {
    private static final String TAG = "ExplosionField";
    private static final String text="消散文字动画";
//    字体转图片
    private Bitmap textPic;
    private int currentHeight;
    private int currentWidth;
    private int defaultTextX=0;
    private int defaultTextY=0;
    private FlyawayFactory factory;
    private ExplosionAnimator animator;
    public FadingTextView(Context context) {
        super(context);
        init();
    }
    public FadingTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }
    private void init() {
        factory=new FlyawayFactory();
        textPic= BitmapFactory.decodeResource(getResources(), R.drawable.ceshi);
        defaultTextX=currentWidth/2==0?300:currentWidth/2;
        defaultTextY=currentHeight/2==0?700:currentHeight/2;
    }

    private void initAnim(){
        final Rect rect = new Rect();
        rect.set(0,0,textPic.getWidth(),textPic.getHeight());
        animator=new ExplosionAnimator(this,textPic,rect,factory);
        animator.setStartDelay(3000);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(100,200);
        animator.draw(canvas);
        invalidate();
    }


    public static Bitmap fromText(float textSize, String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int width = (int)paint.measureText(text);
        int height = fm.descent - fm.ascent;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, fm.leading - fm.ascent, paint);
        canvas.save();

        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentHeight=h;
        currentWidth=w;
        Log.d("currentHeight",String.valueOf(currentHeight));
        Log.d("currentWidth",String.valueOf(currentWidth));
        initAnim();
    }
}
