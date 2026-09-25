package EasyTask;

public class PrimeBitCounter {

    private static boolean isPrime(int n) {
        if (n < 2)
            return false;

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0)
                return false;

        }
        return true;
    }

    public int[] countPrimeBits(int n){
        int res = 0;
        int primeNumber = 0;
        int count;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                count = Integer.bitCount(i);
                if (count >= res){
                    res = count;
                    primeNumber = i;
                }
            }
        }
        return new int[]{res, primeNumber};
    }

}
