package mrh5493.edu.psu.doodlepadpro;

import android.os.Parcel;
import android.os.Parcelable;

public class DoodleDEF implements Parcelable{
    private String Title;
    private String Description;
    private String Image;

    public DoodleDEF() {

    }

    public DoodleDEF(String title, String description, String image) {
        Title = title;
        Description = description;
        Image = image;
    }

    protected DoodleDEF(Parcel in) {
        Title = in.readString();
        Description = in.readString();
        Image = in.readString();
    }

    public static final Creator<DoodleDEF> CREATOR = new Creator<DoodleDEF>() {
        @Override
        public DoodleDEF createFromParcel(Parcel in) {
            return new DoodleDEF(in);
        }

        @Override
        public DoodleDEF[] newArray(int size) {
            return new DoodleDEF[size];
        }
    };

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeString(Image);
    }
}
