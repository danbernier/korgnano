import korgnano.*;

Kontrol2 nano;

/*
This example shows off a bit more of what the Kontrol2 can do.

There are two animations:
1. some Squares, controlled by group 1
2. a Spiral of dots, controlled by group 2

Turn them on and off with the RECORD button.
Turn on multiples of each with the MUTE button.
Make them Pulse with the SOLO button.

They also Spin. You can control the spin for both with the #7 dial.

Wait, did I say Pulse? with the solo button?
You can control the pulse speed with the #8 dial, and the amount with the #8 slider.
*/

Pulse pulse;
Spin spin;
Squares squares;
Spiral spiral;

void setup() {
  size(600, 600);
  colorMode(HSB);
  
  pulse = new Pulse(90, 1, 0.7);
  spin = new Spin(0.1);
  squares = new Squares();
  spiral = new Spiral();
  
  nano = new Kontrol2();
  nano.buttonMode(1, ButtonMode.Toggle); // sets all buttons for position 1 to Toggle mode
  nano.buttonMode(2, ButtonMode.Toggle);
}

void draw() {
  background(255);
  
  pulse.setBpm(map(nano.dial(8), 0, 127, 40, 160));
  pulse.setAmp(map(nano.slider(8), 0, 127, 0.2, 5));
  pulse.update();
  spin.update();

  if (nano.record(1)) {
    squares.draw();
  }
  if (nano.record(2)) {
    spiral.draw();
  }
}