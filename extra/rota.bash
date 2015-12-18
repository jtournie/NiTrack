#!/bin/bash
i=1
while [ $i -le 360 ]
do
   echo "Producing $i..."
   cp clock_colorbg_widget.png tmp.png
   sips -r $i --cropToHeightWidth 451 451 tmp.png
   mv tmp.png clock_colorbg_widget_$i.png
   i=$(( $i + 10))
done
