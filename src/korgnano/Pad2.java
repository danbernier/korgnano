package korgnano;

import javax.sound.midi.*;

public class Pad2 implements Receiver {
  private MidiDevice inDevice;
  private MidiDevice outDevice;
  private Scene scene;
  private boolean debug;

  public Pad2() {
    this(false);
  }
  public Pad2(boolean debug) {
    this.debug = debug;

    this.inDevice = getNanoPad2InputDevice();
    this.outDevice = getNanoPad2OutputDevice();
    this.scene = new Scene();
  }

  private void sendMessageToDevice(byte[] messageBytes) {
    debug("Sending message: ...");
    try {
      SysexMessage message = new SysexMessage(messageBytes, messageBytes.length);

      // Always send 0 for the current timestamp. According to themidibus' README,
      // passing the timestamps breaks MMJ, and passing 0 doesn't seem to affect
      // it on Linux, so, just do that. If we find a reason to send actual
      // timestamps, we'll reconsider.
      outDevice.getReceiver().send(message, 0);
      // outDevice.getReceiver().send(message, System.currentTimeMillis());

    } catch(InvalidMidiDataException x) {  // if you borked the message
      System.out.println(x);
    } catch(MidiUnavailableException x) { // sheesh
      System.out.println(x);
    }
  }




  // Receiver methods:
  public void send(MidiMessage midiMessage, long timestamp) {  // send, aka receive
    debug("WE GOT ONE! " + midiMessage.getStatus() + " " + ShortMessage.CONTROL_CHANGE);
    if (midiMessage.getStatus() == ShortMessage.CONTROL_CHANGE) {  // nanoKONTROL only sends CONTROL messages. (Well. Sysex messages, too.)
      ShortMessage message = (ShortMessage)midiMessage;

      int channelId = message.getData1();
      int messageValue = message.getData2();

      debug(channelId + ": " + messageValue);

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
      else if (channelId == 58) { prevTrack = messageValue == 127; }
      else if (channelId == 59) { nextTrack = messageValue == 127; }
      else if (channelId == 60) { setMarker = messageValue == 127; }
      else if (channelId == 61) { prevMarker = messageValue == 127; }
      else if (channelId == 62) { nextMarker = messageValue == 127; }
      else if (channelId == 46) { cycle = messageValue == 127; }
      else if (channelId == 43) { rewind = messageValue == 127; }
      else if (channelId == 44) { fastForward = messageValue == 127; }
      else if (channelId == 42) { stop = messageValue == 127; }
      else if (channelId == 41) { play = messageValue == 127; }
      else if (channelId == 45) { record = messageValue == 127; }
    }
    else {
      byte[] data = midiMessage.getMessage();
      debug("Got a message..." + data.length + " bytes long");

      if (midiMessage.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE) {
        debug("Got a scene dump!");
        if (data[7] == 0x7f && data[12] == 0x40) {
          scene.setData(data);
          scheduleScenePump();
          // if (debug) { scene.display(); }
        }
      }
    }
  }

  /* prolly wise to keep this one around:
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
  */

  public void close() {
    this.inDevice.close();
    this.outDevice.close();
  }

  // initialization methods
  private MidiDevice getNanoPad2InputDevice() {
    return getNanoPad2Device(true);
  }
  private MidiDevice getNanoPad2OutputDevice() {
    return getNanoPad2Device(false);
  }
  private MidiDevice getNanoPad2Device(boolean forInput) {
    for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
      if (info.getName().indexOf("nanoKONTROL2") >= 0 ||
          info.getDescription().indexOf("nanoKONTROL2") >= 0) {
        MidiDevice device = null;
        debug("Trying to open MIDI device (for " + (forInput ? "input" : "output") + "): " + info.getName() + " :: " + info.getDescription());
        try {
          device = MidiSystem.getMidiDevice(info);
          if (!device.isOpen()) {
            debug("  opening the device...");
            device.open();
          }

          if (forInput && device.getMaxTransmitters() != 0) {  // -1 means unlimited. only == 0 is bad.
            debug("  setting THIS as the receiver...");
            device.getTransmitter().setReceiver(this); // Wire us up to this device, so our #send() method is called.
            return device; // Return it only so we can close() it when we're done.
          }
          else if (!forInput && device.getMaxReceivers() != 0) {  // -1 means unlimited. only == 0 is bad.
            debug("  using this device for output!");
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
    System.err.println("Couldn't find the korg nanoPad2!");
    return null;
  }

  private void debug(String message) {
    if (this.debug) {
      System.out.println(message);
    }
  }
}
