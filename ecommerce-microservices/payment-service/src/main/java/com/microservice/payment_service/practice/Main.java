package com.microservice.payment_service.practice;

public class Main  {

    public static int count = 1;

    public static void main(String[] args) throws InterruptedException {
        Main obj = new Main();

        Thread t1 = new Thread(obj::printEven);
        Thread t2 = new Thread(obj::printOdd);

        t1.start();
        t2.start();

    }

    public synchronized void printOdd() {
        while(count <= 20) {
            if(count % 2 != 0) {
                System.out.println("ODD_" + count);
                count++;
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public synchronized void printEven() {
        while(count <= 20) {
            if(count % 2 == 0) {
                System.out.println("EVEN_" + count);
                count++;
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
