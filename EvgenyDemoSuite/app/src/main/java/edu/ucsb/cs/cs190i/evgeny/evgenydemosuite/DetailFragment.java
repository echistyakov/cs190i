package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DetailFragment extends Fragment {

    int action = -1;

    public DetailFragment() {
        // Do nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read arguments
        Bundle args = getArguments();
        if (args != null) {
            this.action = args.getInt(Actions.ACTION_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_detail, container, false);


        return  layout;
    }

}
