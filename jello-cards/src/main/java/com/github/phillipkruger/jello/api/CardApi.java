/*
 * Copyright 2019 Phillip Kruger (phillip.kruger@redhat.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.service.CardService;
import java.net.URI;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
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
 * Basic API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@RequestScoped
@Path("/card")
public class CardApi {

    @Inject
    private CardService cardService;
    
    @Context 
    private UriInfo uriInfo;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCard(Card card){
        card = cardService.createCard(card);
        log.log(Level.SEVERE, ">>>>> Create card {0}", card);
        return Response.created(getGetUri(card)).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCard(Card card){
        if(card.getId()==null)return createCard(card);
        card = cardService.updateCard(card);
        log.log(Level.SEVERE, ">>>>> Updating card {0}", card);
        return Response.ok(card).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCard(@PathParam("id") Long id){
        Card card = cardService.getCard(id);
        log.log(Level.SEVERE, ">>>>> Get card {0}", card);
        return Response.ok(card).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCard(@PathParam("id") Long id){
        Card card = cardService.deleteCard(id);
        log.log(Level.SEVERE, ">>>>> Delete card {0}", card);
        return Response.noContent().build();
    }
    
    private URI getGetUri(Card card){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(card.getId()));
        return builder.build();
    }
}