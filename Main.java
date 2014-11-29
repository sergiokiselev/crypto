import org.omg.DynamicAny._DynStructStub;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Sergio on 10/24/14.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String size = scanner.next();
        long sizeLong = Integer.parseInt(size);
        int[] n = NumberGenerator.generate(sizeLong);
        NumberGenerator.printBigNumber(n);
        System.out.println();
        System.out.println("Тест Ферма:");
        System.out.println(phermaTest(new BigInt(n), 100));
        System.out.println("Тест Миллера Рабина");
        System.out.println(millerRabinTest(new BigInt(new int[] {3,2})));
    }

    public static boolean millerRabinTest(BigInt n) {
        BigInt bufN = n.copy();
        BigInt bufN1 = n.copy();
        bufN1.number[0]--;
        n.number[0] -= 1;
        int s = 0;
        while(n.number[0] % 2 == 0) {
            n = n.divideByTwo();
            s++;
        }
        BigInt a = BigInt.getRandom(bufN);
        BigInt pow1 = a.pow(n);
        BigInt y = pow1.mod(bufN);
        if (!y.equals(new int[]{1}) && !y.equals(bufN1)) {
            int j = 1;
            for (j =1; j <= s-1;j++) {
                BigInt pow = y.pow(new BigInt(new int[]{2}));
                y = pow.mod(bufN);
                if (y.equals(new int[]{1})) {
                    return false;
                }
            }
        }
        if (!y.equals(bufN1)) {
            return false;
        }
        return true;
    }

    public static boolean phermaTest(BigInt n, int count) {
        BigInt tempN = n.copy();
        if (n.number[0] > 0) {
            n.number[0]--;
        } else {
            n.number[1]--;
            n.number[0] = 9;
        }
        boolean global = false;
        for (int j = 0 ; j< count;j++) {
            BigInt a = BigInt.getRandom(tempN);
            String output = "";
            for (int k = a.getLength() - 1; k >= 0; k--) {
                output+=a.number[k];
            }
            System.out.println("A" + j + " = " + output);
            BigInt pow = a.pow(n);
            BigInt r = pow.mod(tempN);
            boolean result = true;
            for (int i = 1; i < r.getLength(); i++) {
                if (r.number[i]!=0) {
                    result = false;
                }
            }
            result = r.number[0] == 1;
            System.out.println(result);
            global = result;
        }
        return global;
    }
}
