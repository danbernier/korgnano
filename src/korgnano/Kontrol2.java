package korgnano;

import javax.sound.midi.*;
import java.util.Queue;
import java.util.LinkedList;

public class Kontrol2 implements Receiver {
  private MidiDevice inDevice;
  private MidiDevice outDevice;
  private Scene scene;
  private Queue<SceneChange> sceneChanges;

  public Kontrol2() {
    this.inDevice = getNanoKontrol2InputDevice();
    this.outDevice = getNanoKontrol2OutputDevice();
    this.scene = new Scene();
    this.sceneChanges = new LinkedList<SceneChange>();
    requestSceneData();
  }

  private void requestSceneData() {
    sendMessageToDevice(new byte[] {
      (byte)0xf0, // exclusive status
        (byte)0x42, // ...for Korg
        (byte)0x40, // 4g, where g = the Global midi channel. I'm assuming it's 0 here, but that could change.
        (byte)0x00, // "Software Project (nanoKONTROL2: 000113H)". IDK
        (byte)0x01, // no info
        (byte)0x13, // no info
        (byte)0x00, // "Sub ID",
        (byte)0x1f, // 00 = Native mode I/O Request, 1f = Data dump request, 7f = data dump
        (byte)0x10, // 10 = current scene data dump request (if prev byte == 1f)
        (byte)0x00, // sort of an empty placeholder for the data
        (byte)0xf7  // EOX ("End of Exclusive")
    });
  }

  private void sendMessageToDevice(byte[] messageBytes) {
    try {
      SysexMessage message = new SysexMessage(messageBytes, messageBytes.length);
      outDevice.getReceiver().send(message, System.currentTimeMillis());
    } catch(InvalidMidiDataException x) {  // if you borked the message
      System.out.println(x);
    } catch(MidiUnavailableException x) { // sheesh
      System.out.println(x);
    }
  }

  ShortMessage lastMessage;  // TODO can prolly kill that
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

  public void buttonMode(ButtonMode buttonMode) {
    soloMode(buttonMode);
    muteMode(buttonMode);
    recordMode(buttonMode);
  }
  public void buttonMode(int index, ButtonMode buttonMode) {
    soloMode(index, buttonMode);
    muteMode(index, buttonMode);
    recordMode(index, buttonMode);
  }
  public void soloMode(ButtonMode buttonMode) {
    for (int i = 1; i <= 8; i++) {
      soloMode(i, buttonMode);
    }
  }
  public void muteMode(ButtonMode buttonMode) {
    for (int i = 1; i <= 8; i++) {
      muteMode(i, buttonMode);
    }
  }
  public void recordMode(ButtonMode buttonMode) {
    for (int i = 1; i <= 8; i++) {
      recordMode(i, buttonMode);
    }
  }
  public void soloMode(int index, ButtonMode buttonMode) {
    queueSceneChange(scene.group(index).soloButton().setBehavior(buttonMode));
  }
  public void muteMode(int index, ButtonMode buttonMode) {
    queueSceneChange(scene.group(index).muteButton().setBehavior(buttonMode));
  }
  public void recordMode(int index, ButtonMode buttonMode) {
    queueSceneChange(scene.group(index).recordButton().setBehavior(buttonMode));
  }




  // queuing scene writes
  void queueSceneChange(SceneChange sc) {
    System.out.println("Queueing SceneChange. (" + sceneChanges.size() + ")");
    sceneChanges.add(sc);
    if (scene.data != null && sceneChanges.size() == 1) {
      scheduleScenePump();
    }
  }

  void scheduleScenePump() {
    System.out.println("Scheduling Scene Pump in 25ms");
    new ScenePumper(25).run();
  }

  class ScenePumper extends Thread {
    private final int msDelay;
    ScenePumper(int msDelay) {
      this.msDelay = msDelay;
    }
    public void run() {
      try {
        sleep(msDelay);
      } catch (InterruptedException x) {}
      pumpSceneChanges();
    }

    private void pumpSceneChanges() {
      // Get the current size first, in case new messages come in in the meantime.
      while(!sceneChanges.isEmpty()) {
        SceneChange sc = sceneChanges.remove();
        System.out.println("Applying SceneChange...");
        scene.data[sc.byteArrayIndex] = sc.newValue;
      }
      System.out.println("Sending SceneChange request.");
      sendMessageToDevice(scene.data);
    }
  }




  // Receiver methods:
  public void send(MidiMessage midiMessage, long timestamp) {  // send, aka receive
    if (midiMessage.getStatus() == ShortMessage.CONTROL_CHANGE) {  // nanoKONTROL only sends CONTROL messages. (Well. Sysex messages, too.)
      ShortMessage message = (ShortMessage)midiMessage;

      int channelId = message.getData1();
      int messageValue = message.getData2();

      //System.out.println(channelId + ": " + messageValue);

      if (0 <= channelId && channelId <= 7) {
        sliders[channelId] = messageValue;
      }
      else if (16 <= channelId && channelId <= 24) {
        dials[channelId-16] = messageValue;
      }
      else if (32 <= channelId && channelId <= 39) {
        soloButtons[channelId-32] = messageValue == 127;
      }
      else if (48 <= channelId && channelId <= 55) {
        muteButtons[channelId-48] = messageValue == 127;
      }
      else if (64 <= channelId && channelId <= 71) {
        recordButtons[channelId-64] = messageValue == 127;
      }

      lastMessage = message;
      lastMessageTimestamp = timestamp;
    }
    else {
      byte[] data = midiMessage.getMessage();

      if (midiMessage.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE) {
        if (data[7] == 0x7f && data[12] == 0x40) {
          scene.setData(data);
          scheduleScenePump();
          //scene.display();
        }
      }
    }
  }

  public void close() {
    this.inDevice.close();
    this.outDevice.close();
  }

  // initialization methods
  private MidiDevice getNanoKontrol2InputDevice() {
    return getNanoKontrol2Device(true);
  }
  private MidiDevice getNanoKontrol2OutputDevice() {
    return getNanoKontrol2Device(false);
  }
  private MidiDevice getNanoKontrol2Device(boolean forInput) {
    for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
      if (info.getName().indexOf("nanoKONTROL2") == 0) { // [hw:2,0,0]  <- the '2' is, like, the USB port #
        MidiDevice device = null;
        try {
          device = MidiSystem.getMidiDevice(info);
          if (!device.isOpen()) {
            device.open();
          }

          if (forInput && device.getMaxTransmitters() != 0) {  // -1 means unlimited. only == 0 is bad.
            device.getTransmitter().setReceiver(this); // Wire us up to this device, so our #send() method is called.
            return device; // Return it only so we can close() it when we're done.
          }
          else if (!forInput && device.getMaxReceivers() != 0) {  // -1 means unlimited. only == 0 is bad.
            // Nothing to do; we'll send messages to it ourselves.
            return device; // Return it so we can send messages to it, and close() it when we're done.
          }
        }
        catch(MidiUnavailableException x) {
          System.err.println("Tried to connect to this Midi device: " + device + " but it looks like Midi is unavailable");
          x.printStackTrace();
          return null;
        }
      }
    }
    System.err.println("Couldn't find the korg nanoKontrol2!");
    return null;
  }
}
