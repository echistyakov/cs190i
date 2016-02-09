package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentAudio extends Fragment {

    private MediaPlayer mp = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mp = MediaPlayer.create(getActivity(), R.raw.fanfare);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_audio, container, false);
        Button playButton = (Button) layout.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
            }
        });
        return layout;
    }

    private void playSound() {
        if (!this.mp.isPlaying()) {
            this.mp.seekTo(0);
            this.mp.start();
        }
    }
}
