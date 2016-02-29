package mcgill.ca.fragilitysurvey.repo.entity;

import org.joda.time.DateTime;

import java.util.Date;

public class Survey {

    private String surveyId;
    private Date timestamp = new Date();

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

}
