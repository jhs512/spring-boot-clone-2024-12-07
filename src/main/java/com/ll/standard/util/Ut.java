package com.ll.standard.util;

public class Ut {
    public static class str {
        public static String ucFirst(String str) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        public static String lcFirst(String str) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
    }
}