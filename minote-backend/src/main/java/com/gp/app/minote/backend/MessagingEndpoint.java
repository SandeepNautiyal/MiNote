/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.gp.app.minote.backend;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.NotFoundException;
import com.gp.app.minote.backend.userdata.UserRegistrationInfo;
import com.gp.app.minote.backend.userdata.UserRegistrationInfoEndpoint;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.gp.app.minote.backend.OfyService.ofy;

/**
 * An endpoint to send messages to devices registered with the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "messaging", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.minote.app.gp.com", ownerName = "backend.minote.app.gp.com", packagePath = ""))
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = "AIzaSyDcbVwtuHFlpDBMnjEZGoa9XNLQFnalWMs";

    private static final Logger logger = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    public void sendMessage(@Named("message") String message, @Named("emailId") String emailId) throws IOException {
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }

        log.info("Message =" + message +"emailId="+emailId);

        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);

        log.info("API KEY = " + API_KEY);

        Message msg = new Message.Builder().addData("message", message).build();

        UserRegistrationInfoEndpoint userRegistrationInfoEndpoint = new UserRegistrationInfoEndpoint();

        try
        {
            UserRegistrationInfo userRegistrationInfo = userRegistrationInfoEndpoint.get(emailId);

            log.info("userRegistrationInfo =" + userRegistrationInfo);

            Result result = sender.send(msg, userRegistrationInfo.getDeviceRegistrationId(), 5);
                if (result.getMessageId() != null) {
                    log.info("Message sent to " + userRegistrationInfo.getDeviceRegistrationId());
                    String canonicalRegId = result.getCanonicalRegistrationId();
                    if (canonicalRegId != null) {
                        // if the regId changed, we have to update the datastore
                        log.info("Registration Id changed for " + userRegistrationInfo.getDeviceRegistrationId() + " updating to " + canonicalRegId);
                        userRegistrationInfo.setDeviceRegistrationId(canonicalRegId);
                        ofy().save().entity(userRegistrationInfo).now();
                    }
                } else {
                    String error = result.getErrorCodeName();
                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        log.warning("Registration Id " + userRegistrationInfo.getDeviceRegistrationId() + " no longer registered with GCM, removing from datastore");
                        // if the device is no longer registered with Gcm, remove it from the datastore
                        ofy().delete().entity(userRegistrationInfo).now();
                    } else {
                        log.warning("Error when sending message : " + error);
                    }
                }

        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
        }


    }
}
