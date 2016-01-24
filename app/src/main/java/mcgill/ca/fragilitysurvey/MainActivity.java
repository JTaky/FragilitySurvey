package mcgill.ca.fragilitysurvey;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

import mcgill.ca.fragilitysurvey.credentials.CredentialsActivity;
import mcgill.ca.fragilitysurvey.neweditpatient.NewEditPatientActivity;
import mcgill.ca.fragilitysurvey.patientlist.PatientListActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";

    private View.OnClickListener newPatientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, NewEditPatientActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    };

    private View.OnClickListener editPatientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, PatientListActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    };

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, CredentialsActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initBtns();
        initLanguageSpinner();
    }

    private void initBtns() {
        Button btnNewPatient = (Button) findViewById(R.id.btnNewPatient);
        btnNewPatient.setOnClickListener(newPatientListener);
        Button btnEditPatient = (Button) findViewById(R.id.btnEditPatient);
        btnEditPatient.setOnClickListener(editPatientListener);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(signUpListener);
    }

    private void setLocale(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initLanguageSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerLang);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.supportedLanguages, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Set action processor
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String lang = String.valueOf(parent.getItemAtPosition(position));
        setLocale(lang.toLowerCase());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.v(TAG, "Nothing is selected on the language");
    }
}
