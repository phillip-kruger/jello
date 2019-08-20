package com.github.phillipkruger.jello.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;

/**
 * Controller for theme
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Named
public class ThemeController implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Getter
    private List<String> themes;
     
    @PostConstruct
    public void init() {
        themes = new ArrayList<>();
        themes.add("admin");
        themes.add("nova-light");
        themes.add("nova-dark");
        themes.add("nova-colored");
        themes.add("luna-blue");
        themes.add("luna-amber");
        themes.add("luna-green");
        themes.add("luna-pink");
        themes.add("omega");
    }
}