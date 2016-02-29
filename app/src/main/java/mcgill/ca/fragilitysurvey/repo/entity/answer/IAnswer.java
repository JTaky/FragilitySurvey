package mcgill.ca.fragilitysurvey.repo.entity.answer;

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

}
