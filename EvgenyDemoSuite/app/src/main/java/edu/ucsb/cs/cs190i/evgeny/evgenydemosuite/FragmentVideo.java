package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;


public class FragmentVideo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onStart() {
        VideoView videoView = (VideoView) getView().findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this.getActivity()));
        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.bigbuck);
        videoView.setVideoURI(video);
        videoView.start();
    }
}
