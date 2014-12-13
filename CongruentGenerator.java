import java.util.*;

public class CongruentGenerator {

    public static ArrayList<Double> generate(double c, double M, double a, long t) {
        ArrayList<Double> result = new ArrayList<Double>();
        double x0 = new Random().nextDouble();
        double x;
        result.add(new Random().nextDouble());
        int i = 1;
        while(i < t) {
            x = (a * x0 + c) % M;
            x0 = x;
            result.add(x / M);
            i++;
        }
        return result;
    }
}
