package mcgill.ca.fragilitysurvey.repo;

import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import mcgill.ca.fragilitysurvey.filter.SurveySearchFilter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;

public class SurveyService {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");

    private final Resources resources;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;

    public SurveyService(DBContext dbContext, Resources res){
        surveyRepository = new SurveyRepository(dbContext);
        answerRepository = new AnswerRepository(dbContext);
        resources = res;
    }

    public Survey insertNewSurvey(LinkedList<Question> questions) {
        Question idQuestion = null;
        for(Question q : questions){
            if(q.id() == Questions.ID_QUESTION_INDEX){
                idQuestion = q;
            }
        }
        questions.remove(idQuestion);
        Survey survey = new Survey().surveyId(generateId(idQuestion)).questions(questions);
        //ideally start transaction here
        answerRepository.insertQuestions(survey, questions);
        surveyRepository.insertSurvey(survey);
        return survey;
    }

    public SurveyService saveSurvey(Survey survey, LinkedList<Question> questions){
        answerRepository.insertQuestions(survey, questions);
        return this;
    }

    private String generateId(Question question) {
        int inputterIndex = 0;
        String lastName = AnswerType.TEXT.fromAnswer(question.inputters().get(inputterIndex++).answers().get(0));
        String firstName = AnswerType.TEXT.fromAnswer(question.inputters().get(inputterIndex++).answers().get(0));
        Integer monthOfBirth = NumberUtils.toInt(AnswerType.INT.fromAnswer(question.inputters().get(inputterIndex++).answers().get(0)));
        String monthStr = new DecimalFormat("00").format(monthOfBirth);
        return StringUtils.substring(lastName, 0, 3) + StringUtils.substring(firstName, 0, 3) + monthStr;
    }

    public List<Survey> getSurveys(SurveySearchFilter searchFilter) {
        List<Survey> result = new ArrayList<>();
        for(Survey survey : getSurveys()){
            if(StringUtils.isNoneBlank(searchFilter.id())){
                if(survey.surveyId().contains(searchFilter.id())){
                    result.add(survey);
                }
            } else {
                if( survey.timestamp().getTime() > searchFilter.from().getTime() && survey.timestamp().getTime() < searchFilter.to().getTime() ){
                    result.add(survey);
                }
            }
        }
        return result;
    }

    public List<Survey> getSurveys() {
        List<Survey> surveys = surveyRepository.getSurveys();
        for(Survey curSurvey : surveys) {
            curSurvey.questions(
                    answerRepository.getBySurveyId(curSurvey.surveyId(), resources)
            );
        }
        return surveys;
    }

    public Survey getSurveyById(String surveyId) {
        Survey survey = surveyRepository.getSurveysById(surveyId);
        survey.questions(answerRepository.getBySurveyId(survey.surveyId(), resources));
        return survey;
    }
}
