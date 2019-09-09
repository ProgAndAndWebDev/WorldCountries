package com.example.worldcountries.ui.dailyprogram

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldcountries.R
import com.example.worldcountries.app.WCApplication
import com.example.worldcountries.data.room.Country
import com.example.worldcountries.ui.countries.CountriesAdapter
import com.example.worldcountries.ui.countries.CountriesItemDecoration
import com.example.worldcountries.ui.countries.CountryViewHolder
import com.example.worldcountries.ui.quiz.*
import kotlinx.android.synthetic.main.fragment_daily_program.*
import javax.inject.Inject

class DailyProgramFragment : Fragment(), DailyProgramPresentationContract.View,
    CountryViewHolder.OnCountrySelectedListener{

    @Inject
    lateinit var presenter: DailyProgramPresentationContract.Presenter
    private var component: DailyProgramComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createComponent()?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_program, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onScreenStarted()
        startQuizButton.setOnClickListener {
            navigateToQuizScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun setCountries(countries: List<Country>) {
        val countriesAdapter = CountriesAdapter(countries, this)
        if (countriesRecyclerView?.itemDecorationCount == 0)
            countriesRecyclerView.addItemDecoration(CountriesItemDecoration(context!!))

        countriesRecyclerView.adapter = countriesAdapter
        countriesRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }

    override fun navigateToQuizScreen() {
        view?.findNavController()
            ?.navigate(
                R.id.action_dailyProgramFragment_to_quizFragment,
                bundleOf(TYPE to TYPE_CAPITALS_DAILY, ORDER to DIRECT_ORDER))
    }

    override fun onSelected(countryName: String?) {
        view?.findNavController()
            ?.navigate(
                R.id.action_dailyProgramFragment_to_countryInfoFragment,
                bundleOf("name" to countryName)
            )
    }

    private fun createComponent(): DailyProgramComponent? {
        component = (activity?.application as WCApplication)
            .component
            .createSubComponent(DailyProgramModule(this))
        return component
    }
}