package com.declantech_softwares.ardine.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.declantech_softwares.ardine.R
import com.declantech_softwares.ardine.databinding.CourseOutlineItemBinding
import com.declantech_softwares.ardine.types.CustomDBViewHolder
import com.declantech_softwares.ardine.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_course_outline.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CourseOutlineFragment : Fragment(R.layout.fragment_course_outline){
    private val viewModel by sharedViewModel<MainViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.get("courseId") ?: view.findNavController().popBackStack()
        initUI()
        viewModel.getCourseOutlines(id as Int)
        viewModel.getCoursePastQuestions(id)
        viewModel.currentCourseOutline.observe(viewLifecycleOwner, Observer {
            course_outline_recyclerView?.adapter?.notifyDataSetChanged()
        })
    }

    private fun initUI() {
        course_outline_recyclerView?.layoutManager = LinearLayoutManager(context)
        course_outline_recyclerView?.adapter = CustomAdapter()
        course_outline_recyclerView?.setHasFixedSize(true)
    }

    inner class CustomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        private val TEST_VIEW = 1
        private val ITEM_VIEW = 0
        private val PAST_QUESTIONS = 2
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                ITEM_VIEW -> {
                    val view = layoutInflater.inflate(R.layout.course_outline_item, parent)
                    CustomDBViewHolder.from(view)
                }

                TEST_VIEW,PAST_QUESTIONS -> {
                    val view = layoutInflater.inflate(R.layout.test_courseoutline_item, parent)
                    CustomDBViewHolder(view)
                }
                else -> {
                    val view = layoutInflater.inflate(R.layout.course_outline_item, parent)
                    CustomDBViewHolder(view)
                }
            }
        }

        override fun getItemCount(): Int {
            viewModel.courseMatList.value?.size ?: return 0
            return if(viewModel.currentPastQuestions.isEmpty()) viewModel.courseMatList.value!!.size
            else viewModel.courseMatList.value!!.size + 2
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
           val view = holder.itemView
            when(holder.itemViewType) {
                ITEM_VIEW -> {
                    val data = viewModel.currentCourseOutline.value!![position]
                    holder as CustomDBViewHolder
                    val binder = holder.getBinder<CourseOutlineItemBinding>()
                    binder.outline = data
                    binder.executePendingBindings()
                    view.setOnClickListener {
                        val args = bundleOf("courseOutlineIndex" to position)
                        it.findNavController().navigate(R.id.action_courseOutlineFragment_to_readingFragment, args)
                    }
                }
                TEST_VIEW -> {
                    view.findViewById<TextView>(R.id.test_name).text = "Test"
                    view.setOnClickListener {  }
                }

                PAST_QUESTIONS -> {
                    view.findViewById<TextView>(R.id.test_name).text = "Past Questions"
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                viewModel.currentPastQuestions.isEmpty() -> ITEM_VIEW
                position == viewModel.courseMatList.value!!.size -> TEST_VIEW
                position == (viewModel.courseMatList.value!!.size + 1) -> PAST_QUESTIONS
                else -> ITEM_VIEW
            }
        }

    }
}