package com.nac.abc;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testThreadLocalSetAndGet(){

        //提供一个ThreadLocal对象
        ThreadLocal threadLocal = new ThreadLocal();

        //开启两个线程
        new Thread(()->{
            threadLocal.set("niu");
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
        },"蓝色").start();

        new Thread(()->{
            threadLocal.set("chu");
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
            System.out.println(Thread.currentThread().getName()+":"+threadLocal.get());
        },"绿色").start();
    }

}
