package com.protsprog.ministore.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.protsprog.ministore.models.IconClass;
import com.protsprog.ministore.models.ProductEntity;
import com.protsprog.ministore.models.ProductType;
import com.protsprog.ministore.models.SEOs;
import com.protsprog.ministore.models.ServiceItem;
import com.protsprog.ministore.repositories.ProductRepository;
import com.protsprog.ministore.repositories.SeoRepository;
import com.protsprog.ministore.repositories.ServiceRepository;

/*
 * page params
 * - seos
 * - composeLink
 * - search
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/public/content";

    private final String siteUrl = "/admin";
    private final String sectionSeoUrl = "/seo";
    private final String sectionServiceUrl = "/service";
    private final String sectionProductUrl = "/product";

    private final int pageSize = 5;

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private SeoRepository seoRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String dashboard(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl);
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl));

        return "admin-dashboard";
    }

    // SEOs
    @GetMapping(sectionSeoUrl)
    public String listSEO(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionSeoUrl);
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionSeoUrl));
        model.addAttribute("search", siteUrl + sectionSeoUrl);
        model.addAttribute("composeLink", siteUrl + sectionSeoUrl + "/create");

        Pageable pageable = PageRequest.of(page - 1, this.pageSize);

        if (search.length() > 0) {
            List<SEOs> list = this.seoRepo.findByPathContaining(search);
            model.addAttribute("list", list);
            model.addAttribute("pageSize", list.size());
            model.addAttribute("totalItems", list.size());

        } else {
            Page<SEOs> pList = this.seoRepo.findAll(pageable);
            model.addAttribute("list", pList.getContent());

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pList.getTotalPages());
            model.addAttribute("pageSize", this.pageSize);
            model.addAttribute("totalItems", pList.getTotalElements());
        }

        return "admin-list-seo";
    }

    @GetMapping(sectionSeoUrl + "/create")
    public String createSEO(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionSeoUrl + "/create");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("item", new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionSeoUrl));

        return "admin-edit-seo";
    }

    @PostMapping(sectionSeoUrl)
    private String processCreateSEO(SEOs item, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in creating");
            return "admin-edit-seo";
        }

        this.seoRepo.save(item);
        redirectAttributes.addFlashAttribute("message", "New item Created");

        return "redirect:" + siteUrl + sectionSeoUrl;
    }

    @DeleteMapping(sectionSeoUrl + "/{id}")
    private ResponseEntity<Void> deleteSEO(@PathVariable Long id) {
        if (this.seoRepo.existsById(id)) {
            this.seoRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(sectionSeoUrl + "/{id}/edit")
    public String updateSEO(@PathVariable Long id, Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionSeoUrl + "/edit");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionSeoUrl));

        SEOs item;

        if (this.seoRepo.existsById(id)) {
            item = this.seoRepo.findById(id).get();
            model.addAttribute("id", id);
        } else {
            item = new SEOs();
        }

        model.addAttribute("item", item);
        return "admin-edit-seo";
    }

    @PostMapping(sectionSeoUrl + "/{id}/edit")
    public String processUpdateSEO(SEOs item, BindingResult result,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in updating.");
            return "admin-edit-seo";
        }

        item.setId(id);
        this.seoRepo.save(item);

        redirectAttributes.addFlashAttribute("message", "Values Updated");

        return "redirect:" + siteUrl + sectionSeoUrl;
    }

    // SERVICES

    @GetMapping(sectionServiceUrl)
    public String listService(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionServiceUrl);
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionServiceUrl));
        model.addAttribute("composeLink", siteUrl + sectionServiceUrl + "/create");

        ArrayList<ServiceItem> list = this.serviceRepo.findAllByOrderBySortAsc();
        model.addAttribute("list", list);

        return "admin-list-service";
    }

    @GetMapping(sectionServiceUrl + "/create")
    public String createService(Model model) {// Map<String, Object> model
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionServiceUrl + "/create");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        ServiceItem item = new ServiceItem();
        model.addAttribute("item", item);

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionServiceUrl));

        ArrayList<IconClass> iconClasses = ServiceItem.getListIconClasses();
        model.addAttribute("allIconClasses", iconClasses);

        return "admin-edit-service";
    }

    @PostMapping(sectionServiceUrl)
    private String processCreateService(ServiceItem item, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in creating the service.");
            return "admin-edit-service";
        }

        this.serviceRepo.save(item);
        redirectAttributes.addFlashAttribute("message", "New Service Created");

        // return "redirect:/admin/services/" + item.getId();
        return "redirect:" + siteUrl + sectionServiceUrl;
    }

    @DeleteMapping(sectionServiceUrl + "/{id}")
    private ResponseEntity<Void> deleteService(@PathVariable Long id) {

        if (this.serviceRepo.existsById(id)) {
            this.serviceRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(sectionServiceUrl + "/{id}/edit")
    public String updateService(@PathVariable Long id, Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionServiceUrl + "/edit");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());
        model.addAttribute("menu", AdminMenuHolder.getMenu("/admin/services"));

        ArrayList<IconClass> iconClasses = ServiceItem.getListIconClasses();
        model.addAttribute("allIconClasses", iconClasses);

        ServiceItem item;

        if (this.serviceRepo.existsById(id)) {
            item = this.serviceRepo.findById(id).get();
            model.addAttribute("id", id);
        } else {
            item = new ServiceItem();
        }

        model.addAttribute("item", item);
        return "admin-edit-service";
    }

    @PostMapping(sectionServiceUrl + "/{id}/edit")
    public String processUpdateService(ServiceItem item, BindingResult result, @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in updating the service.");
            return "admin-edit-service";
        }

        item.setId(id);
        this.serviceRepo.save(item);

        redirectAttributes.addFlashAttribute("message", "Service Values Updated");

        // return "redirect:/admin/services/{id}";
        return "redirect:" + siteUrl + sectionServiceUrl;

    }

    // PRODUCT
    @GetMapping(sectionProductUrl)
    public String listProduct(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl);
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionProductUrl));
        model.addAttribute("composeLink", siteUrl + sectionProductUrl + "/create");

        List<ProductEntity> list = this.productRepo.findAllByOrderBySortAsc();
        model.addAttribute("list", list);

        ArrayList<ProductType> types = ProductEntity.getProductTypes();
        HashMap<String, String> typeMap = new HashMap<>();
        for (ProductType type : types) {
            typeMap.put(type.code(), type.name());
        }
        model.addAttribute("types", typeMap);

        return "admin-list-product";
    }

    @GetMapping(sectionProductUrl + "/create")
    public String createProduct(Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl + "/create");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());

        ProductEntity item = new ProductEntity();
        model.addAttribute("item", item);

        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionProductUrl));

        ArrayList<ProductType> types = ProductEntity.getProductTypes();
        model.addAttribute("productTypes", types);

        return "admin-edit-product";
    }

    @PostMapping(sectionProductUrl)
    private String processCreateProduct(ProductEntity item,
            @RequestParam("pictureFile") MultipartFile file,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in creating");
            // return "admin-edit-product";
            return "redirect:" + siteUrl + sectionProductUrl;
        }

        if (!file.isEmpty()) {
            try {
                String filename = imageService.saveImageToStorage(UPLOAD_DIRECTORY, file);
                item.setPicture(filename);
            } catch (Exception e) {
            }
        }

        this.productRepo.save(item);

        redirectAttributes.addFlashAttribute("message", "New item Created");

        return "redirect:" + siteUrl + sectionProductUrl;
    }

    @GetMapping(sectionProductUrl + "/{id}/edit")
    public String updateProduct(@PathVariable Long id, Model model) {
        List<SEOs> seos = this.seoRepo.findByPath(siteUrl + sectionProductUrl + "/edit");
        model.addAttribute("seos", seos.size() > 0 ? seos.get(0) : new SEOs());
        model.addAttribute("menu", AdminMenuHolder.getMenu(siteUrl + sectionProductUrl));

        ProductEntity item;
        if (this.productRepo.existsById(id)) {
            item = this.productRepo.findById(id).get();
            model.addAttribute("id", id);
        } else {
            item = new ProductEntity();
        }

        ArrayList<ProductType> types = ProductEntity.getProductTypes();
        model.addAttribute("productTypes", types);

        model.addAttribute("item", item);
        return "admin-edit-product";
    }

    @PostMapping(sectionProductUrl + "/{id}/edit")
    public String processUpdateProduct(
            ProductEntity item,
            @RequestParam("pictureFile") MultipartFile file,
            BindingResult result,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in updating.");
            // return "admin-edit-product";
            return "redirect:" + siteUrl + sectionProductUrl;
        }

        if (!file.isEmpty()) {
            try {
                ProductEntity itemInDB = this.productRepo.findById(id).get();
                this.imageService.deleteImage(UPLOAD_DIRECTORY, itemInDB.getPicture());
            } catch (Exception e) {
                item.setPicture("");
            }

            try {
                String filename = imageService.saveImageToStorage(UPLOAD_DIRECTORY, file);
                item.setPicture(filename);
            } catch (Exception e) {
                item.setPicture("");
            }
        }

        item.setId(id);
        this.productRepo.save(item);

        redirectAttributes.addFlashAttribute("message", "Values Updated");

        return "redirect:" + siteUrl + sectionProductUrl;

    }

    @DeleteMapping(sectionProductUrl + "/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        if (this.productRepo.existsById(id)) {
            try {
                ProductEntity item = this.productRepo.findById(id).get();
                this.imageService.deleteImage(UPLOAD_DIRECTORY, item.getPicture());
            } catch (Exception e) {
            }
            this.productRepo.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
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