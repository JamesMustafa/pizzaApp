package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import bg.jamesmustafa.pizzaria.db.entity.Offer;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.db.repository.OfferRepository;
import bg.jamesmustafa.pizzaria.dto.binding.OfferAddBindingModel;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @BeforeEach
    public void setUp(){
        mockModelMapper = new ModelMapper();
        serviceToTest = new OfferService(mockOfferRepository,mockProductService,mockModelMapper);
    }

    @Test
    public void testCreateOffer() throws Exception {
        //arrange
        Product first = createProduct();
        Product second = createProduct();
        products = new ArrayList<>();
        products.add(first);
        products.add(second);

        OfferAddBindingModel offer = this.mockModelMapper.map(createOffer(), OfferAddBindingModel.class);
    }

    private Offer createOffer() {
        Offer offer = new Offer();
        offer.setOldPrice(BigDecimal.valueOf(30.00));
        offer.setPromoPrice(BigDecimal.valueOf(19.99));
        offer.setValidUntil(LocalDateTime.now().plusMonths(3));
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
        product.setId((long)1);
        product.setPrice(BigDecimal.valueOf(1.99));
        return product;
    }
}
