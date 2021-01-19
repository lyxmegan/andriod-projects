package witchel.cs371m.fclivedata.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.consume_fragment.*
import kotlinx.android.synthetic.main.produce_fragment.title
import witchel.cs371m.fclivedata.R

class ConsumeFragment : Fragment() {
    companion object {
        const val titleKey = "consumeTitle"
        fun newInstance(title: String): ConsumeFragment {
            val frag = ConsumeFragment()
            val bundle = Bundle()
            // XXX set the fragment's arguments
            bundle.putString(titleKey, title)
            frag.arguments = bundle
            return frag
        }
    }
    //NB: This is the new way to initialize a viewModel that is shared
    // with the parent activity
    // XXX declare and initialize viewModel
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.consume_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // XXX Write me.
        super.onActivityCreated(savedInstanceState)
        title.text = "Consumer"
        viewModel.observeData().observe(viewLifecycleOwner, Observer<String> {
               consumeTV.text = it
        })
        val consumerFragmentName = "consumerFragment"
        killMeBut.setOnClickListener {
            val removeFragment = parentFragmentManager.findFragmentByTag(consumerFragmentName)
            if (removeFragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(removeFragment)
                    .commit()
            }
        }
    }
}
