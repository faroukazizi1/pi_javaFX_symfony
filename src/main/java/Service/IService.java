package Service;

import java.util.List;
import Model.user;
public interface IService<T> {

    void add(T t);
    void update(T t);
    void delete(T t);
    List<T> getAll();
}
