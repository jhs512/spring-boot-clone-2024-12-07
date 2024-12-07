package com.ll;

import com.ll.framework.ioc.ApplicationContext;

public class App {
    public static ApplicationContext CONTEXT;

    public static void run() {
        ApplicationContext context = new ApplicationContext("com.ll");
        context.init();
        CONTEXT = context;
    }
}
