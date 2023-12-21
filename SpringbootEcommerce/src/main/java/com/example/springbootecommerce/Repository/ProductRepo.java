package com.example.springbootecommerce.Repository;

import com.example.springbootecommerce.Model.Category;
import com.example.springbootecommerce.Model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, ObjectId> {

    @Query("{id: \"?0\"}")
    List<Product> findByID(ObjectId id);
    @Query("{name: \"?0\"}")
    List<Product> findByName(String name);

    @Query("{description: \"?0\"}")
    List<Product> findByDesc(String description);

    @Query("{price: ?0}")
    List<Product> findByPrice(int price);
}
