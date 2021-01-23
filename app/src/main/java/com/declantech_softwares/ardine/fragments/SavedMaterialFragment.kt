package com.declantech_softwares.ardine.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.declantech_softwares.ardine.R
import com.declantech_softwares.ardine.databinding.SearchItemBinding
import com.declantech_softwares.ardine.services.AppCache
import com.declantech_softwares.ardine.services.AppDatabase
import com.declantech_softwares.ardine.types.AppCallback
import com.declantech_softwares.ardine.types.AppContext
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CustomDBViewHolder
import com.declantech_softwares.ardine.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_saved_material.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class SavedMaterialFragment : Fragment(R.layout.fragment_saved_material), CoroutineScope{
    val appDatabase by inject<AppDatabase>()
    val appContext by inject<AppContext>()
    val appCache by inject<AppCache>()
    val viewModel by sharedViewModel<MainViewModel>()
    val list = mutableListOf<CourseMaterial>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpAdapterData()
        with(saved_frag_recycler){
            adapter = CustomAdapter()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        swipe_refresh_layout?.setOnRefreshListener {
            // call database.
            setUpAdapterData()
        }
    }

    private fun initUI() {

    }

    private fun setUpAdapterData() {
        launch {
            val appdao = appDatabase.appDao()
            list.addAll(appdao.loadAllCourses())
            withContext(Dispatchers.Main){
                saved_frag_recycler?.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class CustomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
            return CustomDBViewHolder.from(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                launch {
                    val data = list.getOrNull(position) ?: return@launch
                    val outlines = appDatabase.appDao().loadOutlinesByCourseId(data.id)
                    appCache.outlineCache[data.id] = outlines
                    withContext(Dispatchers.Main){
                        val args = bundleOf("courseId" to data.id)
                        it.findNavController().navigate(R.id.action_mainFragment_to_courseOutlineFragment, args)
                    }
                }
            }
            holder as CustomDBViewHolder
            val binder = holder.getBinder<SearchItemBinding>()
            binder.courseData = list.getOrNull(position) ?: return
            binder.context = appContext
            binder.searchItemDeleteBtn.setOnClickListener {
                viewModel.deleteCourseOutline(binder.courseData, object : AppCallback<CourseMaterial> {
                    override fun onSuccess(data: CourseMaterial) {
                        showMssg("Deleted Successfully!")
                    }

                    override fun onFail(e: Error) {
                        showMssg("Wahala Dey")
                    }

                })
            }
            binder.executePendingBindings()
        }
    }

    fun showMssg(mssg : String) {
        Toast.makeText(context, mssg, Toast.LENGTH_LONG).show()
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
}