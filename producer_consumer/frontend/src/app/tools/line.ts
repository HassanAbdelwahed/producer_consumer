import {fabric} from 'fabric'

export class Line {


  drawArrowLine(startPoint: fabric.Point, endPoint: fabric.Point) {

    var ang = Math.atan((startPoint.y -endPoint.y)/(startPoint.x -endPoint.x));
    //console.log(ang * 180/Math.PI);
    const line = new fabric.Line([startPoint.x, startPoint.y, endPoint.x, endPoint.y], {
      stroke: 'black', // Line color
      strokeWidth: 2, // Line width
    });

    const arrowHead = new fabric.Triangle({
      width: 10,
      height: 15,
      fill: 'black', // Arrow color
      left: endPoint.x,
      top: endPoint.y + (15 / 2),
      angle: -90 + ang * 180/Math.PI
    });
    const group = new fabric.Group([line, arrowHead]);
    return group;
  }

}
