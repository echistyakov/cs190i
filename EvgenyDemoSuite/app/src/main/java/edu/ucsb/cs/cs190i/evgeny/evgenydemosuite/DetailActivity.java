package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = new Bundle();
        bundle.putInt(Actions.ACTION_TYPE, getIntent().getIntExtra(Actions.ACTION_TYPE, -1));

        /*DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.detailActivityPlaceholder, fragment).commit();*/
    }
}
