---
layout: default
---

This is a library to read values from the Korg [NanoKontrol2](http://www.korg.com/us/products/controllers/nanokontrol2/), via MIDI. I'd eventually like to add support for the [NanoKey2](http://www.korg.com/us/products/controllers/nanokey2/), and maybe the [NanoPad2](http://www.korg.com/us/products/controllers/nanopad2/).

I made it for use with [Processing](http://processing.org), but it should be usable with any JVM program. It relies on no external libraries.

It's meant to be easy, and not a full-featured MIDI client.

## Project Status: v1, but only Kontrol2.

The Kontrol2 is ready for use, but that's all there is. I own a nanoKeys, so that'll be the next one I work on. (But no ETA yet.)

## Usage

You can read from the sliders, dials, and Solo, Mute, and Record buttons, and all the "Transport" buttons: next track, previous track, stop, record, etc.

The sliders and dials will always yield values between 0 and 127 inclusive.

The Solo, Mute, and Record buttons operate in moment mode, by default - press a button, and the Kontrol2 will report that it's "on," until you release the button. You can switch it to Toggle mode - press a button, and release it, the Kontrol2 will report that it's "on"; press and release it again, and the Kontrol2 will report that it's "off." The buttons will light up whenever they're "on."

    import korgnano.*;
    
    Kontrol2 k2;
    void setup() {
      k2 = new Kontrol2();
    
      // The buttons all default to Momentary mode - they're only "on" while you're
      // pressing them down. Let's set some to Toggle mode.
      k2.recordMode(3, ButtonMode.Toggle);
      k2.soloMode(8, ButtonMode.Toggle);
    } 
    
    void draw() {
      background(
        map(k2.slider(1), 0, 127, 0, 255),
        map(k2.slider(2), 0, 127, 0, 255),
        map(k2.dial(3), 0, 127, 0, 255));
      
      // If the eighth solo button ("S") is pressed, toggle between stroke & fill.
      // Note: it's 1-based, not 0-based.
      if (k2.solo(8)) {
        stroke(255);
        noFill();
      }
      else {
        noStroke();
        fill(255);
      }
      
      // If the seventh mute button ("M") is pressed, draw a circle.
      if (k2.mute(7)) {
        ellipse(width/2, height/2, 50, 50);
      }
      
      // If the third record button ("R") is pressed, toggle RGB & HSB.
      if (k2.record(3)) {
        colorMode(HSB);
      }
      else {
        colorMode(RGB);
      }
    }

## `TODO`

* Maybe add configurable range mapping: instead of 0..127, maybe -1..1, or 0..100, or 0..1, or 0..360, or whatever.
* Add event hooks: onSolo1(), onPlay()...
* Support the NanoKey2, and maybe the NanoPad2.
* ~~[Register with Processing.org](https://github.com/processing/processing/wiki/Library-Basics).~~
