package mrh5493.edu.psu.doodlepadpro;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
/**
 * Created by lrs5302 on 2/12/16.
 */

public class DrawingView extends View{

        //drawing path
        private Path drawPath;
        //drawing and canvas paint
        private Paint drawPaint, canvasPaint;
        //initial color
        private int paintColor = 0xFF660000;
        //canvas
        private Canvas drawCanvas;
        //canvas bitmap
        private Bitmap canvasBitmap;

        private boolean erase=false;

        private float brushSize, lastBrushSize;


        public DrawingView(Context context, AttributeSet attrs){
            super(context, attrs);
            setupDrawing();
        }

         public void setErase(boolean isErase){
            //set erase true or false
             erase=isErase;

             if(erase) {

                 drawPaint.setColor(Color.WHITE);//set the color to white

             }
             else drawPaint.setColor(paintColor);

         }

    public void startNew(){
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
        }
        private void setupDrawing(){
            //get drawing area setup for interaction
            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(paintColor);
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(20);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);

            canvasPaint = new Paint(Paint.DITHER_FLAG);
            brushSize = getResources().getInteger(R.integer.medium_size);
            lastBrushSize = brushSize;
            drawPaint.setStrokeWidth(brushSize);
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
            return true;
        }
        public void setColor(String newColor){
            invalidate();
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);

        }
        public void setBrushSize(float newSize){
            float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    newSize, getResources().getDisplayMetrics());
            brushSize=pixelAmount;
            drawPaint.setStrokeWidth(brushSize);
        }
        public void setLastBrushSize(float lastSize){
            lastBrushSize=lastSize;
        }
        public float getLastBrushSize(){
            return lastBrushSize;
        }
}
