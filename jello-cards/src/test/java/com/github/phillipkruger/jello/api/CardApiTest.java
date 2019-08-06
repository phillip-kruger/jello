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

import com.github.javafaker.Faker;
import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Comment;
import com.github.phillipkruger.jello.service.CardService;
import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.java.Log;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * Testing Card service
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class CardApiTest extends Arquillian {

    private static final String PATH = "api/card";
    
    @ArquillianResource
    private URI uri;
    
    private Client client;
    private Card testCard;
    
    @Deployment
    public static WebArchive createDeployment() {

        final File[] dbdriver = Maven.resolver().resolve("com.h2database:h2:1.4.199")
                .withoutTransitivity().asFile();
        
        return ShrinkWrap.create(WebArchive.class, "CardApiTest.war")
                .addPackage(CardApi.class.getPackage())
                .addPackage(Card.class.getPackage())
                .addPackage(CardService.class.getPackage())
                .addAsLibraries(dbdriver)
                .addAsResource("META-INF/persistence.xml")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @BeforeTest
    public void setup(){
        this.client = ClientBuilder.newClient();
        this.testCard = createRandomCard();
    }
    
    @AfterTest
    public void tearDown(){
        this.client.close();
    }
    
    @Test(priority = 1)
    @RunAsClient
    public void testCreateCard() throws Exception {
        
        Response response = client.target(this.uri).path(PATH)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(testCard, MediaType.APPLICATION_JSON_TYPE), Response.class);
        
        Assert.assertEquals(response.getStatus(), 201);
        
        URI location = response.getLocation();
        Assert.assertNotNull(location);
        
        Card createdCard = client.target(location)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Card.class);
        
        Assert.assertNotNull(createdCard.getId());
        Assert.assertEquals(createdCard.getTitle(), testCard.getTitle());
        
        this.testCard = createdCard;
    }
    
    @Test(priority = 2)
    @RunAsClient
    public void testUpdateCard() throws Exception {
        
        testCard.setTitle("This is an updated title");
        
        Response response = client.target(this.uri).path(PATH)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(testCard, MediaType.APPLICATION_JSON_TYPE), Response.class);
        
        Assert.assertEquals(response.getStatus(), 200);
        
        Card updatedCard = response.readEntity(Card.class);
                
        Assert.assertEquals(updatedCard.getId(),testCard.getId());
        Assert.assertEquals(updatedCard.getTitle(), testCard.getTitle());   
        
        this.testCard = updatedCard;
    }
    
    @Test(priority = 3)
    @RunAsClient
    public void testDeleteCard() throws Exception {
        String idpath = String.valueOf(testCard.getId());
                
        Response response = client.target(this.uri).path(PATH).path(idpath)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        
        Assert.assertEquals(response.getStatus(), 204);
        
        Card nullCard = client.target(this.uri).path(PATH).path(idpath)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Card.class);
        
        Assert.assertNull(nullCard);
    }
    
    
    private Card createRandomCard(){
        Card card = new Card();
        card.setTitle(faker.funnyName().name());
        card.setDescription(faker.lorem().paragraph(4));
        card.setComments(createRandomComments());
        
        return card;
    }
    
    private List<Comment> createRandomComments(){
        List<Comment> comments = new ArrayList<>();
        int rnd = faker.number().numberBetween(1, 20);
        
        for(int i=0;i<rnd;i++){
            comments.add(createRandomComment());
        }
        return comments;
    }
    
    private Comment createRandomComment(){
        
        Comment comment = new Comment();
        comment.setMadeBy(faker.name().fullName());
        comment.setComment(faker.chuckNorris().fact());
        comment.setMadeOn(createRandonLocalDateTime());
        
        return comment;
    }
    
    private LocalDateTime createRandonLocalDateTime(){
        Date date = faker.date().past(365, TimeUnit.DAYS);
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
    
    private final Faker faker = new Faker();
}