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
//        themes.add("afterdark");
//        themes.add("afternoon");
//        themes.add("afterwork");
//        themes.add("black-tie");
//        themes.add("blitzer");
//        themes.add("bluesky");
//        themes.add("bootstrap");
//        themes.add("casablanca");
//        themes.add("cruze");
//        themes.add("cupertino");
//        themes.add("dark-hive");
//        themes.add("dot-luv");
//        themes.add("eggplant");
//        themes.add("excite-bike");
//        themes.add("flick");
//        themes.add("glass-x");
//        themes.add("home");
//        themes.add("hot-sneaks");
//        themes.add("humanity");
//        themes.add("le-frog");
//        themes.add("midnight");
//        themes.add("mint-choc");
//        themes.add("overcast");
//        themes.add("pepper-grinder");
//        themes.add("redmond");
//        themes.add("rocket");
//        themes.add("sam");
//        themes.add("smoothness");
//        themes.add("south-street");
//        themes.add("start");
//        themes.add("sunny");
//        themes.add("swanky-purse");
//        themes.add("trontastic");
//        themes.add("ui-darkness");
//        themes.add("ui-lightness");
//        themes.add("vader");
    }
}