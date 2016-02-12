package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;
import android.os.Bundle;

/* Based on: http://inthecheesefactory.com/blog/best-approach-to-keep-android-fragment-state/en */
public class StateFragment extends Fragment {

    private Bundle savedState;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    private void saveState() {
        savedState = new Bundle();
        onSaveState(savedState);
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    protected void onSaveState(Bundle outState) {}
    protected void onRestoreState(Bundle savedInstanceState) {}
}
