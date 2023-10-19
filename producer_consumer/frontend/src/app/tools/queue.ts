import {fabric} from 'fabric'

export class Queue {

  addQueue(id: number){
    document.querySelectorAll('._button').forEach((butt) =>{
      butt.classList.remove('active');
    });
    document.getElementById('queue')?.classList.add('active');
    const rect = new fabric.Rect({
      left: 40,
      top: 40,
      fill: 'red',
      width: 60,
      height: 60
    });
    const text = new fabric.Text('Q' + id.toString() + "\n" + "n=0", {
      left: 55,
      top: 45,
      fontSize: 20,
      fill: 'white', // Text color
    });

    const group = new fabric.Group([rect, text], {
      name: 'Q' + id.toString()
    });

    return group;

  }
}
