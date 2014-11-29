import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sergio on 11/29/2014.
 */
public class BigInt {

    public int[] number;

    public BigInt(int size) {
        this.number = new int[size];
    }

    public BigInt(int[] number) {
        this.number = number;
    }

    public BigInt() {
        this.number = new int[] {1};
    }

    private BigInt(BigInt bigInt) {
        this.number = bigInt.number;
    }

    public int getLength() {
        return number.length;
    }

    public BigInt multiply(BigInt b) {
        int[] c = new int[this.number.length + b.getLength()];
        for (int i=0; i<this.number.length; ++i)
            for (int j=0, carry=0; j<(int)b.getLength() || carry >0; ++j) {
                int cur = c[i+j] + this.number[i] *  (j < (int)b.getLength() ? b.number[j] : 0) + carry;
                c[i+j] =  (cur % 10);
                carry = (cur / 10);
            }
        return new BigInt(c);
    }

    public BigInt pow(BigInt n) {
        BigInt res = new BigInt();
        boolean stop = true;
        while (!(n.getLength() == 1 && n.number[0] == 0 || n.getLength() == 2 && n.number[0] == 0 && n.number[1] == 0) ) {
            stop = true;
            if (n.number[0] % 2 == 1) {
                res = multiply(this);
                n.number[0]--;
            } else {
                this.set(this.multiply(this));
                n =  this.divideByTwo();
            }
            for (int elem : n.number) {
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

    private void set(BigInt multiply) {
        this.number = multiply.number;
    }

    public BigInt divideByTwo() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < this.getLength(); i++) {
            int t = 1;
            long buffer = number[i];
            if (i < number.length - 1) {
                while (number[i+1] % 2 == 1) {
                    buffer+=Math.pow(10, t) * number[i+1];
                    t++;
                    i++;
                    if (i == number.length -1) {
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
        BigInt res = new BigInt(result.size());
        for (int i = 0; i < result.size();i++) {
            res.number[i] = result.get(i);
        }
        return res;
    }

    public boolean equals(BigInt b) {
        boolean first;
        int size;
        if (this.getLength() > b.getLength()) {
            first = false;
            size = b.getLength();
        } else {
            first = true;
            size = this.getLength();
        }
        boolean y1 = true;
        for (int i = 0; i < size;i++) {
            if (this.number[i] != b.number[i]) {
                y1 = false;
                break;
            }
        }
        if (y1) {
            if (first) {
                for (int i = size; i < this.getLength(); i++) {
                    if (this.number[i] != 0) {
                        y1 = false;
                        break;
                    }
                }
            } else {
                for (int  i = size; i < b.getLength(); i++) {
                    if (b.number[i] != 0) {
                        y1 = false;
                        break;
                    }
                }
            }
        }
        return y1;
    }

    public static BigInt getRandom(BigInt number) {
        int start = number.getLength() - 1;
        while(number.number[start] == 0) {
            start--;
            if (start == 0)
                break;
        }
        if (start==0) {
            return new BigInt(new Random().nextInt(number.number[0]));
        }
        int randSize = (new Random()).nextInt(start + 1);
        if (randSize == 0)
            randSize = 1;
        BigInt result = new BigInt(randSize);
        for (int i = 0; i < result.getLength(); i++) {
            result.number[i] = (new Random()).nextInt(10);
        }
        return result;
    }

    public BigInt mod(BigInt b) {

        int start = this.getLength() - 1;
        while(this.number[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revA = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revA[start - i] = (byte)this.number[i];
        }
        start = b.getLength() - 1;
        while(b.number[start] == 0) {
            start--;
            if (start==-1)
                break;
        }
        byte[] revB = new byte[start+1];
        for (int i = start; i >= 0;i--) {
            revB[start - i] = (byte)b.number[i];
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
        BigInt revResult = new BigInt(result.length());
        for (int i = 0; i < result.length();i++) {
            char sym = result.charAt(i);
            int intSym = Integer.parseInt(Character.toString(sym));
            revResult.number[result.length() - i - 1] = intSym;
        }
        return revResult;
    }

    public BigInt divide(BigInt b) {
        byte[] revA = new byte[this.getLength()];
        byte[] revB = new byte[this.getLength()];
        for (int i = this.getLength() - 1; i >= 0;i--) {
            revA[this.getLength() - i - 1] = (byte)this.number[i];
        }
        for (int i = b.getLength() - 1; i >= 0;i--) {
            revB[b.getLength() - i - 1] = (byte)b.number[i];
        }
        BigInteger bigIntA = new BigInteger(revA);
        BigInteger bigIntB = new BigInteger(revB);
        BigInteger g = bigIntA.divide(bigIntB);
        byte[] result = g.toByteArray();
        BigInt revResult = new BigInt(result.length);
        for (int i = result.length - 1; i >= 0; i--) {
            revResult.number[result.length - i - 1] = result[i];
        }
        return revResult;
    }

    public BigInt copy() {
        return new BigInt(this);
    }
}
