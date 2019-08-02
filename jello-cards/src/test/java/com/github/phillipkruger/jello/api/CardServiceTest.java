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
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Testing Card service
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class CardServiceTest extends Arquillian {

//    private static final String DEPLOYMENT_VERSION = "2.5.0.Final";
    
    private static final String PATH = "api/card";
    
    @ArquillianResource
    private URI uri;
    
    private Client client;
    private WebTarget webTarget;
    
    @Deployment
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "CardServiceTest.war")
                .addPackage(CardService.class.getPackage())
                .addPackage(Card.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @BeforeMethod
    public void setup(){
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(this.uri).path(PATH);
    }
    
    @AfterMethod
    public void tearDown(){
        this.client.close();
    }
    
    @Test
    @RunAsClient
    public void testPing() throws Exception {
        final String expect = "pong";
        
        String content = this.webTarget
            .request(MediaType.TEXT_PLAIN)
            .get(String.class);
        
        Assert.assertTrue(content.length() > 0 , "While testing [" + this.uri.toString() + PATH + "]");
        Assert.assertEquals(content, expect);
    }
    
    
    @Test
    @RunAsClient
    public void testCreateCard() throws Exception {
        Card card = new Card();
        Response response = this.webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(card));

        log.severe(">>>>>>>>> " + response.getLocation());
    }
}