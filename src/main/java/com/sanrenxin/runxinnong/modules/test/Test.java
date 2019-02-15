package com.sanrenxin.runxinnong.modules.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mh
 * @create 2019-01-29 10:42
 */
public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        String[] strings = new String[list.size()];
        strings = list.toArray(strings);
        System.out.println(list.toArray(strings));

    }
}
