var w_width = window.innerWidth;
var w_height = window.innerHeight;

var i = 0;
var h1 = 0;
var h2 = 0;
var h3 = 0;
var h4 = 0;


function setup() {
  createCanvas(w_width * 990/1000, w_height * 375/1000);
}

function draw() {
	background(255,255,255);

	    stroke(0, 250, 0);
        strokeWeight(2)
	    fill(255, 255, 255);
	    line(0, w_height * 15 / 100, Math.min(i, 80) * w_width / 100, w_height * 15 / 100);
	    if (i >= 80)
	    	line(w_width * 80 / 100, w_height * 15 / 100, Math.min(i, 83) * w_width / 100, w_height * 15 / 100 + h1)
	    if (i >= 83)
	    	line(w_width * 83 / 100, w_height * 15 / 100 + h1, Math.min(i, 86) * w_width / 100, w_height * 15 / 100 + h2)
	    if (i >= 86)
	    	line(w_width * 86 / 100, w_height * 15 / 100, Math.min(i, 89) * w_width / 100, w_height * 15 / 100 + h3)
	    if (i >= 89)
	    	line(w_width * 89 / 100, w_height * 15 / 100 + h3, Math.min(i, 92) * w_width / 100, w_height * 15 / 100 + h4)
	    if (i >= 92)
	    	line(w_width * 92 / 100, w_height * 15 / 100, i * w_width / 100, w_height * 15 / 100)

	    setTimeout(function() {
	    	if (i != 100)
	    		i = i + 1;

	    	if (i > 80 && i <= 83) {
	    		h1 = h1 - w_height/27;
	    		h2 = h2 - w_height/27;
	    	}
	    	if (i > 83 && i <= 86) {
	    		h2 = h2 + w_height/27;
	    	}
	    	if (i > 86 && i <= 89) {
	    		h3 = h3 + w_height/27;
	    		h4 = h4 + w_height/27;
	    	}
	    	if (i > 89 && i <= 92) {
	    		h4 = h4 - w_height/27;
	    	}

	    }, 10);
	}

function mouseClicked() {
	i = 0;
	h1 = 0;
	h2 = 0;
	h3 = 0;
	h4 = 0;
}
