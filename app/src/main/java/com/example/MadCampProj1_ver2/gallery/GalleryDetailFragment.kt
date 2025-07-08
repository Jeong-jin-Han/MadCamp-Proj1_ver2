package com.example.MadCampProj1_ver2.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
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
import com.example.MadCampProj1_ver2.foodbank.Constants
import com.example.MadCampProj1_ver2.foodbank.Ingredient
import com.example.MadCampProj1_ver2.sampledata.GalleryData
import com.example.MadCampProj1_ver2.sampledata.GalleryDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.abs

@Suppress("DEPRECATION")
class GalleryDetailFragment : Fragment() {

    private lateinit var allIngredients: List<Ingredient>
    private lateinit var gestureDetector: GestureDetector

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    // assets/ingredients.json을 읽어 Ingredient 리스트 반환
    private fun loadIngredients(context: Context): List<Ingredient> {
        val jsonString = context.assets.open("ingredients.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val listType = object : TypeToken<List<Ingredient>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    // photoId에 해당하는 재료 리스트 텍스트 생성 및 색상, 클릭 설정
    private fun generateTextForPhoto(photoId: Int): SpannableString {
        val gallery = GalleryData.getGalleryDataList().find { it.id == photoId }
        val ingredientIds = gallery?.ingredients ?: emptyList()

        val ingredients = ingredientIds.mapNotNull { id -> allIngredients.find { it.foodId == id } }
        val fullText = ingredients.joinToString(", ") { it.name }
        val spannable = SpannableString(fullText)

        var startIndex = 0
        for (ingredient in ingredients) {
            val name = ingredient.name
            val endIndex = startIndex + name.length
            val isInFridge = Constants.fridge.contains(ingredient.foodId)
            val colorRes = if (isInFridge) android.R.color.black else android.R.color.holo_red_dark
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), colorRes)),
                startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (!isInFridge) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val query = Uri.encode(name)
                        val url = "https://m.coupang.com/nm/search?q=$query"
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                }
                spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            startIndex = endIndex + 2
        }
        return spannable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allIngredients = loadIngredients(requireContext())

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                duration = 500
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gallery_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryId = arguments?.getInt("id") ?: -1
        if (galleryId == -1) return

        val galleryDataList = GalleryData.getGalleryDataList()
        val gallery = galleryDataList.find { it.id == galleryId }
        val memberDataList = MemberData.getPhoneDataList(requireContext())
        val member = memberDataList.find { it.memberId == gallery?.memberId }

        val galleryImageView = view.findViewById<ImageView>(R.id.gallery_component_image)
        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
        val ingredientsTextView = view.findViewById<TextView>(R.id.gallery_detail_ingredients)

        // 이미지 설정
        if (gallery?.image == -1) {
            gallery.imagePath?.let { path ->
                try {
                    val bitmap = if (path.startsWith("content://")) {
                        requireContext().contentResolver.openInputStream(Uri.parse(path))?.use { BitmapFactory.decodeStream(it) }
                    } else {
                        BitmapFactory.decodeFile(path)
                    }
                    galleryImageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Log.e("GalleryDetailFragment", "Error decoding image: $e")
                }
            }
        } else {
            gallery?.image?.let { galleryImageView.setImageResource(it) }
        }

        // 재료 텍스트 생성 및 링크 이동 활성화
        ingredientsTextView.text = generateTextForPhoto(galleryId)
        ingredientsTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        // 타이틀, 추상, 날짜 표시
        view.findViewById<TextView>(R.id.gallery_detail_title).text = gallery?.title
        view.findViewById<TextView>(R.id.gallery_detail_abstract).text = gallery?.abstract
        view.findViewById<TextView>(R.id.gallery_detail_date).text = gallery?.date

        // 제스처 감지기 초기화 (스와이프 좌우 이동)
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null || e2 == null) return false
                val deltaX = e2.x - e1.x
                if (abs(deltaX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) navigateToAdjacentPhoto(-1) else navigateToAdjacentPhoto(1)
                    return true
                }
                return false
            }
        })

        val customScrollView = view.findViewById<CustomScrollView>(R.id.custom_scroll_view)
        customScrollView.gestureDetector = gestureDetector

        // 이미지 뷰 터치 이벤트에 제스처 연결
        galleryImageView.post {
            val drawable = galleryImageView.drawable
            if (drawable is android.graphics.drawable.BitmapDrawable) {
                val bitmap = drawable.bitmap
                val arrowLocation = IntArray(2)
                backArrow.getLocationInWindow(arrowLocation)
                val arrowX = arrowLocation[0] - galleryImageView.left
                val arrowY = arrowLocation[1] - galleryImageView.top

                if (arrowX in 0 until bitmap.width && arrowY in 0 until bitmap.height) {
                    val pixel = bitmap.getPixel(arrowX, arrowY)
                    val red = (pixel shr 16) and 0xff
                    val green = (pixel shr 8) and 0xff
                    val blue = pixel and 0xff

                    val blackDistance = red + green + blue
                    val whiteDistance = (255 - red) + (255 - green) + (255 - blue)
                    if (blackDistance < whiteDistance) {
                        backArrow.setImageResource(R.drawable.arrow_white)
                    } else {
                        backArrow.setImageResource(R.drawable.arrow_black)
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
            requireActivity().supportFragmentManager.popBackStack(
                "galleryFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun navigateToAdjacentPhoto(direction: Int) {
        val currentPhotoId = arguments?.getInt("id") ?: return
        val sortedIdList = arguments?.getIntegerArrayList("sortedList") ?: return
        val currentIndex = sortedIdList.indexOf(currentPhotoId)
        val newIndex = (currentIndex + direction).coerceIn(0, sortedIdList.size - 1)
        if (newIndex == currentIndex) return

        val newPhotoId = sortedIdList[newIndex]
        val fragment = GalleryDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("id", newPhotoId)
                putIntegerArrayList("sortedList", sortedIdList)
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
        transaction.replace(R.id.content_frame_ver2, fragment)
            .commit()
    }
}



