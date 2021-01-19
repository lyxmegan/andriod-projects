package edu.cs371m.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.list_image_fragment.*

class ListPhotoFragment : Fragment() {

    companion object {
        fun newInstance() : ListPhotoFragment {
            return ListPhotoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements
        return inflater.inflate(R.layout.list_image_fragment, container, false)
    }

    private fun makeImageClickable(imageView: ImageView) {
        imageView.setOnClickListener {
            val bitmap = imageView.drawable.toBitmap()
            val imageFragment = ImageFragment.newInstance(bitmap)
            // XXX Write me (FragmentManager)
            fragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.main_fragment, imageFragment)
                    ?.addToBackStack("pic")
                    ?.commit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageViews = listOf (
            image0, // All from XML
            image1,
            image2,
            image3
        )

        imageViews.forEach{
            // XXX Write me
            makeImageClickable(it)
            it.setOnLongClickListener {
                var bitMap0= image0.drawable.toBitmap()
                image0.setImageBitmap(imageViews[1].drawable.toBitmap())
                image1.setImageBitmap(imageViews[2].drawable.toBitmap())
                image2.setImageBitmap(imageViews[3].drawable.toBitmap())
                image3.setImageBitmap(bitMap0)
                true
            }
        }
    }
}