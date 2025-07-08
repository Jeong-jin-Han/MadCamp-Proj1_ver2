package com.example.MadCampProj1_ver2.gallery

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.transition.TransitionInflater
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.customview.CustomScrollView
import com.example.MadCampProj1_ver2.sampledata.GalleryData
import com.example.MadCampProj1_ver2.sampledata.GalleryDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import kotlin.math.abs
import com.example.MadCampProj1_ver2.foodbank.Constants.fridge

@Suppress("DEPRECATION")
class GalleryDetailFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector
    private val SWIPE_THRESHOLD = 100 // 스와이프 거리 임계값
    private val SWIPE_VELOCITY_THRESHOLD = 100 // 스와이프 속도 임계값
    private fun generateTextForPhoto(photoId: Int): SpannableString {
        val gallery = GalleryData.getGalleryDataList().find { it.id == photoId }
        val ingredients = gallery?.ingredients ?: emptyList()

        val fullText = ingredients.joinToString(", ")
        val spannable = SpannableString(fullText)

        var startIndex = 0
        for (ingredient in ingredients) {
            val endIndex = startIndex + ingredient.length

            val isInFridge = fridge.contains(ingredient)
            val color = if (isInFridge) {
                ContextCompat.getColor(requireContext(), android.R.color.black)
            } else {
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
            }

            spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // ❗ 없는 재료일 때만 클릭 이벤트 추가
            if (!isInFridge) {
                val clickableSpan = object : android.text.style.ClickableSpan() {
                    override fun onClick(widget: View) {
                        val query = Uri.encode(ingredient)
                        val url = "https://m.coupang.com/nm/search?q=$query"
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                }
                spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            startIndex = endIndex + 2 // ", " 길이
        }

        return spannable
    }



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

        val galleryImageView = view.findViewById<ImageView>(R.id.gallery_component_image)
        val detailTextView = view.findViewById<TextView>(R.id.gallery_detail_abstract)
        Log.d("GalleryDetail", "galleryImageView: $galleryImageView")
        galleryImageView.isClickable = true
        galleryImageView.isFocusable = true

        /*galleryImageView.setOnClickListener {
            Log.d("GalleryDetail", "ImageView clicked!")
            val photoId = arguments?.getInt("id") ?: -1
            Log.d("GalleryDetail", "Clicked photoId: $photoId")
            if(photoId != -1) {
                val spannableText = generateTextForPhoto(photoId)
                Log.d("GalleryDetail", "Generated ingredients: $spannableText")
                ingredientsTextView.text = spannableText
                ingredientsTextView.visibility = View.VISIBLE
            }
        }*/
        Log.d("String", "View created")
        val customScrollView = view.findViewById<CustomScrollView>(R.id.custom_scroll_view)
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null || e2 == null) return false
                val deltaX = e2.x - e1.x
                if (abs(deltaX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) {
                        navigateToAdjacentPhoto(-1)
                    } else {
                        navigateToAdjacentPhoto(1)
                    }
                    return true
                }
                return false
            }
        })

        customScrollView.gestureDetector = gestureDetector


        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)

        val galleryId = arguments?.getInt("id") ?: -1
        if (galleryId != -1){
            val galleryDataList: List<GalleryDto> = GalleryData.getGalleryDataList()
            val gallery = galleryDataList.find { it.id == galleryId }
            val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())
            val member = memberDataList.find { it.memberId == gallery?.memberId }

            if (gallery?.image == -1) {
                gallery.imagePath?.let { path ->
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
                gallery?.image?.let {
                    view.findViewById<ImageView>(R.id.gallery_component_image).setImageResource(it)
                }
            }
            val ingredientsTextView = view.findViewById<TextView>(R.id.gallery_detail_ingredients)
            ingredientsTextView.text = generateTextForPhoto(galleryId)
            ingredientsTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

            val abstractText = gallery?.abstract
            val titleText = gallery?.title
            val date = gallery?.date
            if (date != null) {
                Log.d("date", date)
            }
            val name = member?.name


            view.findViewById<TextView>(R.id.gallery_detail_abstract).text = abstractText
            view.findViewById<TextView>(R.id.gallery_detail_title).text = titleText
            view.findViewById<TextView>(R.id.gallery_detail_date).text = date
            view.findViewById<TextView>(R.id.gallery_detail_ingredients).text = generateTextForPhoto(galleryId)
        }



        // RGB값 가져와서 arrow색깔 변형하기
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
        var memberId = arguments?.getInt("id") ?: return
        val memberDataList: List<GalleryDto> = GalleryData.getGalleryDataList()
        Log.d("String", memberId.toString())
        val t: Int = memberId/20
        if (memberId % 20 == 0) {
            memberId = 20
        }
        else {
            memberId = memberId % 20
        }
        val currentPhoto = memberDataList.find { it.memberId == memberId } ?: return
        val currentPhotoId = arguments?.getInt("id")?:return
        val sortedIdList = arguments?.getIntegerArrayList("sortedList") ?: return
        Log.d("String", currentPhoto.toString())

        /*val sameDatePhotos = memberDataList
            .filter { it.date == currentPhoto.date }
            .sortedWith(compareByDescending<GalleryDto> { gallery ->
                gallery.ingredients.count { it !in fridge }  // 필요한 재료 개수 (내가 없는 재료)
            }.thenBy { gallery ->
                gallery.ingredients.count { it in fridge }   // 내가 가지고 있는 재료 개수 (많을수록 좋음)
            })*/
        val currentIndex = sortedIdList.indexOf(currentPhotoId)

        val newIndex = (currentIndex + direction).coerceIn(0, sortedIdList.size - 1)
        Log.d("String", newIndex.toString())
        if (newIndex == currentIndex) return

        val newPhotoId = sortedIdList[newIndex]
        val fragment = GalleryDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("id", newPhotoId)
                putIntegerArrayList("sortedList", sortedIdList)
            }
        }

        /*if (newIndex != currentIndex) {
            val newPhoto = sameDatePhotos[newIndex]

            val fragment = GalleryDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", (newPhoto.memberId + t*20))
                    Log.d("id", newPhoto.memberId.toString())
                }
            }*/

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

            transaction.replace(R.id.content_frame_ver2, fragment)
                .commit() // addToBackStack() 제거
        }
    }


