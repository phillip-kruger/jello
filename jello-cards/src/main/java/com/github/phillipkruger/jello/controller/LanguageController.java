package com.github.phillipkruger.jello.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * Controller for language (i18n)
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@SessionScoped
@Named
public class LanguageController implements Serializable {
	
    private static final long serialVersionUID = 1L;
	
    @Getter @Setter
    private String localeCode;
    @Getter @Setter
    private String selectedLanguage = DEFAULT_LANGUAGE;
    
    private final static Map<String,Locale> languages;
    
    public void change(String newLanguage){
        languages.entrySet().stream().filter((entry) -> (entry.getValue().toString().equals(newLanguage))).map((entry) -> {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(entry.getValue());
            return entry;
        }).forEachOrdered((entry) -> {
            this.selectedLanguage = entry.getKey();
        });
    }
    
    public void languageChanged(ValueChangeEvent e){
        
        String newLocaleValue = e.getNewValue().toString();
        change(newLocaleValue);
        
    }

    public Map<String,Locale> getAvailableLanguages(){
        return languages;
    }
    
    // TODO: Load from properties ?
    static{
        languages = new LinkedHashMap<>();
        languages.put("English", Locale.ENGLISH);
        languages.put("Afrikaans", new Locale("af"));
        languages.put("Hindi", new Locale("hi"));
    }
    
    private static final String DEFAULT_LANGUAGE = "English";
}