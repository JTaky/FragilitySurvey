package mcgill.ca.fragilitysurvey.repo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;

public class Survey implements Parcelable {

    public static final Creator<Survey> CREATOR = new Creator<Survey>() {
        @Override
        public Survey createFromParcel(Parcel in) {
            return new Survey(in);
        }

        @Override
        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };

    private String surveyId;
    private Date timestamp = new Date();
    private List<Question> questions;

    public Survey() {

    }

    protected Survey(Parcel in) {
        surveyId = in.readString();
        timestamp = new Date(in.readLong());
        questions = in.createTypedArrayList(Question.CREATOR);
    }

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

    public List<Question> questions() {
        return Collections.unmodifiableList(this.questions);
    }

    public boolean isCompleted() {
        return this.questions.size() > Questions.PATIENT_QUESTIONS_COUNT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(surveyId);
        dest.writeLong(timestamp.getTime());
        dest.writeTypedList(questions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (surveyId != null ? !surveyId.equals(survey.surveyId) : survey.surveyId != null)
            return false;
        if (timestamp != null ? !timestamp.equals(survey.timestamp) : survey.timestamp != null)
            return false;
        return !(questions != null ? !questions.equals(survey.questions) : survey.questions != null);
    }

    @Override
    public int hashCode() {
        int result = surveyId != null ? surveyId.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId='" + surveyId + '\'' +
                ", timestamp=" + timestamp +
                ", questions=" + questions +
                '}';
    }
}
