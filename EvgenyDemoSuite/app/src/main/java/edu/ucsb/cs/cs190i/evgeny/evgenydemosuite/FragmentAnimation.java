package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;


public class FragmentAnimation extends Fragment {

    private DrawingThread thread = null;
    private int fps = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation, container, false);
    }

    private void playAnimation() {
        AnimationView animationView = (AnimationView) getView().findViewById(R.id.animationView);
        if (this.thread == null) {
            this.thread = new DrawingThread(animationView, 50);
        }
        if (!this.thread.isRunning()) {
            this.thread.start();
        }
    }

    private void pauseAnimation() {
        if (this.thread != null && this.thread.isRunning()) {
            this.thread.stop();
        }
    }

    @Override
    public void onResume() {
        super.onStart();
        this.playAnimation();
    }

    @Override
    public void onPause() {
        super.onStart();
        this.pauseAnimation();
    }

    class AnimationView extends View {

        Random rand = new Random();
        Paint red = new Paint();
        int ballRadius = 50;
        int x = ballRadius;
        int y = ballRadius;
        int xDir = 1;
        int yDir = 1;
        int xmax = 0;
        int ymax = 0;

        public AnimationView(Context context) {
            super(context);
            this.red.setColor(Color.RED);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.xmax = w;
            this.ymax = h;
        }

        @Override
        public void onDraw(Canvas c) {
            super.onDraw(c);
            // Draw ball
            moveBall();
            c.drawCircle(x, y, ballRadius, red);
        }

        private void moveBall() {
            // Moving part
            int xVel = rand.nextInt(80);
            int yVel = rand.nextInt(80);
            int newX = x + xDir * xVel;
            int newY = y + yDir * yVel;

            // Bouncing part
            if (newX < 0) {
                newX = 0;
                xDir = -xDir;
            } else if (newX > xmax - ballRadius) {
                newX = xmax - ballRadius;
                xDir = -xDir;
            }

            if (newY < 0) {
                newY = 0;
                yDir = -yDir;
            } else if (newY > ymax - ballRadius) {
                newY = ymax - ballRadius;
                yDir = -yDir;
            }
        }
    }
}
