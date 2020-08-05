package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.OfferDTO;
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

private final OfferService offerService;
private final ProductService productService;

    public OfferController(OfferService offerService, ProductService productService) {
        this.offerService = offerService;
        this.productService = productService;
    }

    //view offers
    //add offers
    //delete offers
    //TODO: I could make adding products better...(with multiplying products and etc.)
    //TODO: Should I make verification for the allProducts as well ?
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addOffer(Model model) {

        OfferDTO offerInputForm;
        if (model.containsAttribute("offerInputForm")) {
            offerInputForm = (OfferDTO) model.getAttribute("offerInputForm");
        }  else {
            offerInputForm = new OfferDTO();
        }

        model.addAttribute("offerInputForm", offerInputForm);
        model.addAttribute("allProducts", productService.findAll());

        return "offer/createOffer";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("offerInputForm") OfferDTO offerDTO,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("offerInputForm", offerDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "offerInputForm",
                    bindingResult);

            return "redirect:/offers/add";
        }

        offerService.createOffer(offerDTO);

        return "redirect:/home";
    }
}
