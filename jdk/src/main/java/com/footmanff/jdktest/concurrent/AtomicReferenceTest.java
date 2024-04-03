package com.footmanff.jdktest.concurrent;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    @Test
    public void t1() {
        Integer a = 123;
        AtomicReference<Integer> reference = new AtomicReference<>(a);
        reference.compareAndSet(a, 345);
        System.out.println(reference.get());

        reference.lazySet(1);
    }

    private static volatile BankCard bankCard = new BankCard("cxuan", 100);

    @Test
    public void t2() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 先读取全局的引用
                final BankCard card = bankCard;
                // 构造一个新的账户，存入一定数量的钱
                BankCard newCard = new BankCard(card.getAccountName(), card.getMoney() + 100);
                System.out.println(newCard);
                // 最后把新的账户的引用赋给原账户
                bankCard = newCard;
                try {
                    TimeUnit.MICROSECONDS.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Test
    public void t3() throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (AtomicReferenceTest.class) {
                    // 先读取全局的引用
                    final BankCard card = bankCard;
                    // 构造一个新的账户，存入一定数量的钱
                    BankCard newCard = new BankCard(card.getAccountName(), card.getMoney() + 100);
                    System.out.println(newCard);
                    // 最后把新的账户的引用赋给原账户
                    bankCard = newCard;
                    try {
                        TimeUnit.MICROSECONDS.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(100L);
    }

    private static AtomicReference<BankCard> bankCardRef = new AtomicReference<>(new BankCard("cxuan", 100));

    @Test
    public void t4() throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    // 使用 AtomicReference.get 获取
                    final BankCard card = bankCardRef.get();
                    BankCard newCard = new BankCard(card.getAccountName(), card.getMoney() + 100);
                    // 使用 CAS 乐观锁进行非阻塞更新
                    if (bankCardRef.compareAndSet(card, newCard)) {
                        System.out.println(newCard);
                        break;
                    }
                }
            }).start();
        }
    }

    static class BankCard {

        private final String accountName;
        private final int money;

        // 构造函数初始化 accountName 和 money
        public BankCard(String accountName, int money) {
            this.accountName = accountName;
            this.money = money;
        }

        // 不提供任何修改个人账户的 set 方法，只提供 get 方法
        public String getAccountName() {
            return accountName;
        }

        public int getMoney() {
            return money;
        }

        // 重写 toString() 方法， 方便打印 BankCard
        @Override
        public String toString() {
            return "BankCard{" +
                    "accountName='" + accountName + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }
    }

}
