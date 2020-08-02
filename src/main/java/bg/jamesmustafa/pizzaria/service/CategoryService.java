package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.entity.Category;
import bg.jamesmustafa.pizzaria.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //TODO: Add DTO object if needed
    public List<Category> findAll() {

        return categoryRepository.findAll();
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }
}
