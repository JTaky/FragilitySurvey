package mcgill.ca.fragilitysurvey.credentials;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mcgill.ca.fragilitysurvey.R;

public class CredentialsActivity extends AppCompatActivity {

    private View.OnClickListener btnSaveCredentialsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //make validation
            //save preferences
            //close activity
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        initBtns();
        hideIfNeed();
    }

    private void initBtns() {
        Button btnSaveCredentials = (Button) findViewById(R.id.btwSaveCredentials);
        btnSaveCredentials.setOnClickListener(btnSaveCredentialsListener);
    }

    private void hideIfNeed() {
        //check user properties
        TextView lblCurrentPassword = (TextView) findViewById(R.id.lblCurrentPassword);
        lblCurrentPassword.setVisibility(View.INVISIBLE);
        TextView txtCurrentPassword = (TextView) findViewById(R.id.txtCurrentPassword);
        txtCurrentPassword.setVisibility(View.INVISIBLE);
    }

}
