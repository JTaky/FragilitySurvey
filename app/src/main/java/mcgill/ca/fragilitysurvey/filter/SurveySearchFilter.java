package mcgill.ca.fragilitysurvey.filter;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public final class SurveySearchFilter implements Parcelable {

    private String id;

    private Date from;
    private Date to;

    protected SurveySearchFilter() {}

    protected SurveySearchFilter(Parcel in) {
        id = in.readString();
        from = new Date(in.readLong());
        to = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(from.getTime());
        dest.writeLong(to.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SurveySearchFilter> CREATOR = new Creator<SurveySearchFilter>() {
        @Override
        public SurveySearchFilter createFromParcel(Parcel in) {
            return new SurveySearchFilter(in);
        }

        @Override
        public SurveySearchFilter[] newArray(int size) {
            return new SurveySearchFilter[size];
        }
    };

    public String id(){
        return id;
    }
    public SurveySearchFilter id(String id){
        this.id = id;
        return this;
    }

    public Date from(){
        return from;
    }
    public SurveySearchFilter from(Date from){
        this.from = from;
        return this;
    }

    public Date to(){
        return to;
    }
    public SurveySearchFilter to(Date to){
        this.to = to;
        return this;
    }

}
