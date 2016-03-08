# Getting LEDs to work: where I'm at

Seems you have to get the scene data, edit it, and write it back. There's a
great table in `docs/nanoKONTROL2_MIDIimp.txt`, which should help you figure it
out.


# Docs

Great source of korg docs: http://www.korg.com/us/support/download/manual/0/159/1912/

Summary of MIDI messages: https://www.midi.org/specifications/item/table-1-summary-of-midi-message

MidiMessage javadocs: https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/MidiMessage.html
ShortMessage: http://docs.oracle.com/javase/7/docs/api/javax/sound/midi/ShortMessage.html


# Other People's Fixes

## LogicPro

How someone got LED buttons to work in LogicPro: http://www.logicprohelp.com/forum/viewtopic.php?f=17&t=80257
Summary:
    Four things must be done for it to work:
    1.) Button type must be set to Toggle in MS.
    2.) "Send value to" must be set to the device input.
    3.) Button type must be set in Korg KONTROL Editor.
    4.) "LED Control" set to External in KKE.
    "...for some bizarre reason the nanoKONTROL2 requires you to use the KKE to enable it to listen to LED messages via MIDI."

## MaxMSP

Here's a MaxMSP patch:
http://nickhwang.com/2012/12/27/korg-nanokontrol2-and-maxmsp/
Mainly interesting now, because it confirms that you have to write the scene data.


# How to Reset it

How to reset the nanokontrol if you bork it:
* disconnect it from the USB, so it powers down
* press & hold PREVIOUS-TRACK, NEXT-TRACK, and CYCLE buttons
* plug the nanokontrol back into USB, and let go

The transport buttons (except CYCLE) will blink. Give it a few seconds to boot. Keep it plugged in for a bit.
