package com.example.demo;

import java.util.ArrayList;
import java.util.Queue;

public class Contain {
    private ArrayList<Machine> machines = new ArrayList<>();
    private ArrayList<DataQueue> dataQueues = new ArrayList<>();

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;
    }

    public ArrayList<DataQueue> getDataQueues() {
        return dataQueues;
    }

    public void setDataQueues(ArrayList<DataQueue> dataQueues) {
        this.dataQueues = dataQueues;
    }
}
