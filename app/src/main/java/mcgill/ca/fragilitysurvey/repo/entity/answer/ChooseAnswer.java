package mcgill.ca.fragilitysurvey.repo.entity.answer;

import android.os.Parcel;

public final class ChooseAnswer implements IAnswer<Integer> {

    public static final Creator CREATOR = new Creator() {
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
