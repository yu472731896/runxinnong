package com.thinkgem.jeesite.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mh
 * @create 2018-12-03 13:37
 */
public class ReadBigFile {

    public static void mian(String[] args){
        FileInputStream is = null;
        String path = "D:\\test.txt";
        String regex = "查询字符串";
        int num = 0;
        try {
            File file = new File(path);
            is = new FileInputStream(file);
            Scanner sc = new Scanner(is);

            while (sc.hasNextLine()){
                String line="";
                line = sc.nextLine();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()){
                    num++;
                }
            }
            System.out.println("num-->"+num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
