package mysite.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import mysite.exception.ModelException;

import java.util.ArrayList;

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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof Customer) {
            Customer cu = (Customer) other;
            if (this.getUsername().equals(cu.getUsername())
                    && this.getPassword().equals(cu.getPassword())
                    && this.getEmail().equals(cu.getEmail())
                    && this.getFirstName().equals(cu.getFirstName())
                    && this.getLastName().equals(cu.getLastName())
                    && this.getAddress().equals(cu.getAddress())
                    && this.getPhoneNumber().equals(cu.getPhoneNumber())
                    && this.getShoppingCart().equals(cu.getShoppingCart())) {
                return true;
            }
        }
        return false;
    }
}
