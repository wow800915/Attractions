package com.weiyou.attractions.ui.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weiyou.attractions.R
import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.api.news.NewsOutput
import com.weiyou.attractions.databinding.FragmentHomeBinding
import com.weiyou.attractions.ui.MainActivity
import com.weiyou.attractions.utils.listener.UpperBarRightBottonListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels() // 使用 Hilt 注入 ViewModel

    private lateinit var homeAdapter: HomeAdapter
    private val mediatorLiveData = MediatorLiveData<Pair<AttractionsOutput?, NewsOutput?>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpperBar()
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchAttractions()
            homeViewModel.fetchNews()
        }

        mediatorLiveData.addSource(homeViewModel.attractions) { attractions ->
            mediatorLiveData.value = attractions to mediatorLiveData.value?.second
        }

        mediatorLiveData.addSource(homeViewModel.news) { news ->
            mediatorLiveData.value = mediatorLiveData.value?.first to news
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (attractions, news) ->
            if (attractions != null && news != null) {
                binding.tvAttractionsCount.text = getString(
                    R.string.app_home_attractions_with_value,
                    attractions.total.toString()
                )

                val homeItems = mutableListOf<HomeItem>()

                news.data.forEach { newsItem ->
                    homeItems.add(HomeNewsItem(newsItem))
                }

                attractions.data.forEach { attraction ->
                    homeItems.add(HomeAttraction(attraction))
                }

                homeAdapter.setItems(homeItems)  // 更新 RecyclerView 的數據
            }
        }

        binding.tvAttractionsCount.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpperBar() {
        val title = getString(R.string.app_home_title) // 替换 your_string_id 为你的字符串资源ID

        val bottonListener = object : UpperBarRightBottonListener {
            override fun performAction() {
                if (isAdded) {
                    showLanguagePickerDialog()
                }
            }
        }

        (activity as? MainActivity)?.setUpperBar(title, null, bottonListener)
    }

    private fun showLanguagePickerDialog() {
        val languages = resources.getStringArray(R.array.language_options)
        val languageValues = resources.getStringArray(R.array.language_values)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.app_home_choose_language)
            .setItems(languages) { dialog, which ->
                val selectedLanguage = languageValues[which]

                viewLifecycleOwner.lifecycleScope.launch {
                    homeViewModel.saveLanguage(selectedLanguage)
                }
            }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}