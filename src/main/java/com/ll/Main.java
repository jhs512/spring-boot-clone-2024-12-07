package com.ll;

import com.ll.domain.post.post.controller.PostController;

public class Main {
    public static void main(String[] args) {
        App.run();

        PostController postController = App.CONTEXT.genBean("postController");
        System.out.println(postController);
    }
}
