package com.github.phillipkruger.jello.controller;

import com.github.phillipkruger.jello.service.LanguageService;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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
    private Locale selectedLocale = Locale.ENGLISH; // Default
    
    @Inject
    private LanguageService languageService; 
    
    public void change(Locale locale){
        this.selectedLocale = locale;
        locale.getDisplayLanguage(locale);
        
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
    
    public List<Locale> getAvailableLocales(){
        return languageService.getLocales();
    }
}