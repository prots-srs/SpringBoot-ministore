package com.protsdev.ministore.domen;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.dto.PanelMenu;
import com.protsdev.ministore.localize.LocalizeService;

@Service
public class PanelMenuService {

    private Boolean activeChild = false;

    @Autowired
    private LocalizeService localizeService;

    public TreeSet<PanelMenu> getLeftMenu(String activeLink) {

        TreeSet<PanelMenu> menu = new TreeSet<>();

        // define root item
        menu.add(new PanelMenu(10,
                localizeService
                        .getMessage("page.panel.menu." + MenuConfigurator.PANEL_DASHBOARD.toString()),
                MenuConfigurator.PANEL_DASHBOARD.getLink(),
                MenuConfigurator.PANEL_DASHBOARD.getIco(),
                MenuConfigurator.PANEL_DASHBOARD.getLink() == activeLink, null, false));
        // content
        var subMenu = getContentMenu(activeLink);
        menu.add(new PanelMenu(20,
                localizeService
                        .getMessage("page.panel.menu." + MenuConfigurator.PANEL_CONTENT.toString()),
                MenuConfigurator.PANEL_CONTENT.getLink(),
                MenuConfigurator.PANEL_CONTENT.getIco(),
                false, subMenu, activeChild));

        return menu;
    }

    // define children items
    private TreeSet<PanelMenu> getContentMenu(String activeLink) {
        TreeSet<PanelMenu> menu = new TreeSet<>();
        activeChild = false;
        // seo
        menu.add(new PanelMenu(5,
                localizeService
                        .getMessage("page.panel.menu." + MenuConfigurator.PANEL_SEO.toString()),
                MenuConfigurator.PANEL_SEO.getLink(),
                MenuConfigurator.PANEL_SEO.getIco(),
                MenuConfigurator.PANEL_SEO.getLink() == activeLink, null, false));
        if (MenuConfigurator.PANEL_SEO.getLink() == activeLink)
            activeChild = true;
        // service
        menu.add(new PanelMenu(10,
                localizeService
                        .getMessage("page.panel.menu." + MenuConfigurator.PANEL_SERVICE.toString()),
                MenuConfigurator.PANEL_SERVICE.getLink(),
                MenuConfigurator.PANEL_SERVICE.getIco(),
                MenuConfigurator.PANEL_SERVICE.getLink() == activeLink, null, false));
        if (MenuConfigurator.PANEL_SEO.getLink() == activeLink)
            activeChild = true;
        // products
        menu.add(new PanelMenu(20,
                localizeService
                        .getMessage("page.panel.menu." + MenuConfigurator.PANEL_PRODUCT.toString()),
                MenuConfigurator.PANEL_PRODUCT.getLink(),
                MenuConfigurator.PANEL_PRODUCT.getIco(),
                MenuConfigurator.PANEL_PRODUCT.getLink() == activeLink, null, false));
        if (MenuConfigurator.PANEL_SEO.getLink() == activeLink)
            activeChild = true;

        return menu;
    }
}
