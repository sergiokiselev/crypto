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
        System.out.println(phermaTest(n, 100));
        System.out.println("Тест Миллера Рабина");
        System.out.println(millerRabinTest(new int[] {3,2}));
    }



    public static int[] binpow (int[] a, int[] n) {
        int[] res = {1};
        boolean stop = true;
        while (!(n.length == 1 && n[0] == 0 || n.length == 2 && n[0] == 0 && n[1] == 0) ) {
            stop = true;
            if (n[0] % 2 == 1) {
                res = multiply(res, a);
                n[0]--;
            } else {
                a = multiply(a, a);
                n =  divideByTwo(n);
            }
            for (int elem : n) {
                if (elem != 0) {
                    stop = false;
                }
            }
            if (stop) {
                break;
            }
        }
        return res;
    }

    public static boolean millerRabinTest(BigInt n) {
        BigInt bufN = n.copy();
        int[] bufN1 = n.copy();
        bufN1[0]--;
        n.[0] -= 1;
        int s = 0;
        while(n[0] % 2 == 0) {
            n = n.divideByTwo();
            s++;
        }
        int[] a = BigInt.getRandom(bufN);
        int[] pow1 = binpow(a, n);
        int[] y = mod(pow1, bufN);
        if (!equals(y, new int[]{1}) && !equals(y, bufN1)) {
            int j = 1;
            for (j =1; j <= s-1;j++) {
                int[] pow = binpow(y, new int[]{2});
                y = mod(pow, bufN);
                if (equals(y, new int[]{1})) {
                    return false;
                }
            }
        }
        if (!equals(y, bufN1)) {
            return false;
        }
        return true;
    }

    public static boolean phermaTest(int[] n, int count) {
        int[] tempN = n.clone();
        if (n[0] > 0) {
            n[0]--;
        } else {
            n[1]--;
            n[0] = 9;
        }
        boolean global = false;
        for (int j = 0 ; j< count;j++) {
            int[] a = BigInt.getRandom(tempN);
            String output = "";
            for (int k = a.length - 1; k >= 0; k--) {
                output+=a[k];
            }
            System.out.println("A" + j + " = " + output);
            int[] pow = binpow(a, n);
            int[] r = mod(pow, tempN);
            boolean result = true;
            for (int i = 1; i < r.length; i++) {
                if (r[i]!=0) {
                    result = false;
                }
            }
            result = r[0] == 1;
            System.out.println(result);
            global = result;
        }
        return global;
    }
}
