package edu.cs371m.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.image_fragment.*

class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        image_fragmentIV.setOnClickListener {
            // XXX Write me (one liner)
            fragmentManager?.popBackStack()
        }
        // XXX Write me (one liner)
        val mImageBitmap = arguments!!.getParcelable<Bitmap>("bitmap")
        image_fragmentIV.setImageBitmap(mImageBitmap)
    }

    companion object {
        //  Why fragments need a constructor with zero parameters
        //    https://stackoverflow.com/questions/10450348/do-fragments-really-need-an-empty-constructor
        internal fun newInstance(bitmap: Bitmap?): ImageFragment {
            val imageFragment = ImageFragment()
            // Notice how this process resembles Bundles in Intents
            val b = Bundle()
            b.putParcelable("bitmap", bitmap)
            // NB: Fragment has an arguments property
            imageFragment.arguments = b
            return imageFragment
        }
    }
}