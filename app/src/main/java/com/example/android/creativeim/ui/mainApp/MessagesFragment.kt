package com.example.android.creativeim.ui.mainApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.creativeim.MainActivity
import com.example.android.creativeim.data.User
import com.example.android.creativeim.databinding.FragmentMessagesBinding
import com.example.android.creativeim.ui.MainViewModel
import com.example.android.creativeim.ui.MessagesAdapter
import com.example.android.creativeim.utils.Logger
import com.example.android.creativeim.utils.Result
import com.example.android.creativeim.utils.getViewModelFactory
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_messages.*

private const val TAG = "MessagesFragment"
class MessagesFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    private lateinit var viewBinding: FragmentMessagesBinding

    private lateinit var currentUser: FirebaseUser
    private lateinit var listAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMessagesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewmodel = viewModel
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setTitleForActionBar()
        setUpClickListeners()
        setUpAdapter()
        setUpView()
        handleBackPress()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToHome()
                }
            })
    }

    private fun setUpAdapter() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.viewmodel?.let {
            layoutManager.stackFromEnd = true
            listAdapter = MessagesAdapter(viewModel)
            viewBinding.messagesList.layoutManager = layoutManager
            viewBinding.messagesList.adapter = listAdapter
        }
    }

    private fun getMessages() {
        viewModel.getMessages(currentUser.uid, arguments.let { (it!!.get("user") as User).userId })
    }

    private fun setUpView() {
        viewModel.authEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    currentUser = it.data as FirebaseUser
                    getMessages()
                }

                is Result.Error -> navigateToHome()
            }
        })
    }

    private fun navigateToHome() {
        findNavController().navigate(MessagesFragmentDirections.actionMessagesFragmentToHomeFragment())
    }

    private fun setUpClickListeners() {
        send.setOnClickListener {
            if (et_message.text.toString().trim().isNotEmpty()) {
                val message = et_message.text.toString()
                val fromId = currentUser.uid
                val toId = arguments?.let { (it.get("user") as User).userId }
                val timeStamp = System.currentTimeMillis()
                val toUser = arguments?.let { (it.get("user") as User).firstName }
                val fromUser = currentUser.displayName

                Logger.log(
                    TAG, """
                    Message : $message,
                    fromId : $fromId,
                    toId : $toId,
                    timeStamp : $timeStamp
                """.trimIndent()
                )

                viewModel.sendMessage(message, fromId, toId!!, timeStamp, toUser!!, fromUser!!)
                et_message.setText("")
                getMessages()
            }
        }
    }

    private fun setTitleForActionBar() {
        (requireContext() as MainActivity).toolbar.title =
            arguments?.let { (it.get("user") as User).firstName }
    }


}
