package com.zen.covid_19news.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zen.covid_19news.R
import kotlinx.android.synthetic.main.contact_fragment.*

class ContactFragment : Fragment(R.layout.contact_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        insta.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/zenpeaceout/")
                )
            )
        }

        linkedin.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/suhas-ch-189561170/")
                )
            )
        }

        gmail.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("zendeveloper27@gmail.com"))
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Choose an Email client :"))

        }
    }
}