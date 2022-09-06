package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.Offer;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.db.repository.OfferRepository;
import bg.jamesmustafa.pizzaria.dto.binding.OfferAddBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.error.OfferNotFoundException;
import bg.jamesmustafa.pizzaria.util.TimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, ProductService productService, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Offer createOffer(OfferAddBindingModel offerDTO) {
        Offer offer = this.modelMapper.map(offerDTO, Offer.class);
        offer.setValidUntil(TimeUtil.parseDateToTime(offerDTO.getValidUntil()));

        List<Product> products = Arrays.stream(offerDTO.getProducts())
                .map(productId -> modelMapper.map(productService.findById(Long.parseLong(productId)), Product.class))
                .collect(Collectors.toList());

        BigDecimal oldPrice = BigDecimal.valueOf(products.stream()
                .map(Product::getPrice)
                .mapToLong(BigDecimal::longValue)
                .sum());

        offer.setProducts(products);
        offer.setOldPrice(oldPrice);
        this.offerRepository.save(offer);
        return offer;
    }

    //On the other hand, hardDelete method deletes the whole object without chance of putting the object back to our project.
    @Transactional
    public void hardDelete(Long productId) {
        this.offerRepository.deleteById(productId);
    }

    public List<OfferBindingModel> findAllValidOffers() {
        return this.offerRepository.findAll()
                .stream()
                .filter(offer -> offer.getValidUntil().isAfter(LocalDateTime.now()))
                .map(offer -> this.modelMapper.map(offer, OfferBindingModel.class))
                .collect(Collectors.toList());
    }

    public OfferBindingModel findById(Long offerId) {
        return this.offerRepository.findById(offerId)
                .map(offer -> this.modelMapper.map(offer, OfferBindingModel.class))
                .orElseThrow(() -> new OfferNotFoundException("Offer with this id was not found"));
    }

}
