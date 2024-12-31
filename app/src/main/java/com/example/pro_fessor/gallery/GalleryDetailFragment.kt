package com.example.pro_fessor.gallery

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pro_fessor.R
import com.example.pro_fessor.sampledata.GalleryData
import com.example.pro_fessor.sampledata.GalleryDto
import kotlin.math.abs

@Suppress("DEPRECATION")
class GalleryDetailFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector
    private val SWIPE_THRESHOLD = 100 // 스와이프 거리 임계값
    private val SWIPE_VELOCITY_THRESHOLD = 100 // 스와이프 속도 임계값


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                duration = 500
            }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gallery_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null || e2 == null) return false

                val deltaX = e2.x - e1.x
                if (abs(deltaX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) {
                        navigateToAdjacentPhoto(-1) // 오른쪽 스와이프 -> 이전 사진
                    } else {
                        navigateToAdjacentPhoto(1) // 왼쪽 스와이프 -> 다음 사진
                    }
                    return true
                }
                return false
            }
        })

        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)

        val memberId = arguments?.getInt("id") ?: -1
        if (memberId != -1){
            val memberDataList: List<GalleryDto> = GalleryData.getGalleryDataList()
            val member = memberDataList.find { it.memberId == memberId }

            if (member?.image == -1) {
                member.imagePath?.let { path ->
                    try {
                        val bitmap = if (path.startsWith("content://")) {
                            val inputStream = requireContext().contentResolver.openInputStream(Uri.parse(path))
                            BitmapFactory.decodeStream(inputStream)
                        } else {
                            BitmapFactory.decodeFile(path)
                        }
                        view.findViewById<ImageView>(R.id.gallery_component_image).setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("GalleryUploadFragment", "Error decoding image: $e")
                    }
                }
            }
            else {
                member?.image?.let {
                    view.findViewById<ImageView>(R.id.gallery_component_image).setImageResource(it)
                }
            }

            val abstractText = member?.abstract
            val titleText = member?.title

            view.findViewById<TextView>(R.id.gallery_detail_abstract).text = abstractText
            view.findViewById<TextView>(R.id.gallery_detail_title).text = titleText
        }

        val galleryImageView = view.findViewById<ImageView>(R.id.gallery_component_image)
        // gallery_component_image를 Bitmap으로 변환
        galleryImageView.post {
            val drawable = galleryImageView.drawable
            if (drawable is android.graphics.drawable.BitmapDrawable) {
                val bitmap = drawable.bitmap
                // 특정 좌표의 픽셀 RGB 값 가져오기 (top_bar_arrow의 위치를 기준으로 픽셀 값을 추출)
                val arrowLocation = IntArray(2)
                backArrow.getLocationInWindow(arrowLocation) // 또는 getLocationOnScreen

                val arrowX = arrowLocation[0] - galleryImageView.left
                val arrowY = arrowLocation[1] - galleryImageView.top

                if (arrowX in 0 until bitmap.width && arrowY in 0 until bitmap.height) {
                    val pixel = bitmap.getPixel(arrowX, arrowY)

                    // RGB 값 추출
                    val red = (pixel shr 16) and 0xff
                    val green = (pixel shr 8) and 0xff
                    val blue = pixel and 0xff

                    // 검은색과 흰색에 대한 "가까움" 계산
                    val blackDistance = red + green + blue // 검은색(0,0,0)에서의 거리
                    val whiteDistance = (255 - red) + (255 - green) + (255 - blue) // 흰색(255,255,255)에서의 거리

                    // 가까운 색상에 따라 이미지 변경
                    if (blackDistance < whiteDistance) {
                        backArrow.setImageResource(R.drawable.arrow_white) // 검은색에 가까우면 흰색 화살표
                    } else {
                        backArrow.setImageResource(R.drawable.arrow_black) // 흰색에 가까우면 검은색 화살표
                    }
                }
            }
            galleryImageView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }
        }

        backArrow.visibility = View.VISIBLE
        backArrow.setOnClickListener {
            // 스택에서 모든 galleryDetailFragment를 제거하고 galleryFragment로 이동
            requireActivity().supportFragmentManager.popBackStack(
                "galleryFragment", // galleryFragment에 지정한 태그
                FragmentManager.POP_BACK_STACK_INCLUSIVE // 스택 위의 모든 Fragment 제거
            )
        }
    }

    private fun navigateToAdjacentPhoto(direction: Int) {
        val memberId = arguments?.getInt("id") ?: return
        val memberDataList: List<GalleryDto> = GalleryData.getGalleryDataList()
        val currentPhoto = memberDataList.find { it.memberId == memberId } ?: return

        val sameDatePhotos = memberDataList.filter { it.date == currentPhoto.date }
        val currentIndex = sameDatePhotos.indexOf(currentPhoto)

        val newIndex = (currentIndex + direction).coerceIn(0, sameDatePhotos.size - 1)
        if (newIndex != currentIndex) {
            val newPhoto = sameDatePhotos[newIndex]

            val fragment = GalleryDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", newPhoto.memberId)
                }
            }

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            if (direction > 0) {
                transaction.setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
            } else {
                transaction.setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
            }

            transaction.replace(R.id.content_frame, fragment)
                .commit() // addToBackStack() 제거
        }
    }

}