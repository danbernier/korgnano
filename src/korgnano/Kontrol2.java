package korgnano;

import javax.sound.midi.*;

public class Kontrol2 implements Receiver {
  private MidiDevice device;
  public Kontrol2() {
    this.device = getNanoKontrol2Device();
  }

  ShortMessage lastMessage;
  long lastMessageTimestamp;

  private int[] sliders = new int[8];
  private int[] dials = new int[8];
  private boolean[] soloButtons = new boolean[8];
  private boolean[] muteButtons = new boolean[8];
  private boolean[] recordButtons = new boolean[8];

  public int slider(int index) {
    return sliders[index-1];
  }
  public int dial(int index) {
    return dials[index-1];
  }
  public boolean solo(int index) {
    return soloButtons[index-1];
  }
  public boolean mute(int index) {
    return muteButtons[index-1];
  }
  public boolean record(int index) {
    return recordButtons[index-1];
  }

  // Receiver methods:
  public void send(MidiMessage midiMessage, long timestamp) {  // send, aka receive
    if (midiMessage.getStatus() == ShortMessage.CONTROL_CHANGE) {  // nanoKONTROL only sends CONTROL messages.
      ShortMessage message = (ShortMessage)midiMessage;

      int data1 = message.getData1();
      int data2 = message.getData2();

      if (0 <= data1 && data1 <= 7) {
        sliders[data1] = data2;
      }
      else if (16 <= data1 && data1 <= 24) {
        dials[data1-16] = data2;
      }
      else if (32 <= data1 && data1 <= 39) {
        // only on when you're holding it:
        //soloButtons[data1-32] = data2 == 127;

        // toggle when you press down:
        if (data2 == 127) { soloButtons[data1-32] = !soloButtons[data1-32]; }

        // toggle when you release:
        //if (data2 == 0) { soloButtons[data1-32] = !soloButtons[data1-32]; }
      }
      else if (48 <= data1 && data1 <= 55) {
        if (data2 == 127) { muteButtons[data1-48] = !muteButtons[data1-48]; }
      }
      else if (64 <= data1 && data1 <= 71) {
        if (data2 == 127) { recordButtons[data1-64] = !recordButtons[data1-64]; }
      }

      lastMessage = message;
      lastMessageTimestamp = timestamp;
    }
  }
  public void close() {
  }

  // initialization methods
  private MidiDevice getNanoKontrol2Device() {
    MidiDevice.Info info = getNanoKontrol2DeviceInfo();
    if (info != null) {
      try {
        MidiDevice device = MidiSystem.getMidiDevice(info);
        if (!device.isOpen()) {
          device.open();
        }

        device.getTransmitter().setReceiver(this);
        //device.getTransmitter().setReceiver(device.getReceiver());

        return device;
      }
      catch(MidiUnavailableException x) {
        System.err.println("Tried to connect to the Midi device, but it looks like Midi is unavailable");
        return null;
      }
    }
    return null;
  }

  private MidiDevice.Info getNanoKontrol2DeviceInfo() {
    //println(MidiSystem.getMidiDeviceInfo());
    for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
      //println(info.getName());
      //println(info.getDescription());
      if (info.getName().indexOf("nanoKONTROL2") == 0) { // [hw:2,0,0]  <- the '2' is, like, the channel
        return info;
      }
    }
    System.err.println("Couldn't find the korg nanoKontrol2!");
    return null;
  }
}
