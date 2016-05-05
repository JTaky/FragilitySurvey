package mcgill.ca.fragilitysurvey;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import mcgill.ca.fragilitysurvey.credentials.CredentialsActivity;
import mcgill.ca.fragilitysurvey.preferences.Preferences;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.patientlist.PatientListActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.report.CsvExporter;
import mcgill.ca.fragilitysurvey.report.PdfExporter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int SIGN_UP_RESULT_SAVED = 1;

    private View.OnClickListener newPatientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, QuizActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelableArrayList(QuizActivity.QUESTIONS_KEY, Questions.newPatientQuestions(getResources()));
            myIntent.putExtra(QuizActivity.EXTRAS_KEY, extras);
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

    private View.OnClickListener exportCsvPatientsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                String filePath = new CsvExporter().exportPatients(
                        getExportDir(MainActivity.this, MainActivity.this.getString(getApplicationInfo().labelRes)),
                        new DBContext(MainActivity.this),
                        MainActivity.this
                );
                showMessage("Success", "Data was exported to the '" + filePath + "'");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener exportPdfPatientsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                String filePath = new PdfExporter().exportPatients(
                        getExportDir(MainActivity.this, MainActivity.this.getString(getApplicationInfo().labelRes)),
                        new DBContext(MainActivity.this),
                        MainActivity.this
                );
                showMessage("Success", "Data was exported to the '" + filePath + "'");
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        }
    };

    private AdapterView.OnItemSelectedListener localeSelectedListener = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String lang = String.valueOf(parent.getItemAtPosition(position));
            Locale newLocale = new Locale(lang.toLowerCase());
            Resources res = getResources();
            if(!res.getConfiguration().locale.getLanguage().equalsIgnoreCase(newLocale.getLanguage())) {
                //save in the preferences
                SharedPreferences sharedPref = getPreferences();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.preferences_key_locale), String.valueOf(lang.toLowerCase()));
                editor.apply();
                //update resources
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = newLocale;
                res.updateConfiguration(conf, dm);
                restartActivity();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG, "Nothing is selected on the language");
        }
    };

    private void restartActivity() {
        //refresh main form
        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private void showMessage(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent createntialsActivityIntent = new Intent(MainActivity.this, CredentialsActivity.class);
            MainActivity.this.startActivityForResult(createntialsActivityIntent, SIGN_UP_RESULT_SAVED);
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public File getExportDir(Context context, String fileName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        if (!file.exists() && !file.mkdirs()) {
            Log.e(TAG, "Export directory was not created");
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SIGN_UP_RESULT_SAVED){
            restartActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initBtns();
        initLanguageSpinner();
        initWelcomeMsg();
    }

    private SharedPreferences getPreferences(){
        return Preferences.getPreferences(this);
    }

    private void initWelcomeMsg() {
        TextView txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        SharedPreferences sharedPref = getPreferences();
        String organisationName = sharedPref.getString(getString(R.string.preferences_key_organisation), "");
        if(StringUtils.isNotBlank(organisationName)){
            //update label
            txtWelcome.setText(getString(R.string.welcome_label_prefix, organisationName));
            //change 'Sign UP' to edit caption
            Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
            btnSignUp.setText(getString(R.string.sign_up_edit));
        } else {
            txtWelcome.setText("");
        }
    }

    private void initBtns() {
        Button btnNewPatient = (Button) findViewById(R.id.btnNewPatient);
        btnNewPatient.setOnClickListener(newPatientListener);
        Button btnEditPatient = (Button) findViewById(R.id.btnEditPatient);
        btnEditPatient.setOnClickListener(editPatientListener);
        Button btnExportCsvPatients = (Button) findViewById(R.id.btnExportCsvPatients);
        btnExportCsvPatients.setOnClickListener(exportCsvPatientsListener);
        Button btnExportPdfPatients = (Button) findViewById(R.id.btnExportPdfPatients);
        btnExportPdfPatients.setOnClickListener(exportPdfPatientsListener);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(signUpListener);
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
        //update state, according to the settings
        SharedPreferences sharedPref = getPreferences();
        String localeLanguage = sharedPref.getString(getString(R.string.preferences_key_locale), "en");
        if(localeLanguage.equalsIgnoreCase("fr")) {
            spinner.setSelection(1);
        }
        //Set action processor
        spinner.setOnItemSelectedListener(localeSelectedListener);
    }

}
