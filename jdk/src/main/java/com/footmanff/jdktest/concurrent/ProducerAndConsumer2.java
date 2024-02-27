package com.footmanff.jdktest.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fm on 2017/3/20.
 */
public class ProducerAndConsumer2 {

    private static boolean isMeatAvailable = false;

    private static class Producer implements Runnable{
        private Consumer consumer;
        public void setConsumer(Consumer consumer) {
            this.consumer = consumer;
        }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()){
                    synchronized (this){
                        while (isMeatAvailable){
                            System.out.println("生产者等待肉被吃掉");
                            wait();
                        }
                    }
                    synchronized (consumer){
                        isMeatAvailable = true;
                        System.out.println("生产者生产肉");
                        consumer.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Consumer implements Runnable{
        private Producer producer;
        public void setProducer(Producer producer) {
            this.producer = producer;
        }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()){
                    synchronized (this){
                        while (!isMeatAvailable){
                            System.out.println("消费者等待肉做出来");
                            wait();
                        }
                    }
                    synchronized (producer){
                        isMeatAvailable = false;
                        System.out.println("消费者吃掉肉");
                        producer.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.setConsumer(consumer);
        consumer.setProducer(producer);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(producer);
        executorService.execute(consumer);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

}
