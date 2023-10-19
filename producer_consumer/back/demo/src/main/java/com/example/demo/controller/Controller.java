package com.example.demo.controller;


import com.example.demo.Contain;
import com.example.demo.Machine;
import com.example.demo.memento.Caretaker;
import com.example.demo.memento.Memento;
import com.example.demo.service.Service;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class Controller {
    Service service = new Service();
    public static List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @GetMapping("addMachine/{id}/")
    public void addMachine(@PathVariable int id) {
        this.service.addMachine(id);
        // System.out.println("machine added");
    }
    @GetMapping("addQueue/{id}/")
    public void addQueue(@PathVariable int id) {
        this.service.addQueue(id);
        //System.out.println("queue added");
    }
    @GetMapping("machineToQueue/{mId}/{qId}/")
    public void machineToQueue(@PathVariable int mId, @PathVariable int qId) {
        //System.out.println("from M " + mId + " to Q " + qId);
        this.service.machineToQueue(mId, qId);
    }
    @GetMapping("queueToMachine/{mId}/{qId}/")
    // connect queue and machine
    public void queueToMachine(@PathVariable int mId, @PathVariable int qId) {
        //System.out.println("from Q " + qId + " to M " + mId);
        this.service.queueToMachine(mId, qId);
    }
    @GetMapping("SetProducts/{num}/")
    public void SetProducts(@PathVariable int num) {
        this.service.SetProducts(num);
    }
    @GetMapping("addProduct/")
    public void addProduct() {
        this.service.addProduct();
    }

    @GetMapping("start/")
    public void start() {
        this.service.start();
    }

    @GetMapping("getContainer/")
    public Contain getContainer() {
        //System.out.println("get machines");
        return this.service.getContainer();
    }

    @RequestMapping(value = "/update", consumes = MediaType.ALL_VALUE)
    public SseEmitter update() {
        System.out.println("IN UPDATE");
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(sseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        emitters.add(sseEmitter);

        return sseEmitter;
    }
    public static void disPatch(String[] data) {

        //System.out.println("Sent data!  " + res[0] + " " + res[1] + " " + res[2] + " " + res[3] + "\n");
        //System.out.println(machine);

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("last").data(data));
                emitter.send(SseEmitter.event().data(data));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
    @GetMapping("replay/")
    public void replay() throws InterruptedException {
        // System.out.println("get machines");
        // ArrayList<String[]> states = new ArrayList<>();
        Caretaker caretaker = Caretaker.getInstance();
        ArrayList<Memento> mementoList = caretaker.getMementoList();
        for (Memento memento: mementoList) {
            String[] state = memento.getState();
            Thread.sleep(Integer.parseInt(state[state.length - 1]));
            disPatch(state);
        }

    }

}
