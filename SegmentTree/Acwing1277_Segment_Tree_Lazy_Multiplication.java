package SegmentTree;

import java.io.*;
import java.util.InputMismatchException;


public class Acwing1277_Segment_Tree_Lazy_Multiplication implements Runnable
{
    @Override
    public void run() {
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);

        String[] cur = in.nextLine().split(" ");
        int n = Integer.parseInt(cur[0]);
        int p = Integer.parseInt(cur[1]);
        mod = p;

        cur = in.nextLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(cur[i - 1]);
        }
        build(1, 1, n);

        int m = Integer.parseInt(in.nextLine());
        for (int i = 0; i < m; i++) {
            cur = in.nextLine().split(" ");
            int op = Integer.parseInt(cur[0]);
            int l = Integer.parseInt(cur[1]);
            int r = Integer.parseInt(cur[2]);
            if (op == 1) modify(1, l, r, 0, Long.parseLong(cur[3]));
            else if (op == 2) modify(1, l, r, Long.parseLong(cur[3]), 1);
            else w.println(query(1, l, r));
        }

        w.flush();
        w.close();
    }

    static int N = 100010;
    static int mod;
    static int[] arr = new int[N];
    static Node[] tr = new Node[4 * N];

    static class Node {
        int left, right;
        long sum, add, mul;
        public Node(int l, int r, long s, long a, long m) {
            left = l;
            right = r;
            sum = s;
            add = a;
            mul = m;
        }
    }

    private static long query(int root, int left, int right) {
        // System.out.println("start: " + root + " " + left + " " + right + " " + tr[root].sum + " " + tr[root].add);
        if (tr[root].left >= left && tr[root].right <= right) return tr[root].sum;
        // before each query, if we need to query children, pushdown the lazy tag
        pushdown(root);
        int mid = tr[root].left + tr[root].right >> 1;
        // System.out.println(tr[root].left + " " + tr[root].right + " " + mid);
        long res = 0;
        if (left <= mid) res += query(root << 1, left, right);
        if (right > mid) res += query(root << 1 | 1, left, right);
        return res % mod;
    }

    private static void modify(int root, int left, int right, long val, long m) {
        // if we can put the lazy tag here, we do it!
        if (tr[root].left >= left && tr[root].right <= right) {
            tr[root].mul = tr[root].mul * m % mod;
            tr[root].sum = (tr[root].sum * m % mod) % mod;
            tr[root].add = tr[root].add * m % mod;
            tr[root].add = (tr[root].add + val) % mod;
            tr[root].sum = (tr[root].sum + (tr[root].right - tr[root].left + 1) * val) % mod;
            return;
        }
        // if we cannot, pushdown this lazy tag first to its children
        pushdown(root);
        int mid = tr[root].left + tr[root].right >> 1;
        if (left <= mid) modify(root << 1, left, right, val, m);
        if (right > mid) modify(root << 1 | 1, left, right, val, m);
        pushup(root);
    }

    private static void pushdown(int root) {
        Node cur = tr[root], l =  tr[root << 1], r = tr[root << 1 | 1];
        pushdown(cur, l);
        pushdown(cur, r);
        cur.add = 0;
        cur.mul = 1;
    }

    private static void pushdown(Node cur, Node child) {
        child.mul = (child.mul * cur.mul) % mod;
        child.add = child.add * cur.mul % mod;
        child.add = (child.add + cur.add) % mod;
        child.sum = (child.sum * cur.mul % mod + (child.right - child.left + 1) * cur.add) % mod;
    }

    private static void build(int root, int left, int right) {
        tr[root] = new Node(left, right, 0, 0, 1);
        if (left >= right)  {
            tr[root].sum = arr[left];
            return;
        }
        int mid = left + right >> 1;
        build(root << 1, left, mid);
        build(root << 1 | 1, mid + 1, right);
        pushup(root);
    }

    private static void pushup(int root) {
        tr[root].sum = (tr[root << 1].sum + tr[root << 1 | 1].sum) % mod;
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
        new Thread(null, new Acwing1277_Segment_Tree_Lazy_Multiplication(),"Main",1<<27).start();
    }

}
