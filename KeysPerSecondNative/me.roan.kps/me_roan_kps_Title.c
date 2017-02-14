/*
 * me_roan_kps_Title.c
 *
 *  Created on: 14 Feb 2017
 *      Author: Roan
 */

#include <jni.h>
#include <stdio.h>
#include "me_roan_kps_Title.h"

// Implementation of native method getMSNStatus() of me.roan.kps.Title class
JNIEXPORT jstring JNICALL Java_me_roan_kps_Title_getMSNStatus (JNIEnv *env, jclass obj){
   printf("Loaded Native Library for Keys Per Second\n");

   char msg[60] = "This is a test string";
   jstring result = (*env)->NewStringUTF(env, msg);

   return result;
}
