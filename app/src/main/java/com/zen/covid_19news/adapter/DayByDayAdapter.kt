package com.zen.covid_19news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zen.covid_19news.R
import com.zen.covid_19news.models.CountryTotalAllStatusResponse
import com.zen.covid_19news.models.CountryTotalAllStatusResponseItem
import com.zen.covid_19news.utils.UIHelper
import kotlinx.android.synthetic.main.day_by_day_card.view.*

class DayByDayAdapter : RecyclerView.Adapter<DayByDayAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack =
        object : DiffUtil.ItemCallback<CountryTotalAllStatusResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CountryTotalAllStatusResponseItem,
                newItem: CountryTotalAllStatusResponseItem
            ): Boolean {
                return oldItem.Date == newItem.Date
            }

            override fun areContentsTheSame(
                oldItem: CountryTotalAllStatusResponseItem,
                newItem: CountryTotalAllStatusResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.day_by_day_card, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = differ.currentList[position]

        holder.itemView.apply {

            date.text = UIHelper.formatDate(current.Date)
            oneDayConfirmed.text = current.Confirmed.toString()
            oneDayDeath.text = current.Deaths.toString()
            oneDayRecovered.text = current.Recovered.toString()
        }
    }
}