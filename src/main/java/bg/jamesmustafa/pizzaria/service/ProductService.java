package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
import bg.jamesmustafa.pizzaria.entity.Product;
import bg.jamesmustafa.pizzaria.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public void createProduct(ProductDTO productDTO) {

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setActive(productDTO.getActivity());
        product.setPrice(productDTO.getPrice());
        product.setImgSrc(productDTO.getImgSrc());
        product.setCategory(categoryService.findById(productDTO.getCategoryId()));
        //TODO: Set category from input
        productRepository.save(product);
    }

    public void softDelete(Long productId) {
        //TODO: set isDeleted  to true and deletedOn
    }

    public void hardDelete(Long productId) {
        productRepository.deleteById(productId);
    }
}
