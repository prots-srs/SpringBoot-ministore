package com.protsdev.ministore.pagePublicMain;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.protsdev.ministore.enums.MenuConfigurator;
import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.pagePanelBanner.PanelBannerService;
import com.protsdev.ministore.pagePanelProduct.PanelProductService;
import com.protsdev.ministore.pagePanelSeo.PanelSeoService;
import com.protsdev.ministore.pagePanelService.PanelServiceService;

@Controller
public class PublicPageController {

    @Autowired
    private PanelSeoService seoService;

    @Autowired
    private PanelServiceService serviceService;

    @Autowired
    private PanelProductService productService;

    @Autowired
    private PanelBannerService bannerService;

    @Autowired
    private LocalizeService localizeService;
    private Set<PublicMenuItem> topMenu = new TreeSet<>();

    @ModelAttribute
    public void panelAttribute(Model model) {

        // home
        topMenu.add(new PublicMenuItem(1,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_HOME.toString()),
                MenuConfigurator.MP_HOME.getLink(), true));
        // sale
        topMenu.add(new PublicMenuItem(40,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_SALE.toString()),
                MenuConfigurator.MP_SALE.getLink(), false));
        // blog
        topMenu.add(new PublicMenuItem(50,
                localizeService.getMessage("page.mainpage.menu." + MenuConfigurator.MP_BLOG.toString()),
                MenuConfigurator.MP_BLOG.getLink(), false));
    }

    @GetMapping("/")
    public String mainpage(Model model) {
        // fetchAllNeedContent();

        model.addAttribute("seo", seoService.getForPublicPage("/"));

        var banners = bannerService.getForPublicPage();
        model.addAttribute("banners", banners);

        // service
        var services = serviceService.getForPublicPage();
        model.addAttribute("services", services);
        if (services != null) {
            topMenu.add(new PublicMenuItem(10,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_SERVICES.toString()),
                    MenuConfigurator.MP_SERVICES.getLink(), false));
        }

        // mobile
        var mobiles = productService.getForPublicPage(ProductTypes.MOBILE);
        if (mobiles.size() > 0) {
            topMenu.add(new PublicMenuItem(20,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_MOBILES.toString()),
                    MenuConfigurator.MP_MOBILES.getLink(), false));
        }
        model.addAttribute("mobiles", mobiles);

        // watch
        var watches = productService.getForPublicPage(ProductTypes.WATCH);
        if (watches.size() > 0) {
            topMenu.add(new PublicMenuItem(30,
                    localizeService
                            .getMessage("page.mainpage.menu." + MenuConfigurator.MP_WATCHES.toString()),
                    MenuConfigurator.MP_WATCHES.getLink(), false));
        }
        model.addAttribute("watches", watches);

        model.addAttribute("topMenu", topMenu);

        return "mainpage";
    }

}
