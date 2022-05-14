package jp.techacademy.yuya.ishikawa.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: AppCompatActivity(), FragmentCallback {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val favImageView: ImageView = findViewById(R.id.favImageView)

        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())



        if (FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) != null) {
            favImageView.setImageResource(R.drawable.ic_star)
        } else {
            favImageView.setImageResource(R.drawable.ic_star_border)
        }

        favImageView.setOnClickListener {
            if (FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) == null) {
                val shop: Shop = Shop(intent.getStringExtra(KEY_ADDRESS).toString(), CouponUrls(intent.getStringExtra(KEY_URL).toString(), intent.getStringExtra(KEY_URL).toString()), intent.getStringExtra(KEY_ID).toString(), intent.getStringExtra(
                    KEY_IMAGE).toString(), intent.getStringExtra(KEY_NAME).toString())

                onAddFavorite(shop)

                favImageView.setImageResource(R.drawable.ic_star)
            } else {
                onDeleteFavorite(intent.getStringExtra(KEY_ID).toString())


            }
        }

    }



    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        private const val KEY_ADDRESS = "key_address"
        private const val KEY_NAME = "key_name"
        private const val KEY_IMAGE = "key_image"

        fun start(activity: Activity, shop: Shop) {

            var url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url).putExtra(KEY_ID, shop.id).putExtra(
                KEY_ADDRESS, shop.address).putExtra(KEY_NAME, shop.name).putExtra(KEY_IMAGE, shop.logoImage))
        }
    }

    //override fun onClickItem(url: String, id: String) {
    override fun onClickItem(shop: Shop) {
        TODO("Not yet implemented")
    }

    override fun onAddFavorite(shop: Shop) {
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
            address = shop.address
        })

        if (FavoriteShop.findBy(KEY_ID) != null) {
            favImageView.setImageResource(R.drawable.ic_star)
        } else {
            favImageView.setImageResource(R.drawable.ic_star_border)
        }


    }

    override fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(id)
    }

    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
                favImageView.setImageResource(R.drawable.ic_star_border)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        //(MainActivity.viewPagerAdapter.fragments[0] as ApiFragment).updateView()
        //(MainActivity.viewPagerAdapter.fragments[1] as FavoriteFragment).updateData()
    }

}