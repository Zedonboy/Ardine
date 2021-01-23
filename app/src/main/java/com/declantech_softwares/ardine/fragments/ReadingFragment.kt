package com.declantech_softwares.ardine.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.declantech_softwares.ardine.R
import com.declantech_softwares.ardine.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_reading2.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ReadingFragment : Fragment(R.layout.fragment_reading2) {
    private val viewModel by sharedViewModel<MainViewModel>()
    private var currentIndex = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reading_viewpager?.currentItem = currentIndex
        reading_viewpager?.adapter = ScreenSlidePager(this)
        currentIndex = arguments!!.getInt("courseOutlineIndex", 0)
    }

    private inner class ScreenSlidePager(frag : Fragment) : FragmentStateAdapter(frag){
        override fun getItemCount(): Int {
            return viewModel.currentCourseOutline.value!!.size
        }

        override fun createFragment(position: Int): Fragment {
            val data = viewModel.currentCourseOutline.value!![position]
            return ContentFragment().apply {
                arguments = bundleOf("courseOutlineId" to data.id)
            }
        }

    }
}
