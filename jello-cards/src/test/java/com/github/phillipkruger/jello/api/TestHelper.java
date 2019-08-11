package com.github.phillipkruger.jello.api;

import com.github.javafaker.Faker;
import com.github.phillipkruger.jello.Card;
import com.github.phillipkruger.jello.Comment;
import com.github.phillipkruger.jello.Swimlane;
import com.github.phillipkruger.jello.adapter.LocalDateTimeAdapter;
import com.github.phillipkruger.jello.service.CardService;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class TestHelper {

    public static WebArchive createDeployment() {

        final File[] dbdriver = Maven.resolver().resolve("com.h2database:h2:1.4.199")
                .withoutTransitivity().asFile();
        
        return ShrinkWrap.create(WebArchive.class, "CardApiTest.war")
                .addPackage(CardRestApi.class.getPackage())
                .addPackage(Card.class.getPackage())
                .addPackage(CardService.class.getPackage())
                .addPackage(LocalDateTimeAdapter.class.getPackage())
                .addAsLibraries(dbdriver)
                .addAsResource("META-INF/persistence.xml")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    
    public static Card createRandomCard(){
        Card card = new Card();
        card.setTitle(FAKER.funnyName().name());
        card.setDescription(FAKER.lorem().paragraph(2));
        card.setComments(createRandomComments());
        card.setSwimlane(Swimlane.pipeline);
        return card;
    }
    
    private static List<Comment> createRandomComments(){
        List<Comment> comments = new ArrayList<>();
        int rnd = FAKER.number().numberBetween(1, 20);
        
        for(int i=0;i<rnd;i++){
            comments.add(createRandomComment());
        }
        return comments;
    }
    
    private static Comment createRandomComment(){
        
        Comment comment = new Comment();
        comment.setMadeBy(FAKER.name().fullName());
        comment.setComment(FAKER.chuckNorris().fact());
        comment.setMadeOn(createRandonLocalDateTime());
        
        return comment;
    }
    
    private static LocalDateTime createRandonLocalDateTime(){
        Date date = FAKER.date().past(365, TimeUnit.DAYS);
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
    
    private static final Faker FAKER = new Faker();
}
