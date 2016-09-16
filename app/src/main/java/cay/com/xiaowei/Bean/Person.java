package cay.com.xiaowei.Bean;

/**
 * Created by C on 2016/9/15.
 */
public class Person {
    public String resCode;
    public String resMsg;
    public String userName;
    public String nikeName;
    public String phone;
    public String gender;
    public String userId;
    public String vip;

    @Override
    public String toString() {
        return "Person{" +
                "resCode='" + resCode + '\'' +
                ", resMsg='" + resMsg + '\'' +
                ", userName='" + userName + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", userId='" + userId + '\'' +
                ", vip='" + vip + '\'' +
                '}';
    }
}
