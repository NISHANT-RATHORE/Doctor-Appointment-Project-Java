package com.example.userservice.Repository;

import com.example.userservice.Model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId> {
    User findByEmail(String email);
    User deleteByEmail(String email);
    User findByName(String name);
}
