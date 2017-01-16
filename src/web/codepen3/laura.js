function upload() {
    var image = new SimpleImage(fileinput);
    image.drawTo(imgcanvas);
}
 var image;
function upload() {
    image = new SimpleImage(fileinput);


function makeGray() {
    for (var pixel in image.values()) {
        var avg = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) /3;
        pixel.setRed(avg);
        pixel.setGreen(avg);
        pixel.setBlue(avg);
    }
    var imgcanvas = document.getElementById("can")
    image.drawTo(imgcanvas);
}


