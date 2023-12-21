package com.example.springbootecommerce.Model;

public class PayloadValidation {
    public static boolean payloadValCat(Category category) {
        if(category.getId() != null) {
            return false;
        }
        return true;
    }

    public static boolean payloadValProd(Product product) {
        if(product.getId() != null) {
            return false;
        }
        return true;
    }
}
