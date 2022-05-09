package com.task.ui.school

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.task.databinding.HomeFragmentBinding
import com.task.databinding.SchoolFragmentBinding
import com.task.ui.base.BaseFragment
import com.task.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolFragment: BaseFragment() {

    private val schoolViewModel: SchoolFragmentViewModel by viewModels()
    private lateinit var binding: SchoolFragmentBinding


    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    override fun observeViewModel() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments?.getString(ARG_PARAM1)
            mParam2 = arguments?.getString(ARG_PARAM2)
        }
    }

    fun newInstance(param1: String?, param2: String?): HomeFragment? {
        val fragment = HomeFragment()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SchoolFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
}