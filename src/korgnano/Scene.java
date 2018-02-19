package korgnano;

class Scene {
  byte[] data;
  Group[] groups;
  TransportButton prevTrack;
  TransportButton nextTrack;
  TransportButton setMarker;
  TransportButton prevMarker;
  TransportButton nextMarker;
  TransportButton cycle;
  TransportButton rewind;
  TransportButton fastForward;
  TransportButton stop;
  TransportButton play;
  TransportButton record;
  
  Scene() {
    groups = new Group[8];
    for (int i = 0; i < 8; i++) {
      groups[i] = new Group(i+1);
    }
    
    prevTrack = new TransportButton(0, "PrevTrack");
    nextTrack = new TransportButton(1, "NextTrack");
    setMarker = new TransportButton(3, "Marker Set");
    prevMarker = new TransportButton(4, "PrevMarker");
    nextMarker = new TransportButton(5, "NextMarker");
    cycle = new TransportButton(2, "Cycle");
    rewind = new TransportButton(6, "Rewind");
    fastForward = new TransportButton(7, "FastForward");
    stop = new TransportButton(8, "Stop");
    play = new TransportButton(9, "Play");
    record = new TransportButton(10, "Record");
  }

  // This should only be called once the Kontrol2 gets a scene-dump response.
  void setData(byte[] data) {
    this.data = data;
  }
  
  void display() {
    System.out.println("Global midi channel: " + globalMidiChannel());
    System.out.println("Control Mode: " + controlMode());
    System.out.println("LED Mode: " + ledMode());
    
    for (int groupNum = 1; groupNum <= 8; groupNum++) {
      System.out.println();
      group(groupNum).display();
    }
    
    System.out.println();
    System.out.println("Transport button MIDI channel: " + transportButtonMidiChannel());
    prevTrack().display();
    nextTrack().display();
    setMarker().display();
    prevMarker().display();
    nextMarker().display();
    cycle().display();
    rewind().display();
    fastForward().display();
    stop().display();
    play().display();
    record().display();
  }
  
  int globalMidiChannel() { return (int)p(0); }
  String controlMode() {
    return pick(p(1), "CC Mode", "Cubase", "DP", "Live", "ProTools", "SONAR"); 
  }  
  String ledMode() { return pick(p(2), "Internal", "External"); }
  int channelMidiChannel() { return p(0); }
  
  Group group(int groupNum) {
    return groups[groupNum-1];
  }
  
  class Group {
    // groupNum is 1-based: 1..8
    int groupNum;
    GradualControl slider;
    GradualControl knob;
    Button solo;
    Button mute;
    Button record;
    Group(int groupNum) {
      this.groupNum = groupNum;
      slider = new GradualControl(1, "Slider");
      knob = new GradualControl(7, "Knob");
      solo = new Button(13, "Solo");
      mute = new Button(19, "Mute");
      record = new Button(25, "Record");
    }
    
    void display() {
      System.out.println("Group " + groupNum);
      System.out.println("Group midi channel: " + midiChannel());
      slider().display();
      knob().display();
      soloButton().display();
      muteButton().display();
      recordButton().display();
    }
    
    int midiChannel() { return groupP(0); }
    
    GradualControl slider() { return slider; }
    GradualControl knob() { return knob; }
    Button soloButton() { return solo; }
    Button muteButton() { return mute; }
    Button recordButton() { return record; }
    
    class GradualControl {
      int paramOffsetInGroup;
      String label;
      GradualControl(int pOff, String label) { 
        paramOffsetInGroup = pOff; 
        this.label = label;
      }
      
      void display() {
        System.out.println(label + " assign type: " + assignType());
        System.out.println(label + " note number: " + noteNumber());
        System.out.println(label + " range: " + minValue() + ".." + maxValue());
      }
      
      String assignType() {
        return pick(groupP(paramOffsetInGroup + 0), "Disabled", "Enabled");
      }
      int noteNumber() { return groupP(paramOffsetInGroup + 2); }
      int minValue() { return groupP(paramOffsetInGroup + 3); }
      int maxValue() { return groupP(paramOffsetInGroup + 4); }
    }
    
    class Button {
      int paramOffsetInGroup;
      String label;
      Button(int pOff, String label) { 
        paramOffsetInGroup = pOff; 
        this.label = label;
      }
      
