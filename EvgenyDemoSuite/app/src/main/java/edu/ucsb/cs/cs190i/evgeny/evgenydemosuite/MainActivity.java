package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private int currentActionId = -1;

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

        // Restore state
        if (savedInstanceState != null) {
            currentActionId = savedInstanceState.getInt("currentActionId", -1);
        }
    }

    private int getActionIdFromButtonId(int buttonId) {
        switch (buttonId) {
            case R.id.speechToText: return Actions.SPEECH_TO_TEXT;
            case R.id.textToSpeech: return Actions.TEXT_TO_SPEECH;
            case R.id.audio:        return Actions.AUDIO;
            case R.id.video:        return Actions.VIDEO;
            case R.id.animation:    return Actions.ANIMATION;
        }
        return -1;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentActionId", currentActionId);
        super.onSaveInstanceState(outState);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        boolean landscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if (landscape && currentActionId != -1) {
            // Set fragment
            Fragment fragment = Actions.getFragmentFromId(currentActionId);
            if (fragment != null) {
                getFragmentManager().beginTransaction().replace(R.id.mainActivityPlaceholder, fragment).commit();
            }
        }
    }*/

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && currentActionId != -1) {
            Fragment fragment = Actions.getFragmentFromId(currentActionId);
            getFragmentManager().beginTransaction().remove(fragment).commit();
            // Launch intent
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Actions.ACTION_TYPE, currentActionId);
            startActivity(intent);
        } else {
            recreate();
        }
    }*/

    class ActionClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean landscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
            int action = getActionIdFromButtonId(v.getId());
            currentActionId = action;

            if (landscape) {
                // Set fragment
                Fragment fragment = Actions.getFragmentFromId(action);
                if (fragment != null) {
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.mainActivityPlaceholder, fragment).commit();
                }
            } else {
                // Launch intent
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(Actions.ACTION_TYPE, action);
                v.getContext().startActivity(intent);
            }
        }
    }
}
