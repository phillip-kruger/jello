package com.github.phillipkruger.jello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * Help with the language support
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
public class LanguageService {

    @Resource(lookup = "java:global/supportedLanguages")
    private String supportedLanguages;
    
    @Getter
    private List<Locale> locales;
    
    @PostConstruct
    public void init(){
        String langs[] = supportedLanguages.split(",");
        
        this.locales = new ArrayList<>();
        for(String lang:langs){
            this.locales.add(new Locale(lang));
        }
    }   
}