      void display() {                                      
        System.out.println(label + " Button assign type    : " + assignType());
        System.out.println(label + " Button behavior       : " + behavior());
        System.out.println(label + " Button note number    : " + noteNumber());
        System.out.println(label + " Button trigger values : " + offValue() + ".." + onValue());
      }
      
      String assignType() {
       return pick(groupP(paramOffsetInGroup + 0), "No Assign", "CC", "Note");
      }
      String behavior() {
       return pick(groupP(paramOffsetInGroup + 1), "Momentary", "Toggle");
      }
      int noteNumber() { return groupP(paramOffsetInGroup + 2); }
      int offValue() { return groupP(paramOffsetInGroup + 3); }
      int onValue() { return groupP(paramOffsetInGroup + 4); }
      
      SceneChange setBehavior(ButtonMode newMode) {
        boolean momentary = newMode == ButtonMode.Momentary;
        byte newByteVal = (byte)(momentary ? 0x00 : 0x01);
        return writeToScene(groupPNum(paramOffsetInGroup + 1), newByteVal);
      }
    }
    
    private byte groupP(int groupParamNum) {
      return p(groupPNum(groupParamNum));
    }
    private int groupPNum(int groupParamNum) {
      // groupNum = 1-based: 1..8
      int groupIndex = groupNum - 1;
      int getPastFirstThreeVals = 3;
      int numParamsPerGroup = 31;
      int paramNum = getPastFirstThreeVals + (groupIndex * numParamsPerGroup) + groupParamNum;
      return paramNum;
    }
  }
  
  
  int transportButtonMidiChannel() { return p(251); }
  TransportButton prevTrack() { return prevTrack; }
  TransportButton nextTrack() { return nextTrack; }
  TransportButton setMarker() { return setMarker; }
  TransportButton prevMarker() { return prevMarker; }
  TransportButton nextMarker() { return nextMarker; }
  TransportButton cycle() { return cycle; }
  TransportButton rewind() { return rewind; }
  TransportButton fastForward() { return fastForward; }
  TransportButton stop() { return stop; }
  TransportButton play() { return play; }
  TransportButton record() { return record; }
  
  class TransportButton {
    int paramOffset;
    String label;
    TransportButton(int paramOffset, String label) {
      this.paramOffset = 252 + (paramOffset * 6);
      this.label = label;
    }
    
    void display() {
      System.out.println(label + " assign type:     " + assignType());
      System.out.println(label + " button behavior: " + buttonBehavior());
      System.out.println(label + " note number:     " + noteNumber());
      System.out.println(label + " trigger values:  " + offValue() + ".." + onValue());
    }
    
    String assignType() { return pick(p(paramOffset + 0), "No Assign", "CC", "Note"); }
    String buttonBehavior() { return pick(p(paramOffset + 1), "Momentary", "Toggle"); }
    int noteNumber() { return p(paramOffset + 2); }
    int offValue() { return p(paramOffset + 3); }
    int onValue() { return p(paramOffset + 4); }
  }
  
  // 'p' = short for 'parameter'
  private byte p(int paramNumber) {
    return data[paramNumberToByteIndex(paramNumber)];
  }
  
  /*
  private void setP(int paramNumber, byte newVal) {
    System.out.println("changing " + paramNumber + " from " + 
      data[paramNumberToByteIndex(paramNumber)] +
      " to " + newVal);
    
    data[paramNumberToByteIndex(paramNumber)] = newVal;
    System.out.println(data[paramNumberToByteIndex(paramNumber)]);
  }
  */

  private SceneChange writeToScene(int paramNumber, byte newValue) {
    //System.out.println("gonna request a change... " + paramNumber + " from " + 
    //  data[paramNumberToByteIndex(paramNumber)] +
    //  " to " + newValue);
    
    int byteArrayIndex = paramNumberToByteIndex(paramNumber);
    return new SceneChange(byteArrayIndex, newValue);
  }
  
  private int paramNumberToByteIndex(int paramNumber) {
    int indexBump = paramNumber / 7; // int division =~ floor
    int headerLength = 13;
    int index = headerLength + paramNumber + indexBump + 1;
    return index;
  }
  
  private String pick(int index, String... choices) {
    return choices[index];
  }
}
