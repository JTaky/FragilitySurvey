package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

public class OptionValue implements Parcelable {

    public static final Creator<OptionValue> CREATOR = new Creator<OptionValue>() {
        @Override
        public OptionValue createFromParcel(Parcel in) {
            return new OptionValue(in);
        }

        @Override
        public OptionValue[] newArray(int size) {
            return new OptionValue[size];
        }
    };

    private String caption;
    private int id;

    public OptionValue(){}

    protected OptionValue(Parcel in) {
        caption = in.readString();
        id = in.readInt();
    }

    public String caption() {
        return caption;
    }

    @Override
    public String toString() {
        return "OptionValue{" +
                "caption='" + caption + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionValue that = (OptionValue) o;

        if (id != that.id) return false;
        return !(caption != null ? !caption.equals(that.caption) : that.caption != null);

    }

    @Override
    public int hashCode() {
        int result = caption != null ? caption.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }

    public OptionValue caption(String caption) {
        this.caption = caption;
        return this;
    }

    public int id() {
        return id;
    }

    public OptionValue id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeInt(id);
    }
}
