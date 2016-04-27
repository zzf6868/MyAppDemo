//
// Created by zfzhao on 2016/4/27.
//

#include "com_zfzhao_myappdemo_MainActivity.h"

/*
 * Class:     com_zfzhao_myappdemo_MainActivity
 * Method:    getJniStr
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_zfzhao_myappdemo_MainActivity_getJniStr
  (JNIEnv *env, jobject thiz) {
  return (*env)->NewStringUTF(env, "Hello, this is jni C");
}