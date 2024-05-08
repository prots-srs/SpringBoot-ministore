package com.protsprog.ministore.mainpage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.protsprog.ministore.models.Credentials;
import com.protsprog.ministore.models.ProductEntity;
import com.protsprog.ministore.models.SEOs;
import com.protsprog.ministore.models.ServiceItem;
import com.protsprog.ministore.repositories.ProductRepository;
import com.protsprog.ministore.repositories.SeoRepository;
import com.protsprog.ministore.repositories.ServiceRepository;

@Controller
public class MainpageController {

    private final PublicMenuService topMenu;

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private SeoRepository seoRepo;

    @Autowired
    private ProductRepository productRepo;

    public MainpageController(PublicMenuService menu) {
        this.topMenu = menu;
    }

    @GetMapping("/")
    public String mainpage(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath("/");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        Credentials creds = new Credentials("prots.srs@gmail.com", "+380 96 26 55 4 26");
        model.addAttribute("credentials", creds);

        // add menu
        topMenu.addItem(1, "Home", "#billboard", true);

        topMenu.addItem(40, "Sale", "#yearly-sale");
        topMenu.addItem(50, "Blog", "#latest-blog");

        ArrayList<ServiceItem> serviceItems = this.serviceRepo.findByActiveOrderBySortAsc(true);

        model.addAttribute("services", serviceItems);
        if (serviceItems.size() > 0) {
            topMenu.addItem(10, "Services", "#company-services");
        }

        List<ProductEntity> mobileItems = this.productRepo.findByProductTypeCodeAndActiveTrueOrderBySortAsc("mobile");
        if ((mobileItems.size() > 0)) {
            topMenu.addItem(20, "Products", "#mobile-products");
        }
        model.addAttribute("productMobile", mobileItems);

        List<ProductEntity> watchItems = this.productRepo.findByProductTypeCodeAndActiveTrueOrderBySortAsc("watch");
        if (watchItems.size() > 0) {
            topMenu.addItem(30, "Watches", "#smart-watches");
        }
        model.addAttribute("productWatch", watchItems);

        model.addAttribute("topMenu", topMenu.getMenu());

        return "mainpage";
    }
}
