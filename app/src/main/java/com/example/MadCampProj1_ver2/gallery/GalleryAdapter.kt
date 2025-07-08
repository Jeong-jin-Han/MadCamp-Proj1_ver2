package com.example.MadCampProj1_ver2.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.GalleryData
import com.example.MadCampProj1_ver2.sampledata.GalleryDto
import com.example.MadCampProj1_ver2.sampledata.GalleryGroupData
import com.example.MadCampProj1_ver2.foodbank.Constants.fridge
import com.example.MadCampProj1_ver2.myfoodmergedata.MyFoodMergeData

@Suppress("DEPRECATION")
class GalleryAdapter (private val context: Context,
                      private var dataList: MutableList<GalleryDto>,
                      private val onItemClick: (Int, View) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.gallery_component_image)
        val cardView: CardView = view.findViewById(R.id.gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_component, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val data = dataList[position]
        try {
            if (data.image == -1) { // 이미지 리소스 ID가 없는 경우
                data.imagePath?.let { path ->
                    val bitmap = when {
                        path.startsWith("content://") -> {
                            context.contentResolver.openInputStream(Uri.parse(path))
                                ?.use { inputStream ->
                                    BitmapFactory.decodeStream(inputStream)
                                }
                        }

                        path.isNotEmpty() -> {
                            BitmapFactory.decodeFile(path)
                        }

                        else -> null
                    }
                    if (bitmap != null) {
                        // 비율 유지하며 ImageView 크기 동적 조정
                        val aspectRatio = bitmap.width.toFloat() / bitmap.height
                        holder.imageView.layoutParams = holder.imageView.layoutParams.apply {
                            width =
                                holder.imageView.resources.displayMetrics.widthPixels / 2 // 열 너비 조정
                            height = (width / aspectRatio).toInt()
                        }
                        holder.imageView.setImageBitmap(bitmap)
                    } else {
                        holder.imageView.setImageResource(R.drawable.example_mask) // 기본 이미지 설정
                    }
                } ?: run {
                    holder.imageView.setImageResource(R.drawable.example_mask) // 기본 이미지 설정
                }
            } else { // 이미지 리소스 ID가 있는 경우
                holder.imageView.setImageResource(data.image)
            }
        } catch (e: Exception) {
            Log.e("GalleryAdapter", "Error decoding image: ${e.localizedMessage}")
            holder.imageView.setImageResource(R.drawable.example_mask) // 오류 시 기본 이미지 설정
        }

        holder.imageView.setOnClickListener {
            onItemClick(data.id, holder.imageView)
        }
    }


    override fun getItemCount(): Int = dataList.size
    fun getCurrentData(): List<GalleryDto> = dataList

    fun updateData(id: Int) {

        val imgData = GalleryData.getGalleryDataList()
        val filteredData = if (id == 0) {
            imgData
        } else {
            val groupData = GalleryGroupData.getGalleryGroupDataList()
            val date = groupData.find { it.memberId == id }?.title ?: ""
            imgData.filter { it.date == date }
        }
        val mergedFridge = MyFoodMergeData.getMergedList().map { it.foodId }.toSet()
        val sortedData = filteredData.sortedWith(compareByDescending<GalleryDto> { dto ->
//            dto.ingredients.count { it in fridge }
            // 변경
            dto.ingredients.count { it in mergedFridge }
        }.thenBy { dto ->
//            dto.ingredients.count { it !in fridge }

            dto.ingredients.count { it !in mergedFridge }
        })


        dataList.clear()
        dataList.addAll(sortedData)
        notifyDataSetChanged()
    }

}