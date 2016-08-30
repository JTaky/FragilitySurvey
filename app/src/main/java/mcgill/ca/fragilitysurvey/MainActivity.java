package mcgill.ca.fragilitysurvey;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

import mcgill.ca.fragilitysurvey.credentials.CredentialsActivity;
import mcgill.ca.fragilitysurvey.filter.SurveyFilterActivity;
import mcgill.ca.fragilitysurvey.filter.SurveySearchFilter;
import mcgill.ca.fragilitysurvey.patientlist.PatientListActivity;
import mcgill.ca.fragilitysurvey.preferences.Preferences;
import mcgill.ca.fragilitysurvey.quiz.QuestionViewFactory;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.report.CsvExporter;
import mcgill.ca.fragilitysurvey.report.PdfExporter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int IGNORE_ANSWER = 0;
    public static final int SIGN_UP_RESULT_SAVED = 1;
    public static final int FILTER_ACTIVATED = 2;

    public static class PostFilterActions {
        public static final int SHOW_PATIENTS = 0;
        public static final int EXPORT_CSV = 1;
        public static final int EXPORT_PDF = 2;
    }
    public static final String FILTER_OBJECT = "filter.object";

    private final Context context = MainActivity.this;

    private View.OnClickListener newPatientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, QuizActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelableArrayList(QuizActivity.QUESTIONS_KEY, Questions.newPatientQuestions(getResources()));
            myIntent.putExtra(QuizActivity.EXTRAS_KEY, extras);
            myIntent.putExtra(QuizActivity.POST_SUBMIT_ACTION, QuizActivity.PostSubmitActions.SAY_GOODBYE.value);
            MainActivity.this.startActivity(myIntent);
        }
    };

    private String getPassword(){
        SharedPreferences sharedPref = getPreferences();
        return sharedPref.getString(getString(R.string.preferences_key_password), "");
    }

    private View.OnClickListener editPatientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Set up the input
            final EditText input = new EditText(MainActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            final String expectedPwd = getPassword();

            if(StringUtils.isEmpty(expectedPwd)){
                Intent myIntent = new Intent(context, SurveyFilterActivity.class);
                MainActivity.this.startActivityForResult(myIntent, PostFilterActions.SHOW_PATIENTS);
            } else {
                //open search activity
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(QuestionViewFactory.isDebugMode() ?
                                getResources().getString(R.string.dialog_enter_pwd_debug, expectedPwd) :
                                getResources().getString(R.string.dialog_enter_pwd)
                        )
                        .setView(input).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = input.getText().toString();
                        if (pwd.equals(expectedPwd)) {
                            Intent myIntent = new Intent(context, SurveyFilterActivity.class);
                            MainActivity.this.startActivityForResult(myIntent, PostFilterActions.SHOW_PATIENTS);
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(getResources().getString(R.string.dialog_wrong_pwd))
                                    .setMessage(getResources().getString(R.string.dialog_wrong_pwd))
                                    .setPositiveButton(R.string.positive_btn, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        }
    };

    private View.OnClickListener exportCsvPatientsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(context, SurveyFilterActivity.class);
            MainActivity.this.startActivityForResult(myIntent, PostFilterActions.EXPORT_CSV);
        }
    };

    private View.OnClickListener exportPdfPatientsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(context, SurveyFilterActivity.class);
            MainActivity.this.startActivityForResult(myIntent, PostFilterActions.EXPORT_PDF);
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
                Questions.reset();  //request recreate for all questions
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
        } else if(resultCode == FILTER_ACTIVATED){
            if(requestCode == PostFilterActions.SHOW_PATIENTS){
                SurveySearchFilter searchFilter = data.getParcelableExtra(MainActivity.FILTER_OBJECT);
                Intent myIntent = new Intent(context, PatientListActivity.class);
                myIntent.putExtra(MainActivity.FILTER_OBJECT, searchFilter);
                MainActivity.this.startActivity(myIntent);
            } else if(requestCode == PostFilterActions.EXPORT_CSV){
                File csvFile;
                try {
                    csvFile = new CsvExporter().exportPatients(
                            getExportDir(MainActivity.this, MainActivity.this.getString(getApplicationInfo().labelRes)),
                            new DBContext(MainActivity.this),
                            MainActivity.this
                    );
                    showMessage("Success", "Data was exported to the '" + csvFile.getAbsolutePath() + "'");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == PostFilterActions.EXPORT_PDF){
                File pdfFile;
                SurveySearchFilter searchFilter = data.getParcelableExtra(MainActivity.FILTER_OBJECT);
                try {
                    pdfFile = new PdfExporter().exportPatients(
                            getExportDir(MainActivity.this, MainActivity.this.getString(getApplicationInfo().labelRes)),
                            new DBContext(MainActivity.this),
                            MainActivity.this,
                            searchFilter
                    );
                    showMessage("Success", "Data was exported to the '" + pdfFile + "'");

                    PackageManager packageManager = getPackageManager();
                    Intent testIntent = new Intent(Intent.ACTION_VIEW);
                    testIntent.setType("application/pdf");
                    List<ResolveInfo> list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (list.size() > 0) {  //is executed only on the real device, since it is hard to install pdf reader on the emulator
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Opening pdf file...");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                        }
                    } else {
                        showMessage("Info", "You can find the exported document by path - '" + pdfFile.getAbsolutePath() + "'. Please install the PDF reader.");
                    }
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                    showMessage("Error", "Cannot export file. Internal error.");
                }
            }
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
