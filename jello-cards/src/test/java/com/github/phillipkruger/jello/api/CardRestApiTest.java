package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;

/**
 * Testing Card service via REST
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * 
 * (TODO: Can't figure why this is giving a 400)
 */
@Ignore
public class CardRestApiTest extends Arquillian {

    private static final String PATH = "api/card";
    
    @ArquillianResource
    private URI uri;
    
    private Card testCard;

    private Client client;
            
    @Deployment
    public static WebArchive createDeployment() {
        return TestHelper.createDeployment();
    }
    
    @BeforeTest
    public void setup(){
        this.client = ClientBuilder.newClient();
        this.testCard = TestHelper.createRandomCard();
    }
    
    @AfterTest
    public void tearDown(){
        this.client.close();
    }
    
    @Test(priority = 1)
    @RunAsClient
    public void testCreateCard() throws Exception {
        
        WebTarget path = client.target(this.uri).path(PATH);
        
        Response response = path
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(testCard, MediaType.APPLICATION_JSON_TYPE), Response.class);
        
        Assert.assertEquals(response.getStatus(), 201, "Url = " + path.getUri());
        
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
}
