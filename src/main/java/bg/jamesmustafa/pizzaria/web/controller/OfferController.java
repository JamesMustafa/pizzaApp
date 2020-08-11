package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.OfferAddBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/offers")
public class OfferController {
    //TODO: Super Important: Offer and order must check if the products they contain are all active and if not automatically to remove them from cart !!!
    private final OfferService offerService;
    private final ProductService productService;

    public OfferController(OfferService offerService, ProductService productService) {
        this.offerService = offerService;
        this.productService = productService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addOffer(Model model){
        model.addAttribute("offerInputForm", new OfferAddBindingModel());
        model.addAttribute("allProducts", this.productService.findAll());

        return "offer/createOffer";
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String allOffers(Model model){
        model.addAttribute("offers", this.offerService.findAllValidOffers()) ;
        return "offer/allOffers";
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String save
            (@Valid @ModelAttribute("offerInputForm") OfferAddBindingModel offerDTO,
             BindingResult bindingResult,
             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/offers/add";
        }
        this.offerService.createOffer(offerDTO);
        return "redirect:/home";
    }
}
