package edu.ucsb.cs.cs190i.evgeny.tapcounterprogrammatically;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String mainTextTemplate = "You tapped %d times.";
    RelativeLayout layout = null;
    TextView mainTextView = null;
    Button mainButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new RelativeLayout(this);
        mainTextView = new TextView(this);
        mainButton = new Button(this);
        int mainTextViewId = 1234;

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mainTextView.setLayoutParams(params);
        mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        mainTextView.setId(mainTextViewId);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, mainTextViewId);
        mainButton.setLayoutParams(params);
        mainButton.setText("Tap me!");

        layout.addView(mainTextView);
        layout.addView(mainButton);

        setMainText(0);

        setContentView(layout);

        mainButton.setOnClickListener(new View.OnClickListener() {
            int tapNum = 0;
            @Override
            public void onClick(View view) {
                setMainText(++tapNum);
            }
        });
    }

    private void setMainText(int tapNum) {
        String mainText = String.format(mainTextTemplate, tapNum);
        mainTextView.setText(mainText);
    }
}
