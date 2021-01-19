package witchel.cs371m.fclivedata

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import kotlinx.android.synthetic.main.main_activity.*
import witchel.cs371m.fclivedata.ui.main.ConsumeFragment
import witchel.cs371m.fclivedata.ui.main.ProduceFragment
import witchel.cs371m.fclivedata.ui.main.ViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val consumerFragmentName = "consumerFragment"
    private val consumerTitle = "Consumer"
    private val producerTitle = "Producer"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        // If savedInstanceState is null this is the first time we have run
        // and we need to make the fragments.
        if (savedInstanceState == null) {
            // XXX Write me.
            supportFragmentManager.beginTransaction()
                .add(R.id.produce_container, ProduceFragment.newInstance(producerTitle), "produceFragment")
                .add(R.id.consume_container, ConsumeFragment.newInstance(consumerTitle), consumerFragmentName)
                .commitNow()
        }
        // XXX Write me.
        killConsumeBut.setOnClickListener {
            val removedFragment = supportFragmentManager.findFragmentByTag(consumerFragmentName)
            if (removedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(removedFragment)
                    .commit()
            }
        }
        spawnConsumeBut.setOnClickListener {
            val addedFragment = supportFragmentManager.findFragmentByTag(consumerFragmentName)
            if(addedFragment == null){
                supportFragmentManager.beginTransaction()
                    .add(R.id.consume_container, ConsumeFragment.newInstance(consumerTitle), consumerFragmentName)
                    .commit()
            }
        }






    }
}
