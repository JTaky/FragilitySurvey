package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

/**
 * Represent model for the current input(part of the question)
 */
public class Inputter implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public Inputter createFromParcel(Parcel in) {
            int id = in.readInt();
            AnswerType inputType = AnswerType.fromId(in.readInt());
            boolean orLogic = BooleanUtils.toBoolean(in.readInt());
            String caption = in.readString();
            Boolean isValidateable = BooleanUtils.toBoolean(in.readInt());
            List<OptionValue> options = new ArrayList<>();
            in.readList(options, OptionValue.class.getClassLoader());
            List<IAnswer> answers = new ArrayList<>();
            in.readList(answers, IAnswer.class.getClassLoader());
            return new Inputter()
                    .id(id)
                    .inputType(inputType)
                    .orLogic(orLogic)
                    .caption(caption)
                    .isValidateable(isValidateable)
                    .options(options)
                    .answers(answers);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    //common fields
    private int id;
    private AnswerType inputType;
    private String caption;
    private boolean isValidateable = true;
    //option specific
    private List<OptionValue> options = new ArrayList<>();
    private boolean orLogic = true;
    //answers
    private List<IAnswer> answers = new ArrayList<>();

    public Inputter() {}

    public int id(){
        return id;
    }

    public Inputter id(int id){
        this.id = id;
        return this;
    }

    public AnswerType inputType() {
        return inputType;
    }

    public Inputter inputType(AnswerType inputType) {
        this.inputType = inputType;
        return this;
    }

    public String caption() {
        return caption;
    }

    public Inputter caption(String caption) {
        this.caption = caption;
        return this;
    }

    public boolean isValidateable() {
        return isValidateable;
    }

    public Inputter isValidateable(boolean isValidateable) {
        this.isValidateable = isValidateable;
        return this;
    }

    public List<OptionValue> options() {
        return options;
    }

    public Inputter options(List<OptionValue> options) {
        this.options = options;
        return this;
    }

    public boolean isOrLogic() {
        return orLogic;
    }

    public Inputter orLogic(boolean orLogic) {
        this.orLogic = orLogic;
        return this;
    }

    public Inputter answer(IAnswer answer) {
        this.answers = Collections.singletonList(answer);
        return this;
    }

    private Inputter answers(List<IAnswer> answers) {
        this.answers = answers;
        return this;
    }

    public List<IAnswer> answers() {
        return answers;
    }

    public Inputter addAnswer(IAnswer answer) {
        this.answers.add(answer);
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(inputType.id);
        dest.writeInt(BooleanUtils.toInteger(orLogic));
        dest.writeString(caption);
        dest.writeInt(BooleanUtils.toInteger(isValidateable));
        dest.writeList(options);
        dest.writeList(answers);
    }

    @Override
    public String toString() {
        return "Inputter{" +
                "id=" + id +
                ", inputType=" + inputType +
                ", caption='" + caption + '\'' +
                ", options=" + options +
                ", orLogic=" + orLogic +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inputter inputter = (Inputter) o;

        if (orLogic != inputter.orLogic) return false;
        if (inputType != inputter.inputType) return false;
        if (caption != null ? !caption.equals(inputter.caption) : inputter.caption != null)
            return false;
        if (options != null ? !options.equals(inputter.options) : inputter.options != null)
            return false;
        return !(answers != null ? !answers.equals(inputter.answers) : inputter.answers != null);

    }

    @Override
    public int hashCode() {
        int result = inputType != null ? inputType.hashCode() : 0;
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (options != null ? options.hashCode() : 0);
        result = 31 * result + (orLogic ? 1 : 0);
        result = 31 * result + (answers != null ? answers.hashCode() : 0);
        return result;
    }
}
