package com.example.demo.memento;

import com.example.demo.Machine;

public class Originator {
    private String[] state;

    public void setState(String[] state){
        this.state = state;
    }

    public Memento createMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
    }

}
