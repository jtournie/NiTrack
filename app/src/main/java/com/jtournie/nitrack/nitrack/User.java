package com.jtournie.nitrack.nitrack;

/**
 * Created by jtournie on 22/11/14.
 *
 */
public class User {
    private String userName;
    private String fullName;
    private NiTime intakeTime;

    public User()
    {
        intakeTime = new NiTime();
        intakeTime.Hour = 7; //hours from [0-11] but it is normally filled from shared preferences
        intakeTime.Minute = 00;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public NiTime getIntakeTime() {
        return intakeTime;
    }

    public void setIntakeTime(NiTime intakeHour) {
        this.intakeTime = intakeHour;
    }
}
