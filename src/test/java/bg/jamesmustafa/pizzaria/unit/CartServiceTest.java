package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.dto.binding.CategoryBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.dto.view.CartProductViewModel;
import bg.jamesmustafa.pizzaria.dto.view.ProductDetailsViewModel;
import bg.jamesmustafa.pizzaria.service.CartService;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartServiceTest {

    private CartService serviceToTest;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private UserDetailsServiceImpl mockUserDetailsService;

    @Mock
    private OfferService mockOfferService;

    @Mock
    private HttpSession mockHttpSession;

    @Mock
    private HttpServletRequest request;

    private long PRODUCT_ID = 99;
    private List<CartProductViewModel> cart;
    private List<ProductBindingModel> products;

    @BeforeEach
    public void setUp() {
        mockModelMapper = new ModelMapper();
        serviceToTest = new CartService(mockHttpSession,mockModelMapper,mockUserDetailsService,mockOfferService);
    }

    @Test
    public void testAddOneProductToCart() throws Exception {
        //arrange
        ProductBindingModel product = createProduct("Apple", 1);
        cart = new ArrayList<>();

        when(request.getSession()).thenReturn(mockHttpSession);
        when(request.getSession().getAttribute("shopping-cart")).thenReturn(cart);
        //act
        this.serviceToTest.addOneProductToCart(product,mockHttpSession);
        //assert
        Assertions.assertEquals(1,cart.size());
        Assertions.assertEquals(product.getName(), cart.get(0).getProductDetailsViewModel().getName());
    }

    @Test
    public void testAddOneProductToCartWithQuantity() throws Exception {
        //arrange
        ProductBindingModel product = createProduct("Apple", 1);
        cart = new ArrayList<>();

        when(request.getSession()).thenReturn(mockHttpSession);
        when(request.getSession().getAttribute("shopping-cart")).thenReturn(cart);
        //act
        this.serviceToTest.addOneProductToCart(product,9,mockHttpSession);
        //assert
        Assertions.assertEquals(1, cart.size());
        Assertions.assertEquals(9, cart.get(0).getQuantity());
    }

    @Test
    public void testAddListOfProductsToCart() throws Exception {
        //arrange
        ProductBindingModel firstProduct = createProduct("Apple", 1);
        ProductBindingModel secondProduct = createProduct("Banana", 2);
        ProductBindingModel thirdProduct = createProduct("Watermelon", 3);
        cart = new ArrayList<>();
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);

        when(request.getSession()).thenReturn(mockHttpSession);
        when(request.getSession().getAttribute("shopping-cart")).thenReturn(cart);
        //act
        this.serviceToTest.addListOfProductsToCart(products, mockHttpSession);
        //assert
        Assertions.assertEquals(3, cart.size());
        Assertions.assertEquals(firstProduct.getName(), cart.get(0).getProductDetailsViewModel().getName());
        Assertions.assertEquals(secondProduct.getPrice(), cart.get(1).getProductDetailsViewModel().getPrice());
    }

    @Test
    public void testCalcTotal() throws Exception {
        //arrange
        ProductBindingModel firstProduct = createProduct("Apple", 1);
        ProductBindingModel secondProduct = createProduct("Banana", 2);
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        cart = setTestCart(products);

        //act
        BigDecimal actual = this.serviceToTest.calcTotal(cart);

        //assert
        double expectedSum = (1 * firstProduct.getPrice().doubleValue()) + (1 * secondProduct.getPrice().doubleValue());
        Assertions.assertEquals(BigDecimal.valueOf(expectedSum), actual);
    }

    @Test
    public void testRemoveItemFromCart() throws Exception {
        //arrange
        ProductBindingModel firstProduct = createProduct("Apple", 1);
        ProductBindingModel secondProduct = createProduct("Banana", 2);
        ProductBindingModel thirdProduct = createProduct("Peach", 3);
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);
        cart = setTestCart(products);
        //act
        this.serviceToTest.removeItemFromCart((long)2, cart);
        //assert
        Assertions.assertEquals(2, cart.size());
        Assertions.assertEquals(false, cart.contains(secondProduct));
    }

    @Test
    public void testPrepareOrder() throws Exception {
        //arrange
        ProductBindingModel firstProduct = createProduct("Apple", 1);
        ProductBindingModel secondProduct = createProduct("Banana", 2);
        ProductBindingModel thirdProduct = createProduct("Peach", 3);
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);
        cart = setTestCart(products);

        //user
        User test = createUser();

        when(mockUserDetailsService.loadUserByUsername("test")).thenReturn(test);
        //act
        OrderBindingModel order = this.serviceToTest
                .prepareOrder(cart,"test", "How much should I wait", BigDecimal.valueOf(5.99));
        //assert
        Assertions.assertEquals("How much should I wait", order.getComment());
        Assertions.assertEquals(test.getEmail(), order.getCustomer().getEmail());
        Assertions.assertEquals(3, order.getProducts().size());
    }

    @Test
    public void testPrepareOrderFromOffer() throws Exception {
        //arrange
        //list of products
        ProductBindingModel firstProduct = createProduct("Apple", 1);
        ProductBindingModel secondProduct = createProduct("Banana", 2);
        ProductBindingModel thirdProduct = createProduct("Peach", 3);
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);

        //customer
        User test = createUser();

        //offer
        OfferBindingModel offer = new OfferBindingModel();
        offer.setId((long)1);
        offer.setOldPrice(BigDecimal.valueOf(9.99));
        offer.setPromoPrice(BigDecimal.valueOf(5.99));
        offer.setProducts(products);
        offer.setValidUntil(LocalDateTime.now().plusMonths(3));

        when(mockUserDetailsService.loadUserByUsername("test")).thenReturn(test);
        when(mockOfferService.findById((long)1)).thenReturn(offer);

        //act
        OrderBindingModel order = serviceToTest
                .prepareOrderFromOffer((long)1,"Hurry up a little", BigDecimal.valueOf(5.99), "test");
        //assert
        Assertions.assertEquals("Hurry up a little", order.getComment());
        Assertions.assertEquals(3, order.getProducts().size());
        Assertions.assertEquals(offer.getPromoPrice(), order.getTotalPrice());
    }

    @Test
    public void testCheckIfEmailConfirmed() throws Exception {
        //arrange
        User test = createUser();
        when(mockUserDetailsService.loadUserByUsername("test")).thenReturn(test);
        //act
        boolean isConfirmed = this.serviceToTest.checkIfEmailConfirmed("test");
        //assert
        Assertions.assertEquals(test.getEmailConfirmed(), isConfirmed);
    }

    private List<CartProductViewModel> setTestCart(List<ProductBindingModel> products){
        cart = new ArrayList<>();
        for (ProductBindingModel product: products){
            CartProductViewModel cartProduct = new CartProductViewModel();
            cartProduct.setQuantity(1);
            cartProduct.setProductDetailsViewModel(this.mockModelMapper.map(product, ProductDetailsViewModel.class));
            cart.add(cartProduct);
        }
        return cart;
    }

    private ProductBindingModel createProduct(String keyWord,long productId){
        ProductBindingModel product = new ProductBindingModel();
        product.setName(keyWord);
        product.setCategory(new CategoryBindingModel());
        product.setImgSrc("Not available");
        product.setDescription("Best products from madagascar right next to you.");
        product.setActivity(true);
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(1.99));
        return product;
    }

    private User createUser(){
        //user
        User test = new User();
        test.setEmail("test@example.com");
        test.setUsername("test");
        test.setName("test");
        test.setSurname("test");
        test.setPhoneNumber("0894888210");
        test.setCity("test");
        test.setAddress("test");
        test.setPassword("test");
        test.setEmailConfirmed(false);
        test.setPhoneNumberConfirmed(false);
        test.setRoles(Set.of(new Role()));
        return test;
    }
}
