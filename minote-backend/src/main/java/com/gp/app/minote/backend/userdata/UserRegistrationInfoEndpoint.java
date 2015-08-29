package com.gp.app.minote.backend.userdata;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "userRegistrationInfoApi",
        version = "v1",
        resource = "userRegistrationInfo",
        namespace = @ApiNamespace(
                ownerDomain = "userdata.backend.minote.app.gp.com",
                ownerName = "userdata.backend.minote.app.gp.com",
                packagePath = ""
        )
)
public class UserRegistrationInfoEndpoint {

    private static final Logger logger = Logger.getLogger(UserRegistrationInfoEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(UserRegistrationInfo.class);
    }

    /**
     * Returns the {@link UserRegistrationInfo} with the corresponding ID.
     *
     * @param userEmail the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code UserRegistrationInfo} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "userRegistrationInfo/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public UserRegistrationInfo get(@Named("userEmail") String userEmail) throws NotFoundException {
        logger.info("Getting UserRegistrationInfo with ID: " + userEmail);
        UserRegistrationInfo userRegistrationInfo = ofy().load().type(UserRegistrationInfo.class).id(userEmail).now();
        if (userRegistrationInfo == null) {
            throw new NotFoundException("Could not find UserRegistrationInfo with ID: " + userEmail);
        }
        return userRegistrationInfo;
    }

    /**
     * Inserts a new {@code UserRegistrationInfo}.
     */
    @ApiMethod(
            name = "insert",
            path = "userRegistrationInfo",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserRegistrationInfo insert(UserRegistrationInfo userRegistrationInfo) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that userRegistrationInfo.userEmail has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(userRegistrationInfo).now();
        logger.info("Created UserRegistrationInfo with ID: " + userRegistrationInfo.getUserEmail());

        return ofy().load().entity(userRegistrationInfo).now();
    }

    /**
     * Updates an existing {@code UserRegistrationInfo}.
     *
     * @param userEmail            the ID of the entity to be updated
     * @param userRegistrationInfo the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code userEmail} does not correspond to an existing
     *                           {@code UserRegistrationInfo}
     */
    @ApiMethod(
            name = "update",
            path = "userRegistrationInfo/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public UserRegistrationInfo update(@Named("userEmail") String userEmail, UserRegistrationInfo userRegistrationInfo) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(userEmail);
        ofy().save().entity(userRegistrationInfo).now();
        logger.info("Updated UserRegistrationInfo: " + userRegistrationInfo);
        return ofy().load().entity(userRegistrationInfo).now();
    }

    /**
     * Deletes the specified {@code UserRegistrationInfo}.
     *
     * @param userEmail the ID of the entity to delete
     * @throws NotFoundException if the {@code userEmail} does not correspond to an existing
     *                           {@code UserRegistrationInfo}
     */
    @ApiMethod(
            name = "remove",
            path = "userRegistrationInfo/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("userEmail") String userEmail) throws NotFoundException {
        checkExists(userEmail);
        ofy().delete().type(UserRegistrationInfo.class).id(userEmail).now();
        logger.info("Deleted UserRegistrationInfo with ID: " + userEmail);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "userRegistrationInfo",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<UserRegistrationInfo> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<UserRegistrationInfo> query = ofy().load().type(UserRegistrationInfo.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<UserRegistrationInfo> queryIterator = query.iterator();
        List<UserRegistrationInfo> userRegistrationInfoList = new ArrayList<UserRegistrationInfo>(limit);
        while (queryIterator.hasNext()) {
            userRegistrationInfoList.add(queryIterator.next());
        }
        return CollectionResponse.<UserRegistrationInfo>builder().setItems(userRegistrationInfoList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String userEmail) throws NotFoundException {
        try {
            ofy().load().type(UserRegistrationInfo.class).id(userEmail).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find UserRegistrationInfo with ID: " + userEmail);
        }
    }
}