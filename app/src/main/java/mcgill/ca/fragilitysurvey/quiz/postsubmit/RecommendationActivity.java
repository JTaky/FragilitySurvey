package mcgill.ca.fragilitysurvey.quiz.postsubmit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.quiz.questions.esstimation.ScoreEstimator;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class RecommendationActivity extends AppCompatActivity {

    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation);
        extractParameters();
        initBtns();
        showData();
    }

    private void showData() {
        ScoreEstimator scoreEstimator = new ScoreEstimator(survey);
        //recos
        ListView listRecomendations = (ListView)findViewById(R.id.listRecomendations);
        listRecomendations.setAdapter(new ArrayAdapter<>(this, R.layout.recomendations_listview, scoreEstimator.buildRecommendations(getResources())));
        //scores
        TextView scoreP7 = (TextView)findViewById(R.id.lblScoreP7Value);
        scoreP7.setText(String.valueOf(scoreEstimator.scoreP7()));
        TextView scoreER2 = (TextView)findViewById(R.id.lblScoreER2Value);
        scoreER2.setText(String.valueOf(scoreEstimator.scoreER2()));
        TextView riskFall = (TextView)findViewById(R.id.lblRiskFallValue);
        int riskLevel = scoreEstimator.riskLevel();
        if(riskLevel == 0){
            riskFall.setText(getResources().getText(R.string.lblRiskFall_low));
        } else if(riskLevel == 1){
            riskFall.setText(getResources().getText(R.string.lblRiskFall_moderate));
        } else if(riskLevel == 2){
            riskFall.setText(getResources().getText(R.string.lblRiskFall_high));
        }
    }

    private void extractParameters() {
        Bundle extra = getIntent().getBundleExtra(QuizActivity.EXTRAS_KEY);
        this.survey = extra.getParcelable(QuizActivity.SURVEY_KEY);
    }

    private void initBtns() {
        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendationActivity.this.setResult(0);
                RecommendationActivity.this.finish();
            }
        });
    }
}
