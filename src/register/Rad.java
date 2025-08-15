package register;

import java.util.ArrayList;

public class Rad {

    ArrayList<String> attribut = new ArrayList<>();

    public void setAttribut(int index, String varde) {
        attribut.set(index, varde);
    }

    public void addAttribut(String varde) {
        attribut.add(varde);
    }

    public String getAttribut(int index) {
        return attribut.get(index);
    }
}