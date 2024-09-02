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
import com.weiyou.attractions.data.models.api.attractions.Attraction
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

        setUpperBar(getString(R.string.app_home_title))
        setupRecyclerView()
        setObservers()

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchAttractions()//TODO 如果資料太多 可以考慮用一次拿一些page 然後用recycleView的loadmore
            homeViewModel.fetchNews()
        }
    }

    private fun setUpperBar(title: String) {
        val bottonListener = object : UpperBarRightBottonListener {
            override fun performAction() {
                if (isAdded) {
                    showLanguagePickerDialog()
                }
            }
        }

        (activity as? MainActivity)?.setUpperBar(title, null, bottonListener)
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(context)

        homeAdapter.setOnNewsItemClickListener(object : HomeAdapter.OnNewsItemClickListener {
            override fun onNewsItemClick(url: String) {
                val bundle = Bundle()
                bundle.putString("url", url)
                findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
            }
        })

        homeAdapter.setOnAttractionClickListener(object : HomeAdapter.OnAttractionClickListener {
            override fun onAttractionClick(attraction: Attraction) {
                //位置A
                val bundle = Bundle().apply {
                    putParcelable("attraction", attraction) // 确保 Attraction 实现 Parcelable 接口
                }
                findNavController().navigate(R.id.action_homeFragment_to_attractionFragment, bundle)
            }
        })
    }

    private fun setObservers() {
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
                setItemIntoRV(attractions, news)
            }
        }
    }

    private fun setItemIntoRV(attractions: AttractionsOutput, news: NewsOutput) {
        val homeItems = mutableListOf<HomeItem>()

        homeItems.add(
            HomeTitle(
                getString(
                    R.string.app_home_news
                )
            )
        )

        // 只選擇前三個新聞項目添加到列表
        news.data.take(3).forEach { newsItem ->
            homeItems.add(HomeNewsItem(newsItem))
        }

        homeItems.add(
            HomeTitle(
                getString(
                    R.string.app_home_attractions
                )
            )
        )

        attractions.data.forEach { attraction ->
            homeItems.add(HomeAttraction(attraction))
        }

        homeAdapter.setItems(homeItems)  // 更新 RecyclerView 的數據
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

        mediatorLiveData.removeSource(homeViewModel.attractions)
        mediatorLiveData.removeSource(homeViewModel.news)

        _binding = null
    }
}