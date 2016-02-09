package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class FragmentSpeechToText extends Fragment {

    private static final int ACTION_RECOGNIZE_SPEECH = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_speech_to_text, container, false);
        ImageButton micButton = (ImageButton) layout.findViewById(R.id.micButton);
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSpeechRecognition();
            }
        });
        return layout;
    }

    private void launchSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Prompt text
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
        startActivityForResult(intent, ACTION_RECOGNIZE_SPEECH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ACTION_RECOGNIZE_SPEECH) {
            if (resultCode == Activity.RESULT_OK) {
                // Extract spoken text
                List<String> list = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String spokenText = list.get(0);
                TextView textView = (TextView) getView().findViewById(R.id.spokenText);
                textView.setText(spokenText);
            }
        }
    }
}
