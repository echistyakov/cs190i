package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;


public class AnimationView extends View {

    Random rand = new Random();
    Paint red = new Paint();
    int ballRadius = 50;
    int x = ballRadius;
    int y = ballRadius;
    int maxVel = 80;
    int minVel = 10;
    int xVel = 0;
    int yVel = 0;
    int xDir = 1;
    int yDir = 1;
    int xmax = 0;
    int ymax = 0;

    public AnimationView(Context context) {
        super(context);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        this.red.setColor(Color.RED);
        xVel = newSpeed();
        yVel = newSpeed();
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

    private int newSpeed() {
        return minVel + rand.nextInt(maxVel - minVel);
    }

    private void moveBall() {
        // Moving part
        int newX = x + xDir * xVel;
        int newY = y + yDir * yVel;

        // Bouncing part (X)
        if (newX < ballRadius) {
            newX = ballRadius;
            xDir = -xDir;
            xVel = newSpeed();
        } else if (newX > xmax - ballRadius) {
            newX = xmax - ballRadius;
            xDir = -xDir;
            xVel = newSpeed();
        }

        // Bouncing part (Y)
        if (newY < ballRadius) {
            newY = ballRadius;
            yDir = -yDir;
            yVel = newSpeed();
        } else if (newY > ymax - ballRadius) {
            newY = ymax - ballRadius;
            yDir = -yDir;
            yVel = newSpeed();
        }

        x = newX;
        y = newY;
    }
}