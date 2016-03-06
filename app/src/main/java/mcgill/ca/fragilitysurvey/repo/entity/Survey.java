package mcgill.ca.fragilitysurvey.repo.entity;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;

public class Survey {

    private String surveyId;
    private Date timestamp = new Date();
    private List<Question> questions;

    public String surveyId(){
        return surveyId;
    }

    public Survey surveyId(String surveyId){
        this.surveyId = surveyId;
        return this;
    }

    public Date timestamp(){
        return timestamp;
    }

    public Survey timestamp(Date timestamp){
        this.timestamp = timestamp;
        return this;
    }

    public Survey questions(List<Question> questions) {
        this.questions = questions;
        return this;
    }

    public boolean isCompleted() {
        return this.questions.size() > Questions.PATIENT_QUESTIONS_COUNT;
    }
}
