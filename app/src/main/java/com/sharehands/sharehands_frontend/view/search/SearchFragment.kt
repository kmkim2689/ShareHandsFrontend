package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ViewPagerAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentSearchBinding
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: ServiceSearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ServiceSearchViewModel::class.java)
        binding.lifecycleOwner = MainActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        // 글쓰기 완료 시 바로 반영 하려면 onResume에 넣음
        val viewPager: ViewPager2 = binding.viewpagerService
        val tabLayout = binding.layoutServiceTab
        val orderSpinner = binding.spinnerServiceCategory

        // ViewPager 어댑터 설정
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    // 탭 이름 설정
                    tab.text = "전체"
                }
                1 -> {
                    tab.text = "교육"
                }
                2 -> {
                    tab.text = "문화"
                }
                3 -> {
                    tab.text = "보건"
                }
                4 -> {
                    tab.text = "환경"
                }
                5 -> {
                    tab.text = "기술"
                }
                6 -> {
                    tab.text= "기타"
                }

            }
        }.attach()

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.service_order,
            android.R.layout.simple_spinner_item
        )

        // 드롭다운 시 레이아웃 설정
        spinnerAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item)
        // address(spinner 뷰)에 만들어놓은 adapter를 할당한다.
        orderSpinner.adapter = spinnerAdapter
        orderSpinner.dropDownVerticalOffset = 120

        var orderBy = "최신순"

        orderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                orderBy = when (position) {
                    0 -> {
                        "최신순"
                    }
                    1 -> {
                        "좋아요순"
                    }
                    else -> {
                        "스크랩순"
                    }
                }
                Log.d("선택된 정렬 순서", orderBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않는 경우는 발생하지 않으므로 비워둠
            }
        }

        binding.btnWriteService.setOnClickListener {
            val intent = Intent(requireContext(), ServiceWriteActivity::class.java)
            startActivity(intent)
        }

        binding.ivSearchService.setOnClickListener {
            val isSuccess = viewModel.searchKeyword()
            if (isSuccess) {
                // 화면 이동하기
                val intent = Intent(requireContext(), SearchResultActivity::class.java)
                intent.putExtra("keyword", viewModel.searchKeyword.value)
                Log.d("봉사활동 search keyword", viewModel.searchKeyword.value.toString())
                startActivity(intent)
            } else {
                // 스낵바 띄우기
                Snackbar.make(requireView(), "2글자 이상 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

}