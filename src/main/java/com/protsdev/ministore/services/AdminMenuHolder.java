package com.protsdev.ministore.services;

import java.util.TreeSet;

import com.protsdev.ministore.models.AdminMenuItem;

public class AdminMenuHolder {

    public static TreeSet<AdminMenuItem> getMenu(String activeLink) {
        final String ROOT_PATH = "/admin";

        String link = "";
        Boolean active = false;
        Boolean activeChild = false;

        TreeSet<AdminMenuItem> menu = new TreeSet<>();

        // define root item
        link = ROOT_PATH;
        active = link == activeLink;
        menu.add(
                new AdminMenuItem(10, "Dashboard", link,
                        "bx-home-circle", active, null, false));

        // define children items
        TreeSet<AdminMenuItem> contentMenu = new TreeSet<>();

        activeChild = false;

        // loop items
        link = ROOT_PATH + "/seo";
        active = link == activeLink;

        if (active) {
            activeChild = true;
        }
        contentMenu.add(
                new AdminMenuItem(5, "SEO", link,
                        "", active, null, false));

        link = ROOT_PATH + "/service";
        active = link == activeLink;

        if (active) {
            activeChild = true;
        }
        contentMenu.add(
                new AdminMenuItem(10, "Service", link,
                        "", active, null, false));

        link = ROOT_PATH + "/product";
        active = link == activeLink;

        if (active) {
            activeChild = true;
        }
        contentMenu.add(
                new AdminMenuItem(20, "Product", link,
                        "", active, null, false));

        // define root item
        link = "";
        active = link == activeLink;
        menu.add(
                new AdminMenuItem(20, "Content", link,
                        "bx-layout", active, contentMenu, activeChild));

        return menu;
    }
}
