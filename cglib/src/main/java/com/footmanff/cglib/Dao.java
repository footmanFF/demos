package com.footmanff.cglib;

public class Dao {

    public void update() {
        System.out.println("PeopleDao.update()");
    }

    public void select() {
        System.out.println("PeopleDao.select()");
    }

    public String haveReturn() {
        return "result";
    }

}