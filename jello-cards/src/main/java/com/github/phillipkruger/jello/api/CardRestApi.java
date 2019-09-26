package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import lombok.extern.java.Log;

/**
 * RESTFull Web Services, Context and Dependency Injection. Basic REST API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Path("/card")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Card service")
@Log
public class CardRestApi {

    @Inject
    private CardService cardService;
    
    @Context 
    private UriInfo uriInfo;
    
    @POST
    @ApiOperation(value = "Create a new Card", notes = "Create a card using json")
    public Response createCard(Card card){
        card = cardService.createCard(card);
        return Response.created(getGetUri(card)).build();
    }
    
    @PUT
    @ApiOperation(value = "Update a new Card", notes = "Update a card using json")
    public Response updateCard(Card card){
        if(card.getId()==null)return createCard(card);
        card = cardService.updateCard(card);
        return Response.ok(card).build();
    }
    
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get a Card", notes = "Get a card using an Id", response = Card.class)
    public Response getCard(@PathParam("id") Long id){
        Card card = cardService.getCard(id);
        if(card==null)return Response.noContent().build();
        return Response.ok(card).build();
    }
    
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete a Card", notes = "Delete a card with an Id")
    public Response removeCard(@PathParam("id") Long id){
        Card card = cardService.getCard(id);
        if(card!=null)cardService.removeCard(card);
        return Response.noContent().build();
    }
    
    @GET
    @ApiOperation(value = "Get all cards", notes = "Get all cards in json")
    public Response getCards(){
        List<Card> cards = cardService.getAllCards();
        if(cards==null || cards.isEmpty())return Response.noContent().build();
        return Response.ok(cards).build();
    }
    
    private URI getGetUri(Card card){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(card.getId()));
        return builder.build();
    }
}