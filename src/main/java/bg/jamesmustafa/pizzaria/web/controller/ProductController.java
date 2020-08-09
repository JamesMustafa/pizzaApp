package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.service.CategoryService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addProduct(Model model) {

        model.addAttribute("productInputForm", new ProductBindingModel());
        model.addAttribute("categoryTypes", this.categoryService.findAll());

        return "product/createProduct";
    }

    //TODO: Should I make my own image uploader???
    //TODO: Should this method have the same name with the getMethod above?
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save
    (@Valid @ModelAttribute("productInputForm") ProductBindingModel productDTO,
                    BindingResult bindingResult,
                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "redirect:/products/add";
        }

        this.productService.createProduct(productDTO);

        return "redirect:/products/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public String delete(@ModelAttribute(name="deleteId") Long deleteId) {

        this.productService.hardDelete(deleteId);

        return "redirect:/home";
    }

    @GetMapping("/")
    public String viewProducts(Model model){

        model.addAttribute("pizzas", this.productService.findAllByCategory("pizza"));
        model.addAttribute("drinks", this.productService.findAllByCategory("drinks"));
        model.addAttribute("deserts", this.productService.findAllByCategory("deserts"));
        model.addAttribute("salads", this.productService.findAllByCategory("salads"));

        //TODO: can i make foreach for every model attribute?
        return "product/index";
    }

    //TODO: to make the getter without /{id}
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(@PathVariable("id") Long productId, Model model) {

        model.addAttribute("productEditForm", this.productService.findById(productId));
        model.addAttribute("categoryTypes", this.categoryService.findAll());

        return "product/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProductConfirm(@Valid @ModelAttribute("productEditForm") ProductBindingModel productDTO,
                                     @PathVariable("id") Long id,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            return "redirect:/products/";
        }

        this.productService.editProduct(id, productDTO);

        return "redirect:/home";
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/activate")
    public String activate(@ModelAttribute(name="activateId") Long activateId) {

        this.productService.activateProduct(activateId);

        return "redirect:/products/";
    }

}
