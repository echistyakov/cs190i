package edu.ucsb.cs.cs190i.evgeny.touchgestures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class TouchImageView extends ImageView {

    private Matrix matrix;
    private PointF markerLocation = null;
    private Paint paint = new Paint();

    // Gesture detectors
    private MyTapGestureDetector tapGestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private MyDragGestureDetector dragGestureDetector;
    private MyRotateGestureDetector rotateGestureDetector;

    public TouchImageView(Context context) {
        super(context);
        this.initialize();
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    public void initialize() {
        this.scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new MyScaleGestureListener());
        this.dragGestureDetector = new MyDragGestureDetector();
        this.rotateGestureDetector = new MyRotateGestureDetector();
        this.tapGestureDetector = new MyTapGestureDetector();
        this.matrix = this.getImageMatrix();
        this.paint.setColor(Color.RED);
        this.markerLocation = null;
        this.setScaleType(ScaleType.MATRIX);
    }

    @Override
    public void setImageBitmap(Bitmap mp) {
        super.setImageBitmap(mp);
        this.matrix = new Matrix();
        this.markerLocation = null;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        // Apply matrix before drawing
        this.setImageMatrix(this.matrix);
        // Draw
        super.onDraw(canvas);
        // Draw marker
        this.drawMarker(canvas);
    }

    private void drawMarker(Canvas canvas) {
        if (markerLocation != null) {
            canvas.drawCircle(markerLocation.x, markerLocation.y, 15, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Tap gesture detector
        this.tapGestureDetector.onTouchEvent(event);

        // Scale gesture handling
        this.scaleGestureDetector.onTouchEvent(event);

        // Drag gesture handling
        this.dragGestureDetector.onTouchEvent(event);

        // Rotation gesture handling
        this.rotateGestureDetector.onTouchEvent(event);

        return true;
    }

    /////////////////
    // Tap Gesture //
    /////////////////
    private class MyTapGestureDetector {
        private PointF tapDownLocation = new PointF(0, 0);

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();

            // Primary pointer going down
            if (action == MotionEvent.ACTION_DOWN) {
                this.tapDownLocation.set(event.getX(), event.getY());
            }
            // Primary pointer is being released
            else if (action == MotionEvent.ACTION_UP) {
                PointF tapUpLocation = new PointF(event.getX(), event.getY());
                if (tapUpLocation.equals(this.tapDownLocation)) {
                    markerLocation = tapUpLocation;
                    invalidate();
                }
            }
            return true;
        }
    }

    ///////////////////
    // Scale Gesture //
    ///////////////////
    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private PointF scaleFocalPoint = new PointF(0, 0);

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            this.scaleFocalPoint.set(detector.getFocusX(), detector.getFocusY());
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            matrix.postScale(scaleFactor, scaleFactor, this.scaleFocalPoint.x, this.scaleFocalPoint.y);
            invalidate();
            return true;
        }
    }

    //////////////////
    // Drag Gesture //
    //////////////////
    private class MyDragGestureDetector {
        private PointF lastLocation = new PointF(0, 0);

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            int pointerCount = event.getPointerCount();
            int currentActionIndex = event.getActionIndex();
            int currentPointerId = event.getPointerId(currentActionIndex);
            int currentPointerIndex = event.findPointerIndex(currentPointerId);

            if (action == MotionEvent.ACTION_DOWN) {
                float newX = event.getX();
                float newY = event.getY();
                this.lastLocation.set(newX, newY);
            } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
                float sumX = 0;
                float sumY = 0;
                for (int i = 0; i < pointerCount; i++) {
                    sumX += event.getX(i);
                    sumY += event.getY(i);
                }
                float newX = sumX / pointerCount;
                float newY = sumY / pointerCount;
                this.lastLocation.set(newX, newY);
            } else if (action == MotionEvent.ACTION_POINTER_UP) {
                float sumX = 0;
                float sumY = 0;
                for (int i = 0; i < pointerCount; i++) {
                    sumX += event.getX(i);
                    sumY += event.getY(i);
                }
                float newX = sumX / pointerCount;
                float newY = sumY / pointerCount;
                matrix.postTranslate(newX - this.lastLocation.x, newY - this.lastLocation.y);
                invalidate();

                float pointerX = event.getX(currentPointerIndex);
                float pointerY = event.getY(currentPointerIndex);
                sumX -= pointerX;
                sumY -= pointerY;
                newX = sumX / (pointerCount - 1);
                newY = sumY / (pointerCount - 1);
                this.lastLocation.set(newX, newY);
            } else if (action == MotionEvent.ACTION_MOVE) {
                float sumX = 0;
                float sumY = 0;
                for (int i = 0; i < pointerCount; i++) {
                    sumX += event.getX(i);
                    sumY += event.getY(i);
                }
                float newX = sumX / pointerCount;
                float newY = sumY / pointerCount;
                matrix.postTranslate(newX - this.lastLocation.x, newY - this.lastLocation.y);
                this.lastLocation.set(newX, newY);
                invalidate();
            }
            return true;
        }
    }

    ////////////////////
    // Rotate Gesture //
    ////////////////////
    private class MyRotateGestureDetector {
        private PointF firstPointerLocation = new PointF(0, 0);
        private PointF secondPointerLocation = new PointF(0, 0);
        private int firstPointerId = -1;  // -1 for "undefined"
        private int secondPointerId = -1; // -1 for "undefined"

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            int currentActionIndex = event.getActionIndex();
            int currentPointerId = event.getPointerId(currentActionIndex);
            int pointerCount = event.getPointerCount();
            int firstPointerIndex = event.findPointerIndex(this.firstPointerId);
            int secondPointerIndex = event.findPointerIndex(this.secondPointerId);

            // First pointer going down
            if (action == MotionEvent.ACTION_DOWN) {
                this.firstPointerId = currentPointerId;
                this.firstPointerLocation.set(event.getX(), event.getY());
            }
            // Second pointer going down
            else if (action == MotionEvent.ACTION_POINTER_DOWN && pointerCount == 2) {
                this.secondPointerId = currentPointerId;
                secondPointerIndex = event.findPointerIndex(this.secondPointerId);
                this.secondPointerLocation.set(event.getX(secondPointerIndex), event.getY(secondPointerIndex));
            }
            // Two tracked pointers are down, moving
            else if (pointerCount == 2 && this.firstPointerId != -1 && this.secondPointerId != -1 && action == MotionEvent.ACTION_MOVE) {
                PointF newFirstPointerLocation = new PointF(event.getX(firstPointerIndex), event.getY(firstPointerIndex));
                PointF newSecondPointerLocation = new PointF(event.getX(secondPointerIndex), event.getY(secondPointerIndex));
                PointF pivotLocation = this.getMiddlePoint(newFirstPointerLocation, newSecondPointerLocation);
                double rotationAngle = this.getAngle(this.firstPointerLocation, this.secondPointerLocation, newFirstPointerLocation, newSecondPointerLocation);

                matrix.postRotate((float)rotationAngle, pivotLocation.x, pivotLocation.y);
                this.firstPointerLocation = newFirstPointerLocation;
                this.secondPointerLocation = newSecondPointerLocation;
                invalidate();
            }
            else if (action == MotionEvent.ACTION_POINTER_UP) {
                if (this.firstPointerId == currentPointerId) {
                    this.firstPointerId = -1;
                } else if (secondPointerId == currentPointerId) {
                    this.secondPointerId = -1;
                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                this.firstPointerId = this.secondPointerId = -1;
            }
            return true;
        }

        private double getAngle(PointF p1, PointF p2, PointF np1, PointF np2) {
            // Based on the following StackOverflow post: http://stackoverflow.com/questions/10682019/android-two-finger-rotation
            double oldAtan = Math.atan2(p1.y - p2.y, p1.x - p2.x);
            double newAtan = Math.atan2(np1.y - np2.y, np1.x - np2.x);
            double angle = Math.toDegrees(newAtan - oldAtan) % 360;
            if (angle < -180) angle += 360;
            if (angle > 180) angle -= 360;
            return angle;
        }

        private PointF getMiddlePoint(PointF p1, PointF p2) {
            return new PointF((p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f);
        }
    }
}