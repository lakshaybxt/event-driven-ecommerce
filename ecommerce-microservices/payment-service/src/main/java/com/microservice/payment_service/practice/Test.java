package com.microservice.payment_service.practice;

/*
public class Test extends Thread {
    public static void main(String[] args) {
        Test thread = new Test();
        thread.start();
        System.out.println("This code is outside of the thread");
    }
    public void run() {
        System.out.println("This code is running in a thread");
    }
}

public class Test implements Runnable {
    public static void main(String[] args) {
        Test thread = new Test();
        Thread t1 = new Thread(thread);
        t1.start();
        System.out.println("This code is outside of the thread");
    }
    public void run() {
        System.out.println("This code is running in a thread");
    }
}
*/

public class Test extends Thread {
    public static int amount = 0;

    public static void main(String[] args) {
        Test thread = new Test();
        thread.start();
//        System.out.println(amount);
//        System.out.println(amount);
//        System.out.println(amount);
//        System.out.println(amount);
        while(thread.isAlive()) {
            System.out.println("Waiting...");
        }
        System.out.println(amount);
        amount++;
        System.out.println(amount);
    }

    public void run() {
        amount++;
        System.out.println("Bro");
    }
}