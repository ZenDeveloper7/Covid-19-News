package com.zen.covid_19news.adapter

import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zen.covid_19news.BuildConfig
import com.zen.covid_19news.R
import com.zen.covid_19news.models.Country
import com.zen.covid_19news.utils.Organiser
import com.zen.covid_19news.utils.UIHelper
import kotlinx.android.synthetic.main.bottom_dialog_layout.*
import kotlinx.android.synthetic.main.recovered_card.view.*

class DeathRateAdapter :
    RecyclerView.Adapter<DeathRateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(
            oldItem: Country,
            newItem: Country
        ): Boolean {
            return oldItem.Date == newItem.Date
        }

        override fun areContentsTheSame(
            oldItem: Country,
            newItem: Country
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recovered_card, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            if (UIHelper.getWordsLength(current.Country.trim())!! > 1)
                place.textSize = 18f
            else
                place.textSize = 25f

            if (current.NewDeaths != 0)
                newData.text = "+${UIHelper.withSuffix(current.NewDeaths.toLong())}"
            no.text = UIHelper.withSuffix(current.TotalDeaths.toLong())
            place.text = current.Country
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardLayout.setCardBackgroundColor(
                    context.getColor(R.color.deathCardColor)
                )
            } else {
                cardLayout.setCardBackgroundColor(
                    context.resources.getColor(R.color.deathCardColor)
                )
            }
            cardLayout.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(context)
                bottomSheetDialog.setContentView(R.layout.bottom_dialog_layout)
                bottomSheetDialog.setCanceledOnTouchOutside(true)
                bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                bottomSheetDialog.show()
                bottomSheetDialog.apply {

                    UIHelper.valueAnimator(current.TotalConfirmed, confirmed, null)
                    UIHelper.valueAnimator(current.TotalRecovered, recovered, null)
                    UIHelper.valueAnimator(current.TotalDeaths, deaths, null)


                    recoveredRank.text = "# ${(Organiser.organiseRecoveredRate(
                        differ.currentList.toList()
                    ).indexOf(current) + 1)}"

                    deathsRank.text = "# ${(Organiser.organiseDeathRate(
                        differ.currentList.toList()
                    ).indexOf(current) + 1)}"

                    confirmedRank.text = "# ${(Organiser.organiseConfirmedRate(
                        differ.currentList.toList()
                    ).indexOf(current) + 1)}"


                    country.text = Html.fromHtml("<b>${current.Country}</b> stands")

                    share.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Stats")
                        var shareMessage =
                            "${current.Country} has a total of ${current.TotalConfirmed} Active cases, " +
                                    "${current.TotalRecovered} Recovered cases and ${current.TotalDeaths} Death Cases.\n" +
                                    "As of ${UIHelper.formatDate(current.Date)}, ${current.NewConfirmed} Active cases, " +
                                    "${current.NewRecovered} Recovered cases and ${current.NewDeaths} Death cases are spiked up.\n\n " +
                                    "To get Daily statistics of COVID 19 cases all around the world. Download COVID 19 News from Playstore - "
                        shareMessage =
                            """${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}""".trimIndent()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        context.startActivity(Intent.createChooser(shareIntent, "Choose one"))
                    }

                }
            }
        }

    }
}