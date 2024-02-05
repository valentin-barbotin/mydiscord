package com.mydiscord

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache

fun myImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components(fun ComponentRegistry.Builder.() {
            add(VideoFrameDecoder.Factory())
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        })
        //.memoryCache(MemoryCache.Builder(context).build())
        //.diskCache(DiskCache.Builder().build())
        .build()
}
