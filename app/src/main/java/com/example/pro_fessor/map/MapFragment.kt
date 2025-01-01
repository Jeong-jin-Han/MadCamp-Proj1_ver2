package com.example.pro_fessor.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pro_fessor.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_fessor.mission.MissionAdapter
import com.example.pro_fessor.mission.MissionCompleteFragment
import com.example.pro_fessor.sampledata.CVDto
import com.example.pro_fessor.sampledata.MemberData
import com.example.pro_fessor.sampledata.MemberDto
import com.example.pro_fessor.sampledata.MissionData
import com.example.pro_fessor.sampledata.MissionDto
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import java.lang.reflect.Member
import kotlin.math.abs


@Suppress("DEPRECATION")
class MapFragment : Fragment() {
    private lateinit var naverMap: NaverMap
    private var currentMemberId: Int? = null
    private var isMapInitialized = false
    private var isInitialPinSet = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("view", "viewCreate")
        super.onViewCreated(view, savedInstanceState)

        val lat: Double = arguments?.getDouble("lat") ?: -1.0 // 위도
        val lng: Double = arguments?.getDouble("lng") ?: -1.0 // 경도
        val memberId: Int = arguments?.getInt("memberId") ?: -1 // memberId

        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList()

        // 상단바 이름 변경
        view.findViewById<TextView>(R.id.top_bar_text).text = "지도"

        // Naver MapFragment를 가져오거나 새로 생성
        var mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? com.naver.maps.map.MapFragment
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(R.id.mapView, mapFragment)
                .commit()
        }

        if (!isMapInitialized) {
            mapFragment?.getMapAsync { naverMap ->
                Log.d("d", "시작")
                this.naverMap = naverMap
                this.isMapInitialized = true
                naverMap.uiSettings.isZoomControlEnabled = true

                // 멤버 데이터로 마커 설정
                memberDataList.forEach { member ->
                    createAndSetMarker(member)
                }

                // RecyclerView 설정
                setupRecyclerView(view, memberDataList)

                // 특정 GPS 위치로 이동 (lat, lng 값이 유효한 경우)
                if (lat != -1.0 && lng != -1.0 && memberId != -1) {
                    val member = memberDataList.find { it.memberId == memberId }
                    member?.let {
                        moveToLocation(lat, lng, it)

                        // 초기 핀 설정 후 1초 동안 스크롤 리스너 비활성화
                        isInitialPinSet = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            isInitialPinSet = false
                        }, 1000)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View, memberDataList: List<MemberDto>) {
        val recyclerView: RecyclerView = view.findViewById(R.id.map_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = MapAdapter(memberDataList) { id ->
            val member = memberDataList.find { it.memberId == id }
            member?.let { moveToLocation(it.lat, it.lng, it) }
        }

        // RecyclerView 중앙 위치로 카메라 이동
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isInitialPinSet) {
                    // 초기 핀 설정 중에는 스크롤 리스너 무시
                    return
                }
                val closestMember = getClosestMember(recyclerView, memberDataList)
                closestMember?.let { moveToLocation(it.lat, it.lng, it) }
            }
        })
    }

    private fun getClosestMember(recyclerView: RecyclerView, memberDataList: List<MemberDto>): MemberDto? {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val center = recyclerView.width / 2
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()

        return (firstVisible..lastVisible).minByOrNull { position ->
            val view = recyclerView.findViewHolderForAdapterPosition(position)?.itemView ?: return@minByOrNull Int.MAX_VALUE
            val itemCenter = (view.left + view.right) / 2
            abs(itemCenter - center)
        }?.let { memberDataList.getOrNull(it) }
    }

    private fun moveToLocation(pinlat: Double, pinlng: Double, pinmember: MemberDto) {
        if (currentMemberId == pinmember.memberId) {
            // 이미 현재 멤버에 대해 이동했으면 중복 호출 방지
            return
        }
        currentMemberId = pinmember.memberId

        val marker = Marker()
        createAndSetMarker(pinmember, marker)
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(pinlat, pinlng))
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun createAndSetMarker(member: MemberDto, marker: Marker = Marker()) {
        val view = LayoutInflater.from(context).inflate(R.layout.map_pin, null)
        val imageView: ImageView = view.findViewById(R.id.phone_component_image)
        val nameText: TextView = view.findViewById(R.id.phone_component_name)
        val statusText: TextView = view.findViewById(R.id.phone_component_status)

        val cv = CVData.getCVDataList().find { it.memberId == member.memberId }
        cv?.let {
            imageView.setImageResource(member.imgPath)
            imageView.setBackgroundResource(R.drawable.circle)
            nameText.text = member.name
            statusText.text = it.qualification
        }

        marker.icon = OverlayImage.fromBitmap(createBitmapFromView(view))
        marker.position = LatLng(member.lat, member.lng)
        marker.zIndex = 10
        marker.map = naverMap

        marker.setOnClickListener {
            callPhoneNumber(member.phone)
            true
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun callPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber") // 전화번호를 URI 형식으로 설정
        }
        startActivity(intent)
    }
}