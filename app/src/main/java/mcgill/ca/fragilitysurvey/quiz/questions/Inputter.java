package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Inputter implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public Inputter createFromParcel(Parcel in) {
            IQuestion.InputType inputType = IQuestion.InputType.values()[in.readInt()];
            boolean orLogic = BooleanUtils.toBoolean(in.readInt());
            String caption = in.readString();
            List<OptionValue> options = new ArrayList<>();
            in.readList(options, OptionValue.class.getClassLoader());
            List<IAnswer> answers = new ArrayList<>();
            in.readList(answers, IAnswer.class.getClassLoader());
            return new Inputter()
                    .inputType(inputType)
                    .orLogic(orLogic)
                    .caption(caption)
                    .options(options)
                    .answers(answers);
        }

        public IQuestion.Question[] newArray(int size) {
            return new IQuestion.Question[size];
        }
    };

    //common fields
    private IQuestion.InputType inputType;
    private String caption;
    //option specific
    private List<OptionValue> options = new ArrayList<>();
    private boolean orLogic = true;
    //answers
    private List<IAnswer> answers = new ArrayList<>();

    public Inputter() {
    }

    public IQuestion.InputType inputType() {
        return inputType;
    }

    public Inputter inputType(IQuestion.InputType inputType) {
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
        dest.writeInt(inputType.ordinal());
        dest.writeInt(BooleanUtils.toInteger(orLogic));
        dest.writeString(caption);
        dest.writeList(options);
        dest.writeList(answers);
    }

    @Override
    public String toString() {
        return "Inputter{" +
                "inputType=" + inputType +
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
