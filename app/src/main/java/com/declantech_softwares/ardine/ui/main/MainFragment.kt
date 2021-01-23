package com.declantech_softwares.ardine.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.declantech_softwares.ardine.R
import com.declantech_softwares.ardine.databinding.MainFragmentBinding
import com.declantech_softwares.ardine.databinding.SearchItemBinding
import com.declantech_softwares.ardine.services.AppDatabase
import com.declantech_softwares.ardine.types.AppCallback
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CustomDBViewHolder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@BindingAdapter("app:goneUnless")
fun goneUnless(v : View, visible : Boolean) {
    v.visibility = if(visible) View.VISIBLE else View.GONE
}
class MainFragment : Fragment() {
    
    private lateinit var binding : MainFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()
    private val appDB by inject<AppDatabase>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        with(binding.mainFragRecycler){
            layoutManager = LinearLayoutManager(context)
            adapter = CourseAdapter()
            setHasFixedSize(true)
        }
        viewModel.getLatestCourses()
    }

    inner class CourseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val PROGRESS_VIEW = 1
        private val ITEM_VIEW = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if(viewType == 1) {
                val view = layoutInflater.inflate(R.layout.search_item, parent)
                CustomDBViewHolder.from(view)
            } else {
                val view = layoutInflater.inflate(R.layout.progress_search_item, parent)
                CustomDBViewHolder(view)
            }
        }

        override fun getItemCount(): Int {
            return (viewModel.courseMatList.value?.size ?: 0) + 1
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when(holder.itemViewType){
                ITEM_VIEW -> {
                    holder as CustomDBViewHolder
                    val binding = holder.getBinder<SearchItemBinding>()
                    val courseMat = viewModel.courseMatList.value!![position]
                    binding.courseData = courseMat
                    binding.searchItemCard.setOnClickListener {
                        val args = bundleOf("courseId" to courseMat.id)
                        it.findNavController().navigate(R.id.action_mainFragment_to_courseOutlineFragment, args)
                    }
                    binding.searchItemSaveBtn.setOnClickListener {
                        viewModel.saveToDatabase(courseMat, object : AppCallback<CourseMaterial> {
                            override fun onSuccess(data: CourseMaterial) {
                                Toast.makeText(context, "Course Saved!", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFail(e: Error) {
                                it.findNavController().navigate(R.id.action_premiumUpgradeFragment_self)
                            }

                        })
                    }
                    binding.executePendingBindings()
                }
            }

        }

        override fun getItemViewType(position: Int): Int {
            viewModel.courseMatList.value ?: return 0
            return if(position == viewModel.courseMatList.value!!.size) PROGRESS_VIEW
            else ITEM_VIEW
        }

    }

}