package com.example.calendaradmin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ppupTx = findViewById<TextView>(R.id.ppupTx)
        val popupV = findViewById<LinearLayout>(R.id.popupV)
        val calenV = findViewById<LinearLayout>(R.id.calenV)
        val bktcBt = findViewById<Button>(R.id.bktcBt)
        val backBt = findViewById<Button>(R.id.backBt)
        val nextBt = findViewById<Button>(R.id.nextBt)
        val mnthTx = findViewById<TextView>(R.id.mnthTx)
        val pdayTx = findViewById<TextView>(R.id.pdayTx)
        val predBt = findViewById<Button>(R.id.predBt)
        val nexdBt = findViewById<Button>(R.id.nexdBt)
        val dayBtList = arrayOf(
            findViewById<Button>(R.id.day1Bt),
            findViewById<Button>(R.id.day2Bt),
            findViewById<Button>(R.id.day3Bt),
            findViewById<Button>(R.id.day4Bt),
            findViewById<Button>(R.id.day5Bt),
            findViewById<Button>(R.id.day6Bt),
            findViewById<Button>(R.id.day7Bt),
            findViewById<Button>(R.id.day8Bt),
            findViewById<Button>(R.id.day9Bt),
            findViewById<Button>(R.id.day10Bt),
            findViewById<Button>(R.id.day11Bt),
            findViewById<Button>(R.id.day12Bt),
            findViewById<Button>(R.id.day13Bt),
            findViewById<Button>(R.id.day14Bt),
            findViewById<Button>(R.id.day15Bt),
            findViewById<Button>(R.id.day16Bt),
            findViewById<Button>(R.id.day17Bt),
            findViewById<Button>(R.id.day18Bt),
            findViewById<Button>(R.id.day19Bt),
            findViewById<Button>(R.id.day20Bt),
            findViewById<Button>(R.id.day21Bt),
            findViewById<Button>(R.id.day22Bt),
            findViewById<Button>(R.id.day23Bt),
            findViewById<Button>(R.id.day24Bt),
            findViewById<Button>(R.id.day25Bt),
            findViewById<Button>(R.id.day26Bt),
            findViewById<Button>(R.id.day27Bt),
            findViewById<Button>(R.id.day28Bt),
            findViewById<Button>(R.id.day29Bt),
            findViewById<Button>(R.id.day30Bt),
            findViewById<Button>(R.id.day31Bt),
            findViewById<Button>(R.id.day32Bt),
            findViewById<Button>(R.id.day33Bt),
            findViewById<Button>(R.id.day34Bt),
            findViewById<Button>(R.id.day35Bt),
            findViewById<Button>(R.id.day36Bt),
            findViewById<Button>(R.id.day37Bt),
            findViewById<Button>(R.id.day38Bt),
            findViewById<Button>(R.id.day39Bt),
            findViewById<Button>(R.id.day40Bt),
            findViewById<Button>(R.id.day41Bt),
            findViewById<Button>(R.id.day42Bt)
        )



        val current = LocalDateTime.now()

        var year = Integer.parseInt(current.format(DateTimeFormatter.ofPattern("YYYY")))
        var month = Integer.parseInt(current.format(DateTimeFormatter.ofPattern("MM"))) - 1
        var day = 1
        val monthlist = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val shiftList = arrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
        var mLenList = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)

        fun hideNFixDayNums() {
            mnthTx.text = monthlist[month] + " " + year.toString()

            for (i in dayBtList) {
                i.text = ""

                i.isVisible = false
            }


            if (year % 4 == 0){
                mLenList[1] = 29
            }

            var year2 = year

            if (month < 2) {
                year2 -= 1
            }

            var shift = (year2 + year2/4 - year2/100 + year2/400 + shiftList[month] + 1) % 7

            var monthlength = mLenList[month]

            for (i in 1..monthlength) {
                dayBtList[(i + shift - 1)].isVisible = true

                dayBtList[(i + shift - 1)].text = i.toString()
            }
        }


        fun getValueFromDatabase(path: List<String>, reference: String, tvid: TextView){
            var myRef = FirebaseDatabase.getInstance().getReference(reference)

            for (i in path.indices){
                myRef = myRef.child(path[i]);
            }

            //the code below was taken from the firebase documentation
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    val value = dataSnapshot.getValue<String>()
                    if (value == null){
                        tvid.text = ""
                    } else {
                        tvid.text = HtmlCompat.fromHtml("$value", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(getApplicationContext(), "joe" + "Failed to read value." + error.toException(), Toast.LENGTH_LONG).show()
                }
            })
        }


        popupV.isVisible = false


        hideNFixDayNums()


        backBt.setOnClickListener {
            if (month == 0){
                year -= 1
            }

            month -= 1

            while (month < 0){
                month += 12
            }

            month %= 12

            mnthTx.text = monthlist[month]

            hideNFixDayNums()
        }


        nextBt.setOnClickListener {
            if (month == 11){
                year += 1
            }

            month = (month + 1) % 12

            mnthTx.text = monthlist[month]

            hideNFixDayNums()
        }


        for (i in dayBtList) {

            i.setOnClickListener {
                popupV.isVisible = true

                calenV.isVisible = false

                day = Integer.parseInt(i.text.toString())

                getValueFromDatabase(listOf(monthlist[month], day.toString()), "calActivity", ppupTx)

                pdayTx.text = monthlist[month] + " " + day.toString()


            }
        }


        predBt.setOnClickListener {
            if (day == 1){
                if (month == 0){
                    year -= 1
                }
                month -= 1
                while (month < 0){
                    month += 12
                }
                month %= 12
                mnthTx.text = monthlist[month]
                hideNFixDayNums()

                day = mLenList[month]

            }else{
                day -= 1
            }

            getValueFromDatabase(listOf(monthlist[month], day.toString()), "calActivity", ppupTx)

            pdayTx.text = monthlist[month] + " " + day.toString()
        }


        nexdBt.setOnClickListener {
            if (day == mLenList[month]){
                if (month == 11){
                    year += 1
                }
                month = (month + 1) % 12
                mnthTx.text = monthlist[month]
                hideNFixDayNums()

                day = 1

            }else{
                day += 1
            }

            getValueFromDatabase(listOf(monthlist[month], day.toString()), "calActivity", ppupTx)

            pdayTx.text = monthlist[month] + " " + day.toString()
        }


        bktcBt.setOnClickListener {
            popupV.isVisible = false

            calenV.isVisible = true

            ppupTx.text = ""

            pdayTx.text = "M D"
        }

    }
}