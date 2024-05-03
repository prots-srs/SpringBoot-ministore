package com.protsprog.ministore.mainpage;

import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.protsprog.ministore.models.PublicMenuItem;

@Service
public class PublicMenuService {

    private TreeSet<PublicMenuItem> menu = new TreeSet<>();

    public TreeSet<PublicMenuItem> getMenu() {
        return menu;
    }

    public void addItem(Integer sort, String title, String link, Boolean active) {
        menu.add(new PublicMenuItem(sort, title, link, active));
    }

    public void addItem(Integer sort, String title, String link) {
        menu.add(new PublicMenuItem(sort, title, link, false));
    }
}
