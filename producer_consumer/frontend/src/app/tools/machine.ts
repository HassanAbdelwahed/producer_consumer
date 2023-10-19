import {fabric} from 'fabric'

export class Machine {
  color : string = 'green';

  addMachine(id: number){

    document.querySelectorAll('._button').forEach((butt) =>{
      butt.classList.remove('active');
    });
    document.getElementById('machine')?.classList.add('active');
    const circle = new fabric.Circle({
      radius: 24,
      fill: 'green',
      left: 40,
      top: 40
    });
    const text = new fabric.Text('M' + id.toString(), {
      left: 50,
      top: 50,
      fontSize: 24,
      fill: 'white', // Text color
    });
    const group = new fabric.Group([circle, text], {
      name: 'M' + id.toString()
    });
    return group;
  }

}
