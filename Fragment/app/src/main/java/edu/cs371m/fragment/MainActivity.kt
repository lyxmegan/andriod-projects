package edu.cs371m.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // XXX Write me
        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment, ListPhotoFragment.newInstance(), "photoList")
                .commit()
        }
    }
}
