package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int action = getIntent().getIntExtra(Actions.ACTION_TYPE, -1);

        Fragment fragment = Actions.getFragmentFromId(action);
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.detailActivityPlaceholder, fragment).commit();
        }
    }
}
