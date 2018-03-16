
function generateArray(squareSide, boardData) {
    var data = new Array();
    var xpos = 1; //starting xpos and ypos at 1 so the stroke will show when we make the grid below
    var ypos = 1;

    // iterate for rows
    for (var row = 0; row < boardData.dimension.width; row++) {
        data.push( new Array() );

        // iterate for cells/columns inside rows
        for (var column = 0; column < boardData.dimension.height; column++) {
            var obstaclePoint = boardData.obstacles.find(function(o){ return o.p.x === row && o.p.y === column });
            var foodPoint = boardData.food.find(function(f){ return f.p.x === row && f.p.y === column });

            var color = "#fff";
            if (obstaclePoint) {color = "#000";};
            if (foodPoint) {color = "#98fb98";};

            data[row].push({
                x: xpos,
                y: ypos,
                width: squareSide,
                height: squareSide,
                color: color
            })
            // increment the x position. I.e. move it over by 50 (width variable)
            xpos += squareSide;
        }

        // reset the x position after a row is complete
        xpos = 1;
        // increment the y position for the next row. Move it down 50 (height variable)
        ypos += squareSide;
    }

    return data;
};
