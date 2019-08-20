package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import java.net.URI;
import javax.inject.Inject;
//import javax.security.enterprise.SecurityContext;
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
 * Basic REST API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Path("/card")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class CardRestApi {

    @Inject
    private CardService cardService;
    
    @Context 
    private UriInfo uriInfo;
    
    //@Inject
    //private SecurityContext securityContext;
    
    @POST
    public Response createCard(Card card){
        card = cardService.createCard(card);
        return Response.created(getGetUri(card)).build();
    }
    
    @PUT
    public Response updateCard(Card card){
        if(card.getId()==null)return createCard(card);
        card = cardService.updateCard(card);
        return Response.ok(card).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getCard(@PathParam("id") Long id){
        
        Card card = cardService.getCard(id);
        return Response.ok(card).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response removeCard(@PathParam("id") Long id){
        Card card = cardService.getCard(id);
        if(card!=null)cardService.removeCard(card);
        return Response.noContent().build();
    }
    
    private URI getGetUri(Card card){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(card.getId()));
        return builder.build();
    }
}