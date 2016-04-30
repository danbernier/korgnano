// Just a simple class to encapsulate a Pulse.
class Pulse {
  float amp;
  float damp;
  float value;
  long timestamp;
  long timeTillNextPulse;
  long timeBetweenPulses;

  Pulse(float bpm, float amp, float damp) {
    setBpm(bpm);
    this.amp = amp;
    this.damp = damp;
    value = 1;
  }

  void update() {
    long elapsed = millis() - timestamp;
    timestamp = millis();
    timeTillNextPulse -= elapsed;

    if (timeTillNextPulse < 0) {
      value = 1;
      timeTillNextPulse = timeBetweenPulses;
    } else {
      value *= damp;
    }
  }

  float value() {
    return value * amp;
  }

  void setBpm(float bpm) {
    timeBetweenPulses = round(60000.0 / bpm);
  }
  
  void setAmp(float amp) {
    this.amp = amp;
  }
  
  float getAmp() {
    return this.amp;
  }
}