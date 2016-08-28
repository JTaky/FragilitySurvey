package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Question implements Parcelable {

    private int id;
    private String questionText;
    private List<Inputter> inputters = new ArrayList<>();

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question()
                    .id(in.readInt())
                    .questionText(in.readString())
                    .inputers(readInputers(in));
        }

        private List<Inputter> readInputers(Parcel in) {
            List<Inputter> inputers = new ArrayList<>();
            in.readList(inputers, Inputter.class.getClassLoader());
            return inputers;
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(questionText);
        dest.writeList(inputters);
    }

    public int id(){
        return id;
    }

    public Question id(int id){
        this.id = id;
        return this;
    }

    public String questionText() {
        return questionText;
    }

    public mcgill.ca.fragilitysurvey.quiz.questions.Question questionText(String questionText) {
        this.questionText = questionText;
        return this;
    }

    public List<Inputter> inputters() {
        return inputters;
    }

    public Question addInputter(Inputter inputter) {
        removeInputterById(inputter.id());
        inputters.add(inputter);
        return this;
    }

    public void removeInputterById(int inputterId){
        Iterator<Inputter> it = inputters.iterator();
        while (it.hasNext()){
            if(it.next().id() == inputterId){
                it.remove();
            }
        }
    }

    public Question inputers(List<Inputter> inputters) {
        int inputterId = 0;
        for(Inputter inputter : inputters){
            inputter.id(inputterId++);
        }
        this.inputters = inputters;
        return this;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", inputters=" + inputters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != question.id) return false;
        if (questionText != null ? !questionText.equals(question.questionText) : question.questionText != null)
            return false;
        return !(inputters != null ? !inputters.equals(question.inputters) : question.inputters != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (inputters != null ? inputters.hashCode() : 0);
        return result;
    }

    public Inputter getInputterById(int inputterId) {
        for(Inputter inputter : inputters()){
            if(inputter.id() == inputterId){
                return inputter;
            }
        }
        return null;
    }
}
