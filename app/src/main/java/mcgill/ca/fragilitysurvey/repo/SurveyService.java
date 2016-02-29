package mcgill.ca.fragilitysurvey.repo;

import java.util.LinkedList;
import java.util.UUID;

import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class SurveyService {

    private SurveyRepository surveyRepository;
    private AnswerRepository answerRepository;

    public SurveyService(DBContext dbContext){
        surveyRepository = new SurveyRepository(dbContext);
        answerRepository = new AnswerRepository(dbContext);
    }

    public void newSurvey(LinkedList<Question> questions) {
        Survey survey = new Survey().surveyId(generateId());
        //ideally start transaction here
        answerRepository.saveQuestions(survey, questions);
        surveyRepository.saveSurvey(survey);

    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

}
