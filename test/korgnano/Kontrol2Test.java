package korgnano;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.midi.*;

public class Kontrol2Test {
  @Test
  public void testSendingMessages() throws InvalidMidiDataException {
    Kontrol2 nano = new Kontrol2();

    for (int position = 1; position <= 8; position++) {
      int channel = position - 1;
      assertEquals(0, nano.slider(position));
      for (int value = 1; value < 128; value++) {
        send(nano, channel, value);
        assertEquals(value, nano.slider(position));
      }

      assertEquals(0, nano.dial(position));
      for (int value = 1; value < 128; value++) {
        send(nano, channel+16, value);
        assertEquals(value, nano.dial(position));
      }

      // Buttons default to ButtonMode.MOMENT.
      assertFalse(nano.solo(position));
      send(nano, channel+32, 127);  // push solo
      assertTrue(nano.solo(position));
      send(nano, channel+32, 0);  // release solo
      assertFalse(nano.solo(position));

      assertFalse(nano.mute(position));
      send(nano, channel+48, 127);  // push mute
      assertTrue(nano.mute(position));
      send(nano, channel+48, 0);  // release mute
      assertFalse(nano.mute(position));

      assertFalse(nano.record(position));
      send(nano, channel+64, 127);  // push record
      assertTrue(nano.record(position));
      send(nano, channel+64, 0);  // release record
      assertFalse(nano.record(position));
      
      // Switch to Toggle mode.
      nano.soloMode(position, ButtonMode.TOGGLE);
      nano.muteMode(position, ButtonMode.TOGGLE);
      nano.recordMode(position, ButtonMode.TOGGLE);

      assertFalse(nano.solo(position));
      send(nano, channel+32, 127);  // push solo
      send(nano, channel+32, 0);  // release solo
      assertTrue(nano.solo(position));

      assertFalse(nano.mute(position));
      send(nano, channel+48, 127);  // push mute
      send(nano, channel+48, 0);  // release mute
      assertTrue(nano.mute(position));

      assertFalse(nano.record(position));
      send(nano, channel+64, 127);  // push record
      send(nano, channel+64, 0);  // release record
      assertTrue(nano.record(position));
    }
  }

  @Test
  public void testSettingEachButtonModeByPosition() throws InvalidMidiDataException {
    // This will just check the modes directly, rather than test that the
    // buttonmodes are actually honored.
    Kontrol2 nano = new Kontrol2();

    // test setting them  by position
    for (int position = 1; position <= 8; position++) {
      // defaults to MOMENT:
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));

      // you can turn on TOGGLE:
      nano.soloMode(position, ButtonMode.TOGGLE);
      assertEquals(ButtonMode.TOGGLE, nano.soloMode(position));

      nano.muteMode(position, ButtonMode.TOGGLE);
      assertEquals(ButtonMode.TOGGLE, nano.muteMode(position));

      nano.recordMode(position, ButtonMode.TOGGLE);
      assertEquals(ButtonMode.TOGGLE, nano.recordMode(position));

      // you can set it back to MOMENT:
      nano.soloMode(position, ButtonMode.MOMENT);
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));

      nano.muteMode(position, ButtonMode.MOMENT);
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));

      nano.recordMode(position, ButtonMode.MOMENT);
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }
  }

  @Test
  public void testSettingAllButtonModesByPosition() throws InvalidMidiDataException {
    // This will just check the modes directly, rather than test that the
    // buttonmodes are actually honored.
    Kontrol2 nano = new Kontrol2();

    // test setting them  by position
    for (int position = 1; position <= 8; position++) {
      // defaults to MOMENT:
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));

      // you can turn on TOGGLE:
      nano.buttonMode(position, ButtonMode.TOGGLE);
      assertEquals(ButtonMode.TOGGLE, nano.soloMode(position));
      assertEquals(ButtonMode.TOGGLE, nano.muteMode(position));
      assertEquals(ButtonMode.TOGGLE, nano.recordMode(position));

      // you can set it back to MOMENT:
      nano.buttonMode(position, ButtonMode.MOMENT);
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }
  }

  @Test
  public void testSettingAllButtonModesByType() throws InvalidMidiDataException {
    // This will just check the modes directly, rather than test that the
    // buttonmodes are actually honored.
    Kontrol2 nano = new Kontrol2();

    // test setting them  by position
    for (int position = 1; position <= 8; position++) {
      // defaults to MOMENT:
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }

    // you can turn on TOGGLE:
    nano.soloMode(ButtonMode.TOGGLE);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.TOGGLE, nano.soloMode(position));
    }

    nano.muteMode(ButtonMode.TOGGLE);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.TOGGLE, nano.muteMode(position));
    }

    nano.recordMode(ButtonMode.TOGGLE);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.TOGGLE, nano.recordMode(position));
    }

    // you can set it back to MOMENT:
    nano.soloMode(ButtonMode.MOMENT);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
    }

    nano.muteMode(ButtonMode.MOMENT);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
    }

    nano.recordMode(ButtonMode.MOMENT);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }
  }

  @Test
  public void testSettingAllButtonModesAtOnce() throws InvalidMidiDataException {
    // This will just check the modes directly, rather than test that the
    // buttonmodes are actually honored.
    Kontrol2 nano = new Kontrol2();

    // test setting them  by position
    for (int position = 1; position <= 8; position++) {
      // defaults to MOMENT:
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }

    // you can turn on TOGGLE:
    nano.buttonMode(ButtonMode.TOGGLE);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.TOGGLE, nano.soloMode(position));
      assertEquals(ButtonMode.TOGGLE, nano.muteMode(position));
      assertEquals(ButtonMode.TOGGLE, nano.recordMode(position));
    }

    // you can set it back to MOMENT:
    nano.buttonMode(ButtonMode.MOMENT);
    for (int position = 1; position <= 8; position++) {
      assertEquals(ButtonMode.MOMENT, nano.soloMode(position));
      assertEquals(ButtonMode.MOMENT, nano.muteMode(position));
      assertEquals(ButtonMode.MOMENT, nano.recordMode(position));
    }
  }
  private void send(Kontrol2 nano, int data1, int data2) 
    throws InvalidMidiDataException 
  {
    ShortMessage message = new ShortMessage(
        ShortMessage.CONTROL_CHANGE, data1, data2);
    nano.send(message, System.currentTimeMillis());
  }
}
