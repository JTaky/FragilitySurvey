package mcgill.ca.fragilitysurvey.quiz.postsubmit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mcgill.ca.fragilitysurvey.R;

public class GoodByeAtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_bye_ativity);
        initBtns();
    }

    private void initBtns() {
        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodByeAtivity.this.setResult(0);
                GoodByeAtivity.this.finish();
            }
        });
    }
}
