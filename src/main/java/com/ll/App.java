package com.ll;

import com.ll.framework.ioc.ApplicationContext;
import com.ll.framework.web.HttpServer;

public class App {
    public static ApplicationContext CONTEXT;

    public static void run() {
        ApplicationContext context = new ApplicationContext("com.ll");
        context.init();
        CONTEXT = context;

        HttpServer server = new HttpServer();
        server.start(8080);
    }
}
