/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.smartzone.myapp.ui.products.product_details

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.smartzone.diva_wear.R
import com.smartzone.diva_wear.databinding.ActivityFullScreenImageScrollBinding
import com.smartzone.diva_wear.databinding.ItemProductSmallImageViewBinding
import com.smartzone.myapp.ui.base.BaseActivity
import com.smartzone.myapp.ui.base.BaseViewModel
import com.smartzone.myapp.utilis.AppUtils
import java.util.*

class FullScreenImageScrollActivity : BaseActivity<ActivityFullScreenImageScrollBinding>()  {

    companion object {
        val KEY_IMAGE_GALLERY_DATA = "imageGalleryData"
        val KEY_CURRENT_ITEM_POSITION = "position"
    }




    lateinit var binding: ActivityFullScreenImageScrollBinding
    private var mImageList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=getViewDataBinding()
        mImageList = intent.getStringArrayListExtra(KEY_IMAGE_GALLERY_DATA)
        startInitialization()
    }

    private fun startInitialization() {
        for (smallImageListIndex in mImageList!!.indices) {

            val itemProductSmallImageViewBinding = DataBindingUtil.bind<ItemProductSmallImageViewBinding>(layoutInflater.inflate(R.layout.item_product_small_image_view, binding.smallImageContainer, false))

            itemProductSmallImageViewBinding!!.root.tag = smallImageListIndex
            itemProductSmallImageViewBinding!!.executePendingBindings()
            AppUtils.loadGlideImage(this@FullScreenImageScrollActivity,mImageList!!.get(smallImageListIndex),R.drawable.default_image,itemProductSmallImageViewBinding!!.smallImageBtn)

            binding.smallImageContainer.addView(itemProductSmallImageViewBinding.root)
        }

        binding.fullScreenProductViewPager.adapter = TouchImageAdapter()
        binding.fullScreenProductViewPager.currentItem = intent.getIntExtra(
            KEY_CURRENT_ITEM_POSITION, 0)
    }

    fun changeProductsLargeImage(v: View) {
        binding.fullScreenProductViewPager.currentItem = v.tag as Int
    }

    private inner class TouchImageAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mImageList!!.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val touchImageView = ImageView(container.context)
            try {
                AppUtils.loadGlideImage(this@FullScreenImageScrollActivity,mImageList!!.get(position),R.drawable.default_image,touchImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            container.addView(touchImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            return touchImageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_full_screen_image_scroll
    }

    override fun getViewModel(): BaseViewModel? {
        return  null
    }
}