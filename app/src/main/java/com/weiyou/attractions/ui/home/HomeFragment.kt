package com.weiyou.attractions.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var homeAdapter: HomeAdapter
    private val mediatorLiveData = MediatorLiveData<Pair<AttractionsOutput?, NewsOutput?>>()

    private var isFirstLoad = true
    private var isRVLoading = false
    private var attractionTotalAmount = 0
    private var currentAttractionPage = 1 // recyclerView的景點的頁數
    private var preAttractionItemCount = 0

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
            homeViewModel.fetchAttractions(currentAttractionPage)
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
        val layoutManager = LinearLayoutManager(context)
        binding.rvHome.layoutManager = layoutManager

        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                // 設置顯示景點數
                val displayAttractionCount =
                    if ((firstVisibleItemPosition + 1) - preAttractionItemCount <= 0) 1 else (firstVisibleItemPosition + 1) - preAttractionItemCount //+1是因為firstVisibleItemPosition從０開始算
                if (attractionTotalAmount == 0) {
                    binding.tvAttractionsCount.text = getString(
                        R.string.app_home_attractions_with_value,
                        attractionTotalAmount,
                        attractionTotalAmount
                    )
                } else {
                    binding.tvAttractionsCount.text = getString(
                        R.string.app_home_attractions_with_value,
                        displayAttractionCount,
                        attractionTotalAmount
                    )
                }

                // 检查是否滑動到底部
                if (!recyclerView.canScrollVertically(1) && !isRVLoading) {
                    loadMore() // 加載下一頁
                }
            }
        })

        homeAdapter.setOnNewsItemClickListener(object : HomeAdapter.OnNewsItemClickListener {
            override fun onNewsItemClick(url: String) {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.app_home_news))
                bundle.putString("url", url)
                findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
            }
        })

        homeAdapter.setOnAttractionClickListener(object : HomeAdapter.OnAttractionClickListener {
            override fun onAttractionClick(attraction: Attraction) {
                val bundle = Bundle().apply {
                    putParcelable("attraction", attraction)
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
                if (isFirstLoad) {
                    attractionTotalAmount = attractions.total.toString().toInt()
                    binding.tvAttractionsCount.text = getString(
                        R.string.app_home_attractions_with_value,
                        1,
                        attractionTotalAmount
                    )
                }
                setItemIntoRV(attractions, news)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setItemIntoRV(attractions: AttractionsOutput, news: NewsOutput) {
        if (isFirstLoad) {
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

            homeAdapter.setItems(homeItems)
            isFirstLoad = false
            preAttractionItemCount = 2 //這個2,是因為recyclerView前面已經先有最新消息的title,遊憩景點的title,共兩個項目
            preAttractionItemCount += if (news.data.size > 3) {//最新消息最多顯示3個
                3
            } else {
                news.data.size
            }
        } else {
            val newAttractionItems = attractions.data.map { HomeAttraction(it) }
            homeAdapter.addItems(newAttractionItems)
            isRVLoading = false // 在這裡將 isLoading 設置為 false，表示數據加載完成
        }
    }

    private fun loadMore() {
        isRVLoading = true
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchAttractions(currentAttractionPage + 1) // 加載下一頁的數據
            currentAttractionPage++ // 更新當前頁數
        }
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

        isFirstLoad = true
        attractionTotalAmount = 0
        currentAttractionPage = 1

        _binding = null
    }
}