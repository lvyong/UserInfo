package lakala.com.userinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 短信Bean
 * Created by lvyong on 15/11/4.
 */
public class MessageBean implements Parcelable{

    //姓名
    private String name;

    //手机号码
    private String phoneNumber;

    //短信内容
    private String smsbody;

    //时间
    private String date;

    //type 短信类型1是接收到的，2是已发出
    private String type;

    public MessageBean(){

    }

    public MessageBean(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        smsbody = in.readString();
        date = in.readString();
        type = in.readString();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmsbody() {
        return smsbody;
    }

    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(smsbody);
        dest.writeString(date);
        dest.writeString(type);
    }


    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel in) {
            return new MessageBean(in);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };
}
