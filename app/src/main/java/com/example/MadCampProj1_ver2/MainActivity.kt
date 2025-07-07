package com.example.MadCampProj1_ver2

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.MadCampProj1_ver2.gallery.GalleryFragment
import com.example.MadCampProj1_ver2.phone.PhoneFragment
import com.example.MadCampProj1_ver2.foodbank.FoodBankFragment


import android.widget.LinearLayout
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.mission.MainFragment


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)    //자동 생성 상단바 없앰

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ver2)
        supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, PhoneFragment()).commit()

        // 하단바 동작 설정
        val foodbankLayout = findViewById<LinearLayout>(R.id.food_bank_Layout)
        val menubookLayout = findViewById<LinearLayout>(R.id.menu_book_Layout)
        val travelexploreLayout = findViewById<LinearLayout>(R.id.travel_explore_Layout)
        val foodbankButton = findViewById<ImageButton>(R.id.food_bank_button)
        val menubookButton = findViewById<ImageButton>(R.id.menu_book_button)
        val travelexploreButton = findViewById<ImageButton>(R.id.travel_explore_button)

        foodbankLayout.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank)
            menubookButton.setImageResource(R.drawable.menu_book_unselected)
            travelexploreButton.setImageResource(R.drawable.travel_explore_unselected)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, FoodBankFragment()).commit()
        }

        foodbankButton.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank)
            menubookButton.setImageResource(R.drawable.menu_book_unselected)
            travelexploreButton.setImageResource(R.drawable.travel_explore_unselected)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, FoodBankFragment()).commit()
        }


        menubookLayout.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank_unselected)
            menubookButton.setImageResource(R.drawable.menu_book)
            travelexploreButton.setImageResource(R.drawable.travel_explore_unselected)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, GalleryFragment()).commit()
        }

        menubookButton.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank_unselected)
            menubookButton.setImageResource(R.drawable.menu_book)
            travelexploreButton.setImageResource(R.drawable.travel_explore_unselected)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, GalleryFragment()).commit()
        }


        travelexploreLayout.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank_unselected)
            menubookButton.setImageResource(R.drawable.menu_book_unselected)
            travelexploreButton.setImageResource(R.drawable.travel_explore)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, MapFragment()).commit()
        }

        travelexploreButton.setOnClickListener {
            Log.d("Button","button clicked")
            foodbankButton.setImageResource(R.drawable.food_bank_unselected)
            menubookButton.setImageResource(R.drawable.menu_book_unselected)
            travelexploreButton.setImageResource(R.drawable.travel_explore)
            supportFragmentManager.beginTransaction().
            replace(R.id.content_frame_ver2, MapFragment()).commit()
        }

//        // 하단바 동작 설정 (id값으로 들고옴)
//        val phoneLayout= findViewById<LinearLayout>(R.id.phoneLayout)
//        val imageLayout = findViewById<LinearLayout>(R.id.imageLayout)
//        val otherLayout = findViewById<LinearLayout>(R.id.otherLayout)
//        val phoneButton = findViewById<ImageButton>(R.id.phone_button)
//        val imageButton = findViewById<ImageButton>(R.id.image_button)
//        val otherButton = findViewById<ImageButton>(R.id.other_button)
//        val missionButton = findViewById<ImageButton>(R.id.mission_button)
//
//        phoneLayout.setOnClickListener {
//            Log.d("Button","button clicked")
//            phoneButton.setImageResource(R.drawable.bottom_phone)
//            imageButton.setImageResource(R.drawable.bottom_image_unselected)
//            otherButton.setImageResource(R.drawable.bottom_other_unselected)
//            missionButton.setImageResource(R.drawable.bottom_mission_unselected)
//            supportFragmentManager.beginTransaction().
//                replace(R.id.content_frame, PhoneFragment()).commit()
//        }
//        phoneButton.setOnClickListener {
//            Log.d("Button","button clicked")
//            phoneButton.setImageResource(R.drawable.bottom_phone)
//            imageButton.setImageResource(R.drawable.bottom_image_unselected)
//            otherButton.setImageResource(R.drawable.bottom_other_unselected)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, PhoneFragment()).commit()
//        }
//
//        imageLayout.setOnClickListener {
//            phoneButton.setImageResource(R.drawable.bottom_phone_unselected)
//            imageButton.setImageResource(R.drawable.bottom_image)
//            otherButton.setImageResource(R.drawable.bottom_other_unselected)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, GalleryFragment()).commit()
//        }
//        imageButton.setOnClickListener {
//            phoneButton.setImageResource(R.drawable.bottom_phone_unselected)
//            imageButton.setImageResource(R.drawable.bottom_image)
//            otherButton.setImageResource(R.drawable.bottom_other_unselected)
//            missionButton.setImageResource(R.drawable.bottom_mission_unselected)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, GalleryFragment()).commit()
//        }
//
//        otherLayout.setOnClickListener {
//            phoneButton.setImageResource(R.drawable.bottom_phone_unselected)
//            imageButton.setImageResource(R.drawable.bottom_image_unselected)
//            otherButton.setImageResource(R.drawable.bottom_other)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, MapFragment()).commit()
//        }
//        otherButton.setOnClickListener {
//            phoneButton.setImageResource(R.drawable.bottom_phone_unselected)
//            imageButton.setImageResource(R.drawable.bottom_image_unselected)
//            otherButton.setImageResource(R.drawable.bottom_other)
//            missionButton.setImageResource(R.drawable.bottom_mission_unselected)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, MapFragment()).commit()
//        }
//
//        missionButton.setOnClickListener {
//            phoneButton.setImageResource(R.drawable.bottom_phone_unselected)
//            imageButton.setImageResource(R.drawable.bottom_image_unselected)
//            otherButton.setImageResource(R.drawable.bottom_other_unselected)
//            missionButton.setImageResource(R.drawable.bottom_mission)
//            supportFragmentManager.beginTransaction().
//            replace(R.id.content_frame, MainFragment()).commit()
//        }
    }
}