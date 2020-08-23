package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.service.CategoryService;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public String viewProducts(Model model){
        model.addAttribute("pizzas", this.productService.findAllByCategory("pizza"));
        model.addAttribute("drinks", this.productService.findAllByCategory("drinks"));
        model.addAttribute("deserts", this.productService.findAllByCategory("deserts"));
        model.addAttribute("salads", this.productService.findAllByCategory("salads"));
        return "product/index";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(ProductBindingModel productBindingModel, Model model) {
        model.addAttribute("categoryTypes", this.categoryService.findAll());
        return "product/createProduct";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(@PathVariable("id") Long productId, Model model) {
        model.addAttribute("productEditForm", this.productService.findById(productId));
        model.addAttribute("categoryTypes", this.categoryService.findAll());
        return "product/edit";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String save
    (@Valid ProductBindingModel productBindingModel,
     BindingResult bindingResult,
     Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryTypes", this.categoryService.findAll());
            return "product/createProduct";
        }

        this.productService.createProduct(productBindingModel);
        return "redirect:/products";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProductConfirm(@Valid @ModelAttribute("productEditForm") ProductBindingModel productDTO,
                                     @PathVariable("id") Long id,
                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "product/edit";
        }
        this.productService.editProduct(id, productDTO);
        return "redirect:/products";
    }

    @PostMapping("/activate")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String activate(@ModelAttribute(name="activateId") Long activateId) {
        this.productService.activateProduct(activateId);
        return "redirect:/products";
    }

    //Does DeleteMapping works only on restful applications
    //IN HARD DELETE, WE DELETE THE WHOLE ENTITY OBJECT, WITHOUT CHANCE OF RETURNING IT BACK!
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String hardDelete(@ModelAttribute(name="deleteId") Long deleteId) {
        this.productService.hardDelete(deleteId);
        return "redirect:/products";
    }
}
