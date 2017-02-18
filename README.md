# Keys Per Second

A program that counts how many times a certain key is pressed. And shows what the average, maximum and current keys pressed per second is. Also show a nice graph of the key presses over time.
This programm is also fully configurable.<br>
osu! forum post: [post](https://osu.ppy.sh/forum/p/5789644/)<br>

# Forum post
So I wanted to know how many keys / second I pressed in osu! And I had already seen programs like this on streams.
I could however not find a program that worked on my computer so I decided to write one myself.

The program when active looks like this:<br>
![Interface](http://i.imgur.com/9cCzB0Q.png)

For each configured key it shows how many times it is pressed. By default it also shows the maximum, average and current number of keys pressed per second.
When enabled it can also show a graph of the number of keys pressed per second over time. The horizontal line in the graph represents the average number of keys pressed per second.

Everything shown in the picture above can be toggled on or off.<br>
![Config](http://i.imgur.com/liSFuRx.png)

Lastly, there are also some commands that can be sent to the program:<br>
**Ctrl + P**: Causes the program to reset the average and maximum value.<br>
**Ctrl + U**: Terminates the program.<br>
**Ctrl + I**: Causes the program to reset the key press statistics.

Well I hope some of you find this program usefull and/or will use it for your streams (I would love to see that happen  :) ).
And if you find any bugs feel free to report them.

## Notes
- To clarify, you can add any key, and any number of keys to the program. So it can be used for any game mode.
- The overlay option doesn't work on a Mac.
- When changing the key order, the keys are ordered from lowest value to highest value (negative values & skipping indices is allowed).
- To change a GUI colour in the colours menu, click on the current colour.

## Updates
2 February 2017: Added the option for the program to overlay the osu! window. This only works if osu! isn't being run in fullscreen mode.<br>
5 February 2017: Added support for a variable update rate & changed exit key to Ctrl + U (Ctrl + O opens the osu! options menu).<br>
6 February 2017: Added the ability to configure the order in which the keys are displayed & some minor bug fixes.<br>
9 February 2017: Invert the key text color when a key is pressed.<br>
10 February 2017: Added the option to customize the GUI foreground & background colour.<br>
12 February 2017: Added the option to track all keys, show track keys or not & fixed some edge case bugs.

## Downloads (Java 8 required)
Note these download links are not actively being updated. For the latest release check the releases section.<br>
![Windows executable](https://github.com/RoanH/KeysPerSecond/releases/download/v3.11/KeysPerSecond-v3.11.exe)<br>
![Runnable Java Archive](https://github.com/RoanH/KeysPerSecond/releases/download/v3.11/KeysPerSecond-v3.11.jar)

All releases: ![releases](https://github.com/RoanH/KeysPerSecond/releases)<br>
GitHub repository: ![the page you're looking at O.o](https://github.com/RoanH/KeysPerSecond)

## Dependencies
https://github.com/kwhat/jnativehook

## History
Project development started: 23 January 2017
