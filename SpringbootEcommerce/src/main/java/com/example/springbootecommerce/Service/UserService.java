package com.example.springbootecommerce.Service;

import com.example.springbootecommerce.Model.User;
import com.example.springbootecommerce.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private TokenService tokenService;
    @Autowired
    public UserService(UserRepo userRepo, TokenService tokenService) {
        this.userRepo = userRepo;
        this.tokenService = tokenService;
    }

    // list users
    public List<User> getUsers() {
        List<User> getUsers = userRepo.findAll();
        return getUsers;
    }

    // Signup a user
    public String signup(String username, String password1, String password2, String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword1(password1);
        user.setPassword2(password2);  // password2 is basically a backup password
        user.setEmail(email);

        User savedUser = userRepo.save(user);
        return "{" +
                "\"message\":"+"Successfully created user\",\n"+
                "\"data\":"+savedUser+",\n"+
                "}";
    }

    // login a user
    public String login(String username, String password) {
        List<User> foundUsers = userRepo.getUserByUsername(username);
        if(foundUsers.isEmpty()) {
            return "Authentication failed: User not found";
        } else if (!foundUsers.get(0).getPassword1().equals(password) &&
                !foundUsers.get(0).getPassword2().equals(password)) {
            return "Password incorrect";
        }
        return "{\n" +
                "\"message\":"+"\" Successfully Logged-in\",\n"+
                "\"data\": {\n"+" Name : "+foundUsers.get(0).getUsername()+",\n"+
                "Email : "+foundUsers.get(0).getEmail()+"\n"+
                "\"token\":\""+tokenService.createToken(foundUsers.get(0).getId())+"\"" +
                "}";
    }

}

