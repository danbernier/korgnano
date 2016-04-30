import korgnano.*;

Kontrol2 k2;

void setup() {
  size(300, 300);
  
  noStroke();
  fill(#F21465);
  rectMode(CENTER);
  
  k2 = new Kontrol2();
  
  System.out.println(
    "Press the last SOLO button to draw a circle");
  System.out.println(
    "Press the fourth MUTE button to draw a square");
}

void draw() {
  background(255);
    
  if (k2.solo(8)) {
    ellipse(width/2, height/2, 160, 160);
  }
  if (k2.mute(4)) {
    rect(width/2, height/2, 140, 140);
  }
}