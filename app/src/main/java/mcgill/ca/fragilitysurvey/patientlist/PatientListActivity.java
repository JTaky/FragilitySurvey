package mcgill.ca.fragilitysurvey.patientlist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class PatientListActivity extends AppCompatActivity {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);

        showData();
    }

    private void showData() {
        SurveyService surveyService = new SurveyService(new DBContext(this));

        TableLayout patientTable = (TableLayout) findViewById(R.id.patientTable);

        List<Survey> surveys = surveyService.getSurveys();

        setHeaders(patientTable);

        for (Survey survey : surveys) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView surveyIdTextView = new TextView(this);
            String idStr = survey.surveyId();
            if(idStr.length() > 24){
                idStr = idStr.substring(0, 21) + "...";
            }
            surveyIdTextView.setText(idStr);
            row.addView(surveyIdTextView);

            TextView surveyDateTextView = new TextView(this);
            surveyDateTextView.setText(DATE_FORMAT.format(survey.timestamp()));
            row.addView(surveyDateTextView);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setEnabled(false);
            checkBox.setChecked(survey.isCompleted());
            row.addView(checkBox);

            Button completeSurveyBtn = new Button(this);
            completeSurveyBtn.setText(R.string.patient_list_survey_button);
            completeSurveyBtn.setEnabled(!survey.isCompleted());
            row.addView(completeSurveyBtn);
//            completeSurveyBtn.setOnClickListener(OnCompleteSurvey(survey));

            patientTable.addView(row);
        }
    }

    private View.OnClickListener OnCompleteSurvey(Survey survey) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PatientListActivity.this, QuizActivity.class);
                myIntent.putParcelableArrayListExtra(QuizActivity.QUESTIONS_KEY, Questions.completeSurveyQuestions(getResources()));
                PatientListActivity.this.startActivity(myIntent);
            }
        };
    }

    private void setHeaders(TableLayout patientTable) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        row.setBackgroundColor(Color.argb(0xFF, 0x1a, 0xb4, 0xe8));

        TextView surveyIdTextView = new TextView(this);
        surveyIdTextView.setPadding(5, 5, 5, 5);
        surveyIdTextView.setText(R.string.patient_list_survey_id);
        row.addView(surveyIdTextView);

        TextView surveyDateTextView = new TextView(this);
        surveyDateTextView.setPadding(5, 5, 5, 5);
        surveyDateTextView.setText(R.string.patient_list_survey_date);
        row.addView(surveyDateTextView);

        TextView checkBoxLabel = new TextView(this);
        checkBoxLabel.setPadding(5, 5, 5, 5);
        checkBoxLabel.setText(getResources().getText(R.string.patient_list_checkbox_label));
        row.addView(checkBoxLabel);

        patientTable.addView(row);
    }

}
