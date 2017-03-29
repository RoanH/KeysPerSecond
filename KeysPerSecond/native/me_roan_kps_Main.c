/*
 * me_roan_kps_Main.c
 *
 *  Created on: 29 Mar 2017
 *      Author: Roan
 */

#include <jni.h>
#include <stdio.h>
#include "me_roan_kps_Main.h"
#include <windows.h>

#include <windows.h>
#include <tlhelp32.h>

JNIEXPORT void JNICALL Java_me_roan_kps_Main_toTop(JNIEnv *env, jclass obj){
   printf("Loaded Native Library for Keys Per Second\n");

   HWND foreground = FindWindow(NULL, "osu!");
   if (foreground){
       char window_title[256];
       GetWindowText(foreground, window_title, 256);
       printf(window_title);
   }
   SetWindowPos(foreground, HWND_TOP, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE);
}
