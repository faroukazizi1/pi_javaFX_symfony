package Service;

import models.absence;

import java.util.List;

public interface IService <T>{
    void add(T t);

    void update(T t);
    void delete(T t);
    List<T> getAll();
    List<T> getPromotionsByUserId(int id);
    public boolean authenticateUser(String username, String password);
}
