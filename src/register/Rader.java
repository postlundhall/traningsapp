package register;

import java.util.ArrayList;
import java.util.List;

public class Rader {

    private final List<Rad> rader;

    public Rader() {
        rader = new ArrayList<>();
    }

    public void addRad(Rad rad) {
        rader.add(rad);
    }

    public void removeRad(int index) {
        rader.remove(index);
    }

    public Rad getRad(int index) {
        return rader.get(index);
    }

    public List<Rad> getArrayList() {
        return rader;
    }
}