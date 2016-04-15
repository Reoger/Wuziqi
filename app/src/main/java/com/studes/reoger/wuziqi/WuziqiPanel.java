package com.studes.reoger.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reoger on 2016/4/14.
 */
public class WuziqiPanel extends View {
    /**
     * 整个屏幕的宽度
     */
    private int mPanalwidth;
    /**
     * 每个小格子的边长
     */
    private float mWidthLine;
    private static final int MAX_LINE = 10;
    private static final int MAX_COUNT_ITEM =5;

    private boolean mIsGameOver     = false;
    private boolean mIsWhiteWinner  = false;

    //画笔
    private Paint mPaint = new Paint();

    private ArrayList<Point> mWhiteArryList = new ArrayList<Point>();
    private ArrayList<Point> mBalckArryList = new ArrayList<Point>();
    private boolean mIsWhite = true;//用于判断是否为白棋动

    private Bitmap mWhitePiece;
    private Bitmap mBalckPiece;

    private static final float RATE = 3.0f / 4;           //照片压缩的比例

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0x44ff0000);
        initPaint();

    }

    private boolean initPaint() {
        mPaint.setAntiAlias(true);//图像边缘相对清晰一点，锯齿痕迹不那么明显
        mPaint.setColor(0X88000000);
        mPaint.setDither(true);//防抖动。
        mPaint.setStyle(Paint.Style.STROKE);

        mBalckPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mPanalwidth = w;
        mWidthLine = mPanalwidth * 1.0f / MAX_LINE;

        int pieceSize = (int) (RATE * mWidthLine);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceSize, pieceSize, false);
        mBalckPiece = Bitmap.createScaledBitmap(mBalckPiece, pieceSize, pieceSize, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorad(canvas);
        drawPiece(canvas);
        
        checkGameIsOver();
    }

    /**
     * 判断是否结束游戏
     */
    private void checkGameIsOver() {
        boolean whiteWiner = checkFiveLine(mWhiteArryList);
        boolean blackWiner = checkFiveLine(mBalckArryList);

        if(whiteWiner||blackWiner){
            mIsGameOver=true;
            mIsWhiteWinner = whiteWiner;
            String text = mIsWhiteWinner?"白棋胜利":"黑旗胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    
    }

    private boolean checkFiveLine(List<Point> points) {
        for (Point p:points) {
            int x = p.x;
            int y = p.y;
            boolean win1 = checkHorizontal(x, y, points);
            if(win1) return true;
            boolean win2 = checkVertical(x, y, points);
            if(win2) return true;
            boolean win3 = checkLeftOblique(x, y, points);
            if(win3) return true;
            boolean win4 = checkRightOblique(x, y, points);
            if(win4) return true;
        }

        return false;
    }

    private boolean checkRightOblique(int x, int y, List<Point> points) {
        int count =1;

        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }
        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }

        return false;
    }

    private boolean checkLeftOblique(int x, int y, List<Point> points) {
        int count =1;

        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }
        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }

        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count =1;

        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }
        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }

        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count =1;

        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }
        for(int i=1;i<MAX_COUNT_ITEM;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_ITEM){
            return true;
        }

        return false;
    }


    /**
     * 绘制旗子
     *
     * @param canvas
     */
    private void drawPiece(Canvas canvas) {
        float a = 0;
        for (int i = 0, n = mBalckArryList.size(); i < n; i++) {
            Point BalckPiece = mBalckArryList.get(i);
            canvas.drawBitmap(mBalckPiece, (BalckPiece.x + (1 - RATE) / 2) * mWidthLine,
                    (BalckPiece.y + (1 - RATE) / 2) * mWidthLine, null);
        }


        for (int i = 0; i < mWhiteArryList.size(); i++) {
            Point WhitePiece = mWhiteArryList.get(i);
            canvas.drawBitmap(mWhitePiece, (WhitePiece.x + (1 - RATE) / 2) * mWidthLine,
                    (WhitePiece.y + (1 - RATE) / 2) * mWidthLine, null);

        }

    }

    private void drawBorad(Canvas canvas) {
        int width = mPanalwidth;//屏幕的宽度
        float linewitd = mWidthLine;//小正方形的边长

        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (mWidthLine / 2);
            int endX = (int) (width - mWidthLine / 2);
            int y = (int) ((0.5 + i) * linewitd);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver)return false;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getVerticalPoint(x, y);
            if (mBalckArryList.contains(p) || mWhiteArryList.contains(p)) {
                return false;
            }
            if (mIsWhite == true) {
                mWhiteArryList.add(p);
            } else {
                mBalckArryList.add(p);
            }
            mIsWhite = !mIsWhite;
            invalidate();

        }
        return true;
    }

    /**
     * 取得合法的坐标位置
     */
    private Point getVerticalPoint(int x, int y) {
        return new Point((int) (x / mWidthLine), (int) (y / mWidthLine));
    }

    /**
     * 重新开始的逻辑代码
     */
    public  void reStart(){
        mIsGameOver = false;
        mBalckArryList.clear();
        mWhiteArryList.clear();
        invalidate();
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER ="instance_game_over";
    private static final String INSTANCE_WHITE_APPAY ="instance_white_array";
    private static final String INSTANCE_BLACK_APPAY ="instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_APPAY, mWhiteArryList);
        bundle.putParcelableArrayList(INSTANCE_BLACK_APPAY,mBalckArryList);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){//instanceof 运算符是用来在运行时指出对象是否是特定类的一个实例
            Bundle bundle = (Bundle)state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArryList = bundle.getParcelableArrayList(INSTANCE_WHITE_APPAY);
            mBalckArryList = bundle.getParcelableArrayList(INSTANCE_BLACK_APPAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
