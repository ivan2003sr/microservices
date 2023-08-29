package com.ivan.rest.webservices.restfulwebservices;

import com.ivan.rest.webservices.restfulwebservices.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static int userCount = 0;

    static {
        users.add(new User(++userCount,"Ivan", LocalDate.now().minusYears(35)));
        users.add(new User(++userCount,"Eve", LocalDate.now().minusYears(33)));
        users.add(new User(++userCount,"Vale", LocalDate.now().minusYears(6)));
        users.add(new User(++userCount,"Ailen", LocalDate.now().minusYears(2)));
    }

    public List<User> findAll(){
        return users;
    }

    public User findOne(int id){
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
    }

    public User save(User user){
        user.setId(++userCount);
        users.add(user);
        return user;
    }

    public void deleteById(int id){
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }

}
