package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import bg.jamesmustafa.pizzaria.db.repository.CategoryRepository;
import bg.jamesmustafa.pizzaria.dto.binding.CategoryBindingModel;
import bg.jamesmustafa.pizzaria.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTest {

    private CategoryService serviceToTest;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @Mock
    private ModelMapper mockModelMapper;

    private long CATEGORY_ID = 99;
    private ArrayList<Category> allCategories;

    @BeforeEach
    public void setUp(){
        mockModelMapper = new ModelMapper();
        serviceToTest = new CategoryService(mockCategoryRepository,mockModelMapper);
    }

    @Test
    public void testFindAll() throws Exception {
        //arrange
        Category pizzas = new Category();
        pizzas.setName("Pizzas");
        Category fruits = new Category();
        fruits.setName("Fruits");

        allCategories = new ArrayList<>();
        allCategories.add(pizzas);
        allCategories.add(fruits);

        when(mockCategoryRepository.findAll()).thenReturn(allCategories);
        //act
        List<CategoryBindingModel> expectations = this.serviceToTest.findAll();
        //assert
        Assertions.assertEquals(expectations.size(), 2);
        Assertions.assertEquals(expectations.get(0).getName(), pizzas.getName());
        Assertions.assertEquals(expectations.get(1).getName(), fruits.getName());
    }

    @Test
    public void testFindById() throws Exception {
        //arrange
        Category pizzas = new Category();
        pizzas.setName("Pizzas");
        pizzas.setId(CATEGORY_ID);

        when(mockCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(pizzas));
        //act
        CategoryBindingModel expectation = this.serviceToTest.findById(CATEGORY_ID);
        //assert
        Assertions.assertEquals(expectation.getName(), pizzas.getName());
        Assertions.assertEquals(expectation.getId(), pizzas.getId());
    }
}
