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

      int channelId = message.getData1();
      int messageValue = message.getData2();

      if (0 <= channelId && channelId <= 7) {
        sliders[channelId] = messageValue;
      }
      else if (16 <= channelId && channelId <= 24) {
        dials[channelId-16] = messageValue;
      }
      else if (32 <= channelId && channelId <= 39) {
        // only on when you're holding it:
        //soloButtons[channelId-32] = messageValue == 127;

        // toggle when you press down:
        if (messageValue == 127) { soloButtons[channelId-32] = !soloButtons[channelId-32]; }

        // toggle when you release:
        //if (messageValue == 0) { soloButtons[channelId-32] = !soloButtons[channelId-32]; }
      }
      else if (48 <= channelId && channelId <= 55) {
        if (messageValue == 127) { muteButtons[channelId-48] = !muteButtons[channelId-48]; }
      }
      else if (64 <= channelId && channelId <= 71) {
        if (messageValue == 127) { recordButtons[channelId-64] = !recordButtons[channelId-64]; }
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
