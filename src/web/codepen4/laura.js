function upload() {
    var imgcanvas = document.getElementById("can");
    var fileinput = document.getElementById("finput");
    var filename = fileinput.value;
    var image = new SimpleImage(fileinput);
    image.drawTo(imgcanvas);
    alert("chose" + filename);
}

