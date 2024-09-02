package com.weiyou.attractions.ui.attraction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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

        val attraction: Attraction = args.attraction
        setUpperBar(attraction.name)
        setupRecycerVIew(attraction)

    }

    private fun setUpperBar(title: String) {
        val backListener = object : UpperBarBackBottonListener {
            override fun performAction() {
                if (isAdded) {
                    findNavController().popBackStack()
                }
            }
        }

        (activity as? MainActivity)?.setUpperBar(title, backListener, null)
    }

    private fun setupRecycerVIew(attraction: Attraction) {
        val attractionAdapter = AttractionAdapter()
        binding.rvAttraction.adapter = attractionAdapter
        binding.rvAttraction.layoutManager = LinearLayoutManager(context)

        attractionAdapter.setOnUrlListener(object : AttractionAdapter.OnUrlClickListener {
            override fun onUrlClick(url: String) {
                val bundle = Bundle()
                bundle.putString("title", attraction.name)
                bundle.putString("url", attraction.url)
                findNavController().navigate(
                    R.id.action_attractionFragment_to_webViewFragment,
                    bundle
                )
            }
        })

        val attractionItems = mutableListOf<AttractionItem>()

        attraction.images.forEach {
            attractionItems.add(AttractionImage(it.src))
        }

        attractionItems.add(AttractionInfo(attraction))

        attractionAdapter.setItems(attractionItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}