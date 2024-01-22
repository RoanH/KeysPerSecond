# Updates
All releases can be found on the [releases page](https://github.com/RoanH/KeysPerSecond/releases).

## [v8.9] 22nd of January, 2024: Cursor Graph
This is a small release that adds one new major feature and fixes a number of regressions in the previous release that were thankfully caught early by UlyssesZh.

- Added a new graph type that tracks the movement path of the cursor as shown below. Note that a fast update rate is recommended when using this graph type to make it look smooth (50ms - 10ms).
![cursor graph](https://media.roanh.dev/keyspersecond/cursorgraph.gif)
- The backlog for the old graph type (line graph) is now specified in milliseconds instead of update frames.
- The reset and reload command keys are now unbound by default since these are semi destructive and could be triggered by accident.
- Fix that the graph was rendered 1 pixel smaller than it was supposed to be.
- Show a warning when reloading a configuration file that cannot be reloaded (config not loaded from a file).
- Fix a config desync when loading a configuration via the initial dialog.
- Fix that certain settings are not being applied when loading a config via the default option or the command line ([92](https://github.com/RoanH/KeysPerSecond/issues/92)).
- Fix that periodic stats saving saves to the base folder instead of a file inside that folder ([93](https://github.com/RoanH/KeysPerSecond/issues/93)).
- Thanks to efojug fix that the application can initially be offscreen on some monitor layouts ([94](https://github.com/RoanH/KeysPerSecond/issues/94) and [95](https://github.com/RoanH/KeysPerSecond/pull/95)).
- Fix that some settings do not get applied properly when reloading a configuration file.
- Ensure that the directory stats are being saved to actually exists before saving.

## [v8.8] 17th of January, 2024: Major Improvements
It's been a while, but the next release is finally done! This release also lays the foundations for some larger and more exciting features planned for future releases.

- Input handling was completely rewritten:
  - The key/button add dialog now shows in real time which key you're about to add.
  - Similarly, the command keys dialog now also shows which key combination you're setting.
  - Fixed that commands were active before the main window was visible ([66](https://github.com/RoanH/KeysPerSecond/issues/66)).
- The configuration system was completely rewritten:
  - New (backwards compatible) configuration format primarily intended to support planned features.
  - You can now have multiple statistic panels and multiple graphs (more panel specific settings are planned).
  - Support for a detached graph was **removed**.
  - The `show keys` setting was **removed** (though you can still hide individual keys).
  - You can now change the display name of any panel, including those of special panels like AVG ([32](https://github.com/RoanH/KeysPerSecond/issues/32)).
  - The default update rate was changed from 1000ms to 100ms.
  - File paths used for automatic stats saving are now validated.
- General interface changes:
  - Most dialogs were redesigned to support the more powerful configuration format.
  - Panels now have a set of panel specific settings that can be edited.
  - The right click menu now directly allows you to edit the settings of the panel that was right clicked.
  - When using the right click menu all settings now show their changes to the live GUI in real time.
  - The hide command key is now also available from the right click menu.
- Thanks to DeSu0556 the hide command key now adds the program to the system tray if supported ([89](https://github.com/RoanH/KeysPerSecond/issues/89) and [91](https://github.com/RoanH/KeysPerSecond/pull/91)).
- It is now possible to set a default configuration that will be loaded automatically when starting KeysPerSecond ([20](https://github.com/RoanH/KeysPerSecond/issues/20) and [89](https://github.com/RoanH/KeysPerSecond/issues/89)).
- Implement a new `last` panel type that shows the elapsed time since the last input.
- Make it possible to run the program in windowed mode ([77](https://github.com/RoanH/KeysPerSecond/issues/77)).
- The default display name of the CUR (current) panel was changed to KPS.
- Make it possible to set a maximum display value for graphs ([90](https://github.com/RoanH/KeysPerSecond/issues/90)).
- A warning is now shown when the initial configuration contains no panels to display.
- Fix that the total panel was showing up as AVG in the layout settings ([87](https://github.com/RoanH/KeysPerSecond/issues/87)).
- Fixed a bug that caused the program to freeze at extremely small GUI sizes.
- Improved graph rendering and resolved a graph flickering issue at extremely high update rates.
- Fixed that newly added panels sometimes get added behind existing panels in complex layouts.
- Fixed a bug with the track all keys setting if key-modifier tracking is enabled.
- Fixed that the layout dialog crashes under Linux ([59](https://github.com/RoanH/KeysPerSecond/issues/59) and [88](https://github.com/RoanH/KeysPerSecond/issues/88)).
- Fixed a critical rendering failure under Linux ([76](https://github.com/RoanH/KeysPerSecond/issues/76)).
- Major code quality improvements.
- Update dependencies.
- Add some limited (unit) testing.

## [v8.7] 26th of August, 2022: Some Fixes
- Fix a file open dialog being shown on Mac and Linux when trying to save a configuration ([#61](https://github.com/RoanH/KeysPerSecond/issues/61) and [#67](https://github.com/RoanH/KeysPerSecond/issues/67))
- Fix loading mouse buttons from a configuration not working ([#63](https://github.com/RoanH/KeysPerSecond/issues/63))
- Fix incorrect square bracket detection ([#60](https://github.com/RoanH/KeysPerSecond/issues/60))
- Fix the file chooser on Mac and Linux not following the general OS style
- Fix a rare exception when configuring the layout ([#59](https://github.com/RoanH/KeysPerSecond/issues/59))

## [v8.6] 9th of June, 2022: Fix Statistics Saving
- Fix statistics saving and loading being being broken ([#51](https://github.com/RoanH/KeysPerSecond/issues/51) and [#57](https://github.com/RoanH/KeysPerSecond/issues/57)).
- Implement a new text based statistics saving format (meaning you can manually edit key counts if you want).
- Implement a text only rendering mode.
- Implement a value only rendering mode.
- Make it possible to unbind command keys, effectively disabling them ([#45](https://github.com/RoanH/KeysPerSecond/issues/45)).
- Dependency updates & internal improvements.

## [v8.5] 7th of January, 2022: Better Support
A lot of fixes again and some better support.

- **Remove support for legacy configuration file formats.**
    - This concerns the `kpsconf` format which was lasts used in v4.5 (replaced April 8th, 2017).
    - And the `kpsconf2` format which was last used in v7.4 (replaced September 12th, 2018).
    - You can convert configuration files in this format using v8.4.
    - The current `kpsconf3` format will still load, but newly saved configuration files will use the `kps` extension.
- Support running on M1 Mac's ([#39](https://github.com/RoanH/KeysPerSecond/issues/39)).
- Fix being able to move any UI component with the arrow keys ([#31](https://github.com/RoanH/KeysPerSecond/issues/31)).
- Fix the comma (,) key not being loaded properly from the configuration file ([#34](https://github.com/RoanH/KeysPerSecond/issues/34)).
- Fix resetting statistics (<kbd>Ctrl</kbd>+<kbd>P</kbd>) not resetting the total number of key presses ([#42](https://github.com/RoanH/KeysPerSecond/issues/42)).
- Make the Java check more flexible by reading JAVA_HOME meaning more runtimes are detected ([#49](https://github.com/RoanH/KeysPerSecond/issues/49)).
- Fix overlay option and stats saving not properly loading from the config when passed via the command line ([#41](https://github.com/RoanH/KeysPerSecond/issues/41)).
- Fix the reset totals command key not being properly read from the config ([#40](https://github.com/RoanH/KeysPerSecond/issues/40) and [#47](https://github.com/RoanH/KeysPerSecond/issues/47)).
- Make it possible to edit the values in the layout configuration screen with the keyboard.
- Add an about dialog to the right click menu with general information and links.
- Fix some dialog typos.
- Fix cancelling adding a key still reserving a layout slot for that key.
- Update dependencies.

## [v8.4] 24th of May, 2021: Important fixes
- Implement sub-pixel rendering (if your system supports it this will improve the quality of text rendering)
- Implement support for saving/loading statistics on exit/launch ([#25](https://github.com/RoanH/KeysPerSecond/issues/25))
- Update global keyboard/mouse listener library
  - Fixes character combinations not working (e.g. e + " = ë)
  - Resolves some rare freezes on OSX
- Fix the program not working under Java 16 ([#30](https://github.com/RoanH/KeysPerSecond/issues/30))
- Fix the border offset not properly applying to the fill color
- Fix not being able to add the ESC key due to it closing the dialog
- Enable the 'save config' button by default

## [v8.3] 27th of August, 2020: Small improvements
- If you're on Windows with the correct VC++ runtime installed you'll now see the new style Windows file chooser.
- Prevent loading of non existent configuration files via the command line and user interface.
- You now have to properly quote the file path of a configuration file passed via the command line if it has spaces (this used to work without quotes)
- Fix the key add description saying to press the `OK` button while the button is named `Save`.
- Fix background transparency not properly being applied to areas where no key panel is displayed.
- Improve how dialogs look.
- Build process improvements.
- Small internal improvements.

## [v8.2] 5th of February, 2019: More statistics options
- Make the display name of keys editable (can be changed in the key add screen)
- Implement an option to periodically save the statistics to a file ([#18](https://github.com/RoanH/KeysPerSecond/issues/18))
- Make it so dummy keys created by the track all keys function are read back from statistics files
- Split the track all keys option in a track all keys and track all mouse buttons function ([#17](https://github.com/RoanH/KeysPerSecond/issues/17))
- Fix the right mouse button being listed as the left mouse button in the mouse button add screen

## [v8.1] 27th of December, 2018: More layout options
- Add support for changing the layout cell size (basically means pretty much all sizes are now supported again)
- Add support for changing the panel border offset (basically means you can control the space between panels)
- Tried to improve the quality of the readme file a bit >_>
- Make the main configuration screen an actual frame so you can actually find it on the taskbar.
- Make the add key dialog and the layout dialog resizeable.
- Fix a typo on the add key dialog.
- Restyle all the dialogs.
- Actually make the cancel button on the add key dialog revert the visible state of keys.
- Fix a rather severe bug that caused issues for some keys (though the keys that were affected by this bug aren't even on my own keyboard so I'm not actually sure how much of an impact this bug actually had).
- Proper support for the right shift key.
- Fix adding duplicate keys still making the layout longer.
- Actually reset the total number of hits when loading a new configuration.
- Fix loading several configurations in a row and then adding a key creating a lot of empty layout space.

## [v8.0] 12th of September, 2018: The Layout Update
This is a major release that completely changes the layout system of the program.
You are now able to design basically any layout of the panels as long as it aligns to the grid of the program.

**Note however that because of this the old configuration format does not load completely anymore!**
If you never changed any of the layout settings your configuration will load just fine, however if you made
changes to the layout chances are that it won't look exactly the same in this version (though since more is possible now you can fix your layout to look the same again or improve it). It should also be noted that the panels have a slightly different size (changed by a few pixels).

Example of what you can do now:    
![layout](https://i.imgur.com/2HgRJwO.png)

Changes:
- Grid based layout editor that shows layout changes in real time (if opened from the right click menu) and that allows custom panel sizes
- Rendering modes can now be set on a per panel basis
- Overall improved performance
- Removed the size setting

P.S. Since the layout system is completely new I expect there to be at least a few bugs that slipped past my testing.

## [v7.4] 3rd of June, 2018: Small fixes
- Add the GitHub link to the initialisation dialog.
- Fix the configuration file not supporting UTF-8 characters.
- Fix position saving only working when the graph is displayed in its own window.
- Change the arrow key symbols to Unicode symbols with better availability.

## [v7.3] 2nd of June, 2018: Fix the arrow keys
The arrow keys displayed a bit weird...    
![afbeelding](https://user-images.githubusercontent.com/8530896/40865549-16861550-65f9-11e8-9116-389468885a67.png)
Fixed by changing to a new set of arrow symbols: ⯅⯆⯇⯈    
![afbeelding](https://user-images.githubusercontent.com/8530896/40865583-44d73510-65f9-11e8-8665-7d1b6fc82c09.png)

## [v7.2] 24th of December, 2017: Key-modifier fixes
This release mainly focusses on fixing small bugs, most of which were in the key-modifier tracking.
- Fix modifier keys not being highlighted when key-modifier tracking is on.
- Fix modifier keys releasing every other key when key-modifier tracking is on.
- Fix the key-modifier check box not being toggled when loading a config.
- Fix all known cases of key-modifier tracking weirdness.
- Rework key-modifier tracking internally to be more efficient.
- Lots of internal optimizations.
- Better color support for unfilled grids (empty tiles are now either the background color or transparent depending on whether transparency is enabled).
- Fix several minor bugs in the right click menu.

## [v7.1] 15th of November, 2017: Fix command keys
- Fix command keys with modifiers not working
- Repaint the frame when resetting statistics or totals for an immediate display update

## [v7.0] 9th of November, 2017: Lots of fixes & features
- Implement a feature where you can save the statistics to a file
- Fix key capacity checks being incorrect
- Implement 6 additional rendering modes    
![new modes](https://user-images.githubusercontent.com/8530896/32631187-bc03a5ce-c59f-11e7-8718-b093cbcfa40e.png)
- Improve overall performance
- Fix changing the update rate messing up the average
- Fix a multithreading issues (as kindly pointed out by freakode)
- Lots of internal refactoring & optimizations
- Improve startup times
- Fix a bug with the rendering mode loading (bea5316ab8fdb2be34c3991d5aa83170984dda14) ([#12](https://github.com/RoanH/KeysPerSecond/issues/12))

## [v6.6] 16th of August, 2017: Key configuration bug fixes
- Fix a bug where key-modifier versions of the original key don't show
- Add support for disabling key-modifier combinations (allows for modifier keys to work as normal keys)
- Fix adding a mouse button displaying a weird name

## [v6.5] 28th of June, 2017: Minor improvements
- Added warning dialog to prevent more keys being added then there are rows & columns.
- Added an extra render mode for rendering horizontal keys in the value-text format.
- Add specific reset commands to the reset menu.

## [v6.4] 28th of June, 2017: Layout Index Fix
Fix the position feature for keys not working.

## [v6.3] 28th of June, 2017: Customizable layout
Add a lot of layout related options:
- All text can now be rendered either horizontal or vertical (default)
- Panels can now be organized in a grid instead of just a single row. The number of rows and columns is custom.
- Positions can now also be assigned to the max, avg, cur & tot panels. Meaning they can now also be placed at a custom place instead of at the end of the row.
- 4 new modes of the graph are implemented. Under the other components (default), on top of them, on the right of them, on the left of them and detached. When the graph is detached it gets it's own window that can be given a  custom size.
- The graph dimensions are no longer fixed and can be changed (as long as the side isn't shared).
- Support to save & load all of the above to a configuration file.

## [v6.2] 28th of May, 2017: Fix & feature
- Fixed a bug where localized key names would not be converted to their short form
- You can now track key-modifier combinations (eg. Alt+Q or Shift+5 or Ctrl+H, or Alt+Shift+Ctrl+R)

## [v6.1] 30th of April, 2017: Quick fix
- Fix the background opacity controlling the global opacity (for example a background opacity of 0% will make the window invisible even if the foreground opacity isn't 0%).

## [v6.0] 29th of April, 2017: More features & fixes
- Minor internal optimizations.
- Context menu GUI improvements / changes & custom colours compatibility.
- Fix context menu arrow not being colored.
- Fix a bug where the general menu section wasn't saved properly.
- Add a panel that shows the total amount of hits across all keys.
- Fix the context menu highlighting disappearing when changing the background opacity to 100%.
- Fix not all context menu items being colored correctly when using custom colors & having them disabled.
- Fix context menu selection not being reset properly.
- Implement the option to change the command keys and save them to the configuration file.

## [v5.5] 19th of April, 2017: Major fixes
- Added a cancel option for the graph, size, precision & update rate configuration dialog.
- Fix a weird rendering bug that happens on Windows 10.
- Fix loading a new config file not resetting stats.
- Fix removing keys not actually removing keys.
- Fix old keys not being removed when a new config is loaded.
- Fix arrow keys moving both the program & the context menu at the same time.

## [v5.4] 17th of April, 2017: Rendering fixes
- Fix the background opacity being used as the graph foreground opacity.
- Fix the key color not being inverted when the background opacity is 0%.
- Fixed some rendering issues when only displaying a single panel.
- Fix changing the colors resetting the key counters.
- Fix the pause check mark in the right click menu not being toggled by Ctrl + T.
- Change overlay osu! to overlay mode since it doesn't always overlay osu! properly.

## [v5.3] 16th of April, 2017: Mouse tracking
- Added the option the track mouse buttons.
- Added the option to save the onscreen position of the program to the config.

## [v5.2] 13th of April, 2017: Even more bug fixes
- Add a checkmark to the pause menu item
- Fix the overlay option not working immediately
- Implement a config reload command key Ctrl + R
- Fix visual glitches when there are no components added
- Fix disabling track all disabling permanent keys
- Implement work around for a weird config menu NPE
- Fix double click working as Ctrl
- Improve the functionality to pass config files via the command line
- Fix track all disabling key counters

## [v5.1] 12th of April, 2017: More bug fixes
- Fix the checkmark icon in the right click menu not being colored correctly
- Fix custom colors being reset instead of disabled
- Implement default config values
- Implement config value caps
- Fix an issue when loading a config with a custom size
- Add an cancel button to the add key dialog

## [v5.0] 8th of April, 2017: Live configurable
- Fix an issue where the arrows keys move the window by more then 1 pixel.
- The window can now be moved at different speeds with the arrow keys 2 pixels with Ctrl and 3 pixels with Shift.
- Implemented a new configuration format that is text based and can be edited with a text editor (old format still supported).
- Made it possible to configure the program when it's running though the right click menu.
- Added the option to save the config when the program is running.
- Added the option to load a config when the program is running.

## [v4.5] 1st of April, 2017: Enhancements
- Implement 5ms & 1ms update rate
- Prevent infinite program instances being created when your username ends with a !
- Fix incorrect rendering bug with bg = 1.0F and transparency enabled
- Filter config selection screen to only show config files

## [v4.4] 31st of March, 2017: Bug fixes
- Fix a bug when the program doesn't launch when its path contains an !
- Implement pixel perfect moving of the window using the arrow keys
- Improve window dragging
- Fix the graph not being reset properly on a stats reset
- Fix the menu not showing when right clicking the graph

## [v4.3] 30th of March, 2017: Right click menu
- Implemented a right click menu for various commands
- Implemented a pause function (Ctrl + T)
- Implemented a snap to screen edge function (works for multi-monitor setups)

## [v4.2] 24th of March, 2017: Easier config loading
- Added the option to pass a config file via the command line.
- Fix a bug where the overlay option isn't saved to the config.
- Fix the constant flashing of the window icon when the overlay option is enabled.

## [v4.1] 21st of March, 2017: Easier key adding
- Add a separate dialog for adding keys, adding keys should feel more intuitive now.

## [v4.0] 21st of March, 2017: Performance
- Major performance improvements
- Change opacity settings to use a percentage

Euh the major version changed to 4 yay :D

## [v3.12] 18th of February, 2017: Variable size
- Added support for a variable size.
- Added an icon to the frame.
- Improved icon.

## [v3.11] 17th of February, 2017: Version checking
- Remove decimal point for cur since cur is an integer
- Add automatic version checking

## [v3.10] 17th of February, 2017: Transparency
- Added the ability to use transparent colours.
- Fixed a bug when trying to load a 3.9 config using version 3.8 (no one probably even noticed this xD).

## [v3.9] 16th of February, 2017: Precision
- Added the option to display decimal avg & cur values (0-3 digits beyond the decimal point supported).
- Added a key to show / hide the GUI (Ctrl + Y).

## [v3.8] 12th of February, 2017: Key options
Added the option to remove added keys.

## [v3.7] 12th of February, 2017: Key options
- Added support for more then 10k hits on a single key.
- Added support for hiding individual tracked keys.

## [v3.6] 12th of February, 2017: Extra options & fixes
- Added the option to show / hide tracked keys
- Added the option to track all keys
- Fix some edge case bugs 
- Minor optimizations
- Made sure the native library is always unloaded even when the program is suddenly terminated
- Added an 'Exit' option to the config screen

## [v3.5] 10th of February, 2017: Custom Colours
Added the option to customize the GUI foreground & background colour.

## [v3.4] 9th of February, 2017: Invert key text color when pressed
Invert the key text color when a key is pressed.

## [v3.3] 6th of February, 2017: Configurable key order
Added the ability to configure the order in which the keys are displayed & some minor bug fixes.

## [v3.2] 5th of February, 2017: Variable update rate
Added support for a variable update rate & changed exit key to Ctrl + U

## [v3.1] 2nd of February, 2017: More options
Added the option to force the program to be the top window.    
This does however require osu! to not be run in full screen mode.

## [v3.0] 1st of February, 2017: First public release
First stable version and added a graph.

## [v2.0] 25th of January, 2017: GUI version
First version with a GUI

## [v1.0] 24th of January, 2017: CLI version
Basic version with only command line support.