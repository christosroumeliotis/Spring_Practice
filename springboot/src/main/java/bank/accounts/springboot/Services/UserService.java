package bank.accounts.springboot.Services;

import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<String> createNewUser(User user){

        if(user.getUsername()== null || user.getPassword()==null){
            throw new RuntimeException("Username and Password cannot be null!");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //Encrypt the password
        if(!user.getRole().equals("ROLE_ADMIN")){
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body("User with username: " + user.getUsername() + " created and has Id: " + user.getId());

    }

    public ResponseEntity<String> updateUser(Long id, User user) {

        if(user.getUsername()== null || user.getPassword()==null){
            throw new RuntimeException("Username and Password cannot be null!");
        }

        Optional<User> searchedUser = userRepository.findById(id);
        if(searchedUser.isPresent()){

            User getUser = searchedUser.get();
            getUser.setUsername(user.getUsername());
            getUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //Encrypt the password
            userRepository.save(getUser);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User with id: " + id + " updated.");
        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id: " + id + " not found!");
        }
    }

    public ResponseEntity<String> deleteUser(Long id) {

        Optional<User> searchedUser = userRepository.findById(id);
        if(searchedUser.isPresent()){

            User getUser = searchedUser.get();
            userRepository.delete(getUser);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User with id: " + id + " deleted.");
        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id: " + id + " not found!");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

