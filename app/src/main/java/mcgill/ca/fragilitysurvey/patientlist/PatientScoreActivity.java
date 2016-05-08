package mcgill.ca.fragilitysurvey.patientlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.quiz.questions.esstimation.FragilityLevel;
import mcgill.ca.fragilitysurvey.quiz.questions.esstimation.ScoreEstimator;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class PatientScoreActivity extends AppCompatActivity {

    private View.OnClickListener makeTestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //show quiz
            Intent myIntent = new Intent(PatientScoreActivity.this, QuizActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(QuizActivity.SURVEY_KEY, survey);
            extras.putParcelableArrayList(QuizActivity.QUESTIONS_KEY, Questions.completeSurveyQuestions(getResources()));
            myIntent.putExtra(QuizActivity.EXTRAS_KEY, extras);
            PatientScoreActivity.this.startActivityForResult(myIntent, QuizActivity.SURVEY_REQUEST_CODE);
        }
    };
    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PatientScoreActivity.this.finish();
        }
    };

    private Survey survey;
    private ScoreEstimator scoreEstimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_score);

        initSurvey();
        initBtns();
    }

    private void initSurvey() {
        this.survey = getIntent().getBundleExtra(QuizActivity.EXTRAS_KEY).getParcelable(QuizActivity.SURVEY_KEY);
        this.scoreEstimator = new ScoreEstimator(survey);

        updateLabels();
        updateRecommendations();
    }

    private void updateRecommendations() {
        ListView listRecos = (ListView)findViewById(R.id.listRecomendations);
        listRecos.setAdapter(new ArrayAdapter<>(this, R.layout.recomendations_listview, scoreEstimator.buildRecommendations(getResources())));
    }

    private void updateLabels() {
        TextView lblScore = (TextView)findViewById(R.id.lblScore);
        lblScore.setText(
                getResources().getString(
                        R.string.lbl_score_value,
                        String.valueOf(scoreEstimator.score())
                )
        );

        FragilityLevel fragilityLevel = scoreEstimator.getFragilityResult();
        TextView lblFragilityLevel = (TextView)findViewById(R.id.lblFragilityLevel);
        lblFragilityLevel.setText(fragilityLevel.RESOURCE_ID);
        lblFragilityLevel.setTextColor(getResources().getColor(fragilityLevel.COLOR_ID));
        lblScore.setTextColor(getResources().getColor(fragilityLevel.COLOR_ID));
    }

    private void initBtns() {
        Button btnMakeTests = (Button)findViewById(R.id.btnMakeTests);
        btnMakeTests.setOnClickListener(makeTestListener);

        Button btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(finishListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == QuizActivity.SURVEY_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateLabels();
                updateRecommendations();
            }
        }
    }
}