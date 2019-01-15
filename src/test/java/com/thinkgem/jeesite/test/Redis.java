package com.thinkgem.jeesite.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * @author mh
 * @create 2018-12-11 22:54
 */
public class Redis {

    //测试联通
    @Test
    public static void main(String[] args){
        Jedis jedis = new Jedis("192.168.219.128",6379);

      /*  jedis.set("k21","v21");
        jedis.set("k22","v22");
        jedis.set("k23","v23");

        String k21 =   jedis.get("k21");;
        String k22 =   jedis.get("k22");;
        String k23 =   jedis.get("k23");;
        System.out.println(k21+"--"+k22+"--"+k23);*/

        //key
        Set<String> keys = jedis.keys("*");
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            System.out.println(key);
        }
        System.out.println("jedis.exists====>"+jedis.exists("k2"));
        System.out.println(jedis.ttl("k1"));

//        System.out.println(jedis.ping());
    }

}

