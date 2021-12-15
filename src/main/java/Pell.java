public class Pell {
    public Pell() {
    }

    public long get(int n) {
        if (n < 0) throw new UnsupportedOperationException("Pell.get is not supported for negative n");
        else if (n<=2)
            return n;
        long[] pellNum;
        pellNum = new long[n+1];
        pellNum[0] = 0;
        pellNum[1] = 1;
        for(int i=2;i<=n;i++) {
            pellNum[i] = 2*pellNum[i-1] + pellNum[i-2];
        }
        return pellNum[n];
    }
}
