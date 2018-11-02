//
// Created by songsaihua on 2018/10/18.
//

#include <jni.h>
#include "../jni/giflib/gif_lib.h"
#include "logDefault.h"
#include "GifReader.h"
extern "C"
JNIEXPORT jint JNICALL
Java_com_seven_eajy_gif_GifJniTest_initGifLib(JNIEnv *env, jclass type, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    int error;

    GifFileType *fileType = DGifOpenFileName(path,&error);
    if (fileType == NULL) {
        LOGE("DGifOpenFileName is error : %d",error);
    }
    if (DGifSlurp(fileType) == GIF_ERROR) {
        LOGE("DGifSlurp is error");
        return -1;
    }
    GifReader *gifReader = new GifReader();
    gifReader->genv = env;
    gifReader->Gifclass = type;
    gifReader->setGifFileType(fileType);
    gifReader->loadGif2(path,fileType);
    env->ReleaseStringUTFChars(path_,path);
    return 1;
}

