package com.protsdev.ministore.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.protsdev.ministore.dto.PublicMenuItem;
import com.protsdev.ministore.models.ProductEntity;
import com.protsdev.ministore.models.SEOs;
import com.protsdev.ministore.models.ServiceItem;
import com.protsdev.ministore.repositories.ProductRepository;
import com.protsdev.ministore.repositories.SeoRepository;
import com.protsdev.ministore.repositories.ServiceRepository;

@Controller
public class MainpageController {

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private SeoRepository seoRepo;

    @Autowired
    private ProductRepository productRepo;

    private ArrayList<ServiceItem> serviceItems = null;
    private List<ProductEntity> mobileItems = null;
    private List<ProductEntity> watchItems = null;

    @GetMapping("/")
    public String mainpage(Model model) {
        List<SEOs> seos = seoRepo.findByPath("/");
        fetchAllNeedContent();

        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());
        model.addAttribute("services", serviceItems);
        model.addAttribute("productMobile", mobileItems);
        model.addAttribute("productWatch", watchItems);

        model.addAttribute("topMenu", getTopMenu());

        return "mainpage";
    }

    private TreeSet<PublicMenuItem> getTopMenu() {
        // add menu
        TreeSet<PublicMenuItem> menu = new TreeSet<>();
        menu.add(new PublicMenuItem(1, "Home", "#billboard", true));
        menu.add(new PublicMenuItem(40, "Sale", "#yearly-sale", false));
        menu.add(new PublicMenuItem(50, "Blog", "#latest-blog", false));

        if (serviceItems.size() > 0) {
            menu.add(new PublicMenuItem(10, "Services", "#company-services", false));
        }

        if (mobileItems.size() > 0) {
            menu.add(new PublicMenuItem(20, "Products", "#mobile-products", false));
        }

        if (watchItems.size() > 0) {
            menu.add(new PublicMenuItem(30, "Watches", "#smart-watches", false));
        }

        return menu;
    }

    private void fetchAllNeedContent() {
        serviceItems = serviceRepo.findByActiveOrderBySortAsc(true);
        mobileItems = productRepo.findByProductTypeCodeAndActiveTrueOrderBySortAsc("mobile");
        watchItems = productRepo.findByProductTypeCodeAndActiveTrueOrderBySortAsc("watch");
    }

}
