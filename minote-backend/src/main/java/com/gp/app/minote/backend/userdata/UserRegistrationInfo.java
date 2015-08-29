package com.gp.app.minote.backend.userdata;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by dell on 7/9/2015.
 */
@Entity
public class UserRegistrationInfo
{
    @Id
    private String userEmail;

    @Index
    private String deviceRegistrationId;

    private String userName;

    public UserRegistrationInfo()
    {
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getName()
    {
        return userName;
    }

    public void setDeviceRegistrationId(String deviceRegistrationId)
    {
        this.deviceRegistrationId = deviceRegistrationId;
    }

    public String getDeviceRegistrationId()
    {
        return deviceRegistrationId;
    }
}
