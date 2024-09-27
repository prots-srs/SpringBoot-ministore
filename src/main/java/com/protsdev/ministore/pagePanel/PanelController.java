package com.protsdev.ministore.pagePanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.protsdev.ministore.enums.MenuConfigurator;
import com.protsdev.ministore.localize.LocalizeService;

/*
 * page params
 * - seos
 * - composeLink
 * - search
 */
@Controller
@RequestMapping("/panel")
public class PanelController {

    // configs
    private final MenuConfigurator section = MenuConfigurator.PANEL_DASHBOARD;

    @Autowired
    private LocalizeService localizeService;

    @Autowired
    private PanelMenuService panelMenuService;

    // private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") +
    // "/public/content";

    // @Autowired
    // private ServiceRepository serviceRepo;
    // @Autowired
    // private ProductRepository productRepo;

    // @Autowired
    // private ImageService imageService;

    @ModelAttribute
    public void panelAttribute(Model model) {
        model.addAttribute("menu", panelMenuService.getLeftMenu(section.getLink()));
    }

    @GetMapping
    public String dashboard(Model model) {
        // title & menus
        model.addAttribute("title", localizeService
                .getMessage("page.panel.menu." + section.toString()));

        return "panel-dashboard";
    }

    // // PRODUCT
    // @GetMapping(sectionProductUrl)
    // public String listProduct(Model model) {
    // List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl);
    // model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

    // model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl +
    // sectionProductUrl));
    // model.addAttribute("composeLink", siteUrl + sectionProductUrl + "/create");

    // List<ProductEntity> list = this.productRepo.findAllByOrderBySortAsc();
    // model.addAttribute("list", list);

    // ArrayList<ProductType> types = ProductEntity.getProductTypes();
    // HashMap<String, String> typeMap = new HashMap<>();
    // for (ProductType type : types) {
    // typeMap.put(type.code(), type.name());
    // }
    // model.addAttribute("types", typeMap);

    // return "admin-list-product";
    // }

    // @GetMapping(sectionProductUrl + "/create")
    // public String createProduct(Model model) {
    // List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl +
    // "/create");
    // model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

    // ProductEntity item = new ProductEntity();
    // model.addAttribute("item", item);

    // model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl +
    // sectionProductUrl));

    // ArrayList<ProductType> types = ProductEntity.getProductTypes();
    // model.addAttribute("productTypes", types);

    // return "admin-edit-product";
    // }

    // @PostMapping(sectionProductUrl)
    // private String processCreateProduct(ProductEntity item,
    // @RequestParam("pictureFile") MultipartFile file,
    // BindingResult result,
    // RedirectAttributes redirectAttributes) {

    // if (result.hasErrors()) {
    // redirectAttributes.addFlashAttribute("error",
    // "There was an error in creating");
    // // return "admin-edit-product";
    // return "redirect:" + siteUrl + sectionProductUrl;
    // }

    // if (!file.isEmpty()) {
    // try {
    // String filename = imageService.saveImageToStorage(UPLOAD_DIRECTORY, file);
    // item.setPicture(filename);
    // } catch (Exception e) {
    // }
    // }

    // this.productRepo.save(item);

    // redirectAttributes.addFlashAttribute("message", "New item Created");

    // return "redirect:" + siteUrl + sectionProductUrl;
    // }

    // @GetMapping(sectionProductUrl + "/{id}/edit")
    // public String updateProduct(@PathVariable Long id, Model model) {
    // List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl +
    // "/edit");
    // model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());
    // model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl +
    // sectionProductUrl));

    // ProductEntity item;
    // if (this.productRepo.existsById(id)) {
    // item = this.productRepo.findById(id).get();
    // model.addAttribute("id", id);
    // } else {
    // item = new ProductEntity();
    // }

    // ArrayList<ProductType> types = ProductEntity.getProductTypes();
    // model.addAttribute("productTypes", types);

    // model.addAttribute("item", item);
    // return "admin-edit-product";
    // }

    // @PostMapping(sectionProductUrl + "/{id}/edit")
    // public String processUpdateProduct(
    // ProductEntity item,
    // @RequestParam("pictureFile") MultipartFile file,
    // BindingResult result,
    // @PathVariable("id") Long id,
    // RedirectAttributes redirectAttributes) {

    // if (result.hasErrors()) {
    // redirectAttributes.addFlashAttribute("error",
    // "There was an error in updating.");
    // // return "admin-edit-product";
    // return "redirect:" + siteUrl + sectionProductUrl;
    // }

    // if (!file.isEmpty()) {
    // try {
    // ProductEntity itemInDB = this.productRepo.findById(id).get();
    // this.imageService.deleteImage(UPLOAD_DIRECTORY, itemInDB.getPicture());
    // } catch (Exception e) {
    // item.setPicture("");
    // }

    // try {
    // String filename = imageService.saveImageToStorage(UPLOAD_DIRECTORY, file);
    // item.setPicture(filename);
    // } catch (Exception e) {
    // item.setPicture("");
    // }
    // }

    // item.setId(id);
    // this.productRepo.save(item);

    // redirectAttributes.addFlashAttribute("message", "Values Updated");

    // return "redirect:" + siteUrl + sectionProductUrl;

    // }

    // @DeleteMapping(sectionProductUrl + "/{id}")
    // private ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

    // if (this.productRepo.existsById(id)) {
    // try {
    // ProductEntity item = this.productRepo.findById(id).get();
    // this.imageService.deleteImage(UPLOAD_DIRECTORY, item.getPicture());
    // } catch (Exception e) {
    // }
    // this.productRepo.deleteById(id);

    // return ResponseEntity.noContent().build();
    // }

    // return ResponseEntity.notFound().build();
    // }
}

// get current user
// SecurityContext context = SecurityContextHolder.getContext();
// Authentication authentication = context.getAuthentication();
// String username = authentication.getName();
// Object principal = authentication.getPrincipal();
// Collection<? extends GrantedAuthority> authorities =
// authentication.getAuthorities();

// System.out.println("username: " + username);
// System.out.println("principal: " + principal);
// for (GrantedAuthority grantedAuthority : authorities) {
// System.out.println("grantedAuthority: " + grantedAuthority);
// }