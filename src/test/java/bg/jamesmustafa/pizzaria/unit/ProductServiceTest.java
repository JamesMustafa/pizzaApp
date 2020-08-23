package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.db.repository.ProductRepository;
import bg.jamesmustafa.pizzaria.dto.binding.CategoryBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
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
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTest {

    private ProductService serviceToTest;

    @Mock
    private ProductRepository mockProductRepository;

    @Mock
    private ModelMapper mockModelMapper;

    private long PRODUCT_ID = 99;
    private ArrayList<Product> allProducts;

    @BeforeEach
    public void setUp(){
        mockModelMapper = new ModelMapper();
        serviceToTest = new ProductService(mockProductRepository,mockModelMapper);
    }

    @Test
    public void testCreateProduct() throws Exception {
        //Arrange
        CategoryBindingModel category = new CategoryBindingModel();
        category.setName("Salads");

        ProductBindingModel productModel = new ProductBindingModel();
        productModel.setName("Apple");
        productModel.setCategory(category);
        productModel.setActivity(true);
        productModel.setPrice(BigDecimal.valueOf(1.99));
        productModel.setImgSrc("Not available");
        productModel.setDescription("We are collecting fresh apples from all over the country for you!");

        when(mockProductRepository.save(any())).thenAnswer(
                (Answer<Product>) invocation -> {
                    Product productToSave = invocation.getArgument(0);
                    productToSave.setId(PRODUCT_ID);
                    return productToSave;
                });
        //act
        Product expected = this.serviceToTest.createProduct(productModel);

        //assert
        Assertions.assertEquals(expected.getId(), PRODUCT_ID);
        Assertions.assertEquals(expected.getName(), productModel.getName());
        Assertions.assertEquals(expected.getDescription(), productModel.getDescription());


    }
    @Test
    public void testActivateProduct() throws Exception {
        //arrange
        Product testProduct = createProduct();
        boolean activity = testProduct.getActivity();
        when(mockProductRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        //act
        Product expected = this.serviceToTest.activateProduct(PRODUCT_ID);
        //assert
        Assertions.assertEquals(expected.getDescription(), testProduct.getDescription());
        Assertions.assertNotEquals(expected.getActivity(), activity);
    }

    @Test
    public void testEditProduct(){
        //arrange
        Product oldProduct = createProduct();
        ProductBindingModel newProduct = new ProductBindingModel();
        newProduct.setName("Watermelon");
        newProduct.setDescription("Watermelons from Australia here for you now !!!");
        newProduct.setCategory(this.mockModelMapper.map(oldProduct.getCategory(), CategoryBindingModel.class));
        newProduct.setImgSrc("watermelon.jpg");
        newProduct.setPrice(BigDecimal.valueOf(2.99));
        newProduct.setActivity(false);

        when(mockProductRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(oldProduct));
        when(mockProductRepository.save(any())).thenAnswer(
                (Answer<Product>) invocation -> {
                    Product productToSave = invocation.getArgument(0);
                    productToSave.setId(PRODUCT_ID);
                    return productToSave;
                });
        //act
        this.serviceToTest.editProduct(PRODUCT_ID, newProduct);

        //assert
        Assertions.assertEquals(oldProduct.getName(), newProduct.getName());
        Assertions.assertEquals(oldProduct.getDescription(), newProduct.getDescription());
        Assertions.assertEquals(oldProduct.getPrice(), newProduct.getPrice());
    }

    @Test
    public void testHardDelete() throws Exception {
        //arrange
        Product test = createProduct();
        //act
        this.serviceToTest.hardDelete(test.getId());
        //assert
        Assertions.assertEquals(0, this.mockProductRepository.count());
    }

    @Test
    public void testFindAll() throws Exception {
        //arrange
        Product test = createProduct();
        allProducts = new ArrayList<>();
        allProducts.add(test);
        when(mockProductRepository.findAll()).thenReturn(allProducts);
        //act
        List<ProductBindingModel> expectation = this.serviceToTest.findAll();
        //assert
        Assertions.assertEquals(1, expectation.size());
    }

    @Test
    public void testFindAllByCategory() throws Exception {
        //arrange
        Product test = createProduct();
        allProducts = new ArrayList<>();
        allProducts.add(test);
        when(mockProductRepository.findAll()).thenReturn(allProducts);
        //act
        List<ProductBindingModel> expectation = this.serviceToTest.findAllByCategory("Fruits");
        //assert
        Assertions.assertEquals(expectation.size(), 1);
        Assertions.assertEquals(expectation.get(0).getName(), test.getName());
    }

    @Test
    public void testFindById() throws Exception {
        //arrange
        Product test = createProduct();
        when(mockProductRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(test));
        //act
        ProductBindingModel expectation = this.serviceToTest.findById(PRODUCT_ID);
        //assert
        Assertions.assertEquals(expectation.getName(), test.getName());
        Assertions.assertEquals(expectation.getId(), test.getId());
    }

    private Product createProduct() {
        Category fruit = new Category();
        fruit.setName("Fruits");

        Product product = new Product();
        product.setName("Banana");
        product.setCategory(fruit);
        product.setImgSrc("Not available");
        product.setDescription("Best bananas from madagascar for you.");
        product.setActivity(true);
        product.setId(PRODUCT_ID);
        product.setPrice(BigDecimal.valueOf(1.99));
        return product;
    }

}
