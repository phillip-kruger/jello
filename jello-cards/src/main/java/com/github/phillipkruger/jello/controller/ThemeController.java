package com.github.phillipkruger.jello.controller;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;

/**
 * Controller for theme
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Named
public class ThemeController {

    @Resource(lookup = "java:global/supportedThemes")
    private String supportedThemes;
    
    @Getter
    private List<String> themes;
    
    @PostConstruct
    public void init(){
        this.themes = Arrays.asList(supportedThemes.split(","));
    }
}