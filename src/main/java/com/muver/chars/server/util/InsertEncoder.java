package com.muver.chars.server.util;

import java.util.*;

public class InsertEncoder {
    private static final char[] CHARS = new char[] { /*'ͯ', 'ͮ', 'ͭ', 'ͬ', 'ͫ', 'ͪ', 'ͩ', 'ͨ', 'ͧ', 'ͦ', 'ͥ', 'ͤ', 'ͣ',*/ '͘',
            '͗', '͕', '͔', '͓', '͑', '͐', '͏', '͉', '̾', '̽', '̻', '̹', '̩', '̨', '̦',
            '̥', '̣', '̠', '̟', '̞', '̝', '̛', '̚', '̕', '̔', '̓', '̑', '̏', '̉', };
    private static final int N = CHARS.length;

    private static Stack<Integer> to_n_sys(String mess) {
        Stack<Integer> n_sys = new Stack<>();
        while (mess.length() > 0) {
            int t_sys = Integer.parseInt("1" + mess.substring(0, Math.min(9, mess.length())));
            while (t_sys > 0) {
                n_sys.push(t_sys % N);
                t_sys /= N;
            }
            mess = mess.substring(Math.min(9, mess.length()));
        }
        return n_sys;
    }

    private static String to_t_sys(Stack<Integer> n_sys) {
        String output = "";
        long t_sys = 0;
        int j = -1;
        final int length = n_sys.size();
        for (int i = 0; i < length; i += 1) {
            j += 1;
            t_sys += n_sys.pop()*(Math.pow(N, j));
            if (Long.toString(t_sys).length() > 9) {
                output += Long.toString(t_sys).substring(1);
                t_sys = 0;
                j = -1;
            }
        }
        output += Long.toString(t_sys).substring(1);
        return output;
    }

    public static String encoding(String container, String message, int cluster_size) throws TooSmallContainerException {
        String output = "";

        Stack<Integer> insert = to_n_sys(message);
        if (cluster_size == -1)
            cluster_size = Math.max(container.length() / insert.size(), 1);
        while (!insert.isEmpty() && !container.isEmpty()) {
            char[] cluster = container.substring(0, cluster_size).toCharArray();
            int position = 0;//Math.abs(new Random().nextInt() % cluster_size);
            for (int j = 0; j < cluster_size; j += 1) {
                if (position == j)
                    output += CHARS[insert.pop()];
                output += cluster[j];
            }
            container = container.substring(cluster_size);
        }
        if (!insert.isEmpty())
            throw new TooSmallContainerException();
        output += container;
        return output;
    }

    private static int indexOf(char ch) {
        int index = -1;
        for (int i = 0; i < CHARS.length; i += 1) {
            if (ch == CHARS[i]) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String decoding(String container) {
        Stack<Integer> num = new Stack<>();
        for (int i = 0; i < container.length(); i += 1) {
            int index = indexOf(container.charAt(i));
            if (index != -1)
                num.push(index);
        };
        return to_t_sys(num);
    }

    public  static double distortionOfContainer(String container) {
        int length = container.length();
        int countOfAdditionalChars = 0;
        for (int i = 0; i < container.length(); i += 1) {
            int index = indexOf(container.charAt(i));
            if (index != -1)
                countOfAdditionalChars += 1;
        }
        return (double) countOfAdditionalChars / length;
    }
}
