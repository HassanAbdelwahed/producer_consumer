import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class HttpService {
  Url = 'http://localhost:4200/';
  constructor(private http: HttpClient) {
  }
  
  addMachine(Id : number){
    if(this.http.get<boolean>(this.Url + '/addMachine/' + Id)){
      console.log('machine added');
    }else{
      console.log("failed adding machine");
    }
  }
  addQueue(Id : number){
    if(this.http.get<boolean>(this.Url + '/addQueue/' + Id)){
      console.log('queue added');
    }else{
      console.log("failed adding queue");
    }
  }

  connect(){

  }


}
