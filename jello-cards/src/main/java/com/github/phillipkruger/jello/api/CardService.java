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
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Basic API for Cards
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@RequestScoped
@Path("/card")
public class CardService {

    @GET
    public Response ping(){
        return Response.ok("pong").build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public Response createCard(Card card, @Context UriInfo uriInfo){
        
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(123));
        return Response.created(builder.build()).build();
    }
}