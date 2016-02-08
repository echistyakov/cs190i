package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get buttons
        Button speechToText = (Button) findViewById(R.id.speechToText);
        Button textToSpeech = (Button) findViewById(R.id.textToSpeech);
        Button audio        = (Button) findViewById(R.id.audio);
        Button video        = (Button) findViewById(R.id.video);
        Button animation    = (Button) findViewById(R.id.animation);

        ActionClickListener listener = new ActionClickListener();

        // Set listeners
        speechToText.setOnClickListener(listener);
        textToSpeech.setOnClickListener(listener);
        audio.setOnClickListener(listener);
        video.setOnClickListener(listener);
        animation.setOnClickListener(listener);
    }

    class ActionClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean landscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
            int action = -1;
            int id = v.getId();
            int frag_id = -1;
            Fragment fragment = null;
            switch (id) {
                case R.id.speechToText: action = Actions.SPEECH_TO_TEXT; frag_id = R.id.speechToText; fragment = new FragmentSpeechToText(); break;
                case R.id.textToSpeech: action = Actions.TEXT_TO_SPEECH; frag_id = R.id.textToSpeech; fragment = new FragmentTextToSpeech(); break;
                case R.id.audio:        action = Actions.AUDIO;          frag_id = R.id.audio;        fragment = new FragmentAudio();        break;
                case R.id.video:        action = Actions.VIDEO;          frag_id = R.id.video;        fragment = new FragmentVideo();        break;
                case R.id.animation:    action = Actions.ANIMATION;      frag_id = R.id.animation;    fragment = new FragmentAnimation();    break;
            }

            if (landscape) {
                // Set fragment
                Bundle bundle = new Bundle();
                bundle.putInt(Actions.ACTION_TYPE, action);

                /*DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.mainActivityPlaceholder, fragment).commit();*/
            } else {
                // Launch intent
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(Actions.ACTION_TYPE, action);
                v.getContext().startActivity(intent);
            }
        }
    }
}
