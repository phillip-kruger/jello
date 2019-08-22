package com.github.phillipkruger.jello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * JSF. Controller for language (i18n)
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@ApplicationScoped
@Named
public class LanguageController {
	
    @Getter @Setter
    private Locale selectedLocale = Locale.ENGLISH; // Default
    
    @Resource(lookup = "java:global/supportedLanguages")
    private String supportedLanguages;
    
    @Getter
    private List<Locale> availableLocales;
    
    @PostConstruct
    public void init(){
        String langs[] = supportedLanguages.split(",");
        
        this.availableLocales = new ArrayList<>();
        for(String lang:langs){
            this.availableLocales.add(new Locale(lang));
        }
    }  
    
    public void change(Locale locale){
        this.selectedLocale = locale;
        locale.getDisplayLanguage(locale);
        
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }  
}