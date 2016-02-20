package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

public interface IQuestion extends Parcelable {

    enum QuestionType {

        TEXT_INPUT;

        public static QuestionType fromInt(int v){
            return values()[v];
        }

    }

    String questionText();

    QuestionType questionType();

    class Question implements IQuestion {

        private String questionText;
        private QuestionType questionType;

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Question createFromParcel(Parcel in) {
                return new Question()
                        .questionText(in.readString())
                        .questionType(QuestionType.fromInt(in.readInt()));
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
            dest.writeInt(questionType.ordinal());
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
        public QuestionType questionType(){
            return questionType;
        }

        public Question questionType(QuestionType questionType){
            this.questionType = questionType;
            return this;
        }
    }

}
