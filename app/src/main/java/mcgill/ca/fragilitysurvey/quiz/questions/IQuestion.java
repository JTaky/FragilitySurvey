package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;

public interface IQuestion extends Parcelable {

    String questionText();

    List<Inputter> inputters();

    enum InputType {
        TEXT_INPUT,
        CHOOSE,
        INT_INPUT,
        DOUBLE_INPUT;

        public static InputType fromInt(int v){
            return values()[v];
        }

    }

    class Inputter implements Parcelable {

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Inputter createFromParcel(Parcel in) {
                InputType inputType = InputType.values()[in.readInt()];
                boolean orLogic = BooleanUtils.toBoolean(in.readInt());
                String caption = in.readString();
                List<String> options = new ArrayList<>(); in.readStringList(options);
                return new Inputter()
                        .inputType(inputType)
                        .orLogic(orLogic)
                        .caption(caption)
                        .options(options);
            }

            public Question[] newArray(int size) {
                return new Question[size];
            }
        };

        private InputType inputType;
        private String caption;
        private List<String> options = new ArrayList<>();
        private boolean orLogic = true;

        public Inputter(){}

        public InputType inputType(){
            return inputType;
        }

        public Inputter inputType(InputType inputType){
            this.inputType = inputType;
            return this;
        }

        public String caption(){
            return caption;
        }

        public Inputter caption(String caption){
            this.caption = caption;
            return this;
        }

        public List<String> options(){
            return options;
        }

        public Inputter options(List<String> options){
            this.options = options;
            return this;
        }

        public boolean isOrLogic(){
            return orLogic;
        }

        public Inputter orLogic(boolean orLogic){
            this.orLogic = orLogic;
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
            dest.writeStringList(options);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Inputter that = (Inputter) o;

            if (orLogic != that.orLogic) return false;
            if (inputType != that.inputType) return false;
            if (caption != null ? !caption.equals(that.caption) : that.caption != null)
                return false;
            return !(options != null ? !options.equals(that.options) : that.options != null);

        }

        @Override
        public int hashCode() {
            int result = inputType != null ? inputType.hashCode() : 0;
            result = 31 * result + (caption != null ? caption.hashCode() : 0);
            result = 31 * result + (options != null ? options.hashCode() : 0);
            result = 31 * result + (orLogic ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Inputter{" +
                    "inputType=" + inputType +
                    ", caption='" + caption + '\'' +
                    ", options=" + options +
                    ", orLogic=" + orLogic +
                    '}';
        }

    }

    class Question implements IQuestion {

        private String questionText;
        private List<Inputter> inputters = new ArrayList<>();

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Question createFromParcel(Parcel in) {
                return new Question()
                        .questionText(in.readString())
                        .inputers(readInputers(in));
            }

            private List<Inputter> readInputers(Parcel in) {
                int inputerSize = in.readInt();
                List<Inputter> inputers = new ArrayList<>(inputerSize);
                for(int i = 0; i < inputerSize; i++){
                    Inputter inputter = in.readParcelable(Inputter.class.getClassLoader());
                    inputers.add(inputter);
                }
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
            dest.writeString(questionText);
            dest.writeInt(inputters.size());
            for(Inputter cur : inputters){
                dest.writeParcelable(cur, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            }
        }

        @Override
        public String questionText(){
            return questionText;
        }

        public Question questionText(String questionText){
            this.questionText = questionText;
            return this;
        }

        @Override
        public List<Inputter> inputters() {
            return inputters;
        }

        public Question inputers(List<Inputter> chooses) {
            this.inputters = chooses;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Question question = (Question) o;

            if (questionText != null ? !questionText.equals(question.questionText) : question.questionText != null)
                return false;
            return !(inputters != null ? !inputters.equals(question.inputters) : question.inputters != null);

        }

        @Override
        public int hashCode() {
            int result = questionText != null ? questionText.hashCode() : 0;
            result = 31 * result + (inputters != null ? inputters.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Question{" +
                    "questionText='" + questionText + '\'' +
                    ", inputters=" + inputters +
                    '}';
        }
    }

}
