//
// Created by songsaihua on 2018/10/31.
//
#include <cstdlib>
#include <cstring>
#include "GifReader.h"
#include <android/bitmap.h>
#define  argb(a,r,g,b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)
int imageNUm = 0;
static int InterlacedOffset[] = { 0, 4, 2, 1 };
static int InterlacedJumps[] = { 8, 8, 4, 2 };
GifReader::GifReader() {
    LOGE("GifReader");
}
GifReader::~GifReader() {
    LOGE("~GifReader");
}
int GifReader::loadGif(const char *path,GifFileType *fileType) {
    int count = fileType->ImageCount;
    bool bHighColor = true;
    LOGE("gif count : %d",count);
    SavedImage * image = NULL;
    int totalTime = 0;
    int delay = 0;
    int tranColor = -1;
    unsigned int Colors[256];
    int i;
    for (int k = 0; k < fileType->ImageCount; k++) {
        image = &(fileType->SavedImages[k]);
        for (int j = 0;j<image->ExtensionBlockCount;j++) {
            if (image->ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                ExtensionBlock * ext = &(image->ExtensionBlocks[j]);
                delay =  10 * (ext->Bytes[2] << 8 | ext->Bytes[1]);
                totalTime+=delay;
                if (ext->Bytes[0] & 1) {
                    tranColor = (unsigned char)(ext->Bytes[3]);
                }
//                LOGE("image index : %d,delay is : %d,tranColor is : %d",i,delay,tranColor);
            }
        }
        GifImageDesc *desc =  &(image->ImageDesc);
        ColorMapObject *colorMapObject = desc->ColorMap ? desc->ColorMap : fileType->SColorMap;
        int w = desc->Width,h = desc->Height;
        int wh = w * h;
        int size = wh * 2;
        //Size in bytes - in RGBA-4444 format, 2 bytes per pixel, in RGBA-8888, 4
        unsigned char *Buf = (unsigned char *) malloc(size);
        if (Buf == NULL) {
            LOGE("Failed to allocate memory");
            return -1;
        }
        for (i=0;i<colorMapObject->ColorCount;i++) {
            GifColorType &c = colorMapObject->Colors[i];
            Colors[i] =
                    bHighColor ? argb(0xff000000,c.Red,c.Green,c.Blue)
//                    ((unsigned int) c.Red << 16) |
//                    ((unsigned int) c.Green << 8) |
//                    ((unsigned int) c.Blue) |
//                    0xff000000
                               :
                    ((unsigned short) (c.Red & 0xf0) << 8) |
                    ((unsigned short) (c.Green & 0xf0) << 4) |
                    c.Blue | 0xf;
//            Colors[i] = ((unsigned short) (c.Red & 0xf0) << 8) | ((unsigned short) (c.Green & 0xf0) << 4) | c.Blue | 0xf;
        }
        if (tranColor != -1) {
            Colors[tranColor] = 0;
        }
        //把Colors中的数据存入Buf数组中
        unsigned char *psrc = image->RasterBits;
        if (bHighColor) {
            unsigned int *pDest = (unsigned int *) Buf;
            for (i = 0; i < wh; i++)
                *pDest++ = Colors[*psrc++];
        }
//        unsigned short *pDest = (unsigned short *) Buf;
//        for (int i = 0; i < wh; i++) {
//            unsigned short temp = (unsigned short) (Colors[*psrc++]);
//            *pDest++ = temp;
//        }
        jbyteArray jba = genv->NewByteArray(size);
        if (jba == NULL) {
            LOGE("NewByteArray error");
            free(Buf);
            return -1;
        }
        genv->SetByteArrayRegion(jba,0,size,(jbyte *)Buf);
        free(Buf);
        jmethodID  jmethodID = genv->GetStaticMethodID(Gifclass,"callback","(III[B)V");
        genv->CallStaticVoidMethod(Gifclass,jmethodID,w,h,k,jba);
        genv->DeleteLocalRef(jba);
    }
    return 1;
}

