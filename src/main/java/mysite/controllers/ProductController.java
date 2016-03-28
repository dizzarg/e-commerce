package mysite.controllers;

import mysite.models.Product;
import mysite.models.ProductParameters;
import mysite.services.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Inject
    private ShopService shopService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Integer> addProduct(
            @RequestBody ProductParameters param){
        Product product = shopService.addProduct(param);
        return ResponseEntity.ok(product.getId());
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Product> products(){
        return shopService.getProducts();
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public Product getProductWithId(@PathVariable("productId") int productId) {
        return shopService.getProductWithId(productId);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
    public void removeProduct(@PathVariable("productId") int productId) {
        shopService.removeProduct(productId);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
    public void updateProduct(@PathVariable("productId") int productId,
                              @RequestBody ProductParameters param) {
        shopService.updateProduct(productId, param);
    }

}
