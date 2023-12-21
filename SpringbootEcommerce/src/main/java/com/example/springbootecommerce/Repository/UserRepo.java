package com.example.springbootecommerce.Repository;

import com.example.springbootecommerce.Model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {

    //SELECT * FROM user WHERE email = :email
    @Query("{username: \"?0\"}")
    List<User> getUserByUsername(String username);

    //SELECT * FROM user WHERE id = :id
    @Query("{id:?0}")
    Optional<User> getUserById(ObjectId id);

}

