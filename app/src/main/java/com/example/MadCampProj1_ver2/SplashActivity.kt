package com.example.MadCampProj1_ver2

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)    //자동 생성 상단바 없앰
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loadingscreen_layout)

        // 애니메이션 비활성화
        overridePendingTransition(0, 0)

        val splashGif = findViewById<ImageView>(R.id.splashGif)
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash2)
            .apply(
                RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // 디스크 캐싱 사용
//                    .override(800, 800) // 원본 크기로 디코딩
//                    .skipMemoryCache(true) // 메모리 캐시 비활성화
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(Target.SIZE_ORIGINAL)
                    .skipMemoryCache(true)
            )
//            .into(splashGif)
            .into(object : com.bumptech.glide.request.target.CustomTarget<com.bumptech.glide.load.resource.gif.GifDrawable>() {
                override fun onResourceReady(
                    resource: com.bumptech.glide.load.resource.gif.GifDrawable,
                    transition: com.bumptech.glide.request.transition.Transition<in com.bumptech.glide.load.resource.gif.GifDrawable>?
                ) {
                    resource.setLoopCount(1) // 1회만 재생
                    splashGif.setImageDrawable(resource)
                    resource.start()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 필요시 placeholder 설정
                }
            })




        // 일정 시간 후 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 3500) // 3초 대기
    }
}
