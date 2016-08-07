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
    private List<AdditionalTest> additionalTests = new ArrayList<>();

    private int scoreQuae20;

    public ScoreEstimator(Survey survey){
        this.survey = survey;
        this.surveyQuestionsAccessor = new SurveyQuestionsAccessor(survey);
        esstimateSurvey();
    }

    public List<AdditionalTest> additionalTests(){
        return additionalTests;
    }

    public int scoreQuae20(){
        return scoreQuae20;
    }

    public int maxScoreQuae20(){
        return 18;
    }

    private void esstimateSurvey() {
        esstimateScoreQuae20();
    }

    private void esstimateScoreQuae20() {
        //0 add systematic anyway
        additionalTests.add(AdditionalTest.SYSTEMATIC);
        //1
        if (surveyQuestionsAccessor.isTrue(Questions.SELF_ENTER_FIRST_ID)) {
            scoreQuae20 += 2;
            additionalTests.add(AdditionalTest.MNA);
        }
        //2
        int q2Value = getInt(Questions.SELF_ENTER_FIRST_ID + 1);
        if (q2Value >= 5 && q2Value < 9) {
            scoreQuae20 += 1;
        } else if(q2Value > 8) {
            scoreQuae20 += 2;
        }
        //3-4
        if (isTrue(Questions.SELF_ENTER_FIRST_ID + 2)) scoreQuae20 += 1;
        if (isTrue(Questions.SELF_ENTER_FIRST_ID + 3)) scoreQuae20 += 1;
        //5
        if (isTrue(Questions.SELF_ENTER_FIRST_ID + 4)) {
            scoreQuae20 += 2;
            additionalTests.add(AdditionalTest.S_MMSE);
        }
        //7 - 11
        {
            int noCount = 0;
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 6));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 7));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 8));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 9));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 10));
            if (noCount <= 1) {
                scoreQuae20 += 2;
            } else if (noCount >= 2 && noCount <= 3) {
                scoreQuae20 += 1;
            }
        }
        //12 - 15
        {
            int noCount = 0;
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 11));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 12));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 13));
            noCount += BooleanUtils.toInteger(isFalse(Questions.SELF_ENTER_FIRST_ID + 14));
            if(noCount <= 2){
                scoreQuae20 += 2;
            } else if(noCount == 3){
                scoreQuae20 += 1;
            }
        }
        //16
        if(isTrue(Questions.SELF_ENTER_FIRST_ID + 15)){
            scoreQuae20 += 2;
        }
        //17-18
        int happinessAnswer = getInt(Questions.SELF_ENTER_FIRST_ID + 16);
        if(happinessAnswer == SAD_ID || isTrue(Questions.SELF_ENTER_FIRST_ID + 17)){
            scoreQuae20 += 2;
            additionalTests.add(AdditionalTest.GDS_4_ITEM);
        } else if(happinessAnswer == NEITHER_ID){
            scoreQuae20 += 1;
            additionalTests.add(AdditionalTest.GDS_4_ITEM);
        }
        //19-20
        if(isTrue(Questions.SELF_ENTER_FIRST_ID + 19)){
            scoreQuae20 += 2;
            additionalTests.add(AdditionalTest.SITE_TO_STAND);
        } else if(isFalse(Questions.SELF_ENTER_FIRST_ID + 18) && isFalse(Questions.SELF_ENTER_FIRST_ID + 19)){
            scoreQuae20 += 1;
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
        return FragilityLevel.estimate(scoreQuae20());
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

    public int scoreP7() {
        return surveyQuestionsAccessor.p7();
    }

    public int scoreER2() {
        return surveyQuestionsAccessor.er2();
    }

    public int riskLevel() {
        return surveyQuestionsAccessor.riskLevel();
    }
}
