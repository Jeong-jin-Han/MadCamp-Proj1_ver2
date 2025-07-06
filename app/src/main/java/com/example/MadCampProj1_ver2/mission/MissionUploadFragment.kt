package com.example.MadCampProj1_ver2.mission

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.MissionData
import com.example.MadCampProj1_ver2.sampledata.MissionDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MissionUploadFragment() : Fragment(){
    private var selectedImageView: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mission_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topBarTextView = view.findViewById<TextView>(R.id.top_bar_text)
        topBarTextView.text = "도전 과제"
        val searchButton = view.findViewById<ImageView>(R.id.top_bar_search)
        searchButton.visibility = View.GONE

        //date picker
        val startDateFrame: FrameLayout = view.findViewById(R.id.mission_start_date)
        val endDateFrame: FrameLayout = view.findViewById(R.id.mission_end_date)
        val startTimeTextView: TextView = view.findViewById(R.id.mission_start_date_text)
        val endTimeTextView: TextView = view.findViewById(R.id.mission_end_date_text)
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        var startDate: LocalDate? = null
        var endDate: LocalDate? = null
        startDateFrame.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDate = LocalDate.parse(selectedDate, formatter)
                startTimeTextView.text = selectedDate
                startTimeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        endDateFrame.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDate = LocalDate.parse(selectedDate, formatter)
                endTimeTextView.text = selectedDate
                endTimeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        //이미지 선택 이벤트
        var categoryNumber: Int ?= null
        val images = listOf(
            view.findViewById<ImageView>(R.id.image1), view.findViewById<ImageView>(R.id.image2), view.findViewById<ImageView>(R.id.image3), view.findViewById<ImageView>(R.id.image4), view.findViewById<ImageView>(R.id.image5),
            view.findViewById<ImageView>(R.id.image6), view.findViewById<ImageView>(R.id.image7), view.findViewById<ImageView>(R.id.image8), view.findViewById<ImageView>(R.id.image9), view.findViewById<ImageView>(R.id.image10)
        )
        images.forEach { imageView ->
            imageView.setOnClickListener {
                // 이전 선택 해제 후 현재 이미지 선택 지정
                selectedImageView?.background = null
                imageView.setBackgroundResource(R.drawable.mission_category_selected)
                selectedImageView = imageView

                //id에서 마지막 숫자 가져오기
                val idName = resources.getResourceEntryName(it.id)
                categoryNumber = idName.takeLastWhile { char -> char.isDigit() }.toIntOrNull()
            }
        }

        //제출하기
        var submitButton : Button = view.findViewById(R.id.mission_submit)
        submitButton.setOnClickListener{
            var EditText: EditText = view.findViewById(R.id.mission_titleEditText)
            if(EditText.text.isBlank() || startDate == null || endDate == null || selectedImageView == null){
                Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newMissionItem = categoryNumber?.let { it1 ->
                MissionDto(
                    id = MissionData.getMissionDataList().size + 1,
                    name = EditText.text.toString(),
                    startDate = startDate!!,
                    endDate = endDate!!,
                    isDone = false,
                    category = it1,
                    percent = 0
                )
            }

            //list 저장 후 탭 이동
            if (newMissionItem != null) {
                MissionData.addMissionItem(newMissionItem)
            }
            Toast.makeText(requireContext(), "업로드 완료!", Toast.LENGTH_SHORT).show()
            val fragment = MainFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    //DatePicker
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedYear}-${selectedMonth + 1}-${String.format("%02d", selectedDay)}"
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}