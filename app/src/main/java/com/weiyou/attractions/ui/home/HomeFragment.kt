package com.weiyou.attractions.ui.home

import android.app.AlertDialog
import android.os.Bundle
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

    private val homeViewModel: HomeViewModel by viewModels() // 使用 Hilt 注入 ViewModel

    private lateinit var homeAdapter: HomeAdapter
    private val mediatorLiveData = MediatorLiveData<Pair<AttractionsOutput?, NewsOutput?>>()

    private var isFirstLoad = true
    private var isRVLoading = false // recyclerView的添加一个标志位，防止重复加载
    private var attractionTotalAmount = 0
    private var currentAttractionPage = 1 // recyclerView的景點的頁數

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
            homeViewModel.fetchAttractions(currentAttractionPage)//TODO 如果資料太多 可以考慮用一次拿一些page 然後用recycleView的loadmore
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

                // 获取当前显示的第一个和最后一个项的position
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val displayAttractionCount =
                    if (firstVisibleItemPosition - 3 <= 0) 1 else firstVisibleItemPosition - 3

                binding.tvAttractionsCount.text = getString(
                    R.string.app_home_attractions_with_value,
                    displayAttractionCount,
                    attractionTotalAmount
                )

                // 检查是否滑动到底部
                if (!recyclerView.canScrollVertically(1) && !isRVLoading) {
                    loadMore() // 加载下一页
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
            //TODO 處理錯誤訊息
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
        } else {
            val newAttractionItems = attractions.data.map { HomeAttraction(it) }
            homeAdapter.addItems(newAttractionItems) // 添加新数据
            isRVLoading = false // 在这里将isLoading设置为false，表示数据加载完成
        }
    }

    private fun loadMore() {
        isRVLoading = true
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchAttractions(currentAttractionPage + 1) // 加载下一页数据
            currentAttractionPage++ // 更新当前页数
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