package com.weiyou.attractions.ui.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.weiyou.attractions.R
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

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchAttractions()
            homeViewModel.fetchNews()
        }

        // 观察 ViewModel 中的 attractions 数据变化并更新 UI
        homeViewModel.attractions.observe(viewLifecycleOwner) { attractions ->
            attractions?.let {
                binding.tvAttractionsCount.text = getString(
                    R.string.app_home_attractions_with_value,
                    attractions.total.toString()
                )
            }
        }

        homeViewModel.news.observe(viewLifecycleOwner) { attractions ->
            attractions?.let {
                Toast.makeText(
                    requireActivity(), getString(
                        R.string.app_home_attractions_with_value,
                        attractions.total.toString()
                    ), Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvAttractionsCount.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
        }
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