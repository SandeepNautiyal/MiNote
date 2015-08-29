package com.gp.app.minote.backend.entities;

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
        name = "eventEntityApi",
        version = "v1",
        resource = "eventEntity",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.minote.app.gp.com",
                ownerName = "entities.backend.minote.app.gp.com",
                packagePath = ""
        )
)
public class EventEntityEndpoint
{
    private static final Logger logger = Logger.getLogger(EventEntityEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(EventEntity.class);
    }

    /**
     * Returns the {@link EventEntity} with the corresponding ID.
     *
     * @param eventId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code EventEntity} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "eventEntity/{eventId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public EventEntity get(@Named("eventId") Long eventId) throws NotFoundException {
        logger.info("Getting EventEntity with ID: " + eventId);
        EventEntity eventEntity = ofy().load().type(EventEntity.class).id(eventId).now();
        if (eventEntity == null) {
            throw new NotFoundException("Could not find EventEntity with ID: " + eventId);
        }
        return eventEntity;
    }

    /**
     * Inserts a new {@code EventEntity}.
     */
    @ApiMethod(
            name = "insert",
            path = "eventEntity",
            httpMethod = ApiMethod.HttpMethod.POST)
    public EventEntity insert(EventEntity eventEntity)
    {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that eventEntity.eventId has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(eventEntity).now();
        logger.info("Created EventEntity.");

        return ofy().load().entity(eventEntity).now();
    }

    /**
     * Updates an existing {@code EventEntity}.
     *
     * @param eventId     the ID of the entity to be updated
     * @param eventEntity the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code eventId} does not correspond to an existing
     *                           {@code EventEntity}
     */
    @ApiMethod(
            name = "update",
            path = "eventEntity/{eventId}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public EventEntity update(@Named("eventId") Long eventId, EventEntity eventEntity) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(eventId);
        ofy().save().entity(eventEntity).now();
        logger.info("Updated EventEntity: " + eventEntity);
        return ofy().load().entity(eventEntity).now();
    }

    /**
     * Deletes the specified {@code EventEntity}.
     *
     * @param eventId the ID of the entity to delete
     * @throws NotFoundException if the {@code eventId} does not correspond to an existing
     *                           {@code EventEntity}
     */
    @ApiMethod(
            name = "remove",
            path = "eventEntity/{eventId}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("eventId") Long eventId) throws NotFoundException {
        checkExists(eventId);
        ofy().delete().type(EventEntity.class).id(eventId).now();
        logger.info("Deleted EventEntity with ID: " + eventId);
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
            path = "eventEntity",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<EventEntity> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<EventEntity> query = ofy().load().type(EventEntity.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<EventEntity> queryIterator = query.iterator();
        List<EventEntity> eventEntityList = new ArrayList<EventEntity>(limit);
        while (queryIterator.hasNext()) {
            eventEntityList.add(queryIterator.next());
        }
        return CollectionResponse.<EventEntity>builder().setItems(eventEntityList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long eventId) throws NotFoundException {
        try {
            ofy().load().type(EventEntity.class).id(eventId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find EventEntity with ID: " + eventId);
        }
    }
}