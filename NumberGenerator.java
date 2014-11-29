import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergio on 10/31/14.
 */
public class NumberGenerator {
    public static int[] generate(long sizeLong) {
        List<Double> lineGen2 = CongruentGenerator.generate(8121, 134456, 8121, sizeLong);
        List<Integer> number = new ArrayList<Integer>();
        for (int i = 0; i < lineGen2.size(); i++) {
            if (lineGen2.get(i) < 0.5) {
                number.add(0);
            } else {
                number.add(1);
            }
            System.out.print(number.get(i));
        }
        System.out.println();
        int[] n = new int[(int)Math.ceil(number.size() / lg(10)) + 1];
        int p = 0;
        for (int i = 0; i < number.size(); i++)
        {
            for (int j = 0; j < n.length; j++)
                n[j] <<= 1;

            if (number.get(i) == 1) n[0]++;

            for (int j = 0; j <= p; j++)
            {
                n[j + 1] += n[j] / 10;
                n[j] = n[j] % 10;
            }
            if (n[p + 1] > 0) p++;
        }
        return n;
    }

    public static void printBigNumber(int[] bigNumber) {
        int start = bigNumber.length - 1;
        while(bigNumber[start] == 0) {
            start--;
        }
        for (int i = start; i >= 0; i--) {
            System.out.print(bigNumber[i]);
        }
    }

    private static double lg(double x) {
        return Math.log(x)/Math.log(2.0);
    }
}
