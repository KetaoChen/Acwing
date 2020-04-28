package Dp.Deque;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.InputMismatchException;


public class Acwing1088_GasStation_MaxPrefix implements Runnable
{
    @Override
    public void run() {
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
        int n = in.nextInt();
        int[] o = new int[n];
        int[] d = new int[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            o[i] = in.nextInt();
            d[i] = in.nextInt();
            sum += (long) o[i] - d[i];
        }

        // consider clock wise first

        int[] net = new int[2 * n];
        for (int i = 0; i < n; i++) {
            net[i] = o[i] - d[i];
            net[i + n] = net[i];
        }

        // 1. calculate the minimum of prefix, starting from each index.
        // 2. this minimum prefix value = suffix to this index - maxSuffix
        // 3. use deque to keep the index of maxSuffix.

        Deque<Integer> dq = new ArrayDeque<>();
        dq.offerLast(2 * n);
        long[] suf = new long[2 * n + 1];
        boolean[] can = new boolean[n];

        for (int i = 2 * n - 1; i >= 0; i--) {
            suf[i] = suf[i + 1] + net[i];
            if (i < n) can[i] = suf[i] - suf[dq.peekFirst()] >= 0;

            while (!dq.isEmpty() && suf[i] > suf[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            if (dq.peekFirst() - i == n) dq.pollFirst();
        }

        net[0] = net[n] = o[0] - d[n - 1];
        for (int i = 1; i < n; i++) {
            net[i] = net[i + n] = o[i] - d[i - 1];
        }

        dq = new ArrayDeque<>();
        long[] pre = new long[2 * n + 1];
        for (int i = 1; i <= 2 * n; i++) {
            pre[i] = pre[i - 1] + net[i - 1];
            if (i > n) can[i - n - 1] |= pre[i] - pre[dq.peekFirst()] >= 0;

            while (!dq.isEmpty() && pre[i] > pre[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            if (i - dq.peekFirst() == n) dq.pollFirst();
        }

        for (int i = 0; i < n; i++) {
            w.println(can[i] ? "TAK" : "NIE");
        }


        w.flush();
        w.close();
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
        new Thread(null, new Acwing1088_GasStation_MaxPrefix(),"Main",1<<27).start();
    }

}
