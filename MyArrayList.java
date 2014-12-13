import java.util.ArrayList;

/**
 * Created by Sergio on 12/7/2014.
 */
public class MyArrayList extends ArrayList<Integer> {
    public String toString() {
        String result = "";
        for (int elem : this) {
            result += elem;
        }
        return result;
    }
}
