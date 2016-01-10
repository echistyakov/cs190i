package edu.ucsb.cs.cs190i.evgeny.tapcounterprogrammatically;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String mainTextTemplate = "You tapped %d times.";
    String buttonText = "Tap me!";
    LinearLayout layout = null;
    TextView mainTextView = null;
    Button mainButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        mainTextView = new TextView(this);
        mainButton = new Button(this);

        // Layout parameters
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);
        int horizontalPad = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int verticalPad = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
        layout.setPadding(horizontalPad, verticalPad, horizontalPad, verticalPad);

        // TextView parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        mainTextView.setLayoutParams(params);
        mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);

        // Button parameters
        params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, verticalPad, 0, 0);
        mainButton.setLayoutParams(params);
        mainButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        mainButton.setText(buttonText);

        // Add TextView and button to layout
        layout.addView(mainTextView);
        layout.addView(mainButton);

        // Add listener to the button
        mainButton.setOnClickListener(new View.OnClickListener() {
            int tapNum = 0;
            @Override
            public void onClick(View view) {
                setMainText(++tapNum);
            }
        });

        setMainText(0);
        setContentView(layout);
    }

    private void setMainText(int tapNum) {
        String mainText = String.format(mainTextTemplate, tapNum);
        mainTextView.setText(mainText);
    }
}
