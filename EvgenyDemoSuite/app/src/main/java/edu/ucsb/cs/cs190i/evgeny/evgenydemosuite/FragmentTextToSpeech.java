package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTextToSpeech extends StateFragment {

    private TextToSpeech tts;
    private boolean ttsReady;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ttsReady = false;
        this.tts = new TextToSpeech(this.getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsReady = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_text_to_speech, container, false);
        Button speakButton = (Button) layout.findViewById(R.id.speakButton);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });
        return layout;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        TextView textView = (TextView) getView().findViewById(R.id.textToSpeak);
        String textToSpeak = textView.getText().toString();
        outState.putString("textToSpeak", textToSpeak);
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String textToSpeak = savedInstanceState.getString("textToSpeak", "");
            TextView textView = (TextView) getView().findViewById(R.id.textToSpeak);
            textView.setText(textToSpeak);
        }
    }

    private void speakText() {
        if (this.ttsReady) {
            EditText textField = (EditText) getView().findViewById(R.id.textToSpeak);
            String textToSpeak = textField.getText().toString();
            this.tts.speak(textToSpeak,TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "TextToSpeech engine not ready.", Toast.LENGTH_SHORT).show();
        }
    }
}
