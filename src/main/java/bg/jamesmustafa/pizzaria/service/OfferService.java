package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.Offer;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.db.repository.OfferRepository;
import bg.jamesmustafa.pizzaria.dto.binding.OfferAddBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.error.OfferNotFoundException;
import bg.jamesmustafa.pizzaria.util.TimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public void createOffer(OfferAddBindingModel offerDTO) {
        BigDecimal oldPrice = new BigDecimal(0);

        Offer offer = this.modelMapper.map(offerDTO, Offer.class);
        offer.setValidUntil(TimeUtil.parseDateToTime(offerDTO.getValidUntil()));
        List<Product> products = new ArrayList<>();

        for(String productId: offerDTO.getProducts()){
            Product product = this.modelMapper.map(
                    this.productService.findById(Long.parseLong(productId)), Product.class);
            oldPrice = oldPrice.add(product.getPrice()); //collecting the prices from all the products
            products.add(product);
        }
        offer.setProducts(products);
        offer.setOldPrice(oldPrice);
        this.offerRepository.save(offer);
        //TODO: Once again find the differences between save and saveAndFlush !!!
    }

    public List<OfferBindingModel> findAllValidOffers(){
        return this.offerRepository.findAll()
                .stream()
                .filter(offer -> offer.getValidUntil().isAfter(LocalDateTime.now()))
                .map(offer -> this.modelMapper.map(offer, OfferBindingModel.class))
                .collect(Collectors.toList());
    }

    public OfferBindingModel findById(Long offerId){
        return this.offerRepository.findById(offerId)
                .map(offer -> this.modelMapper.map(offer, OfferBindingModel.class))
                .orElseThrow(() -> new OfferNotFoundException("Offer with this id was not found"));
    }
}
