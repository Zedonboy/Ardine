package com.declantech_softwares.ardine


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.declantech_softwares.ardine.databinding.FragmentRegisterBinding
import com.declantech_softwares.ardine.types.AppCallback
import com.declantech_softwares.ardine.types.AppUser
import com.declantech_softwares.ardine.types.RegisterContract
import com.declantech_softwares.ardine.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by sharedViewModel<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)
        binding.contract = RegisterContract()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        register_btn?.setOnClickListener {
            it.isEnabled = false
            viewModel.registerUser(binding.contract, object : AppCallback<AppUser>{
                override fun onSuccess(data: AppUser) {
                    it.isEnabled = true
                    findNavController().navigate(R.id.action_loginfragment)
                }

                override fun onFail(e: Error) {
                    it.isEnabled = true
                    val toast = Toast.makeText(context!!, e.message, Toast.LENGTH_LONG)
                    toast.show()
                }

            })
        }
    }

}
