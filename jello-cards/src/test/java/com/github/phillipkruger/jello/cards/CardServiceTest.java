package com.github.phillipkruger.jello.cards;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
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

/**
 * Testing Card service
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class CardServiceTest extends Arquillian {

    private static final String PATH = "api/card";
    
    @ArquillianResource
    private URI uri;
    
    @Deployment
    public static WebArchive createDeployment() {
        final File[] thorntail = Maven.resolver().resolve("io.thorntail:microprofile-config:2.5.0.Final")
                .withoutTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "CardServiceTest.war")
                .addPackage(CardService.class.getPackage())
                .addAsLibraries(thorntail)
//                .addAsResource(
//                        new File("src/main/resources/META-INF/services/org.eclipse.microprofile.config.spi.Converter"),
//                        "META-INF/services/org.eclipse.microprofile.config.spi.Converter")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testResponse() throws Exception {
        URL url = new URL(this.uri + PATH);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "plain/text");
        
        // Check the response code
        int responseCode = connection.getResponseCode();
        log.log(Level.SEVERE, "responseCode = {0}", responseCode);
        Assert.assertEquals(responseCode, 200, "While testing [" + url.toString() + "]");
        
        // Check the content
        String content = getContent(connection);
        log.log(Level.SEVERE, "content = {0}", content);
        Assert.assertTrue(content.length() > 0 , "While testing [" + url.toString() + "]");
        
        connection.disconnect();
    }
    
    private String getContent(HttpURLConnection connection) throws IOException {
        try (StringWriter sw = new StringWriter();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                sw.write(inputLine);
            }
            return sw.toString();
        }
    }
}
