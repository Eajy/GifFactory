//
// Created by songsaihua on 2018/10/18.
//
#include <jni.h>
#include <android/log.h>
#ifndef GIFFACTORY_LOGDEFAULT_H
#define GIFFACTORY_LOGDEFAULT_H
#if 1
#define  LOG_TAG    "giflog"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#endif
#endif //GIFFACTORY_LOGDEFAULT_H

