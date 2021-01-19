package witchel.cs371m.fclivedata.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.produce_fragment.*
import witchel.cs371m.fclivedata.R
import kotlin.random.Random

class ProduceFragment :
    Fragment()
{
    companion object {
        const val titleKey = "produceTitle"
        fun newInstance(title: String): ProduceFragment {
            val frag = ProduceFragment()
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
        return inflater.inflate(R.layout.produce_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // XXX Write me.
        super.onActivityCreated(savedInstanceState)
        title.text = "Producer"
        produceBut.setOnClickListener {
            produceTV.text = (0..9999).random().toString()
            val text = produceTV.text.toString()
            viewModel.updateData(text)
        }

    }
}
