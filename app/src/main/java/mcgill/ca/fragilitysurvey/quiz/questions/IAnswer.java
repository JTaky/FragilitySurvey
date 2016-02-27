package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;
public interface IAnswer<T> extends Parcelable {

    IAnswer value(T value);

    T value();

    IAnswer NULL_ANSWER = new IAnswer<Void>() {

        @Override
        public IAnswer value(Void value) {
            return this;
        }

        @Override
        public Void value() {
            return null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public String toString() {
            return "$classname{}";
        }

    };

    final class StringAnswer implements IAnswer<String> {

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public StringAnswer createFromParcel(Parcel in) {
                return new StringAnswer().value(in.readString());
            }

            public StringAnswer[] newArray(int size) {
                return new StringAnswer[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(value);
        }

        private String value;

        @Override
        public StringAnswer value(String value) {
            this.value = value;
            return this;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringAnswer that = (StringAnswer) o;

            return !(value != null ? !value.equals(that.value) : that.value != null);

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    final class ChooseAnswer implements IAnswer<Integer> {

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public ChooseAnswer createFromParcel(Parcel in) {
                return new ChooseAnswer().value(in.readInt());
            }

            public ChooseAnswer[] newArray(int size) {
                return new ChooseAnswer[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(value);
        }

        private Integer value;

        @Override
        public ChooseAnswer value(Integer value) {
            this.value = value;
            return this;
        }

        @Override
        public Integer value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChooseAnswer that = (ChooseAnswer) o;

            return !(value != null ? !value.equals(that.value) : that.value != null);

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    final class IntAnswer implements IAnswer<Integer> {

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public IntAnswer createFromParcel(Parcel in) {
                return new IntAnswer().value(in.readInt());
            }

            public IntAnswer[] newArray(int size) {
                return new IntAnswer[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(value);
        }

        private Integer value;

        @Override
        public IntAnswer value(Integer value) {
            this.value = value;
            return this;
        }

        @Override
        public Integer value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntAnswer intAnswer = (IntAnswer) o;

            return !(value != null ? !value.equals(intAnswer.value) : intAnswer.value != null);

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    final class DoubleAnswer implements IAnswer<Double> {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public DoubleAnswer createFromParcel(Parcel in) {
                return new DoubleAnswer().value(in.readDouble());
            }

            public DoubleAnswer[] newArray(int size) {
                return new DoubleAnswer[size];
            }
        };

        private Double value;

        @Override
        public DoubleAnswer value(Double value) {
            this.value = value;
            return this;
        }

        @Override
        public Double value() {
            return value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(value);
        }
    }

}
