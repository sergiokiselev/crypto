

import com.sun.imageio.plugins.common.BitFile;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Sergio on 10/24/14.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //String size = scanner.next();
        long sizeLong = 100;//Integer.parseInt(size);
        int[] n = NumberGenerator.generate(sizeLong);
        if (n[0] == 0) {
            n[0] = 1;
        }
        NumberGenerator.printBigNumber(n);
        BigInt bigInt = new BigInt(n);
        normalize(bigInt, 10);
        BigInt b2 = bigInt.clone();
        normalize(b2, 10);
        System.out.println();
        System.out.println("Тест Ферма:");
        System.out.println(phermaTest(bigInt.clone(), 100));
        System.out.println("Тест Миллера Рабина");
        System.out.println(millerRabinTest(b2.clone(), 100));
    }

    public static boolean millerRabinTest(BigInt n, int rounds) {
        BigInt bufN = n.clone();
        BigInt bufN1 = n.clone();
        BigInt t = n.clone();
        bufN1.digits[0]--;
        n.digits[0] -= 1;
        t.digits[0] -= 1;
        int s = 0;
        while(t.digits[0] % 2 == 0) {
            t = divideByTwo(t);
            s++;
        }
        for (int i = 0; i < rounds;i++) {
            BigInt a = getRandom(bufN);
            BigInt pow1 = binpow(a, t.clone(), bufN.clone());
            BigInt y = mod(pow1, bufN);
            BigInt bigInt = new BigInt(new int[]{1});
            normalize(bigInt, 2);
            if (equals(y, bigInt) || equals(y, bufN1)) {
                continue;
            }
            int j = 1;
            for (j = 1; j <= s - 1; j++) {
                BigInt p2 = new BigInt(new int[]{2});
                normalize(p2, 2);
                BigInt pow = binpow(y, p2, bufN.clone());
                y = mod(pow, bufN);
                BigInt p1 = new BigInt(new int[]{1});
                normalize(p1, 2);
                if (equals(y, p1)) {
                    System.out.println(false);
                }
                if (equals(y, bufN1)) break;
            }
            if (equals(y, bufN1)) continue;
            return false;
        }
        return true;
    }

    public static BigInt minus(BigInt a, BigInt b) {
        BigInt res = a.clone();
        int r = 0;
        for (int i = 0;i<res.length;i++)
        {
            res.digits[i] -= b.digits[i] + r;
            if (res.digits[i]<0)
            {
                res.digits[i]+=10;
                res.digits[i+1]--;
            }
        }
        int pos = res.length;
        while (pos != 0 && res.digits[pos] == 0)
            pos--;
        res.length = pos+1;
        return res;
    }



    public static BigInt binpow (BigInt a, BigInt n, BigInt m) {
        BigInt b = new BigInt(new int[]{1});
        normalize(b, 2);
        while (n.length !=0) {
            if (n.digits[0] % 2 == 1) {
                BigInt p1 = new BigInt(new int[]{1});
                normalize(p1, 2);
                n = minus(n, p1);
                b = multiply(b, a);
                b = mod(b, m);
            } else {
                a = multiply(a, a);
                a = mod(a, m);
                n =  divideByTwo(n);
            }
        }
        return b;
    }

    public  static BigInt divideByTwo(BigInt n) {
        MyArrayList result = new MyArrayList();
        for (int i = 0; i < n.length; i++) {
            int t = 1;
            long buffer = n.digits[i];
            if (i < n.length - 1) {
                while (n.digits[i+1] % 2 == 1) {
                    buffer+=Math.pow(10, t) * n.digits[i+1];
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
        BigInt res = new BigInt(new int[result.size()]);
        for (int i = 0; i < result.size();i++) {
            res.digits[i]=result.get(i);
        }
        normalize(res, n.length);
        return res;
    }

    public static void normalize(BigInt bigInt, int len) {
        for (int i = len; i >= 0;i--) {
            if (bigInt.digits[i] != 0) {
                bigInt.length = i+1;
                break;
            }
        }
    }

    public static BigInt multiply(BigInt a, BigInt b) {
        BigInt c = new BigInt(new int[a.length + b.length]);
        for (int i=0; i<a.length; ++i)
            for (int j=0, carry=0; j<(int)b.length || carry >0; ++j) {
                int cur = c.digits[i+j] + a.digits[i] *  (j < (int)b.length ? b.digits[j] : 0) + carry;
                c.digits[i+j] =  (cur % 10);
                carry = (cur / 10);
            }
        normalize(c, a.length + b.length + 1);
        return c;
    }



    public static boolean equals(BigInt a, BigInt b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a.digits[i] != b.digits[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean phermaTest(BigInt n, int count) {
        BigInt tempN = n.clone();
        if (n.digits[0] > 0) {
            n.digits[0]--;
        } else {
            n.digits[1]--;
            n.digits[0] = 9;
        }
        boolean global = false;
        for (int j = 0 ; j< count;j++) {
            BigInt a = getRandom(tempN);
            normalize(a, tempN.length);
            String output = "";
            for (int k = a.length - 1; k >= 0; k--) {
                output+=a.digits[k];
            }
            System.out.println("A" + j + " = " + output);
            BigInt pow = binpow(a, n, tempN.clone());
            BigInt r = mod(pow, tempN.clone());
            boolean result = true;
            for (int i = 1; i < r.length; i++) {
                if (r.digits[i]!=0) {
                    result = false;
                }
            }
            result = r.digits[0] == 1;
            if (!result) {
                return result;
            }
            global = result;
        }
        return global;
    }

    public static BigInt getRandom(BigInt number) {
        int start = number.length - 1;
        while(number.digits[start] == 0) {
            start--;
            if (start == 0)
                break;
        }
        if (start==0) {
            int random = (new Random().nextInt(number.digits[0]));
            if (random == 0) {
                random = 1;
            }
            return new BigInt(new int[] {random});
        }
        int randSize = (new Random()).nextInt(start + 1);
        if (randSize == 0)
            randSize = 1;
        BigInt result = new BigInt(new int[randSize]);
        result.length = randSize;
        for (int i = 0; i < randSize;i++) {
            int random = (new Random().nextInt(10));
            if (random == 0) {
                random = 1;
            }
            result.digits[i] = random;
        }
        return result;
    }

    public static BigInt mod(BigInt a, BigInt b) {

        int start = a.length - 1;
        while(a.digits[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revA = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revA[start - i] = (byte)a.digits[i];
        }
        start = b.length - 1;
        while(b.digits[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revB = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revB[start - i] = (byte)b.digits[i];
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
        BigInt revResult = new BigInt(new int[result.length()]);
        for (int i = 0; i < result.length();i++) {
            char sym = result.charAt(i);
            int intSym = Integer.parseInt(Character.toString(sym));
            revResult.digits[result.length() - i - 1] = intSym;
        }
        normalize(revResult, result.length());
        return revResult;
    }

//    public static BigInt mod2(BigInt a, BigInt b) {
//        BigInt res = new BigInt();
//        BigInt curValue = new BigInt();
//        for (int i = a.length-1; i>=0; i--) {
//            curValue.length++;
//            curValue.digits[0] = a.digits[i];
//            // подбираем максимальное число x, такое что b * x <= curValue
//            int x = 0;
//            int l = 0, r = 10;
//            while (l <= r) {
//                int m = (l + r) >> 1;
//                BigInt cur = multiply(b, m);
//                if (less(cur, curValue) || equals(cur, curValue)) {
//                    x = m;
//                    l = m+1;
//                }
//                else
//                    r = m-1;
//            }
//            res.digits[i] = x;
//            curValue = minus(curValue - multiply(b, x));
//        }
//        return curValue;
//    }

}


