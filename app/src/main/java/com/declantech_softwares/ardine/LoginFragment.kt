package com.declantech_softwares.ardine


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.declantech_softwares.ardine.databinding.FragmentLoginBinding
import com.declantech_softwares.ardine.types.AppCallback
import com.declantech_softwares.ardine.types.LoginContract
import com.declantech_softwares.ardine.types.LoginResponse
import com.declantech_softwares.ardine.types.valid
import com.declantech_softwares.ardine.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModdel by sharedViewModel<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        binding.contract = LoginContract()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login_btn?.setOnClickListener {
            it.isEnabled = false
            if(binding.contract.valid()){
                viewModdel.loginUser(binding.contract, object : AppCallback<LoginResponse>{
                    override fun onSuccess(data: LoginResponse) {
                        findNavController().navigate(R.id.action_mainfragment)
                        it.isEnabled = true
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


}
