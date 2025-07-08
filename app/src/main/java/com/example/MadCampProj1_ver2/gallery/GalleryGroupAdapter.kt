package com.example.MadCampProj1_ver2.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.GalleryData.getGalleryDataList
import com.example.MadCampProj1_ver2.sampledata.GalleryGroupDto

class GalleryGroupAdapter (private val dataList: List<GalleryGroupDto>,
                           private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<GalleryGroupAdapter.GalleryViewHolder>(){

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageGroupView: ImageView = view.findViewById(R.id.gallery_group_image)
        val imageGroupTitle: TextView = view.findViewById(R.id.gallery_group_title)
        //val imageGroupCount: TextView = view.findViewById(R.id.gallery_group_count)
        val cardView: CardView = view.findViewById(R.id.gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_group_component, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val data = dataList[position]
        holder.imageGroupTitle.text = data.title
        //holder.imageGroupCount.text = data.count.toString()
        val date = data.title
        for (l in getGalleryDataList()) {
            if (l.date == date && l.image != -1) {
                holder.imageGroupView.setImageResource(l.image)
            }
        }
        holder.cardView.setOnClickListener {
            onItemClick(data.memberId)
        }
    }

    override fun getItemCount(): Int = dataList.size
}