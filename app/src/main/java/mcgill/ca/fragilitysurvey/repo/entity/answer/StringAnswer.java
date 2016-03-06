package mcgill.ca.fragilitysurvey.repo.entity.answer;

import android.os.Parcel;

public final class StringAnswer implements IAnswer<String> {

    public static final Creator CREATOR = new Creator() {
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

    @Override
    public String toString() {
        return "StringAnswer{" +
                "value='" + value + '\'' +
                '}';
    }
}
