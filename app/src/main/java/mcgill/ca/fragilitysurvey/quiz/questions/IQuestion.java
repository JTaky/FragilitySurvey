package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

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
