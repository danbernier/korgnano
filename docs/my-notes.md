# Getting LEDs to work: where I'm at

Seems you have to get the scene data, edit it, and write it back. There's a
great table in `docs/nanoKONTROL2_MIDIimp.txt`, which should help you figure it
out.

Ok, 1 byte = 8 bits = 2^8 = 256. 255 in hex == 'ff', so 1 full byte = 'ff'.
But the docs keep saying stuff like this:
> 9th Byte = nn : 2 Bytes Format: Function ID, Variable: Num of Data
Which I'm reading as: it's one byte (8 bits), but it holds two "bytes" of data (pieces of info),
which, I guess, are maybe 4 bits? Midi does that a lot - two values (0-16 and 0-16, or 0-1 and 0-127) in one byte. Maybe that's what's going on. This is also maybe corroborated by the docs that say "8bit x 7Byte" and "7bit x 8Byte"?

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






# Some data from sending Exclusive Messages

Raw Midi Data Received:
--------
Status Byte/MIDI Command:240
Param 2: 42  // korg
Param 3: 40  // 4g, g = global channel id
Param 4: 0   // software project
Param 5: 1    
Param 6: 13
Param 7: 0 
Param 8: 5f   // data dump command
Param 9: 24  // data load error!?
Param 10: 0  // data: empty
Param 11: f7  // EOX


Raw Midi Data Received:
--------
Status Byte/MIDI Command:240
```
    f0   42   40   00   01   13   00   7f      7f   02   03   05   40 [ 00   00   00 ]
    
  [ 00   10   01   00   00   00   00   7f      00   01   00   10   00   00   7f   00      
    01   00   20   00   7f   00   00   01      00   30   00   7f   00   00   01] [00      
    40   00   7f   00   10   00   01   00      01   00   7f   00   01   00   00   11      
    00   7f   00   01   00   00   21   00      7f   00   01   00   31   00]  00   7f      
    00   01   00   41   00   00   7f   00      10   01   00   02   00   00   7f   00      
    01   00   12   00   7f   00   00   01      00   22   00   7f   00   00   01   00      
    32   00   7f   00   01   00   00   42      00   7f   00   10   01   00   00   03      
    00   7f   00   01   00   00   13   00      7f   00   01   00   23   00   00   7f      
    00   01   00   33   00   00   7f   00      01   00   43   00   7f   00   00   10      
    01   00   04   00   7f   00   00   01      00   14   00   7f   00   00   01   00      
    24   00   7f   00   01   00   00   34      00   7f   00   01   00   00   44   00      
    7f   00   10   01   00   00   05   00      7f   00   01   00   15   00   00   7f      
    00   01   00   25   00   00   7f   00      01   00   35   00   7f   00   00   01      
    00   45   00   7f   00   00   10   01      00   06   00   7f   00   00   01   00      
    16   00   7f   00   01   00   00   26      00   7f   00   01   00   00   36   00      
    7f   00   01   00   46   00   00   7f      00   10   01   00   07   00   00   7f      
    00   01   00   17   00   00   7f   00      01   00   27   00   7f   00   00   01      
    00   37   00   7f   00   00   01   00      47   00   7f   00   10   00   01   00      
    3a   00   7f   00   01   00   00   3b      00   7f   00   01   00   00   2e   00      
    7f   00   01   00   3c   00   00   7f      00   01   00   3d   00   00   7f   00      
    01   00   3e   00   7f   00   00   01      00   2b   00   7f   00   00   01   00      
    2c   00   7f   00   01   00   00   2a      00   7f   00   01   00   00   29   00      
    7f   00   01   00   2d   00   00   7f      00   7f   7f   7f   7f   00   7f   00      
    00   00   00   00   00   00   00   00      00   00   00   00   00   00   00   00      
    00   f7  
```


