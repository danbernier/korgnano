import korgnano.*;

Kontrol2 k2;

void setup() {
  size(300, 300);
  k2 = new Kontrol2();
  System.out.println(
    "Use the first 3 sliders to control the background color");
}

void draw() {
  background(
    map(k2.slider(1), 0, 127, 0, 255), 
    map(k2.slider(2), 0, 127, 0, 255), 
    map(k2.slider(3), 0, 127, 0, 255));
}