package korgnano;

class SceneChange {
  int byteArrayIndex;
  byte newValue;

  SceneChange(int byteArrayIndex, byte newValue) {
    this.byteArrayIndex = byteArrayIndex;
    this.newValue = newValue;
  }
}
