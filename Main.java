import org.omg.DynamicAny._DynStructStub;
import sun.security.util.BigInt;

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

    public  static int[] divideByTwo(int [] n) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < n.length; i++) {
            int t = 1;
            long buffer = n[i];
            if (i < n.length - 1) {
                while (n[i+1] % 2 == 1) {
                    buffer+=Math.pow(10, t) * n[i+1];
                    t++;
                    i++;
                    if (i == n.length -1) {
                        break;
                    }
                }
            }
            buffer/=2;
            while (buffer > 10) {
                int k = (int)buffer%10;
                result.add(k);
                buffer/=10;
            }
            result.add((int)buffer);
        }
        int[] res = new int[result.size()];
        for (int i = 0; i < result.size();i++) {
            res[i]=result.get(i);
        }
        return res;
    }

    public static int[] multiply(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        for (int i=0; i<a.length; ++i)
            for (int j=0, carry=0; j<(int)b.length || carry >0; ++j) {
                int cur = c[i+j] + a[i] *  (j < (int)b.length ? b[j] : 0) + carry;
                c[i+j] =  (cur % 10);
                carry = (cur / 10);
            }
        return c;
    }

    public static boolean millerRabinTest(int[] n) {
        int[] bufN = n.clone();
        int[] bufN1 = n.clone();
        bufN1[0]--;
        n[0] -= 1;
        int s = 0;
        while(n[0] % 2 == 0) {
            n = divideByTwo(n);
            s++;
        }
        int[] a = getRandom(bufN);
        int[] pow1 = binpow(a, n);
        int[] y = mod(pow1, bufN);
        if (!equal(y, new int[]{1}) && !equal(y, bufN1)) {
            int j = 1;
            for (j =1; j <= s-1;j++) {
                int[] pow = binpow(y, new int[]{2});
                y = mod(pow, bufN);
                if (equal(y, new int[]{1})) {
                    return false;
                }
            }
        }
        if (!equal(y, bufN1)) {
            return false;
        }
        return true;
    }

    public static boolean equal(int[] a, int[] b) {
        boolean first;
        int size;
        if (a.length > b.length) {
            first = false;
            size = b.length;
        } else {
            first = true;
            size = a.length;
        }
        boolean y1 = true;
        for (int i = 0; i < size;i++) {
            if (a[i] != b[i]) {
                y1 = false;
                break;
            }
        }
        if (y1) {
            if (first) {
                for (int i = size; i < a.length; i++) {
                    if (a[i] != 0) {
                        y1 = false;
                        break;
                    }
                }
            } else {
                for (int  i = size; i < b.length; i++) {
                    if (b[i] != 0) {
                        y1 = false;
                        break;
                    }
                }
            }
        }
        return y1;
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
            int[] a = getRandom(tempN);
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

    public static int[] getRandom(int[] number) {
        int start = number.length - 1;
        while(number[start] == 0) {
            start--;
            if (start == 0)
                break;
        }
        if (start==0) {
            return new int[] {(new Random()).nextInt(number[0])};
        }
        int randSize = (new Random()).nextInt(start + 1);
        if (randSize == 0)
            randSize = 1;
        int[] result = new int[randSize];
        for (int i = 0; i < result.length;i++) {
            result[i] = (new Random()).nextInt(10);
        }
        return result;
    }

    public static int[] divide(int[] a, int[] b) {
        byte[] revA = new byte[a.length];
        byte[] revB = new byte[b.length];
        for (int i = a.length - 1; i >= 0;i--) {
            revA[a.length - i - 1] = (byte)a[i];
        }
        for (int i = b.length - 1; i >= 0;i--) {
            revB[b.length - i - 1] = (byte)b[i];
        }
        BigInteger bigIntA = new BigInteger(revA);
        BigInteger bigIntB = new BigInteger(revB);
        BigInteger g = bigIntA.divide(bigIntB);
        byte[] result = g.toByteArray();
        int[] revResult = new int[result.length];
        for (int i = result.length - 1; i >= 0; i--) {
            revResult[result.length - i - 1] = result[i];
        }
        return revResult;
    }

    public static int[] mod(int[] a, int[] b) {

        int start = a.length - 1;
        while(a[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revA = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revA[start - i] = (byte)a[i];
        }
        start = b.length - 1;
        while(b[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revB = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revB[start - i] = (byte)b[i];
        }
        String aStr = "";
        for (byte aa : revA) {
            aStr+=aa;
        }
        String bStr = "";
        for (byte aa : revB) {
            bStr+=aa;
        }
        BigInteger bigIntA = new BigInteger(aStr);
        BigInteger bigIntB = new BigInteger(bStr);
        BigInteger g = bigIntA.mod(bigIntB);
        String result = g.toString();
        int[] revResult = new int[result.length()];
        for (int i = 0; i < result.length();i++) {
            char sym = result.charAt(i);
            int intSym = Integer.parseInt(Character.toString(sym));
            revResult[result.length() - i - 1] = intSym;
        }
        return revResult;
    }
}
