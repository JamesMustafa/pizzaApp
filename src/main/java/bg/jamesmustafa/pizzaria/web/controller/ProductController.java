package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
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

        ProductDTO productInputForm;
        if (model.containsAttribute("productInputForm")) {
            productInputForm = (ProductDTO) model.getAttribute("productInputForm");
        }  else {
            productInputForm = new ProductDTO();
        }

        model.addAttribute("productInputForm", productInputForm);
        model.addAttribute("categoryTypes", categoryService.findAll());

        return "product/createProduct";
    }

    //TODO: Should this method have the same name with the getMethod above?
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("productInputForm") ProductDTO productDTO,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productInputForm", productDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "productInputForm",
                    bindingResult);

            return "redirect:/products/add";
        }

        productService.createProduct(productDTO);

        return "redirect:/home";
    }

    //TODO: Make it optional hard or soft delete with constructor variable
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public String delete(@ModelAttribute(name="deleteId") Long deleteId) {
        productService.hardDelete(deleteId);
        return "redirect:/home";
    }

}
