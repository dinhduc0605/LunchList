package tutorial.apt.lunchlist.model;

/**
 * Created by dinhduc on 07/09/2016.
 */
public class Restaurant {
    private String mName = "";
    private String mAddress = "";
    private String mType = "";
    private String mNotes = "";

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    @Override
    public String toString() {
        return getName();
    }
}
