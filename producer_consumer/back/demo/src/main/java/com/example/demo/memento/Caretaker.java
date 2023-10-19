package com.example.demo.memento;

import java.util.ArrayList;

public class Caretaker {
    ArrayList<Memento> mementoList = new ArrayList<>();
    private static Caretaker caretaker;

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
    public static Caretaker getInstance(){
        if (caretaker == null)
            caretaker = new Caretaker();
        return caretaker;
    }

    public ArrayList<Memento> getMementoList() {
        return mementoList;
    }
}
