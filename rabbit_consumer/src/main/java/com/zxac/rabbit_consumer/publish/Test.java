package com.zxac.rabbit_consumer.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Test {

    public static void main(String[] argv) throws Exception {

        ArrayList<Integer> list = new ArrayList<>();

        list.add(1);

        list.getClass().getMethod("add", Object.class).invoke(list, "asd");

        System.out.println(list);


    }
}