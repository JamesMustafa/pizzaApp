package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.dto.OfferDTO;
import bg.jamesmustafa.pizzaria.entity.Offer;
import bg.jamesmustafa.pizzaria.repository.OfferRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public void createOffer(OfferDTO offerDTO) {
        Offer offer = this.modelMapper.map(offerDTO, Offer.class);
        /* offer.setProducts(
              this.productService.findAll()
              .stream()
              .filter(productDTO -> offerDTO.getProducts().contains(productDTO.getId()))
              .collect(Collectors.toList());
        ); */
        offerRepository.save(offer);
    }
}
