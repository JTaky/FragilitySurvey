package mcgill.ca.fragilitysurvey.repo.entity.answer;

import android.os.Parcel;

public final class IntAnswer implements IAnswer<Integer> {

    public static final Creator CREATOR = new Creator() {
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

    @Override
    public String toString() {
        return "IntAnswer{" +
                "value=" + value +
                '}';
    }
}
