import java.io.*;
import java.util.InputMismatchException;

// calculate the max sum of subarray in a range.
public class Acwing246_SegmentTree_diff_gcd implements Runnable
{
    @Override
    public void run() {
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
        String[] cur = in.nextLine().split(" ");
        int n = Integer.parseInt(cur[0]), m = Integer.parseInt(cur[1]);
        cur = in.nextLine().split(" ");
        for (int i = 1; i <= n; i++) {
            nums[i] = Long.parseLong(cur[i - 1]);
        }
        for (int i = 1; i <= n; i++) {
            b[i] = nums[i] - nums[i - 1];
        }
        build(1, 1, n);
        for (int i = 0; i < m; i++) {
            cur = in.nextLine().split(" ");
            char op = cur[0].charAt(0);
            int l = Integer.parseInt(cur[1]);
            int r = Integer.parseInt(cur[2]);
            if (op == 'Q') {
                long res = query(1, l, r);
                w.println(res);
            }
            else {
                long val = Long.parseLong(cur[3]);
                modify(1, l, val);
                if (r + 1 <= n) {
                    modify(1, r + 1, -val);
                }
            }
        }

        w.flush();
        w.close();
    }


    static int N = 500050;
    static Node[] tr = new Node[4 * N];
    static long[] nums = new long[N];
    static long[] b = new long[N];

    static class Node {
        int left, right;
        long sum, gcd;
        public Node(int l, int r, long s, long g) {
            left = l;
            right = r;
            sum = s;
            gcd = g;
        }
    }

    static void build(int root, int start, int end) {
        tr[root] = new Node(start, end, 0, 0);
        if (start >= end) {
            tr[root].sum = b[start];
            tr[root].gcd = b[start];
            return;
        }
        int mid = start + end >> 1;
        build(root << 1, start, mid);
        build(root << 1 | 1, mid + 1, end);
        pushup(root);
    }

    static void modify(int root, int index, long val) {
        if (tr[root].left == index && tr[root].right == index) {
            tr[root].sum += val;
            tr[root].gcd += val;
            return;
        }
        int mid = tr[root].left + tr[root].right >> 1;
        if (index <= mid) modify(root << 1, index, val);
        else modify(root << 1 | 1, index, val);
        pushup(root);
    }

    static void pushup(int root) {
        tr[root].gcd = gcd(tr[root << 1].gcd, tr[root << 1 | 1].gcd);
        tr[root].sum = tr[root << 1].sum + tr[root << 1 | 1].sum;
    }

    private  static long gcd(long x, long y) {
        return y == 0 ? x : gcd(y, x % y);
    }


    static long query(int root, int start, int end) {
        long num = sum(1, 1, start);
        if (start == end) return num;
        long gcd = getGcd(root, start + 1, end);
        return Math.abs(gcd(num, gcd));
    }

    static long getGcd(int root, int start, int end) {
        if (tr[root].left >= start && tr[root].right <= end) return tr[root].gcd;
        int mid = tr[root].left + tr[root].right >> 1;
        if (mid >= end) return getGcd(root << 1, start, end);
        if (mid < start) return getGcd(root << 1 | 1, start, end);

        long left = getGcd(root << 1, start, mid);
        long right = getGcd(root << 1 | 1, mid + 1, end);

        return gcd(left, right);
    }

    static long sum(int root, int start, int end) {
        if (tr[root].left >= 1 && tr[root].right <= end) return tr[root].sum;
        int mid = tr[root].left + tr[root].right >> 1;
        long res = 0;
        if (start <= mid) res += sum(root << 1, start,  end);
        if (end > mid) res += sum(root << 1 | 1, start, end);
        return res;
    }


    static class InputReader
    {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private SpaceCharFilter filter;
        private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        public InputReader(InputStream stream)
        {
            this.stream = stream;
        }

        public int read()
        {
            if (numChars==-1)
                throw new InputMismatchException();

            if (curChar >= numChars)
            {
                curChar = 0;
                try
                {
                    numChars = stream.read(buf);
                }
                catch (IOException e)
                {
                    throw new InputMismatchException();
                }

                if(numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public String nextLine()
        {
            String str = "";
            try
            {
                str = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return str;
        }
        public int nextInt()
        {
            int c = read();

            while(isSpaceChar(c))
                c = read();

            int sgn = 1;

            if (c == '-')
            {
                sgn = -1;
                c = read();
            }

            int res = 0;
            do
            {
                if(c<'0'||c>'9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));

            return res * sgn;
        }

        public long nextLong()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-')
            {
                sgn = -1;
                c = read();
            }
            long res = 0;

            do
            {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));
            return res * sgn;
        }

        public double nextDouble()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-')
            {
                sgn = -1;
                c = read();
            }
            double res = 0;
            while (!isSpaceChar(c) && c != '.')
            {
                if (c == 'e' || c == 'E')
                    return res * Math.pow(10, nextInt());
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            if (c == '.')
            {
                c = read();
                double m = 1;
                while (!isSpaceChar(c))
                {
                    if (c == 'e' || c == 'E')
                        return res * Math.pow(10, nextInt());
                    if (c < '0' || c > '9')
                        throw new InputMismatchException();
                    m /= 10;
                    res += (c - '0') * m;
                    c = read();
                }
            }
            return res * sgn;
        }

        public String readString()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do
            {
                res.appendCodePoint(c);
                c = read();
            }
            while (!isSpaceChar(c));

            return res.toString();
        }

        public boolean isSpaceChar(int c)
        {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next()
        {
            return readString();
        }

        public interface SpaceCharFilter
        {
            public boolean isSpaceChar(int ch);
        }
    }

    public static void main(String args[]) throws Exception
    {
        new Thread(null, new Acwing246_SegmentTree_diff_gcd(),"Main",1<<27).start();
    }

}
