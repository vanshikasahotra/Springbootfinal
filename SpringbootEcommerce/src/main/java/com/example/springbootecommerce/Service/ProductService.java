package com.example.springbootecommerce.Service;

import com.example.springbootecommerce.ExceptionHandler.CategoryException;
import com.example.springbootecommerce.ExceptionHandler.ProductException;
import com.example.springbootecommerce.Model.Category;
import com.example.springbootecommerce.Model.Product;
import com.example.springbootecommerce.Repository.CategoryRepo;
import com.example.springbootecommerce.Repository.ProductRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo) { this.productRepo = productRepo;}

    // add product
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    //get product by id
    public Product getProductById(ObjectId id) throws ProductException{
        Optional<Product> optProduct = productRepo.findById(id);
        if(optProduct.isEmpty() || optProduct == null) {
            throw new ProductException("Product doesn't exist");
        }
        return optProduct.orElseGet(optProduct::get);
    }

    //get product by any field
    public List<Product> getProductByField(String field, String value) throws ProductException {
        List<Product> listProduct = switch (field) {
            case "id" -> productRepo.findByID(new ObjectId(value));
            case "name" -> productRepo.findByName(value);
            case "description" -> productRepo.findByDesc(value);
            case "price" -> productRepo.findByPrice(Integer.parseInt(value));
            default -> throw new ProductException("incorrect field");
        };

        if(listProduct.isEmpty()) {
            throw new ProductException("Product doesn't exist");
        }
        return listProduct;
    }

    //get all products
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // update Product
    public String updateProduct(String id, Product product) {
        ObjectId objectId = new ObjectId(id);
        Optional<Product> optProduct = productRepo.findById(objectId);
        if(optProduct.isEmpty()) {
            throw new RuntimeException("given product doesn't exist");
        }
        Product productRec = optProduct.get();
        if (product.getName() != null)
            productRec.setName(product.getName());
        if (product.getDescription() != null)
            productRec.setDescription(product.getDescription());
        if (product.getPrice() != 0)
            productRec.setPrice(product.getPrice());

        productRepo.save(productRec);
        return "{" +
                "\"message\":"+"Successfully updated product\",\n"+
                "\"data\":"+productRec+",\n"+
                "}";
    }

    // link product with category id
    public String linkProductWithCategory(String prodId, String categoryId) {
        Optional<Product> optProduct = productRepo.findById(new ObjectId(prodId));
        if(optProduct.isEmpty()) {
            throw new RuntimeException("Product id: " + prodId + "doesn't exist");
        }
        Optional<Category> optCategory = categoryRepo.findById(new ObjectId(categoryId));
        if(optCategory.isEmpty()) {
            throw new RuntimeException("Category id: " + categoryId + "doesn't exist");
        }
        Product productRec = optProduct.get();
        productRec.setCategory(optCategory.get());
        productRepo.save(productRec);
        return "{" +
                "\"message\":"+"Successfully linked product with category id"+
                "\"data\": "+productRec+
                "}";
    }

    // delete product by id
    public String deleteProductById(ObjectId id) {
        Optional<Product> optProduct = productRepo.findById(id);
        if(optProduct.isEmpty()) {
            throw new RuntimeException("Product id " + id + " doesn't exist");
        }
        productRepo.deleteById(id);
        return "{" +
                "\"message\":"+"Successfully deleted product\",\n"+
                "\"id\":"+id+",\n"+
                "}";
    }


}

