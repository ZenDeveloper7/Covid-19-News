package com.zen.covid_19news.adapter

import android.text.Html
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zen.covid_19news.R
import com.zen.covid_19news.models.DataSourceModel
import kotlinx.android.synthetic.main.sources_card.view.*

class SourceAdapter : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    private val differCallback = object : DiffUtil.ItemCallback<DataSourceModel>() {
        override fun areItemsTheSame(oldItem: DataSourceModel, newItem: DataSourceModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: DataSourceModel,
            newItem: DataSourceModel
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.sources_card, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            title.text = current.title
            api.text = current.api
            endPoint.text = current.endPoint
            website.text = current.webSite
        }
    }
}