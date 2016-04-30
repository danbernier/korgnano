// This is the Spiral animation.
class Spiral {  
  void draw() {
    pushMatrix();
    translate(width/2, height/2);
    rotate(spin.angle());
    
    noStroke();
    fill(20, 90);
    
    float radius = 0;
    float theta = 0;
    
    float dRadius = map(nano.slider(2), 0, 127, 0.0, 4);
    float dTheta = map(nano.dial(2), 0, 127, -0.4, 0.4);
    
    float pulseExtra = 0;
    if (nano.solo(2)) {
      pulseExtra = (pulse.value() * 20);
    }
    
    for (int i = 0; i < 100; i++) {
      
      dotAt(theta, radius, 5 + pulseExtra);
      
      if (nano.mute(2)) {
        dotAt(theta + (TAU/3.0), radius, 5 + pulseExtra);
        dotAt(theta - (TAU/3.0), radius, 5 + pulseExtra);
      }
      
      radius += dRadius;
      theta += dTheta;
    }
    
    popMatrix();
  }
  
  void dotAt(float theta, float radius, float ellipseSize) {
    float x = cos(theta) * radius;
    float y = sin(theta) * radius;
    ellipse(x, y, ellipseSize, ellipseSize);
  }
}