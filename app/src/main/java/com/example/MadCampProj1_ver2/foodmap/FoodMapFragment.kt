package com.example.MadCampProj1_ver2.foodmap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.MadCampProj1_ver2.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.map.MapAdapter
import com.example.MadCampProj1_ver2.myfoodmembercheckeddata.MyFoodCheckedMemberData
import com.example.MadCampProj1_ver2.myfoodmembercheckeddata.MyFoodCheckedMemberData.initializeWithMembers
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberDto
import com.example.MadCampProj1_ver2.myfoodmemberpage.MyFoodMemberpageFragment
import com.example.MadCampProj1_ver2.myfoodpage.MyFoodpageFragment
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
import com.example.MadCampProj1_ver2.phone.PhoneAdapter
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.sampledata.NotificationData
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import kotlin.math.abs

@Suppress("DEPRECATION")
class FoodMapFragment: Fragment() {
    private lateinit var naverMap: NaverMap
    private lateinit var cartAdapter: FoodMapAdapter
    private var currentMemberId: Int? = null
    private var isMapInitizalized = false
    private var isInitialPinSet = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_ver2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("view", "viewCreate")
        super.onViewCreated(view, savedInstanceState)
        val lat: Double = arguments?.getDouble("lat") ?: -1.0 // 위도
        val lng: Double = arguments?.getDouble("lng") ?: -1.0 // 경도
        val memberId: Int = arguments?.getInt("memberId") ?: -1 // memberId

        MyFoodMemberData.initializeIfNeeded(requireContext())

        val MyFoodMemberList: List<MyFoodMemberDto> = MyFoodMemberData.getAllMyFoodMembers()
        initializeWithMembers(MyFoodMemberList)

        val searchButton = view.findViewById<ImageView>(R.id.top_bar_search_ver2)
        searchButton.visibility = View.GONE

        // 카트 아이콘 & 팝업
        val cartButton = view.findViewById<ImageView>(R.id.top_bar_cart_ver2)
        val cartPopup = view.findViewById<CardView>(R.id.cartPopupCard)

        cartButton.visibility = View.VISIBLE
        cartButton.setOnClickListener {
            cartPopup.visibility = if (cartPopup.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // ✅ 여기서 RecyclerView + Adapter 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
//        val cartItems = listOf("계란", "우유", "양파") // 예시 데이터

//        val foodmemberDataList = MyFoodMemberList
        val foodmemberDataList = MyFoodCheckedMemberData.getCheckedFoodMembers()


//        val adapter
        cartAdapter = FoodMapAdapter(
            foodmemberDataList,
            context = requireContext(),
            onFridgeClick = {
                id ->
//                // ✅ MyFoodpageFragment로 전환
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.phone_slide_in_right,
//                        R.anim.phone_slide_out_left,
//                        R.anim.phone_slide_in_left,
//                        R.anim.phone_slide_out_right
//                    )
//                    .replace(R.id.content_frame_ver2, MyFoodpageFragment())
//                    .addToBackStack(null)
//                    .commit()
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                val newFragment = MyFoodMemberpageFragment().apply {
                    arguments = Bundle().apply {
                        putInt("memberId", id)
                    }
                }

                transaction.setCustomAnimations(
                    R.anim.phone_slide_in_right,
                    R.anim.phone_slide_out_left,
                    R.anim.phone_slide_in_left,
                    R.anim.phone_slide_out_right
                )

                transaction
                    .hide(this@FoodMapFragment) // 현재 FoodMapFragment 숨김
                    .add(R.id.content_frame_ver2, newFragment) // 새 Fragment 추가
                    .addToBackStack(null)
                    .commit()
            },
            onLocationClick = {
                id ->
            },
            onCheckClick = { memberId ->
                val updatedList = MyFoodCheckedMemberData.getCheckedFoodMembers()
                Log.d("CartUpdate", "Checked members: ${updatedList.map { it.name }}")
                cartAdapter.updateData(updatedList)
                cartAdapter.notifyDataSetChanged()  // 전체 데이터 변경 후 갱신
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = cartAdapter




        val notificationButton = view.findViewById<ImageView>(R.id.top_bar_bell_ver2)
        notificationButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right,
                    R.anim.phone_slide_out_left,
                    R.anim.phone_slide_in_left,
                    R.anim.phone_slide_out_right,
                )
                .replace(R.id.content_frame_ver2, NotificationFragment())
                .addToBackStack(null)
                .commit()
        }

        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber_ver2)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList(requireContext()).toString()

        val mypageButton = view.findViewById<ImageView>(R.id.top_bar_person_ver2)


        mypageButton.setOnClickListener {
            val fragment = MyFoodpageFragment().apply {
                arguments = Bundle().apply {
                    putString("source", "map")
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                    R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame_ver2, fragment)
                .addToBackStack(null)
                .commit()
        }

        // 상단바 이름 변경
        view.findViewById<TextView>(R.id.top_bar_text_ver2).text = "탐색"

        // Naver MapFragment 가져오거나 새로 생성
        var mapFragment = childFragmentManager.findFragmentById(R.id.mapView_ver2) as? com.naver.maps.map.MapFragment
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(R.id.mapView_ver2, mapFragment)
                .commit()
        }

        if (!isMapInitizalized) {
            mapFragment?.getMapAsync {
                naverMap ->
                Log.d("d", "시작")
                this.naverMap = naverMap
                this.isMapInitizalized = true
                naverMap.uiSettings.isZoomControlEnabled = true

                // 멤버 데이터로 마커 설정
                MyFoodMemberList.forEach {
                    foodmember ->
                    createAndSetMarker(foodmember)
                }

                // RecyclerView 설정
                setupRecyclerView(view, MyFoodMemberList)

                // 특정 GPS 위촐 이동 (lat, lng 값이 유효한 경우)
                if (lat != -1.0 && lng != -1.0 && memberId == -1) {
                    val foodmember = MyFoodMemberList.find {it.memberId == memberId}
                    foodmember?.let {
                        moveToLocation(lat, lng, it)

                        // 초기 핀 설정 후 1초 동안 스크럴 리스ㄴ 비활성화
                        isInitialPinSet = true
                        Handler(Looper.getMainLooper()).postDelayed(
                            {isInitialPinSet = false}, 1000
                        )
                    }
                }
            }
        }
    }

