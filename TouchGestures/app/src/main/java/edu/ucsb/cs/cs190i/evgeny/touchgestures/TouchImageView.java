package edu.ucsb.cs.cs190i.evgeny.touchgestures;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class TouchImageView extends ImageView {

    private Matrix matrix;
    private PointF markerLocation = null;
    private Paint paint = new Paint();
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
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.matrix = this.getImageMatrix();
    }

    @Override
    protected void onDraw (Canvas canvas) {
        // Apply matrix before drawing
        this.setImageMatrix(matrix);
        // Draw
        super.onDraw(canvas);
        // Draw marker
        this.drawMarker(canvas);
    }

    private void drawMarker(Canvas canvas) {
        if (markerLocation != null) {
            canvas.drawCircle(markerLocation.x, markerLocation.y, 10, paint);
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
                tapDownLocation.set(event.getX(), event.getY());
            }
            // Primary pointer is being released
            else if (action == MotionEvent.ACTION_UP) {
                PointF tapUpLocation = new PointF(event.getX(), event.getY());
                if (tapUpLocation.equals(tapDownLocation)) {
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
            matrix.postScale(scaleFactor, scaleFactor, scaleFocalPoint.x, scaleFocalPoint.y);
            invalidate();
            return true;
        }
    }

    //////////////////
    // Drag Gesture //
    //////////////////
    private class MyDragGestureDetector {
        private PointF activePointerLocation = new PointF(0, 0);
        private int activePointerId = -1; // -1 for "undefined"

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            int currentActionIndex = event.getActionIndex();
            int currentPointerId = event.getPointerId(currentActionIndex);
            int activePointerIndex = event.findPointerIndex(activePointerId);

            // Primary pointer going down
            if (action == MotionEvent.ACTION_DOWN) {
                activePointerId = currentPointerId;
                activePointerLocation.set(event.getX(), event.getY());
            }
            // Primary pointer is being released
            else if (action == MotionEvent.ACTION_UP) {
                activePointerId = -1;
            }
            // Primary pointer is being released but more pointers remain
            else if (activePointerId == currentPointerId && action == MotionEvent.ACTION_POINTER_UP) {
                int pointerCount = event.getPointerCount();
                int pointerIndex = (activePointerIndex + 1) % pointerCount;
                activePointerId = event.getPointerId(pointerIndex);
                activePointerLocation.set(event.getX(pointerIndex), event.getY(pointerIndex));
            }
            // Primary pointer moved
            else if (activePointerId != -1 && action == MotionEvent.ACTION_MOVE) {
                float curX = event.getX(activePointerIndex);
                float curY = event.getY(activePointerIndex);
                matrix.postTranslate(curX - activePointerLocation.x, curY - activePointerLocation.y);
                activePointerLocation.set(curX, curY);
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
        private PointF pivotLocation = new PointF(0, 0);
        private int firstPointerId = -1; // -1 for "undefined"
        private int secondPointerId = -1; // -1 for "undefined"

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            int currentActionIndex = event.getActionIndex();
            int currentPointerId = event.getPointerId(currentActionIndex);
            int pointerCount = event.getPointerCount();
            int firstPointerIndex = event.findPointerIndex(firstPointerId);
            int secondPointerIndex = event.findPointerIndex(secondPointerId);

            // First pointer going down
            if (action == MotionEvent.ACTION_DOWN) {
                firstPointerId = currentPointerId;
                firstPointerLocation.set(event.getX(), event.getY());
            }
            // Second pointer going down
            else if (action == MotionEvent.ACTION_POINTER_DOWN && pointerCount == 2) {
                secondPointerId = currentPointerId;
                secondPointerIndex = event.findPointerIndex(secondPointerId);
                secondPointerLocation.set(event.getX(secondPointerIndex), event.getY(secondPointerIndex));
                pivotLocation = getMiddlePoint(firstPointerLocation, secondPointerLocation);
            }
            // Two tracked pointers are down, moving
            else if (pointerCount == 2 && firstPointerId != -1 && secondPointerId != -1 && action == MotionEvent.ACTION_MOVE) {
                PointF newFirstPointerLocation = new PointF(event.getX(firstPointerIndex), event.getY(firstPointerIndex));
                PointF newSecondPointerLocation = new PointF(event.getX(secondPointerIndex), event.getY(secondPointerIndex));
                PointF newPivotLocation = getMiddlePoint(newFirstPointerLocation, newSecondPointerLocation);
                double rotationAngle = getAngle(firstPointerLocation, secondPointerLocation, newFirstPointerLocation, newSecondPointerLocation);

                matrix.postRotate((float)rotationAngle, pivotLocation.x, pivotLocation.y);
                firstPointerLocation = newFirstPointerLocation;
                secondPointerLocation = newSecondPointerLocation;
                pivotLocation = newPivotLocation;
                invalidate();
            }
            else if (action == MotionEvent.ACTION_POINTER_UP) {
                if (firstPointerId == currentPointerId) {
                    firstPointerId = -1;
                } else if (secondPointerId == currentPointerId) {
                    secondPointerId = -1;
                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                firstPointerId = secondPointerId = -1;
            }

            return true;
        }

        private double getAngle(PointF p1, PointF p2, PointF np1, PointF np2) {
            double oldAtan = Math.atan2(p1.y - p2.y, p1.x - p2.x);
            double newAtan = Math.atan2(np1.y - np2.y, np1.x - np2.x);
            double angle = Math.toDegrees(newAtan - oldAtan) % 360;
            if (angle < -180) angle += 360;
            if (angle > 180) angle -= 360;
            return angle;
        }

        private PointF getMiddlePoint(PointF p1, PointF p2) {
            return new PointF((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        }
    }
}
