package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;

public class Actions {
    public static final int SPEECH_TO_TEXT = 1;
    public static final int TEXT_TO_SPEECH = 2;
    public static final int AUDIO          = 3;
    public static final int VIDEO          = 4;
    public static final int ANIMATION      = 5;

    public static final String ACTION_TYPE = "ACTION_TYPE";

    public static FragmentSpeechToText speechToText = new FragmentSpeechToText();
    public static FragmentTextToSpeech textToSpeech = new FragmentTextToSpeech();
    public static FragmentAudio        audio        = new FragmentAudio();
    public static FragmentVideo        video        = new FragmentVideo();
    public static FragmentAnimation    animation    = new FragmentAnimation();


    /*static Fragment getFragmentFromId(int actionId) {
        switch (actionId) {
            case SPEECH_TO_TEXT: return new FragmentSpeechToText();
            case TEXT_TO_SPEECH: return new FragmentTextToSpeech();
            case AUDIO:          return new FragmentAudio();
            case VIDEO:          return new FragmentVideo();
            case ANIMATION:      return new FragmentAnimation();
        }
        return null;
    }*/

    static Fragment getFragmentFromId(int actionId) {
        switch (actionId) {
            case SPEECH_TO_TEXT: return speechToText;
            case TEXT_TO_SPEECH: return textToSpeech;
            case AUDIO:          return audio;
            case VIDEO:          return video;
            case ANIMATION:      return animation;
        }
        return null;
    }
}
