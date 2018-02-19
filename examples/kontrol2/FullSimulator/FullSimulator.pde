import korgnano.*;

Kontrol2 k;

void setup() {
  // The kontrol2 measures 12.75" x 3.25"
  size(1275, 325);
  
  k = new Kontrol2();
  
  // Set all the buttons to Toggle mode (there are finer-grained
  // controls for this; see the docs).
  k.buttonMode(ButtonMode.Toggle);
  
  textFont(createFont("sans", 25));
  textAlign(CENTER, CENTER);
}

void draw() {
  background(20);
  noStroke();
  
  pushMatrix();
  translate(width * 0.25, 0);
  for (int i = 0; i < 8; i++) {
    int control = i + 1;
    
    drawDial(k.dial(control));
    drawSlider(k.slider(control));
    
    if (k.solo(control))   { drawButton("S", 175); }
    if (k.mute(control))   { drawButton("M", 225); }
    if (k.record(control)) { drawButton("R", 275); }
    
    translate(125.0, 0);
  }
  popMatrix();
}

void drawDial(float dialValue) {
  pushStyle();
  stroke(0, 255, 0);
  strokeWeight(10);
  strokeCap(SQUARE);
  noFill();
  
  float arc = map(dialValue, 0, 128, 0.08, TAU*0.75);
  arc(0, 100, 40, 40, -TAU*5.0/8, -TAU*5.0/8 + arc);
  popStyle();
}

void drawSlider(float sliderValue) {
  pushStyle();
  fill(0, 255, 0);
  noStroke();
  
  float barHeight = map(sliderValue, 0, 128, 1, height*0.5);
  rect(0-10, height*0.9 - barHeight, 20, barHeight);
  popStyle();
}

void drawButton(String letter, float y) {
  pushStyle();
  rectMode(CENTER);
  fill(255, 0, 0);
  text(letter, -30, y);
  noFill();
  stroke(255, 0, 0);
  strokeWeight(2);
  rect(-30, y+3, 30, 30);
  popStyle();
}