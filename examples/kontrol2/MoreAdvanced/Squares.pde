// This is the Squares animation.
class Squares {
  void draw() {
    rectMode(CENTER);

    float squareSize = map(nano.slider(1), 0, 127, 0, 230);
    if (nano.solo(1)) {
      squareSize += (pulse.value() * 90);
    }
    drawSpinningSquare(width/2, height/2, squareSize, spin.angle());

    if (nano.mute(1)) {
      drawSpinningSquare(width*0.25, height*0.25, squareSize, spin.angle());
      drawSpinningSquare(width*0.25, height*0.75, squareSize, spin.angle());
      drawSpinningSquare(width*0.75, height*0.25, squareSize, spin.angle());
      drawSpinningSquare(width*0.75, height*0.75, squareSize, spin.angle());
    }
  }

  void drawSpinningSquare(float x, float y, float size, float rotation) {
    pushMatrix();
    translate(x, y);
    rotate(rotation);
    noFill();
    stroke(0);
    strokeWeight(3);
    rect(0, 0, size, size);
    popMatrix();
  }
}