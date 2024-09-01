package com.protsdev.ministore.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.protsdev.ministore.domen.MenuConfigurator;
import com.protsdev.ministore.domen.ProductTypes;
import com.protsdev.ministore.dto.ProductView;
import com.protsdev.ministore.dto.PublicMenu;
import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.models.ProductEntity;
import com.protsdev.ministore.models.ServiceItem;
import com.protsdev.ministore.pageSeo.SeoEntity;
import com.protsdev.ministore.pageSeo.SeoRepository;
import com.protsdev.ministore.repositories.ProductRepository;
import com.protsdev.ministore.repositories.ServiceRepository;

@Controller
public class MainpageController {

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private SeoRepository seoRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private LocalizeService localizeService;

    private ArrayList<ServiceItem> serviceItems = null;
    Map<ProductTypes, List<ProductView>> products = new HashMap<>();

    @GetMapping("/")
    public String mainpage(Model model) {
        List<SeoEntity> seos = seoRepo.findByPath("/");
        fetchAllNeedContent();

        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SeoEntity());
        model.addAttribute("services", serviceItems);
        model.addAttribute("products", products);

        model.addAttribute("topMenu", getTopMenu());

        return "mainpage";
    }

    private TreeSet<PublicMenu> getTopMenu() {
        // add menu
        TreeSet<PublicMenu> menu = new TreeSet<>();
        // home
        menu.add(new PublicMenu(1,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_HOME.toString()),
                MenuConfigurator.MP_HOME.getLink(), true));
        // sale
        menu.add(new PublicMenu(40,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_SALE.toString()),
                MenuConfigurator.MP_SALE.getLink(), false));
        // blog
        menu.add(new PublicMenu(50,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_BLOG.toString()),
                MenuConfigurator.MP_BLOG.getLink(), false));
        // service
        if (!serviceItems.isEmpty()) {
            menu.add(new PublicMenu(10,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_SERVICES.toString()),
                    MenuConfigurator.MP_SERVICES.getLink(), false));
        }
        // mobile
        if (!products.get(ProductTypes.MOBILE).isEmpty()) {
            menu.add(new PublicMenu(20,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_MOBILES.toString()),
                    MenuConfigurator.MP_MOBILES.getLink(), false));
        }
        // watch
        if (!products.get(ProductTypes.WATCH).isEmpty()) {
            menu.add(new PublicMenu(30,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_WATCHES.toString()),
                    MenuConfigurator.MP_WATCHES.getLink(), false));
        }

        return menu;
    }

    private void fetchAllNeedContent() {
        serviceItems = serviceRepo.findByActiveOrderBySortAsc(true);

        products.put(ProductTypes.MOBILE, productRepo.findByTypeAndActiveTrueOrderBySortAsc(ProductTypes.MOBILE));
        products.put(ProductTypes.WATCH, productRepo.findByTypeAndActiveTrueOrderBySortAsc(ProductTypes.WATCH));
    }

}
