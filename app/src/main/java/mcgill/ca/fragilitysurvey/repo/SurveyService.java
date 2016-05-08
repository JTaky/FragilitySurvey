package mcgill.ca.fragilitysurvey.repo;

import android.content.res.Resources;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

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
        Survey survey = new Survey().surveyId(generateId()).questions(questions);
        //ideally start transaction here
        answerRepository.insertQuestions(survey, questions);
        surveyRepository.insertSurvey(survey);
        return survey;
    }

    public SurveyService saveSurvey(Survey survey, LinkedList<Question> questions){
        answerRepository.insertQuestions(survey, questions);
        return this;
    }

    private String generateId() {
        return dateFormat.format(new DateTime().toDate()) + "_" + (surveyRepository.getSurveys(DateTime.now()).size() + 1);
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
}
