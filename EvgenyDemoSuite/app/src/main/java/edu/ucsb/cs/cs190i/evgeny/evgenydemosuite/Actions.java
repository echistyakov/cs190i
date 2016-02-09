package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;

public class Actions {
    public static final int SPEECH_TO_TEXT = 1;
    public static final int TEXT_TO_SPEECH = 2;
    public static final int AUDIO          = 3;
    public static final int VIDEO          = 4;
    public static final int ANIMATION      = 5;

    public static final String ACTION_TYPE = "ACTION_TYPE";


    static Fragment getFragmentFromId(int actionId) {
        switch (actionId) {
            case SPEECH_TO_TEXT: return new FragmentSpeechToText();
            case TEXT_TO_SPEECH: return new FragmentTextToSpeech();
            case AUDIO:          return new FragmentAudio();
            case VIDEO:          return new FragmentVideo();
            case ANIMATION:      return new FragmentAnimation();
        }
        return null;
    }
}
