package com.weiyou.attractions.ui.attraction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weiyou.attractions.R
import com.weiyou.attractions.data.models.api.attractions.Attraction
import com.weiyou.attractions.databinding.FragmentAttractionBinding
import com.weiyou.attractions.ui.MainActivity
import com.weiyou.attractions.utils.listener.UpperBarBackBottonListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttractionFragment : Fragment() {

    private var _binding: FragmentAttractionBinding? = null
    private val binding get() = _binding!!

    private val args: AttractionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttractionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpperBar()

        val attraction: Attraction = args.attraction
        binding.tvOpenTime.text = attraction.open_time // 例如显示景点名称

    }

    private fun setUpperBar() {
        val title = args.attraction.name

        val backListener = object : UpperBarBackBottonListener {
            override fun performAction() {
                if (isAdded) {
                    findNavController().popBackStack()
                }
            }
        }

        (activity as? MainActivity)?.setUpperBar(title, backListener, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}