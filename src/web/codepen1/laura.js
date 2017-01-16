function doCrimson(){
    var canvas =
        document.getElementById("can1");
    canvas.style.backgroundcolor = "#DC143C";
    var context =
        canvas.getContext("2d");

    context.fillStyle = "#7FFF00";
    context.fillRect(10,10,40,40);
    context.fillRect(30,10,40,40);

    context.fillStyle = "#48D1CC";
    context.font = "20px Century Gothic";
    context.fillText("I am Laura", 15, 45);
}



