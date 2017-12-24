# Keys Per Second

A program that counts how many times a certain key is pressed. And shows what the average, maximum and current keys pressed per second is. Also show a nice graph of the key presses over time.
This program is also fully configurable.<br>
osu! forum post: [post](https://osu.ppy.sh/forum/p/5789644/)<br>

# Forum post
So I wanted to know how many keys / second I pressed in osu! And I had already seen programs like this on streams.
I could however not find a program that worked on my computer so I decided to write one myself.

The program when active looks like this:<br>
![Interface](http://i.imgur.com/9cCzB0Q.png)<br>
![Interface](http://i.imgur.com/bLQXABw.png)<br>
![Menu](https://i.imgur.com/lltS2NK.png)<br>
Accessible by right clicking on the program.

For each configured key it shows how many times it is pressed. By default it also shows the maximum, average and current number of keys pressed per second.
When enabled it can also show a graph of the number of keys pressed per second over time. The horizontal line in the graph represents the average number of keys pressed per second.

Everything shown in the picture above can be toggled on or off and all the panels can be arranged in a lot of different ways.<br>
![Config](http://i.imgur.com/u7obayv.png)
![Key config](http://i.imgur.com/N6JOCJk.png)<br>
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

## Updates
2 February 2017: Added the option for the program to overlay the osu! window. This only works if osu! isn't being run in fullscreen mode.<br>
5 February 2017: Added support for a variable update rate & changed exit key to Ctrl + U (Ctrl + O opens the osu! options menu).<br>
6 February 2017: Added the ability to configure the order in which the keys are displayed & some minor bug fixes.<br>
9 February 2017: Invert the key text color when a key is pressed.<br>
10 February 2017: Added the option to customize the GUI foreground & background colour.<br>
12 February 2017: Added the option to track all keys, show track keys or not & fixed some edge case bugs.<br>
12 February 2017: Added support for more then 10k hits on a single key & added the option to hide a tracked key.<br>
12 February 2017: Added the option to remove added keys.<br>
16 February 2017: Added support for decimal avg & cur, also added a new command key that hides the GUI.<br>
17 February 2017: Added support for transparent colours.<br>
17 February 2017: Added automatic version checking & removed the decimal point for cur since cur is an integer.<br>
18 February 2017: Added support for a variable size & added an icon to the window.<br>
21 March 2017: Major performance improvements & change opacity settings to use a percentage.<br>
21 March 2017: Made adding keys a little bit easier.<br>
30 March 2017: Implemented a right click dialog, pause functionality & a snap to screen edge function.<br>
31 March 2017: Fix ! path bug, add arrow key moving, improve window draging, properly reset the graph, fix no on graph.<br>
1 April 2017: Implement 1 & 5ms update rate (CPU heavy), fix infinite instances bug, fix rendering bug, filter config selection view.<br>
8 April 2017: Implement a text based config format & the option to configure the program while it's running & beter arrow moving support.<br>
12 April 2017: Fix checkmark not being colored, fix custom colors being reset, implement config caps & defaults, fix size issue, add add key cancel button<br>
13 April 2017: Add pause checkmark, fix overlay option, implement config reloading (ctrl + R), Fix visual glitch, fix trackall bugs, NPE workaround, fix double click = ctrl, improve command line arguments.<br>
16 April 2017: Added the option to track mouse buttons, added the option to save the onscreen location of the program to the config.<br>
17 April 2017: Rendering fixes & fix the counters being reset when the color is changed.<br>
19 April 2017: Fix keys not being removed when they should be, fix Win 10 rendering bug, Add cancel option for graph, size, precision & update rate, fix move keys moving the context menu & program at the same time, fix loading a new config not resetting stats.<br>
29 April 2017: Minor optimizations, context menu improvements & fixes, add totals panel, fix some bugs.<br>
30 April 2017: Fix an opacity bug that causes the window to be invisible with certain settings.<br>
28 May 2017: Fix a localization bug & add the option to track key-modifier combinations.<br>
28 June 2017: Added a lot of layout options.<br>
28 June 2017: Fix the position/index feature for keys not working.<br>
28 June 2017: Add warning for invalid key/row/columns combinations, improve reset menu, add a value-text horizontal rendering mode.<br>
16 August 2017: Fix several bug involving key-modifier combinations & a bug showing weird mouse button text.<br>
9 November 2017: Implement save stats feature & 6 additional rendering modes. Fix key capacity checks, changing the update rate messing up the average, fix a multi-threading issue, fix a mode loading bug. Improve startup times & overall performance. Lots of internal refactoring & optimizations.<br>
15 November 2017: Fix command keys with modifiers not working & visual enhancements for resetting.<br>
24 December 2017: Fix all known key-modifier bugs, better support for unfilled grids, internal optimizations.

## Todo list / working on
It's kinda empty here right now :c, so please suggest things c:

## Downloads (Java 8 required)
Supported operating systems: Mac (tested 10.11.6), Linux (tested Ubuntu 16.04 LTS) & Windows (tested 7, 8 & 10)<br>
[Windows executable](https://github.com/RoanH/KeysPerSecond/releases/download/v7.2/KeysPerSecond-v7.2.exe)<br>
[Runnable Java Archive](https://github.com/RoanH/KeysPerSecond/releases/download/v7.2/KeysPerSecond-v7.2.jar)

All releases: [releases](https://github.com/RoanH/KeysPerSecond/releases)<br>
GitHub repository: [here](https://github.com/RoanH/KeysPerSecond)

## Dependencies
https://github.com/kwhat/jnativehook

## Donate
If you really like KeysPerSecond, you can consider making [donation](https://www.paypal.me/KeysPerSecond).

## History
Project development started: 23 January 2017