    private fun createAndSetMarker(foodmember: MyFoodMemberDto, marker: Marker = Marker()) {
        val view = LayoutInflater.from(context).inflate(R.layout.map_pin, null)
        val imageView: ImageView = view.findViewById(R.id.phone_component_image)
        val nameText: TextView = view.findViewById(R.id.phone_component_name)
//        val statusText: TextView = view.findViewById(R.id.phone_component_status)
        val cardView: CardView = view.findViewById(R.id.phone)

        // cvdata가 아니라 Food에 대한 정보를 가져오게 할 예정
        val cv = CVData.getCVDataList(requireContext()).find { it.memberId == foodmember.memberId }
        cv?.let {
            imageView.setImageResource(foodmember.imgCirclePath)
            imageView.setBackgroundResource(R.drawable.circle)
            cardView.radius = 50f
            nameText.text = foodmember.name
//            statusText.text = it.qualification
        }

        marker.icon = OverlayImage.fromBitmap(createBitmapFromView(view))
        marker.position = LatLng(foodmember.lat, foodmember.lng)
        marker.zIndex = 10
        marker.map = naverMap

    }

    private fun setupRecyclerView(view: View, foodmemberDataList: List<MyFoodMemberDto>) {
        val recyclerView: RecyclerView = view.findViewById(R.id.map_recycler_view_ver2)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = FoodMapAdapter(foodmemberDataList, context = requireContext(),
            onFridgeClick = {
                id ->
//                // ✅ MyFoodpageFragment로 전환
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.phone_slide_in_right,
//                        R.anim.phone_slide_out_left,
//                        R.anim.phone_slide_in_left,
//                        R.anim.phone_slide_out_right
//                    )
//                    .replace(R.id.content_frame_ver2, MyFoodpageFragment())
//                    .addToBackStack(null)
//                    .commit()
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                val newFragment = MyFoodMemberpageFragment().apply {
                    arguments = Bundle().apply {
                        putInt("memberId", id)
                    }
                }

                transaction.setCustomAnimations(
                    R.anim.phone_slide_in_right,
                    R.anim.phone_slide_out_left,
                    R.anim.phone_slide_in_left,
                    R.anim.phone_slide_out_right
                )

                transaction
                    .hide(this@FoodMapFragment) // 현재 FoodMapFragment 숨김
                    .add(R.id.content_frame_ver2, newFragment) // 새 Fragment 추가
                    .addToBackStack(null)
                    .commit()
            },
            onLocationClick = {
                id ->
            },
            onCheckClick = { memberId ->
                val updatedList = MyFoodCheckedMemberData.getCheckedFoodMembers()
                Log.d("CartUpdate", "Checked members: ${updatedList.map { it.name }}")
                cartAdapter.updateData(updatedList)
                cartAdapter.notifyDataSetChanged()  // 전체 데이터 변경 후 갱신
            }
        )
//        TODO()
        // RecyclerView 중앙 위치로 카메라 이동
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isInitialPinSet) {
                    // 초기 핀 설정 중에는 스크롤 리스너 무시
                    return
                }
                val closestMember = getClosestMember(recyclerView, foodmemberDataList)
                closestMember?.let { moveToLocation(it.lat, it.lng, it) }
            }
        })
    }

    private fun getClosestMember(recyclerView: RecyclerView, memberDataList: List<MyFoodMemberDto>): MyFoodMemberDto? {
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

    private fun createBitmapFromView(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0,0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun moveToLocation(pinlat: Double, pinlng: Double, pinmember: MyFoodMemberDto) {
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
}