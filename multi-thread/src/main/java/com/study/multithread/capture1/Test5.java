package com.study.multithread.capture1;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Test5 {
    public void bar(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    public abstract void foo(SimpleDateFormat simpleDateFormat);

    public static void main(String[] args) {
        new Test5() {
            @Override
            public void foo(SimpleDateFormat simpleDateFormat) {
                String dateStr = "1998-10-12 00:00:00";
                new Thread(()->{
                    try {
                        simpleDateFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }.bar();
    }
}
