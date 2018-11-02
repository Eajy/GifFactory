//
// Created by songsaihua on 2018/10/31.
//

#include <cstdio>
#include <jni.h>
#include "../jni/giflib/gif_lib.h"
#include "logDefault.h"
#ifndef GIFFACTORY_GIFREADER_H
#define GIFFACTORY_GIFREADER_H

#endif //GIFFACTORY_GIFREADER_H
typedef struct GifImage{
    int currentFrame;
    int delay;
    FILE *file;
} GifImage;
class GifReader{
public:
    GifReader();
    ~GifReader();

public:
    int loadGif(const char *path,GifFileType *fileType);
    int loadGif2(const char *path,GifFileType *fileType);
    void setGifFileType(GifFileType *fileType);
    void loadGif3(const char *path,GifFileType *fileType);
private:
    int mWidth;
    int mHeight;
    int totalTime;
    int frameCount;
    GifFileType *fileType;
    jmethodID callbaclId;
public:
    JNIEnv *genv;
    jclass Gifclass;
};

