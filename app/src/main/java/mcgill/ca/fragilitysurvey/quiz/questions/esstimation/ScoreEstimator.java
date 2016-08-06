package mcgill.ca.fragilitysurvey.quiz.questions.esstimation;

import android.content.res.Resources;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.AdditionalTest;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class ScoreEstimator {

    private static final int HAPPY_ID = 0;
    private static final int SAD_ID = 1;
    private static final int NEITHER_ID = 2;

    private Survey survey;
    private SurveyQuestionsAccessor surveyQuestionsAccessor;
    private int score;
    private List<AdditionalTest> additionalTests = new ArrayList<>();

    public ScoreEstimator(Survey survey){
        this.survey = survey;
        this.surveyQuestionsAccessor = new SurveyQuestionsAccessor(survey);
        esstimateSurvey();
    }

    public List<AdditionalTest> additionalTests(){
        return additionalTests;
    }

    public int score(){
        return score;
    }

    public int maxScore(){
        return 18;
    }

    private void esstimateSurvey() {
        //0 add systematic anyway
        additionalTests.add(AdditionalTest.SYSTEMATIC);
        //1
        if (surveyQuestionsAccessor.isTrue(1)) {
            score += 2;
            additionalTests.add(AdditionalTest.MNA);
        }
        //2
        int q2Value = getInt(1);
        if (q2Value >= 5 && q2Value < 9) {
            score += 1;
        } else if(q2Value > 8) {
            score += 2;
        }
        //3-4
        if (isTrue(3)) score += 1;
        if (isTrue(4)) score += 1;
        //5
        if (isTrue(5)) {
            score += 2;
            additionalTests.add(AdditionalTest.S_MMSE);
        }
        //7 - 11
        {
            int noCount = 0;
            noCount += BooleanUtils.toInteger(isFalse(7));
            noCount += BooleanUtils.toInteger(isFalse(8));
            noCount += BooleanUtils.toInteger(isFalse(9));
            noCount += BooleanUtils.toInteger(isFalse(10));
            noCount += BooleanUtils.toInteger(isFalse(11));
            if (noCount <= 1) {
                score += 2;
            } else if (noCount >= 2 && noCount <= 3) {
                score += 1;
            }
        }
        //12 - 15
        {
            int noCount = 0;
            noCount += BooleanUtils.toInteger(isFalse(12));
            noCount += BooleanUtils.toInteger(isFalse(13));
            noCount += BooleanUtils.toInteger(isFalse(14));
            noCount += BooleanUtils.toInteger(isFalse(15));
            if(noCount <= 2){
                score += 2;
            } else if(noCount == 3){
                score += 1;
            }
        }
        //16
        if(isTrue(16)){
            score += 2;
        }
        //17-18
        int happinessAnswer = getInt(17);
        if(happinessAnswer == SAD_ID || isTrue(18)){
            score += 2;
            additionalTests.add(AdditionalTest.GDS_4_ITEM);
        } else if(happinessAnswer == NEITHER_ID){
            score += 1;
            additionalTests.add(AdditionalTest.GDS_4_ITEM);
        }
        //19-20
        if(isTrue(20)){
            score += 2;
            additionalTests.add(AdditionalTest.SITE_TO_STAND);
        } else if(isFalse(19) && isFalse(20)){
            score += 1;
            additionalTests.add(AdditionalTest.SITE_TO_STAND);
        }
    }

    private boolean isFalse(int i) {
        return !isTrue(i);
    }

    private int getInt(int index){
        return surveyQuestionsAccessor.getInt(index);
    }

    private boolean isTrue(int index){
        return surveyQuestionsAccessor.isTrue(index);
    }

    public FragilityLevel getFragilityResult() {
        return FragilityLevel.estimate(score());
    }

    public List<String> buildAdditionalTestsList(Resources res) {
        Questions.initQuestions(res);
        List<String> additionalTests = new ArrayList<>();
        for(AdditionalTest additionalTest: additionalTests()){
            additionalTests.add(res.getString(additionalTest.STRING_ID));
        }
        return additionalTests;
    }

    public List<String> buildRecommendations(Resources res) {
        Questions.initQuestions(res);
        return new Recommendator(survey).buildReco(res);
    }
}
