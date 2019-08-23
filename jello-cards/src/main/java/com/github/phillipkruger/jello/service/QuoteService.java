package com.github.phillipkruger.jello.service;

import com.github.phillipkruger.jello.Quote;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lombok.extern.java.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Jax-rs, JAXP, JCache. Get a quote for the day from an online service
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@RequestScoped
@Log
public class QuoteService {

    @CacheResult(cacheName = "quoteCache")
    public Quote getQuote(){
        WebTarget target = client.target("http://api.forismatic.com/api/1.0/");

        Response response = target
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .buildPost(Entity.form(form)).invoke();
        
        if(response.getStatus()==Response.Status.OK.getStatusCode()){
            String xml = response.readEntity(String.class);
            if(xml!=null && !xml.isEmpty()){
                return toQuote(xml);
            }else{
                throw new NullPointerException("Error while getting qoute from Forismatic [null message]");
            }
        }else { 
            throw new RuntimeException("Error while getting qoute from Forismatic [" + response.getStatusInfo().getReasonPhrase() + "]");
        }
    }
    
    private Quote toQuote(String xml){
        
        try {
            Quote quote = new Quote();
            
            //<forismatic>
            //  <quote>
            //      <quoteText>When we seek to discover the best in others, we somehow bring out the best in ourselves.</quoteText>
            //      <quoteAuthor>William Ward</quoteAuthor>
            //      <senderName></senderName>
            //      <senderLink></senderLink>
            //      <quoteLink>http://forismatic.com/en/6501e54f45/</quoteLink>
            //  </quote>
            //</forismatic>
            
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
            Document document = builder.parse(bais);
            
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            XPathExpression quoteTextExpression = xpath.compile("/forismatic/quote/quoteText");
            XPathExpression quoteAuthorExpression = xpath.compile("/forismatic/quote/quoteAuthor");
            
            String quoteText = quoteTextExpression.evaluate(document);
            String quoteAuthor = quoteAuthorExpression.evaluate(document);
            
            quote.setText(quoteText);
            quote.setAuthor(quoteAuthor);
	
            return quote;
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            log.log(Level.WARNING,"Could not get Quote",ex);
            return new Quote("Bug happen [" + ex.getMessage() + "]", "Your System");
        }
    }
    
    @PostConstruct
    public void init(){
        
        this.client = ClientBuilder.newClient();
        
        this.form = new Form()
            .param("method", "getQuote")
            .param("lang", "en")
            .param("format", "xml");
    }
    
    @PreDestroy
    public void destroy(){
        this.client.close();
        this.client = null;
    }
    
    private Client client;
    private Form form;
    
}