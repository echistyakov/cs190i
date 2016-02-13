package edu.ucsb.cs.cs190i.evgeny.evgenydemosuite;


import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Fragment currentFragment = null;
    private int currentActionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.currentActionId = getIntent().getIntExtra(Actions.ACTION_TYPE, -1);
        this.currentFragment = Actions.getFragmentFromId(this.currentActionId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentFragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.detailActivityPlaceholder, currentFragment).commit();
        }
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getFragmentManager().beginTransaction().remove(currentFragment).commit();
            finish();
        }
    }*/
}
