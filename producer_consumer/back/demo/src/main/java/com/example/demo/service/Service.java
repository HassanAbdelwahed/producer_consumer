package com.example.demo.service;

import com.example.demo.Contain;
import com.example.demo.DataQueue;
import com.example.demo.Machine;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;
import java.util.ArrayList;

public class Service {
    private ArrayList<Machine> machines = new ArrayList<>();
    private ArrayList<DataQueue> queues = new ArrayList<>();
    private ArrayList<Thread> threads = new ArrayList<>();
    public void addMachine(int id) {
        Machine machine = new Machine(id);
        machines.add(machine);
        Thread thread = new Thread(machine);
        threads.add(thread);
    }
    public void addQueue(int id) {
        DataQueue dataQueue = new DataQueue(id);
        queues.add(dataQueue);
    }

    public void machineToQueue(@PathVariable int mId, @PathVariable int qId) {
        Machine mach = null;
        DataQueue queue = null;
        for (Machine machine : machines){
            if (machine.getId() == mId){
                mach = machine;
                break;
            }
        }
        for (DataQueue dataQueue: queues){
            if (dataQueue.getId() == qId){
                queue = dataQueue;
                break;
            }
        }
        if (mach != null && queue != null)
            mach.setOutQueue(queue);
        System.out.println("M " +  mach.getId() + " connected to " + queue.getId());
    }
    public void queueToMachine(@PathVariable int mId, @PathVariable int qId) {
        Machine mach = null;
        DataQueue queue = null;
        for (Machine machine : machines){
            if (machine.getId() == mId){
                mach = machine;
                break;
            }
        }
        for (DataQueue dataQueue: queues){
            if (dataQueue.getId() == qId){
                queue = dataQueue;
                break;
            }
        }
        if (mach != null && queue != null)
            mach.setInQueue(queue);
        System.out.println("Q " + queue.getId() + " connected to " + mach.getId());
    }

    public void SetProducts(int num) {
        if (queues.size() == 0){
            return;
        }
        for (DataQueue dataQueue: queues){
            if (dataQueue.getId() == 0){
                dataQueue.SetProducts(num);
                break;
            }
        }
    }
    public void addProduct(){
        for (DataQueue dataQueue: queues){
            if (dataQueue.getId() == 0){
                dataQueue.addProduct();
                System.out.println("product added");
                return;
            }
        }
        System.out.println("product not added");
    }

    public void start() {
        for (Thread thread: threads){
            thread.start();
        }
    }
    public ArrayList<Machine> getMachines(){
        return this.machines;
    }

    public Contain getContainer(){
        Contain container = new Contain();
        container.setMachines(this.machines);
        container.setDataQueues(this.queues);
        return container;
    }
}
