package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.dto.binding.CategoryBindingModel;
import bg.jamesmustafa.pizzaria.error.CategoryNotFoundException;
import bg.jamesmustafa.pizzaria.db.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    public List<CategoryBindingModel> findAll() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> this.modelMapper.map(category, CategoryBindingModel.class))
                .collect(Collectors.toList());
    }

    public CategoryBindingModel findById(Long categoryId) {

        return this.categoryRepository
                .findById(categoryId)
                .map(category -> this.modelMapper.map(category, CategoryBindingModel.class))
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with the given id!"));

    }
}
