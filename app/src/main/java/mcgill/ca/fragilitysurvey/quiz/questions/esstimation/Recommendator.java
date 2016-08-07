package mcgill.ca.fragilitysurvey.quiz.questions.esstimation;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.questions.AdditionalTest;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class Recommendator {

    private SurveyQuestionsAccessor survey;
    private ScoreEstimator scoreEstimator;

    public Recommendator(Survey survey){
        this.survey = new SurveyQuestionsAccessor(survey);
        this.scoreEstimator = new ScoreEstimator(survey);
    }

    public List<String> buildReco(Resources res) {
        List<String> recos = new ArrayList<>();
        try {
            if (isNutrion()) {
                recos.addAll(nutrionRecomendations(res));
            }
        } catch (RuntimeException e){
            recos.add(res.getString(R.string.recomendation_pass_mna));
        }
        if (isHomeHelpService()){
            recos.addAll(homeHelpService(res));
        }
        if (isMultimorbidity()){
            recos.addAll(multimorbidity(res));
        }
        if (isCommunication()){
            recos.addAll(communication(res));
        }
        try {
            if (isCognition()) {
                recos.addAll(cognition(res));
            }
        } catch (RuntimeException e){
            recos.add(res.getString(R.string.recomendation_pass_smmse));
        }
        try {
            if (isMood()) {
                recos.addAll(mood(res));
            }
        } catch (RuntimeException e){
            recos.add(res.getString(R.string.recomendation_pass_gds));
        }
        try {
            if (isMobility()) {
                recos.addAll(mobility(res));
            }
        } catch (RuntimeException e){
            recos.add(res.getString(R.string.recomendation_pass_ftsts));
        }
        try {
            if (scoreEstimator.scoreP7() >= 3) {
                recos.add(res.getString(R.string.recomendation_p7));
            }
        } catch (RuntimeException e){
            recos.add(res.getString(R.string.recomendation_pass_p7));
        }
        return recos;
    }

    private Collection<? extends String> mobility(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_mobility_1),
                res.getString(R.string.recomendation_mobility_2),
                res.getString(R.string.recomendation_mobility_3)
        );
    }

    private boolean isMobility() {
        try {
            return survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 19) || (isFtsstEligible() && survey.getFtsstInSec() > 15);
        } catch (RuntimeException e){
            return false;
        }
    }

    private Collection<? extends String> mood(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_mood_1)
        );
    }

    private boolean isMood() {
         try {
            return (survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 16) || survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 17)) && (isGdsEligible() && survey.gds() >= 1);
         } catch(RuntimeException e){
            return false;
         }
    }

    private Collection<? extends String> cognition(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_cognition_1)
        );
    }

    private boolean isCognition() {
        return survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 4) && (isSmmseEligible() && survey.sMMSE() <= 4);
    }

    private Collection<? extends String> communication(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_communication_1)
        );
    }

    private boolean isCommunication() {
        try {
            return survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 2) || survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 3);
        } catch(RuntimeException e){
            return false;
        }
    }

    private Collection<? extends String> multimorbidity(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_multimorbidity_1)
        );
    }

    private boolean isMultimorbidity() {
        return survey.getInt(Questions.SELF_ENTER_FIRST_ID + 1) >= 5;
    }

    private Collection<? extends String> homeHelpService(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_home_help_service_1)
        );
    }

    private boolean isHomeHelpService() {
        int from7to11 = 0;
        for(int i = 6; i < 11; i++){
            if(survey.isTrue(Questions.SELF_ENTER_FIRST_ID + i)) from7to11++;
        }
        int from12to15 = 0;
        for(int i = 11; i < 15; i++){
            if(survey.isTrue(Questions.SELF_ENTER_FIRST_ID + i)) from12to15++;
        }
        return survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 5) && from7to11 < 5 && from12to15 < 4;
    }

    private boolean isNutrion() {
        try {
            return survey.isTrue(Questions.SELF_ENTER_FIRST_ID + 0) && (isMnaEligible() && survey.mnaScore() < 11);
        } catch(RuntimeException e){
            return false;
        }
    }

    private Collection<? extends String> nutrionRecomendations(Resources res) {
        return Arrays.asList(
                res.getString(R.string.recomendation_nutrion_1),
                res.getString(R.string.recomendation_nutrion_2),
                res.getString(R.string.recomendation_nutrion_3)
        );
    }

    private boolean isMnaEligible() {
        return scoreEstimator.additionalTests().contains(AdditionalTest.MNA);
    }

    private boolean isSmmseEligible() {
        return scoreEstimator.additionalTests().contains(AdditionalTest.S_MMSE);
    }

    private boolean isGdsEligible() {
        return scoreEstimator.additionalTests().contains(AdditionalTest.GDS_4_ITEM);
    }

    private boolean isFtsstEligible() {
        return scoreEstimator.additionalTests().contains(AdditionalTest.SITE_TO_STAND);
    }
}
