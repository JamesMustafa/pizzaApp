package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import bg.jamesmustafa.pizzaria.db.entity.Offer;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.db.repository.OfferRepository;
import bg.jamesmustafa.pizzaria.dto.binding.OfferAddBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OfferServiceTest {

    private OfferService serviceToTest;

    @Mock
    private OfferRepository mockOfferRepository;
    @Mock
    private ProductService mockProductService;
    @Mock
    private ModelMapper mockModelMapper;

    private List<Product> products;
    private long OFFER_ID = 1;
    private ArrayList<Offer> offers;
    private Product product;

    @BeforeEach
    public void setUp(){
        mockModelMapper = new ModelMapper();
        serviceToTest = new OfferService(mockOfferRepository,mockProductService,mockModelMapper);
    }

    @Test
    public void testCreateOffer() throws Exception {
        //arrange
        ProductBindingModel product = this.mockModelMapper.map(createProduct(), ProductBindingModel.class);
        Product first = createProduct();
        Product second = createProduct();
        products = new ArrayList<>();
        products.add(first);
        products.add(second);

        OfferAddBindingModel offer = this.mockModelMapper.map(createTestOffer(), OfferAddBindingModel.class);
        offer.setProducts(new String[]{"1"});
        offer.setValidUntil("2020-10");
        when(mockProductService.findById(OFFER_ID)).thenReturn(product);
        when(mockOfferRepository.save(any())).thenAnswer(
                (Answer<Offer>) invocation -> {
                    Offer offerToSave = invocation.getArgument(0);
                    offerToSave.setId(OFFER_ID);
                    return offerToSave;
                });
        //act
        Offer actual = this.serviceToTest.createOffer(offer);
        //assert
        Assertions.assertEquals(OFFER_ID, actual.getId());
        Assertions.assertEquals(product.getName(), actual.getProducts().get(0).getName());
    }

    @Test
    public void testHardDelete() throws Exception {
        //arrange
        Offer testOffer = createTestOffer();
        this.mockOfferRepository.save(testOffer);
        //act
        this.serviceToTest.hardDelete(testOffer.getId());
        //assert
        Assertions.assertEquals(0, this.mockOfferRepository.count());
    }

    @Test
    public void testFindAllValidOffers() throws Exception {
        //arrange
        Offer firstOffer = createTestOffer(LocalDateTime.now().plusMonths(3));
        Offer secondOffer = createTestOffer(LocalDateTime.now());
        Offer thirdOffer = createTestOffer(LocalDateTime.now().minusMinutes(2));
        offers = new ArrayList<>();
        offers.add(firstOffer);
        offers.add(secondOffer);
        offers.add(thirdOffer);
        when(this.mockOfferRepository.findAll()).thenReturn(offers);
        //act
        List<OfferBindingModel> actual = this.serviceToTest.findAllValidOffers();
        //assert
        Assertions.assertEquals(1,actual.size()); //because only one offer is valid from the three above
        Assertions.assertEquals(firstOffer.getValidUntil(), actual.get(0).getValidUntil()); //we make sure that is the one
    }

    @Test
    public void testFindById() throws Exception {
        //arrange
        Offer testOffer = createTestOffer();
        when(mockOfferRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        //act
        OfferBindingModel actual = this.serviceToTest.findById(OFFER_ID);
        //assert
        Assertions.assertEquals(testOffer.getId(), actual.getId());
        Assertions.assertEquals(testOffer.getProducts(), actual.getProducts());
    }

    private Offer createTestOffer(LocalDateTime localDateTime) {
        Offer offer = new Offer();
        offer.setOldPrice(BigDecimal.valueOf(30.00));
        offer.setPromoPrice(BigDecimal.valueOf(19.99));
        offer.setValidUntil(localDateTime);
        offer.setProducts(products);

        return offer;
    }

    private Offer createTestOffer() {
        Offer offer = new Offer();
        offer.setOldPrice(BigDecimal.valueOf(30.00));
        offer.setPromoPrice(BigDecimal.valueOf(19.99));
        offer.setValidUntil(LocalDateTime.now());
        offer.setProducts(products);

        return offer;
    }

    private Product createProduct(){
        Category fruit = new Category();
        fruit.setName("Fruits");

        Product product = new Product();
        product.setName("Banana");
        product.setCategory(fruit);
        product.setImgSrc("Not available");
        product.setDescription("Best bananas from madagascar for you.");
        product.setActivity(true);
        product.setId(OFFER_ID);
        product.setPrice(BigDecimal.valueOf(1.99));
        return product;
    }
}
