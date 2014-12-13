class BigInt {
    public static final int MAX_SIZE = 1000000;
    public int[] digits = new int[MAX_SIZE];
    public int length;

    public BigInt(int[] digits) {
        System.arraycopy(digits, 0, this.digits, 0, digits.length);
        for (int i = digits.length; i>= 0;i--) {
            if (this.digits[i] != 0) {
                this.length = i;
                break;
            }
        }
    }

    public BigInt(){}

    public String toString() {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += digits[i];
        }
        return result;
    }

    public BigInt clone() {
        BigInt bigInt = new BigInt();
        System.arraycopy(this.digits, 0, bigInt.digits, 0, length);
        bigInt.length = length;
        return bigInt;
    }
}