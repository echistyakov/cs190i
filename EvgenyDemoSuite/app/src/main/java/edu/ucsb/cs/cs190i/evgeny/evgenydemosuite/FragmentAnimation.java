package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentAnimation extends Fragment {

    private DrawingThread thread = null;
    private static final int FPS = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_animation, container, false);
        AnimationView animationView = (AnimationView) layout.findViewById(R.id.animationView);
        this.thread = new DrawingThread(animationView, FPS);
        return layout;
    }

    private void playAnimation() {
        if (!this.thread.isRunning()) {
            this.thread.start();
        }
    }

    private void pauseAnimation() {
        if (this.thread.isRunning()) {
            this.thread.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.playAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.pauseAnimation();
    }
}
