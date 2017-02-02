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
![Config](http://i.imgur.com/rp90PmZ.png)

Lastly, there are also some commands that can be sent to the program:<br>
**Ctrl + P**: Causes the program to reset the average and maximum value.<br>
**Ctrl + O**: Terminates the program.<br>
**Ctrl + I**: Causes the program to reset the key press statistics.

Well I hope some of you find this program usefull and/or will use it for your streams (I would love to see that happen  :) ).
And if you find any bugs feel free to report them.

## Downloads (Java 8 required)

![Windows executable](https://github.com/RoanH/KeysPerSecond/raw/master/KeysPerSecond.exe)<br>
![Runnable Java Archive](https://github.com/RoanH/KeysPerSecond/raw/master/KeysPerSecond.jar)

## Dependencies
https://github.com/kwhat/jnativehook

## History
Project development started: 23 January 2017