```
Raw Midi Data Received:
--------
Status Byte/MIDI Command:240
f0 42 40 00 01 13 00 7f 7f 02 03 05 40 
00 00 00 
-- 
10 01 -- -- -- -- 7f 
-- 01 -- 10 -- -- 7f 
-- 01 -- 20 -- 7f -- 
-- 01 -- 30 -- 7f -- 
-- 01 -- 40 -- 7f -- 
10 -- 01 -- 01 -- 7f 
-- 01 -- -- 11 -- 7f 
-- 01 -- -- 21 -- 7f 
-- 01 -- 31 -- -- 7f 
-- 01 -- 41 -- -- 7f 
-- 10 01 -- 02 -- -- 7f -- 01 -- 12 -- 7f -- -- 01 -- 22 -- 7f -- 
-- 01 -- 32 -- 7f -- 01 -- -- 42 -- 7f -- 10 01 -- -- 03 -- 7f -- 01 -- -- 13 -- 7f -- 01 -- 
23 -- -- 7f -- 01 -- 33 -- -- 7f -- 01 -- 43 -- 7f -- -- 10 01 -- 04 -- 7f -- -- 01 -- 14 -- 
7f -- -- 01 -- 24 -- 7f -- 01 -- -- 34 -- 7f -- 01 -- -- 44 -- 7f -- 10 01 -- -- 05 -- 7f -- 
01 -- 15 -- -- 7f -- 01 -- 25 -- -- 7f -- 01 -- 35 -- 7f -- -- 01 -- 45 -- 7f -- -- 10 01 -- 
06 -- 7f -- -- 01 -- 16 -- 7f -- 01 -- -- 26 -- 7f -- 01 -- -- 36 -- 7f -- 01 -- 46 -- -- 7f 

-- 10 01 -- 07 -- -- 7f -- 01 -- 17 -- -- 7f -- 01 -- 27 -- 7f -- -- 01 -- 37 -- 7f -- -- 01 
-- 47 -- 7f -- 10 -- 01 -- 3a -- 7f -- 01 -- -- 3b -- 7f -- 01 -- -- 2e -- 7f -- 01 -- 3c -- 
-- 7f -- 01 -- 3d -- -- 7f -- 01 -- 3e -- 7f -- -- 01 -- 2b -- 7f -- -- 01 -- 2c -- 7f -- 01 
-- -- 2a -- 7f -- 01 -- -- 29 -- 7f -- 01 -- 2d -- -- 7f -- 7f 7f 7f 7f -- 7f -- -- -- -- -- 

-- -- -- -- -- -- -- -- -- -- -- -- -- f7 
402
```


Raw Midi Data Received:
--------
Status Byte/MIDI Command:240
Param 1: f0  // exclusive
Param 2: 42  // korg
Param 3: 40  // 4g, g = global midi channel ID
Param 4: 00  // software project
Param 5: 01  // sub id = 00
Param 6: 13  
Param 7: 00
Param 8: 7f  // datadump command

Param 9: 7f  // "Over 0x7f data"
Param 10: 2  // these seem like message metadata: "2bytes structure"
Param 11: 3  // these seem like message metadata: "num of data MSB (1+388 bytes: B'110000101)"
Param 12: 5  // these seem like message metadata: "num of data LSB"
Param 13: 40  // current scene data dump. Data starts w/ next byte:

// common parameters
Param 14: 00  // global midi channel
              // control mode: 0=cc (rest: cubase, DP, live, protools, sonar)
Param 15: 00  // LED mode (0/1: internal/external)
              // 0 = 

Param 16: 0  

