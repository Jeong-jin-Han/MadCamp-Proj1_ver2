package com.example.MadCampProj1_ver2.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.notification.NotificationType
import com.example.MadCampProj1_ver2.sampledata.GalleryData
import com.example.MadCampProj1_ver2.sampledata.GalleryDto
import com.example.MadCampProj1_ver2.sampledata.NotificationData
import com.example.MadCampProj1_ver2.sampledata.NotificationDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class GalleryUploadFragment(private val galleryAdapter: GalleryAdapter?) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gallery_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botbar = requireActivity().findViewById<View>(R.id.botbar)

        val abstractEditText: EditText = view.findViewById(R.id.abstractEditText)

        abstractEditText.requestFocus() // 포커스 설정

        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(abstractEditText, InputMethodManager.SHOW_IMPLICIT) // 키보드 표시
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardVisible = false // 키보드 상태를 추적하기 위한 변수

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                Log.d("KeypadHeight", "Keypad: $keypadHeight, Screen: $screenHeight")
                if (screenHeight < 1400) botbar.visibility = View.GONE
                else botbar.visibility = View.VISIBLE
            }
        })

        val imagePath = arguments?.getString("imagePath")
        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val uploadButton = view.findViewById<Button>(R.id.uploadButton)
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        view.findViewById<TextView>(R.id.gallery_detail_date).text = currentDate

        imagePath?.let { path ->
            try {
                val bitmap = if (path.startsWith("content://")) {
                    val inputStream = requireContext().contentResolver.openInputStream(Uri.parse(path))
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    BitmapFactory.decodeFile(path)
                }
                view.findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("GalleryUploadFragment", "Error decoding image: $e")
            }
        }

        uploadButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val abstractText = abstractEditText.text.toString()

            if (title.isBlank() || abstractText.isBlank()) {
                Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val newGalleryItem = GalleryDto(
                id = GalleryData.getGalleryDataList().size + 1,
                date = currentDate,
                memberId = 1,
                title = title,
                abstract = abstractText,
                imagePath = imagePath,
                image = -1
            )
            val newNotification = NotificationDto(
                id = NotificationData.getNotificationDataList(requireContext()).size + 1,
                type = NotificationType.GALLERY_POST,
                message = "게시물을 업로드하였습니다",
                targetId = GalleryData.getGalleryDataList().size + 1,
                clicked = false
            )
            NotificationData.addNotificationItem(requireContext(), newNotification)
            Log.d("Date", currentDate)
            GalleryData.addGalleryItem(newGalleryItem)
            Log.d("GalleryData", "Current GalleryData: ${GalleryData.getGalleryDataList()}")
            galleryAdapter?.updateData(0)
            Toast.makeText(requireContext(), "업로드 완료!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val botbar = requireActivity().findViewById<View>(R.id.botbar)
        botbar.visibility = if (isKeyboardVisible()) View.GONE else View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // botbar을 다시 VISIBLE로 설정
        val botbar = requireActivity().findViewById<View>(R.id.botbar)
        botbar.visibility = View.VISIBLE
    }

    fun isKeyboardVisible(): Boolean {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }
}
