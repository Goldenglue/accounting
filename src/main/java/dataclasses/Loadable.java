package dataclasses;


import java.util.List;

public interface Loadable {
    List<?> loadFromDatabase(String s);
}
