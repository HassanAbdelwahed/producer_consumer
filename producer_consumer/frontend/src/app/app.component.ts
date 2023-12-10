import { Component, ViewChild, ElementRef, Renderer2, AfterViewInit, OnInit } from '@angular/core';
import { count, delay, noop } from 'rxjs';
import { __makeTemplateObject } from 'tslib';
import {fabric} from 'fabric'
import { group } from '@angular/animations';
import { Machine } from "./tools/machine";
import { Queue } from "./tools/queue";
import { Line } from './tools/line';
import { HttpService } from './service/service';
import { HttpClient } from '@angular/common/http';
import { formatPercent } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  Url = 'http://localhost:8080/';
  update_url = 'http://localhost:8080/update';

  canvas !: fabric.Canvas;
  machineID : number = 0;
  queueID: number = -1;
  isConnect: Boolean = false;
  startingPoint !: fabric.Point ;
  isDrawing : Boolean = false;
  selected1: any;
  selected2: any;
  isRunning = false;

  machines = new Map<number, fabric.Group>;
  queues = new Map<number, fabric.Group>;

  countProduct : number = 0;

  machine = new Machine();
  queue = new Queue();
  line = new Line();

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
    this.addEvent();
    const canvas = new fabric.Canvas(document.querySelector('canvas') ,{
      selection : true,
      isDrawingMode : false
    });

    if (canvas){
      this.canvas = canvas;
    }

    this.canvas.on('mouse:down', (event) => {
      const clickedObject = event.target;

      if (clickedObject && this.isConnect) {
        if (!this.isDrawing) {
          const x = (clickedObject.left || 0);
          const y = (clickedObject.top || 0) + ((clickedObject.height || 0) / 2 || 0);
          this.startingPoint = new fabric.Point(x, y);
          this.isDrawing = true;
          this.selected1 = clickedObject.name;
        } else {
          this.isDrawing = false;
          const x = (clickedObject.left || 0) + (clickedObject.width || 0);
          const y = (clickedObject.top || 0) + ((clickedObject.height || 0) / 2 || 0);
          const endingPoint = new fabric.Point(x, y);
          this.selected2 = clickedObject.name;
          this.drawArrowLine(this.startingPoint, endingPoint);
        }
      }
    });

    this.update();
  }

  update () {
    //type Event = DoneInvokeEvent<any>;
      var eventSource = new EventSource(this.update_url);
      //console.log(eventSource);
      eventSource.onmessage = changes => {
        console.log("Changes");
        console.log(changes.data);
        //const jsonData = JSON.parse(changes.data);
        this.handleChanges(changes.data);
      }
      eventSource.addEventListener("lastestChanges",  function my(event){
        console.log('last changes')
        console.log(event)
      });
  }

  handleChanges(data: any) {
    data = JSON.parse(data);

    console.log(data);

    // change inqueues product number
    const qId3 = parseInt(data[4]);
    const len3 = data[5];
    console.log(qId3);
    console.log(len3);
    const group3 = this.queues.get(qId3);
    group3?._objects[1]._set('text', "Q" + qId3 + "\n" + "n=" + len3);
    this.canvas.renderAll();

    // change machine color
    const mId1 = parseInt(data[0]);
    const color1 = data[1];
    console.log(mId1);
    console.log(color1);
    const group1 = this.machines.get(mId1);
    console.log(group);
    group1?._objects[0].set('fill', color1);
    this.canvas.renderAll();
    // change outqueue product number
    const qId2 = parseInt(data[2]);
    const len2 = data[3];
    console.log(qId2);
    console.log(len2);
    const group2 = this.queues.get(qId2);
    console.log(group2);
    group2?._objects[1]._set('text', "Q" + qId2 + "\n" + "n=" + len2);
    this.canvas.renderAll();
    //console.log(data['inQueues'].length)

  }

  addEvent() {
    document.querySelectorAll('._button').forEach((butt) =>{
      butt.addEventListener('mousedown', function () {
        document.querySelectorAll('._button').forEach((butt) =>{
          butt.classList.remove('clicked');
        });
        butt.classList.add('clicked');
      });
    });

    document.querySelectorAll('._button').forEach((butt) =>{
      butt.addEventListener('mouseup', function () {
        butt.classList.remove('clicked');
      });
    });
  }

  addProduct(){
    this.countProduct++;
    this.http.get('http://localhost:8080/addProduct/',{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
    );
    const group = this.queues.get(0);
    //console.log(group);
    group?._objects[1]._set('text', "Q0" + "\n" + "n=" + this.countProduct.toString());
    this.canvas.renderAll();
    //console.log(group?._objects[1]._set('text', 'll'))
  }

  // delay(ms: number) {
  //   return new Promise( resolve => setTimeout(resolve, ms) );
  // }

  Start(){
    if (this.isRunning) return;
    this.isRunning = true;
    this.http.get('http://localhost:8080/start/',{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
    );
  }

  // async Start(){
  //   if (this.isRunning) return;
  //   this.isRunning = true;
  //   this.http.get('http://localhost:8080/start/',{
  //       reportProgress:true,
  //       observe: 'events',
  //       responseType:'blob'
  //     }).subscribe(
  //       event => {
  //         console.log(event);
  //       }
  //   );
  //   var i  = 0;
  //   while(this.isRunning) {
  //     await this.http.get<any[]>('http://localhost:8080/getMachines/').subscribe(res => {
  //       this.handleRequest(res)
  //     });
  //     i++;
  //     await this.delay(100);
  //   }
  //   //console.log(JSON.parse(mach))
  // }

  drawArrowLine(startPoint: fabric.Point, endPoint: fabric.Point) {

    if (this.selected1[0] == 'Q' && this.selected2[0] == 'M'){
      //console.log(this.selected1);
      const mId = this.selected2.substring(1);
      const qId = this.selected1.substring(1);
      //console.log(mId + " " + qId);
      this.http.get('http://localhost:8080/queueToMachine/'+ mId + "/" + qId + "/",{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
      );
    }else if (this.selected1[0] == 'M' && this.selected2[0] == 'Q'){
      const mId = this.selected1.substring(1);
      const qId = this.selected2.substring(1);
      this.http.get('http://localhost:8080/machineToQueue/'+ mId + "/" + qId + "/",{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
      );
    }else{
      return;
    }
    const group = this.line.drawArrowLine(startPoint, endPoint);
    this.canvas.add(group);

  }

  addMachine(){

    this.isConnect = false;
    this.machineID++;
    this.http.get('http://localhost:8080/addMachine/'+ this.machineID.toString() + "/",{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
      );
    const group = this.machine.addMachine(this.machineID);
    this.canvas.add(group);

    //console.log(group);
    this.machines.set(this.machineID, group);
    // Enable object selection and interaction
    //this.canvas.selection = true;
  }

  addQueue() {
    this.isConnect = false;
    this.queueID++;

    this.http.get('http://localhost:8080/addQueue/'+ this.queueID.toString() + "/",{
        reportProgress:true,
        observe: 'events',
        responseType:'blob'
      }).subscribe(
        event => {
          console.log(event);
        }
      );
    const group = this.queue.addQueue(this.queueID);
    this.canvas.add(group);
    this.queues.set(this.queueID, group);
    // Enable object selection and interaction
    //this.canvas.selection = true;
  }
  connect() {
    document.querySelectorAll('._button').forEach((butt) =>{
      butt.classList.remove('active');
    });
    document.getElementById('Connect')?.classList.add('active');
    this.isConnect = true;
  }

  Replay(){
    this.isRunning = false;
    // this.resetMachines();
    this.resetQueues();
    this.http.get('http://localhost:8080/replay/').subscribe(
      event => {
        console.log(event);
      }
      // const data = JSON.parse(JSON.stringify(response));
      // data.forEach((element: any) => {
      //   this.handleChanges(JSON.stringify(element));
      // })
    );
  }

  // resetMachines(){
  //   for(let i = 1; i <= this.machineID; i++){
  //     const group = this.machines.get(i);
  //     group?._objects[0].set('fill', 'green');;
  //   }
  // }

  resetQueues(){
    const group = this.queues.get(0);
    group?._objects[1]._set('text', "Q0" + "\n" + "n=" + this.countProduct.toString());
    for(let i = 1; i <= this.queueID; i++){
      const group = this.queues.get(i);
      group?._objects[1]._set('text', "Q" + i + "\n" + "n=0");
    }
    this.canvas.renderAll();
  }
  Clear(){
    this.machineID = 0;
    this.queueID= -1;
    this.isConnect = false;
    this.isDrawing = false;
    this.isRunning = false;

    this.machines = new Map<number, fabric.Group>;
    this.queues = new Map<number, fabric.Group>;
    this.countProduct = 0;

    this.machine = new Machine();
    this.queue = new Queue();
    this.line = new Line();

    const context = this.canvas.getContext();
    context.clearRect(0, 0, this.canvas.width || 1500, this.canvas.height || 600);
    this.canvas.clear();
    this.http.get('http://localhost:8080/clear/').subscribe(
      event => {
        console.log(event);
      }
    );
  }

}
