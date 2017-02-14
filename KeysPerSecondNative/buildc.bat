@echo off
echo Building...
cd ..\KeysPerSecond\bin
echo JavaH
"%JAVA_HOME%\bin\javah" -classpath . me.roan.kps.Title
cd ..\..\KeysPerSecondNative\me.roan.kps
echo Compiling
gcc -Wl,--add-stdcall-alias -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -shared -o KeysPerSecond.dll me_roan_kps_Title.c
echo Done
PAUSE