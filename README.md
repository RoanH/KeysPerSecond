# Keys Per Second ![](https://img.shields.io/github/release/RoanH/KeysPerSecond.svg)![](https://img.shields.io/github/downloads/RoanH/KeysPerSecond/total.svg)

A program that counts how many times a certain key is pressed. And shows what the average, maximum and current keys pressed per second is. Also show a nice graph of the key presses over time.
This program is also fully configurable.<br>
osu! forum post: [post](https://osu.ppy.sh/forum/p/5789644/)<br>

# Forum post
So I wanted to know how many keys / second I pressed in osu! And I had already seen programs like this on streams.
I could however not find a program that worked on my computer so I decided to write one myself.

The program when active looks like this:<br>
![Interface](http://i.imgur.com/9cCzB0Q.png)<br>
![Interface](http://i.imgur.com/bLQXABw.png)<br>
![Interface](https://i.imgur.com/2HgRJwO.png)<br>
![Menu](https://i.imgur.com/lltS2NK.png)<br>
_Accessible by right clicking on the program._

For each configured key it shows how many times it is pressed. By default it also shows the maximum, average and current number of keys pressed per second.
When enabled it can also show a graph of the number of keys pressed per second over time. The horizontal line in the graph represents the average number of keys pressed per second.

Everything shown in the picture above can be toggled on or off and all the panels can be arranged in a lot of different ways.<br>
![Config](https://i.imgur.com/G0NYcPE.png)
![Key config](https://i.imgur.com/vwtThmz.png)<br>
![Layout](https://i.imgur.com/6XdgGYe.png)<br>
![Modes](https://i.imgur.com/gNYCSb9.png)  

Lastly, there are also some commands that can be sent to the program:<br>
**Ctrl + P**: Causes the program to reset the average and maximum value.<br>
**Ctrl + U**: Terminates the program.<br>
**Ctrl + I**: Causes the program to reset the key press statistics.<br>
**Ctrl + Y**: Shows / hides the GUI.<br>
**Ctrl + T**: Pauses / resumes the counter.<br>
**Ctrl + R**: Reloads the configuration file.

You can also move the program using the arrow keys this makes pixel perfect positioning possible :D .

Well I hope some of you find this program useful and/or will use it for your streams (I would love to see that happen  :) ).
And if you find any bugs feel free to report them.

## Notes
- To clarify, you can add any key, and any number of keys to the program. So it can be used for any game mode.
- The overlay option doesn't work on a Mac nor does it work when the fullscreen option in osu! is enabled.
- When changing the key order, the keys are ordered from lowest value to highest value (negative values & skipping indices is allowed).
- To change a GUI colour in the colours menu, click on the current colour
- An opacity of 100% means completely opaque and an opacity of 0% means completely transparent.
- The snap to screen edge function works on multi-monitor setups.
- You can move the window with the arrow keys at 3 different speeds 1, 2 & 3 pixels at a time (2=Ctrl, 3=Shift).
- You can also track mouse buttons with this program.
- The order for positions is from the top left to the bottom right.
- You can pass the path to the config file to load via the commandline or a shortcut so you can skip the configuration step.
- When resetting something it will also be printed to the console if this program is run using cmd/shell.

## Todo list / working on
It's kinda empty here right now :c, so please suggest things c:

## Downloads (Java 8 required)
Supported operating systems: Mac (tested 10.11.6), Linux (tested Ubuntu 16.04 LTS) & Windows (tested 7, 8 & 10)<br>
(Note: if you're upgrading _to_ version 8.0 make sure to read the [release notes](https://github.com/RoanH/KeysPerSecond/releases/tag/v8.0))    
[Windows executable](https://github.com/RoanH/KeysPerSecond/releases/download/v8.0/KeysPerSecond-v8.0.exe)<br>
[Runnable Java Archive](https://github.com/RoanH/KeysPerSecond/releases/download/v8.0/KeysPerSecond-v8.0.jar)

All releases: [releases](https://github.com/RoanH/KeysPerSecond/releases)<br>
GitHub repository: [here](https://github.com/RoanH/KeysPerSecond)

## Dependencies
https://github.com/kwhat/jnativehook

## Donate
If you really like KeysPerSecond, you can consider making [donation](https://www.paypal.me/KeysPerSecond).

## History
Project development started: 23 January 2017
