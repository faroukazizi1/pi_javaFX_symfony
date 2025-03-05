package Service;

import java.util.List;
import Model.user;
public interface IService<T> {

    void add(T t);
    void update(T t);
    void delete(T t);
    List<T> getAll();
    public boolean authenticateUser(String username, String password);
    public user HetUser(String email);
    List<T> getPromotionsByUserId(int id);
    public int getUserIdByEmail(String email);
    public void modifyPassword(int userId, String newPassword);
}
