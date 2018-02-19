import korgnano.*;

Kontrol2 k;

void setup() {
  // The kontrol2 measures 12.75" x 3.25"
  size(1275, 325);
  
  k = new Kontrol2();
  
  // Set all the buttons to Toggle mode (there are finer-grained
  // controls for this; see the docs).
  k.buttonMode(ButtonMode.Toggle);
  
  textAlign(CENTER, CENTER);
}

void draw() {
  background(20);
  noStroke();
  
  textFont(createFont("sans", 25));
  pushMatrix();
  translate(width * 0.3, 0);
  for (int i = 0; i < 8; i++) {
    int control = i + 1;
    
    drawDial(k.dial(control));
    drawSlider(k.slider(control));
    
    drawButton(k.solo(control), "[S]", -30, 175);
    drawButton(k.mute(control), "[M]", -30, 225);
    drawButton(k.record(control), "[R]", -30, 275);
    
    translate(117.0, 0);
  }
  popMatrix();
  
  drawButton(k.prevTrack(),   "(‹)",     50, height-150);
  drawButton(k.nextTrack(),   "(›)",    100, height-150);
  drawButton(k.setMarker(),   "(set)",  150, height-90);
  drawButton(k.prevMarker(),  "(‹)",    200, height-90);
  drawButton(k.nextMarker(),  "(›)",    250, height-90);
  drawButton(k.cycle(),       "(cycle)", 50, height-90);
  drawButton(k.rewind(),      "[«]",     50, height-30);
  drawButton(k.fastForward(), "[»]",    100, height-30);
  drawButton(k.stop(),        "[■]",    150, height-30);
  drawButton(k.play(),        "[›]",    200, height-30);
  drawButton(k.record(),      "[•]",    250, height-30);
  
  fill(90);
  text("Track", 75, height-175);
  text("--Marker--", 200, height-120);
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

void drawButton(boolean isOn, String label, float x, float y) {
  pushStyle();
  fill(isOn ? color(255, 0, 0) : color(90));
  text(label, x, y);
  popStyle();
}