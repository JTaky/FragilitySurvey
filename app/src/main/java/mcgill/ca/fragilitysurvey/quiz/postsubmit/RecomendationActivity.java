package mcgill.ca.fragilitysurvey.quiz.postsubmit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.QuizActivity;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class RecomendationActivity extends AppCompatActivity {

    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation);
        extractParameters();
    }

    private void extractParameters() {
        this.survey = getIntent().getParcelableExtra(QuizActivity.SURVEY_KEY);
    }
}
