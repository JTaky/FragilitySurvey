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
import mcgill.ca.fragilitysurvey.quiz.postsubmit.RecommendationActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.quiz.questions.esstimation.FragilityLevel;
import mcgill.ca.fragilitysurvey.quiz.questions.esstimation.ScoreEstimator;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class PatientScoreActivity extends AppCompatActivity {

    private View.OnClickListener makeTestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //show quiz
            Intent myIntent = new Intent(PatientScoreActivity.this, QuizActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(QuizActivity.SURVEY_KEY, survey);
            extras.putParcelableArrayList(
                    QuizActivity.QUESTIONS_KEY,
                    Questions.getQuestionsForTests(getResources(), scoreEstimator.additionalTests())
            );
            myIntent.putExtra(QuizActivity.EXTRAS_KEY, extras);
            myIntent.putExtra(QuizActivity.POST_SUBMIT_ACTION, QuizActivity.PostSubmitActions.SHOW_RECOMENDATIONS.value);
            PatientScoreActivity.this.startActivityForResult(myIntent, QuizActivity.SURVEY_REQUEST_CODE);
        }
    };
    private View.OnClickListener showRecommendationsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(PatientScoreActivity.this, RecommendationActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(QuizActivity.SURVEY_KEY, survey);
            myIntent.putExtra(QuizActivity.EXTRAS_KEY, extras);
            PatientScoreActivity.this.startActivityForResult(myIntent, QuizActivity.RECOMENDATION_REQUEST_CODE);
        }
    };
    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_OK);
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
        Questions.initQuestions(getResources());
        this.scoreEstimator = new ScoreEstimator(survey);

        updateLabels();
        updateTestsToDo();
    }

    private void updateTestsToDo() {
        ListView listTestsToDo = (ListView)findViewById(R.id.listTestsToDo);
        listTestsToDo.setAdapter(new ArrayAdapter<>(this, R.layout.recomendations_listview, scoreEstimator.buildAdditionalTestsList(getResources())));
    }

    private void updateLabels() {
        TextView lblScore = (TextView)findViewById(R.id.lblScore);
        lblScore.setText(
                getResources().getString(
                        R.string.lbl_score_value,
                        String.valueOf(scoreEstimator.scoreQuae20()) + "/" + scoreEstimator.maxScoreQuae20()
                )
        );

        FragilityLevel fragilityLevel = scoreEstimator.getFragilityResult();
        TextView lblFragilityLevel = (TextView)findViewById(R.id.lblFragilityLevel);
        lblFragilityLevel.setText(fragilityLevel.RESOURCE_ID);
        lblFragilityLevel.setTextColor(getResources().getColor(fragilityLevel.COLOR_ID));
        lblScore.setTextColor(getResources().getColor(fragilityLevel.COLOR_ID));
    }

    private void initBtns() {
        if(survey.isCompleted()){
            Button btnMakeTests = (Button) findViewById(R.id.btnMakeTests);
            btnMakeTests.setText(getResources().getText(R.string.lblShowRecomendations));
            btnMakeTests.setOnClickListener(showRecommendationsListener);
        } else {
            Button btnMakeTests = (Button) findViewById(R.id.btnMakeTests);
            btnMakeTests.setOnClickListener(makeTestListener);
        }

        Button btnOk = (Button)findViewById(R.id.btnClose);
        btnOk.setOnClickListener(finishListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == QuizActivity.SURVEY_REQUEST_CODE) {
            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                reloadSurvey();
//                updateLabels();
//                updateTestsToDo();
//            }
            setResult(RESULT_OK);
            finish();
        }
    }

    private void reloadSurvey() {
        SurveyService surveyService = new SurveyService(new DBContext(this), this.getResources());
        survey = surveyService.getSurveyById(survey.surveyId());
    }
}
