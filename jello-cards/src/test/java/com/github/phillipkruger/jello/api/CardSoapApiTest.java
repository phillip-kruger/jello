package com.github.phillipkruger.jello.api;

import com.github.phillipkruger.jello.Card;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;

/**
 * Testing Card service via SOAP
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Ignore
public class CardSoapApiTest extends Arquillian {

    @ArquillianResource
    private URI uri;
    
    private CardApi cardApi;
    private Card testCard;
    
    @Deployment
    public static WebArchive createDeployment() {
        return TestHelper.createDeployment();
}

    @BeforeTest
    public void createTestData(){
        this.testCard = TestHelper.createRandomCard();
    }
    
    @BeforeMethod
    public void setup() throws MalformedURLException{
        
        URL wsdlDocumentLocation = new URL(this.uri.toURL().toExternalForm()
				+ "/CardSoapApi?wsdl");
        
        
        String namespaceURI = "http://api.jello.phillipkruger.github.com/";
        String servicePart = "CardSoapApiService";
        QName serviceQN = new QName(namespaceURI, servicePart);

        Service service = Service.create(wsdlDocumentLocation, serviceQN);

        this.cardApi = service.getPort(CardApi.class);
    }
    
    @AfterMethod
    public void tearDown(){
        this.cardApi = null;
    }
    
    @Test(priority = 1)
    @RunAsClient
    public void testCreateCard() throws Exception {
        
        Card createdCard = this.cardApi.createCard(testCard);
        
        Assert.assertNotNull(createdCard.getId());
        Assert.assertEquals(createdCard.getTitle(), testCard.getTitle());
        
        this.testCard = createdCard;
    }
    
    @Test(priority = 2)
    @RunAsClient
    public void testUpdateCard() throws Exception {
        
        testCard.setTitle("This is an updated title");
        
        Card updatedCard = this.cardApi.updateCard(testCard);
                
        Assert.assertEquals(updatedCard.getId(),testCard.getId());
        Assert.assertEquals(updatedCard.getTitle(), testCard.getTitle());   
        
        this.testCard = updatedCard;
    }
    
    @Test(priority = 3)
    @RunAsClient
    public void testDeleteCard() throws Exception {
        Long id = testCard.getId();
        this.cardApi.removeCard(id);
        Card nullCard = this.cardApi.getCard(id);
        
        Assert.assertNull(nullCard);
    }
}
