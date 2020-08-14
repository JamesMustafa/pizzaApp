package bg.jamesmustafa.pizzaria.dto.binding;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductBindingModel {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3,max = 32, message = "Name must be less than 32 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 1024, message = "Description length must be less than 1024 characters")
    private String description;

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal price;

    @NotNull
    private Boolean activity;

    @NotBlank(message = "Image source cannot be blank")
    private String imgSrc;

    //TODO: Make it categoryDTO class.
    @NotNull(message = "Category cannot be null")
    private CategoryBindingModel category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public CategoryBindingModel getCategory() { return category; }

    public void setCategory(CategoryBindingModel category) { this.category = category; }
}
