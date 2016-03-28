package mysite.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductParameters {
    private final String title, category, manufacturer, description, img;
    private final int quantity;
    private final int price;

    @JsonCreator
    public ProductParameters(@JsonProperty("title") String title,
                             @JsonProperty("category") String category,
                             @JsonProperty("manufacturer") String manufacturer,
                             @JsonProperty("description") String description,
                             @JsonProperty("img") String img,
                             @JsonProperty("price") int price,
                             @JsonProperty("quantity") int quantity) {
        this.title = title;
        this.category = category;
        this.manufacturer = manufacturer;
        this.description = description;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}