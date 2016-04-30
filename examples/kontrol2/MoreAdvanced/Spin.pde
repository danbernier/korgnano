// Just a simple class to encapsulate spin.
class Spin {
  float theta = 0;
  float dTheta = 0.1;

  Spin(float spinSpeed) {
    dTheta = spinSpeed;
  }

  void update() {
    theta += dTheta;
    dTheta = map(nano.dial(7), 0, 127, -1, 1);
  }
  
  float angle() {
    return theta;
  }
}