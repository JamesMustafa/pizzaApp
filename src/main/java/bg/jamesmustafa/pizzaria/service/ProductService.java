package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.db.entity.Product;
import bg.jamesmustafa.pizzaria.error.ProductNotFoundException;
import bg.jamesmustafa.pizzaria.db.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createProduct(ProductBindingModel productModel) {
        Product product = this.modelMapper.map(productModel, Product.class);
        this.productRepository.save(product);
    }

    @Transactional
    public void activateProduct(Long activateId){
        Product product = this.productRepository.findById(activateId)
                .orElseThrow(() -> new ProductNotFoundException("No product with the given id was found!"));

        if(product.getActivity()){
            product.setActivity(false);
        }
        else product.setActivity(true);

        this.productRepository.save(product);
    }

    @Transactional
    public void editProduct(Long productId, ProductBindingModel productDTO){
        Product product = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with this id was not found!"));

        product.setName(productDTO.getName());
        product.setActivity(productDTO.getActivity());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(this.modelMapper.map(productDTO.getCategory(), Category.class));
        this.productRepository.save(product);
    }

    //On the other hand, hardDelete method deletes the whole object without chance of putting the object back to our project.
    //Added @PreRemove method in entity class, in order to delete the object from another tables as well.
    @Transactional
    public void hardDelete(Long productId) {
        this.productRepository.deleteById(productId);
    }

    public List<ProductBindingModel> findAll() {
        return this.productRepository.
                findAll()
                .stream()
                .map(product -> this.modelMapper.map(product, ProductBindingModel.class))
                .collect(Collectors.toList());
    }

    public List<ProductBindingModel> findAllByCategory(String categoryName){
        return this.findAll()
                .stream()
                .filter(product -> product.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }

    public ProductBindingModel findById(Long productId){
        return this.productRepository.findById(productId)
                .map(product -> this.modelMapper.map(product, ProductBindingModel.class))
                .orElseThrow(() -> new ProductNotFoundException("Product with this id was not found"));
    }

}
