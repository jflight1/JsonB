value = "make #FFD933";
onclick = "do#FFD933()";

function doSquare() {
    var dd1 = document.getElementById("canvas1");
    var sizeInput= document.getElementById("sldr");
    var size= sizeInput.value;
    var ctx = dd1.getContext("2d");}
      ctx.clearRect(0, 0 , 10 , 10);
      ctx.fillRect(100, 100, 100 ,100);




}
function doColor() {
    var dd1 = document.getElementById("d1");
    var ctx = dd1.getContext("2d");
    ctx. fillStyle = ("#7873F0");


}