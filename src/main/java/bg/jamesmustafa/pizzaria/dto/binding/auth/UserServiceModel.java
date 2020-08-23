package bg.jamesmustafa.pizzaria.dto.binding.auth;

import javax.validation.constraints.*;

public class UserServiceModel {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3,max = 20, message = "Username length must be less than 20 characters")
    private String username;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2,max = 15, message = "Name length must be between 2 and 15 characters")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    @Size(min = 3,max = 15, message = "Surname length must be between 3 and 15 characters")
    private String surname;

    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "([ 0-9]){8,11}$", message = "Invalid phone number")
    @Size(max = 10 ,message = "Phone number should be maximum of 10 digits")
    private String phoneNumber;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 3,max = 15, message = "City length must be between 3 and 15 characters")
    private String city;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5,max = 100, message = "Address length must be between 5 and 100 characters")
    private String address;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
