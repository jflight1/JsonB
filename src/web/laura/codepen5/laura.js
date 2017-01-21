var fgImage = null;
function loadForegroundImage() {
    var imgFile =
        document.getElementById("fgfile");
    fgImage = new SimpleImage(imgFile);
    var canvas =
        document.getElementById("fgcan")
    fgImage.drawTo(canvas);
}

var bgImage = null;
function loadBackgroundImage() {
    var imgFile =
        document.getElementById("bgfile");
    bgImage = new SimpleImage(imgFile);
    var canvas =
        document.getElementById("bgcan");
    bgImage.drawTo(canvas);
}

function greenScreen() {
    if(fgImage == null || ! fgImage.complete()){
        alert("foreground not loaded");
        return;
    }
    if(bgImage == null || ! bgImage.complete()) {
        alert("background not loaded");
    }
    clearCanvas();
}

var output =
    new SimpleImage(fgImage.getWidth(), fgImage.getHeight());
for (var pixel in fgImage.values()) {
    var x = pixel.getX();
    var y = pixel.getY();
    if (pixel.getGreen() > greenThreshold) {
        var bgPixel = bgImage.getPixel(x, y);
        output.setPixel(x, y, bgPixel);
    }
    else{
        output.setPixel(x,y, pixel);
    }
}

