package mcgill.ca.fragilitysurvey.repo.entity.answer;

import android.os.Parcel;

public final class DoubleAnswer implements IAnswer<Double> {
    public static final Creator CREATOR = new Creator() {
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
