package bg.jamesmustafa.pizzaria.db.entity;

import bg.jamesmustafa.pizzaria.db.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    //phone number, city, address are not necessary until the user decides to order something.

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "is_email_confirmed", nullable = false)
    private Boolean emailConfirmed;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "is_phone_confirmed", nullable = false)
    private Boolean phoneNumberConfirmed;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

//    @OneToMany(mappedBy = "customer", targetEntity = Order.class,
//            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<Order> orders;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Role> roles;

    public User() {
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

    public void setName(String name) { this.name = name; }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) { this.surname = surname; }

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

    public Boolean getEmailConfirmed() { return emailConfirmed; }

    public void setEmailConfirmed(Boolean emailConfirmed) { this.emailConfirmed = emailConfirmed; }

    public Boolean getPhoneNumberConfirmed() { return phoneNumberConfirmed; }

    public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) { this.phoneNumberConfirmed = phoneNumberConfirmed; }

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
