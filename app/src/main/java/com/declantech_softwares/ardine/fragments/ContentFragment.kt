package com.declantech_softwares.ardine.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.declantech_softwares.ardine.R
import com.declantech_softwares.ardine.databinding.ContentFragBinding
import com.declantech_softwares.ardine.types.AppCallback
import com.declantech_softwares.ardine.types.CourseContent
import com.declantech_softwares.ardine.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ContentFragment : Fragment(R.layout.content_frag) {
    val loadingContent = ObservableBoolean(true)
    private val viewModel by sharedViewModel<MainViewModel>()
    private var courseContent : CourseContent? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = ContentFragBinding.inflate(inflater)
        binding.frag = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getInt("courseOutlineId") ?: kotlin.run {
            val toast = Toast.makeText(context, "Outline Identifier not provided", Toast.LENGTH_SHORT)
            toast.show()
            return
        }
        viewModel.getCourseContent(id, object : AppCallback<CourseContent>{
            override fun onSuccess(data: CourseContent) {
                courseContent = data
                loadWebView()
            }

            override fun onFail(e: Error) {
                val toast = Toast.makeText(context, "Omoh!!, wahala dey ooo", Toast.LENGTH_SHORT)
                toast.show()
            }

        })
    }

    private fun loadWebView() {
        TODO("Work on Webview")
    }
}