// controller group 1 params
Param 17: 00   // group midi channel
Param 18: 10
Param 19: 01  // group #?
Param 20: 00  // slider assign disabled
Param 21: 00  // reserved
Param 22: 00  // slider note #
Param 23: 00  // slider min
Param 24: 7f  // slider max
Param 25: 00  // reserved
Param 26: 01  // knob assign enabled
Param 27: 00  // reserved
Param 28: 10  // knob note number
Param 29: 00  // knob min value
Param 30: 00  // knob max value
Param 31: 7f
Param 32: 00
Param 33: 01
Param 34: 00
Param 35: 20
Param 36: 00
Param 37: 7f
Param 38: 00
Param 39: 00
Param 40: 01
Param 41: 00
Param 42: 30
Param 43: 00
Param 44: 7f
Param 45: 00
Param 46: 00
Param 47: 01
Param 48: 00
Param 49: 40
Param 50: 0
Param 51: 7f
Param 52: 0
Param 53: 10
Param 54: 0
Param 55: 1
Param 56: 0
Param 57: 1
Param 58: 0
Param 59: 7f
Param 60: 0
Param 61: 1
Param 62: 0
Param 63: 0
Param 64: 11
Param 65: 0
Param 66: 7f
Param 67: 0
Param 68: 1
Param 69: 0
Param 70: 0
Param 71: 21
Param 72: 0
Param 73: 7f
Param 74: 0
Param 75: 1
Param 76: 0
Param 77: 31
Param 78: 0
Param 79: 0
Param 80: 7f
Param 81: 0
Param 82: 1
Param 83: 0
Param 84: 41
Param 85: 0
Param 86: 0
Param 87: 7f
Param 88: 0
Param 89: 10
Param 90: 1
Param 91: 0
Param 92: 2
Param 93: 0
Param 94: 0
Param 95: 7f
Param 96: 0
Param 97: 1
Param 98: 0
Param 99: 12
Param 100: 0
Param 101: 7f
Param 102: 0
Param 103: 0
Param 104: 1
Param 105: 0
Param 106: 22
Param 107: 0
Param 108: 7f
Param 109: 0
Param 110: 0
Param 111: 1
Param 112: 0
Param 113: 32
Param 114: 0
Param 115: 7f
Param 116: 0
Param 117: 1
Param 118: 0
Param 119: 0
Param 120: 42
Param 121: 0
Param 122: 7f
Param 123: 0
Param 124: 10
Param 125: 1
Param 126: 0
Param 127: 0
Param 128: 3
Param 129: 0
Param 130: 7f
Param 131: 0
Param 132: 1
Param 133: 0
Param 134: 0
Param 135: 13
Param 136: 0
Param 137: 7f
Param 138: 0
Param 139: 1
Param 140: 0
Param 141: 23
Param 142: 0
Param 143: 0
Param 144: 7f
Param 145: 0
Param 146: 1
Param 147: 0
Param 148: 33
Param 149: 0
Param 150: 0
Param 151: 7f
Param 152: 0
Param 153: 1
Param 154: 0
Param 155: 43
Param 156: 0
Param 157: 7f
Param 158: 0
Param 159: 0
Param 160: 10
Param 161: 1
Param 162: 0
Param 163: 4
Param 164: 0
Param 165: 7f
Param 166: 0
Param 167: 0
Param 168: 1
Param 169: 0
Param 170: 14
Param 171: 0
Param 172: 7f
Param 173: 0
Param 174: 0
Param 175: 1
Param 176: 0
Param 177: 24
Param 178: 0
Param 179: 7f
Param 180: 0
Param 181: 1
Param 182: 0
Param 183: 0
Param 184: 34
Param 185: 0
Param 186: 7f
Param 187: 0
Param 188: 1
Param 189: 0
Param 190: 0
Param 191: 44
Param 192: 0
Param 193: 7f
Param 194: 0
Param 195: 10
Param 196: 1
Param 197: 0
Param 198: 0
Param 199: 5
Param 200: 0
Param 201: 7f
Param 202: 0
Param 203: 1
Param 204: 0
Param 205: 15
Param 206: 0
Param 207: 0
Param 208: 7f
Param 209: 0
Param 210: 1
Param 211: 0
Param 212: 25
Param 213: 0
Param 214: 0
Param 215: 7f
Param 216: 0
Param 217: 1
Param 218: 0
Param 219: 35
Param 220: 0
Param 221: 7f
Param 222: 0
Param 223: 0
Param 224: 1
Param 225: 0
Param 226: 45
Param 227: 0
Param 228: 7f
Param 229: 0
Param 230: 0
Param 231: 10
Param 232: 1
Param 233: 0
Param 234: 6
Param 235: 0
Param 236: 7f
Param 237: 0
Param 238: 0
Param 239: 1
Param 240: 0
Param 241: 16
Param 242: 0
Param 243: 7f
Param 244: 0
Param 245: 1
Param 246: 0
Param 247: 0
Param 248: 26
Param 249: 0
Param 250: 7f
Param 251: 0
Param 252: 1
Param 253: 0
Param 254: 0
Param 255: 36
Param 256: 0
Param 257: 7f
Param 258: 0
Param 259: 1
Param 260: 0
Param 261: 46
Param 262: 0
Param 263: 0
Param 264: 7f
Param 265: 0
Param 266: 10
Param 267: 1
Param 268: 0
Param 269: 7
Param 270: 0
Param 271: 0
Param 272: 7f
Param 273: 0
Param 274: 1
Param 275: 0
Param 276: 17
Param 277: 0
Param 278: 0
Param 279: 7f
Param 280: 0
Param 281: 1
Param 282: 0
Param 283: 27
Param 284: 0
Param 285: 7f
Param 286: 0
Param 287: 0
Param 288: 1
Param 289: 0
Param 290: 37
Param 291: 0
Param 292: 7f
Param 293: 0
Param 294: 0
Param 295: 1
Param 296: 0
Param 297: 47
Param 298: 0
Param 299: 7f
Param 300: 0
Param 301: 10
Param 302: 0
Param 303: 1
Param 304: 0
Param 305: 3a
Param 306: 0
Param 307: 7f
Param 308: 0
Param 309: 1
Param 310: 0
Param 311: 0
Param 312: 3b
Param 313: 0
Param 314: 7f
Param 315: 0
Param 316: 1
Param 317: 0
Param 318: 0
Param 319: 2e
Param 320: 0
Param 321: 7f
Param 322: 0
Param 323: 1
Param 324: 0
Param 325: 3c
Param 326: 0
Param 327: 0
Param 328: 7f
Param 329: 0
Param 330: 1
Param 331: 0
Param 332: 3d
Param 333: 0
Param 334: 0
Param 335: 7f
Param 336: 0
Param 337: 1
Param 338: 0
Param 339: 3e
Param 340: 0
Param 341: 7f
Param 342: 0
Param 343: 0
Param 344: 1
Param 345: 0
Param 346: 2b
Param 347: 0
Param 348: 7f
Param 349: 0
Param 350: 0
Param 351: 1
Param 352: 0
Param 353: 2c
Param 354: 0
Param 355: 7f
Param 356: 0
Param 357: 1
Param 358: 0
Param 359: 0
Param 360: 2a
Param 361: 0
Param 362: 7f
Param 363: 0
Param 364: 1
Param 365: 0
Param 366: 0
Param 367: 29
Param 368: 0
Param 369: 7f
Param 370: 0
Param 371: 1
Param 372: 0
Param 373: 2d
Param 374: 0
Param 375: 0
Param 376: 7f
Param 377: 0
Param 378: 7f
Param 379: 7f
Param 380: 7f
Param 381: 7f
Param 382: 0
Param 383: 7f
Param 384: 0
Param 385: 0
Param 386: 0
Param 387: 0
Param 388: 0
Param 389: 0
Param 390: 0
Param 391: 0
Param 392: 0
Param 393: 0
Param 394: 0
Param 395: 0
Param 396: 0
Param 397: 0
Param 398: 0
Param 399: 0
Param 400: 0
Param 401: 0
Param 403: f7  // EOX

