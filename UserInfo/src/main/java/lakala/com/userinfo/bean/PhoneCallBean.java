package lakala.com.userinfo.bean;

/**
 * Created by lvyong on 15/11/4.
 */
public class PhoneCallBean {

    //手机号码
    private String phoneNumber;
    //姓名
    private String name;
    //通话时长
    private String duration;
    //通话时间
    private String time;


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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