int GifReader::loadGif2(const char *path, GifFileType *fileType) {
    bool bHighColor = true;
    int i, Frame;
    unsigned int Colors[256];
    //TODO: don't rebuild colors if there's a global color table only
    for (Frame = 0; Frame < fileType->ImageCount; Frame++) {
        SavedImage &si = fileType->SavedImages[Frame];

        GraphicsControlBlock graphicsControlBlock;
        if (DGifSavedExtensionToGCB(fileType,Frame,&graphicsControlBlock) == GIF_OK) {
            //每个image加个10*0.01sec的延迟，GraphicsControlBlock还可以控制image的基本属性，请参照文档
//            graphicsControlBlock.DelayTime = 10;
//            EGifGCBToSavedExtension(&graphicsControlBlock,fileType,Frame);
        }
//        //给每个image添加文字，除此之外还可以添加Box，Rectangle和BoxedText；
//        const char* text = "hello world";
//        GifDrawText8x8(&si,0,0,text,20);

        int Delay = 0;
        int TransColor = -1;
        jint Disp = 0;
        for (i = 0; i < si.ExtensionBlockCount; i++) {
            ExtensionBlock &eb = si.ExtensionBlocks[i];
            int byteCount = eb.ByteCount;
            //__android_log_print(ANDROID_LOG_INFO, "JNITag1","string From Java To C : %i", byteCount);
            if (eb.Function == 0xf9) //Animation!graphics control (GIF89)
            {
                char c = eb.Bytes[0];
                Delay = (int) (unsigned char) (eb.Bytes[1]) |
                        ((int) (unsigned char) (eb.Bytes[2]) << 8);
//                __android_log_print(ANDROID_LOG_INFO, "JNITag2","string From Java To C : %i", Delay);
                if (c & 1)
                    TransColor = (unsigned char) (eb.Bytes[3]);
                Disp = (c >> 2) & 7;
            }
        }

        //Now format the bits...
        GifImageDesc &id = si.ImageDesc;
        //id.Left = 10;
        //id.Width = id.Width / 3;
        ColorMapObject *cm = fileType->SColorMap ? fileType->SColorMap : id.ColorMap;
//        ColorMapObject *cm = id.ColorMap ? id.ColorMap : fileType->SColorMap;
        int w = id.Width, h = id.Height;
        int wh = w * h;
        int Size = wh * (bHighColor ? 4
                                    : 2); //Size in bytes - in RGBA-4444 format, 2 bytes per pixel, in RGBA-8888, 4
        unsigned char *Buf = (unsigned char *) malloc(Size);
        if (!Buf) {
            LOGE("Buf is null");
            return JNI_FALSE;
        }
        //把image的所有颜色信息存到Colors整型数组中
        //Translate the color map into RGBA-4444 or RGBA-8888 format, take alpha into account. 256 colors tops, static is OK
        for (i = 0; i < cm->ColorCount; i++) {
            GifColorType &c = cm->Colors[i];
            Colors[i] =
                    bHighColor ?
                    ((unsigned int) c.Red << 16) |
                    ((unsigned int) c.Green << 8) |
                    ((unsigned int) c.Blue) |
                    0xff000000
                               :
                    ((unsigned short) (c.Red & 0xf0) << 8) |
                    ((unsigned short) (c.Green & 0xf0) << 4) |
                    c.Blue | 0xf;
        }
        if (TransColor != -1)
            Colors[TransColor] = 0;

        //Convert pixel by pixel...
        //把Colors中的数据存入Buf数组中
        unsigned char *pSrc = si.RasterBits;
        if (bHighColor) {
            unsigned int *pDest = (unsigned int *) Buf;
            for (i = 0; i < wh; i++)
                *pDest++ = Colors[*pSrc++];
        }
        else {
            unsigned short *pDest = (unsigned short *) Buf;
            for (i = 0; i < wh; i++)
                *pDest++ = (unsigned short) (Colors[*pSrc++]);
        }

        jbyteArray ja = genv->NewByteArray(Size);
        if (!ja) {
            free(Buf);
            LOGE("jbyteArray null");
            return JNI_FALSE;
        }
        genv->SetByteArrayRegion(ja, 0, Size, (jbyte *) Buf);
        free(Buf);
        jmethodID  jmethodID = genv->GetStaticMethodID(Gifclass,"callback","(III[B)V");
        genv->CallStaticVoidMethod(Gifclass,jmethodID,w,h,Frame,ja);
//        genv->CallVoidMethod(self, afmid,
//                            (jint) Delay * 10,
//                            (jint) id.Left, (jint) id.Top,
//                            (jint) w, (jint) h, Disp, ja);
        genv->DeleteLocalRef(ja);
    }
    return 1;
}

void GifReader::setGifFileType(GifFileType *gfileType) {
    fileType = gfileType;
    frameCount = gfileType->ImageCount;
    LOGE("setGifFileType");
}

void GifReader::loadGif3(const char *path, GifFileType *fileType) {
    for (int k = 0; k < fileType->ImageCount; k++) {
        SavedImage &image = fileType->SavedImages[k];
        GifImageDesc &desc = image.ImageDesc;
        jmethodID createbitmapMeth = genv->GetStaticMethodID(Gifclass,"createBitmap","(II)Landroid/graphics/Bitmap;");
        jobject bitmap = genv->CallStaticObjectMethod(Gifclass,createbitmapMeth,desc.Width,desc.Height);
        AndroidBitmapInfo info;
        void *pixels;
        //info赋值
        AndroidBitmap_getInfo(genv, bitmap, &info);
        //锁定bitmap，一副图片是二维数组
        AndroidBitmap_lockPixels(genv, bitmap, &pixels);
        ColorMapObject *colorMapObject = desc.ColorMap ? desc.ColorMap : fileType->SColorMap;
        //某个像素位置
        int pointPixel;
        int *px= (int *) pixels;//整幅图片的首地址
        int *line = NULL;//每一行的首地址
        GifColorType gifColorType;
        GifByteType  gifByteType;
        px = (int *)((char *)px + info.stride * desc.Top);
        for(int y=desc.Top;y<desc.Top+desc.Height;y++)
        {
            line=px;
            for(int x=desc.Left;x<desc.Left+desc.Width;x++)
            {
                pointPixel=(y-desc.Top)*desc.Width+(x-desc.Left);
                gifByteType=image.RasterBits[pointPixel];
                //需要给每个像素赋颜色
                if(colorMapObject!=NULL)
                {

                    gifColorType=colorMapObject->Colors[gifByteType];
                    line[x]=argb(255,gifColorType.Red,gifColorType.Green,gifColorType.Blue);
                }

            }
            px = (int *)((char *)px + info.stride);
        }
        LOGE("delete px--start");
        delete px;
        LOGE("delete line--start");
        if (line != NULL) {
            LOGE("delete line--start2222");
            delete line;
        }
        LOGE("delete pixels--start");
        delete pixels;
        LOGE("eeeeeeeeeeeee");
    }
}

