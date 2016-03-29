package mysite.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import mysite.exception.ModelException;

import java.util.Objects;

public final class Customer {
    private final String username;
    private String password,
            email,
            firstName,
            lastName,
            address,
            phoneNumber;
    private ShoppingCart shoppingCart = new ShoppingCart();

    @JsonCreator
    public Customer(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("address") String address,
            @JsonProperty("phoneNumber") String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Customer(String username, String password, String email, String firstName, String lastName, String address, String phoneNumber, ShoppingCart shoppingCart) {
        this(username, password, email, firstName, lastName, address, phoneNumber);
        this.shoppingCart = shoppingCart == null? new ShoppingCart():shoppingCart;
    }

    @JsonIgnore
    public void addProductToShoppingCart(int productId) {
        shoppingCart.addProduct(productId);
    }

    public void removeProductFromShoppingCart(int productId) throws ModelException {
        shoppingCart.removeProduct(productId);
    }

    /**
     * Removes all products of the specified id from this customers shopping
     * cart.
     *
     * @param productId
     * @return true if a product was removed from this customer.
     * @throws ModelException
     */
    public boolean removeProductsWithIdFromShoppingCart(int productId) throws ModelException {
        return shoppingCart.removeAllProductsWithId(productId);
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setMobileNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(username, customer.username) &&
                Objects.equals(password, customer.password) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(address, customer.address) &&
                Objects.equals(phoneNumber, customer.phoneNumber) &&
                Objects.equals(shoppingCart, customer.shoppingCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, firstName, lastName, address, phoneNumber, shoppingCart);
    }
}
