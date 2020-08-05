package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
import bg.jamesmustafa.pizzaria.entity.Product;
import bg.jamesmustafa.pizzaria.error.ProductNotFoundException;
import bg.jamesmustafa.pizzaria.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    public void createProduct(ProductDTO productDTO) {
    //TODO: Use the modelMapper for the mapping and find a solution to the setCategory property when using a mapper.
        Product product = this.modelMapper.map(productDTO, Product.class);
        product.setActivity(productDTO.getActivity());
        //TODO: Set category from input /// Is it good idea to remove the categoryservice here and work with it only through the product controller?
        productRepository.save(product);
    }

    public List<ProductDTO> findAll() {
        return productRepository.
                findAll().
                stream().
                map(product -> this.modelMapper.map(product, ProductDTO.class)).
                collect(Collectors.toList());
    }

    public List<ProductDTO> findAllByCategory(String categoryName){
        return productRepository.
                findAll().
                stream().
                filter(product -> product.getCategory().getName().equalsIgnoreCase(categoryName)).
                map(product -> this.modelMapper.map(product, ProductDTO.class)).
                collect(Collectors.toList());
    }

    public ProductDTO findById(Long productId){
       return this.productRepository.findById(productId)
               .map(product -> {
                   ProductDTO productDTO = this.modelMapper.map(product, ProductDTO.class);
                           return productDTO;
               })
               .orElseThrow(() -> new ProductNotFoundException("Product with this id was not found"));
    }

    public void softDelete(Long productId) {
        //TODO: set isDeleted  to true and deletedOn
    }

    public void hardDelete(Long productId) {
        productRepository.deleteById(productId);
    }


    public void activateProduct(Long activateId){
        Product product = this.productRepository.findById(activateId).orElseThrow();
        if(product.getActivity()){
            product.setActivity(false);
        }
        else product.setActivity(true);

        productRepository.save(product);
    }

    public void editProduct(Long id, ProductDTO productDTO){

        Product product = this.productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with this id was not found!"));

        product.setName(productDTO.getName());
        product.setActivity(productDTO.getActivity());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());

        productRepository.save(product);
    }
}
