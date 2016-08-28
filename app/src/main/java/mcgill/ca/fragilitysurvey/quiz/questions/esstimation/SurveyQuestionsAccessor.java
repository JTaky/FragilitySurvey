package mcgill.ca.fragilitysurvey.quiz.questions.esstimation;

import java.util.ArrayList;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

public class SurveyQuestionsAccessor {

    private Survey survey;

    public SurveyQuestionsAccessor(Survey survey){
        this.survey = survey;
    }

    public boolean isFalse(int i) {
        return !isTrue(i);
    }

    public Integer getInt(int index){
        return getInt(index, 0);
    }

    public Integer getInt(int index, int inputterIndex){
        return (Integer)q(index).inputters().get(inputterIndex).answers().get(0).value();
    }

    public boolean isTrue(int index){
        Question q = q(index);
        if(q == null){
            return false;
        }
        Inputter inputter = q.inputters().get(0);
        Integer optionId = (Integer)inputter.answers().get(0).value();
        return optionId == Questions.YES_ID;
    }

    public Integer answerIndex(int index){
        Inputter inputter = q(index).inputters().get(0);
        return (Integer)inputter.answers().get(0).value();
    }

    public List<Integer> answerCheckBoxes(int index, int inputterIndex){
        List<Integer> res = new ArrayList<>();
        List<Inputter> inputters = q(index).inputters();
        if(!inputters.contains(inputterIndex))
            return new ArrayList<>();
        Inputter inputter = q(index).inputters().get(inputterIndex);
        for(IAnswer answer : inputter.answers()){
            res.add((Integer) answer.value());
        }
        return res;
    }

    public List<Integer> answerIndexes(int index){
        List<Integer> answerIndexes = new ArrayList<>();
        Question q = q(index);
        if(q == null){
            return answerIndexes;
        }
        Inputter inputter = q.inputters().get(0);
        for(IAnswer answer : inputter.answers()){
            answerIndexes.add((Integer)answer.value());
        }
        return answerIndexes;
    }

    private Question q(int index){
        return survey.getQuestionById(index);
    }

    private int age() {
        return getInt(Questions.SYSTEMATIC_FIRST_ID + 1);
    }

    private boolean isMale() {
        return isTrue(Questions.SYSTEMATIC_FIRST_ID + 2);
    }

    public int mnaScore() {
        int mna = 0;
        int questionIndex = -1;
        mna += answerIndex(Questions.MNA_FIRST_ID + questionIndex);    //A
        questionIndex++;
        mna += answerIndex(Questions.MNA_FIRST_ID + questionIndex);    //B
        questionIndex++;
        mna += answerIndex(Questions.MNA_FIRST_ID + questionIndex);    //C
        questionIndex++;
        mna += (answerIndex(Questions.MNA_FIRST_ID + questionIndex) == 0? 0: 2);    //D
        questionIndex++;
        mna += answerIndex(Questions.MNA_FIRST_ID + questionIndex);   //E
        questionIndex++;
        //F1 and F2
        Integer f1 = answerIndex(Questions.MNA_FIRST_ID + questionIndex);
        questionIndex++;
        Integer f2 = answerIndex(Questions.MNA_FIRST_ID + questionIndex);
        if(f1 != null){
            mna += f1;
        } else if(f2 != null){
            mna += f2 == 0? 0 : 3;
        }
        return mna;
    }

    public int sMMSE() {
        int sMMSE = 0;
        int questionIndex = -1;
        //repeat words test is here
        sMMSE += answerIndexes(Questions.sMMSE_FIRST_ID + questionIndex).size();
        questionIndex++;
        sMMSE += isTrue(Questions.sMMSE_FIRST_ID + questionIndex)? 1: 0;
        questionIndex++;
        sMMSE += isTrue(Questions.sMMSE_FIRST_ID + questionIndex)? 1: 0;
        questionIndex++;
        sMMSE += isTrue(Questions.sMMSE_FIRST_ID + questionIndex)? 1: 0;
        return sMMSE;
    }

    public int gds() {
        int gds = 0;
        int questionIndex = -1;
        //repeat words test is here
        questionIndex++;
        gds += isTrue(Questions.GDS_FIRST_ID + questionIndex)? 0: 1;
        questionIndex++;
        gds += isTrue(Questions.GDS_FIRST_ID + questionIndex)? 1: 0;
        questionIndex++;
        gds += isTrue(Questions.GDS_FIRST_ID + questionIndex)? 1: 0;
        questionIndex++;
        gds += isTrue(Questions.GDS_FIRST_ID + questionIndex)? 0: 1;
        return gds;
    }

    public int p7() {
        int p7 = 0;
        p7 += age() > 85? 1 : 0;
        p7 += isMale()? 1 : 0;
        p7 += isHomeKeepService()? 1 : 0;
        p7 += isTrue(Questions.SELF_ENTER_FIRST_ID + 5)? 1 : 0;
        p7 += isTrue(Questions.SELF_ENTER_FIRST_ID + 9)
                && isTrue(Questions.SELF_ENTER_FIRST_ID + 12)
                && isFalse(Questions.SELF_ENTER_FIRST_ID + 18)? 1 : 0;
        p7 += isTrue(Questions.SELF_ENTER_FIRST_ID + 5) && isParentOrFriendsHelp()? 1 : 0;
        p7 += isTrue(Questions.SELF_ENTER_FIRST_ID + 9)? 1 : 0;

        return p7;
    }

    public int er2(){
        int er2 = 0;
        er2 += age() > 85? 1 : 0;
        er2 += isMale()? 1 : 0;
        er2 += needHomeHelp()? 1 : 0;
        er2 += tooMuchMedicine()? 1 : 0;
        er2 += forgotDates()? 1 : 0;
        er2 += isTrue(Questions.SYSTEMATIC_FIRST_ID + 19)? 1 : 0;
        er2 += haveFallen()? 1 : 0;
        return er2;
    }

    public int riskLevel() {
        double riskLevel = 0.0;
        riskLevel += age() > 85? 0.25 : 0;
        riskLevel += isMale()? 0.25 : 0;
        riskLevel += needHomeHelp()? 0.25 : 0;
        riskLevel += tooMuchMedicine()? 0.25 : 0;
        riskLevel += haveFallen()? 1 : 0;
        riskLevel += forgotDates()? 1 : 0;
        return (int)Math.floor(riskLevel);
    }

    public int getFtsstInSec() {
        return getInt(Questions.FTSST_FIRST_ID, 1);
    }

    private boolean forgotDates(){
        return answerIndexes(Questions.SYSTEMATIC_FIRST_ID + 5).contains(1);
    }

    private boolean haveFallen(){
        return isTrue(Questions.SELF_ENTER_FIRST_ID + 19);
    }

    private boolean tooMuchMedicine(){
        return getInt(Questions.SELF_ENTER_FIRST_ID + 1) >= 5;
    }

    private boolean needHomeHelp(){
        return isTrue(Questions.SELF_ENTER_FIRST_ID + 5);
    }

    private boolean isParentOrFriendsHelp() {
        List<Integer> helpIds = answerCheckBoxes(Questions.SELF_ENTER_FIRST_ID + 5, 1);
        return helpIds.contains(0) || helpIds.contains(1);
    }

    private boolean isHomeKeepService() {
        boolean isHomeKeepService = false;
        for(int i = 6; i < 14; i++){
            isHomeKeepService &= isTrue(Questions.SELF_ENTER_FIRST_ID + i);
        }
        return isHomeKeepService;
    }
}
