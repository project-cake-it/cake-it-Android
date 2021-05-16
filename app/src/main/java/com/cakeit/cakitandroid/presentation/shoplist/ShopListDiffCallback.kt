package com.cakeit.cakitandroid.presentation.shoplist

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData

class ShopListDiffCallback(val oldShopList : List<CakeShopData>, val newShopList : List<CakeShopData>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // 두 아이템의 pk 값이 같으면 true, 다르면 false 반환 되도록
        Log.d("songjem", "old name [" + oldItemPosition + "] = " + oldShopList[oldItemPosition].shopName
                + "new name [" + newItemPosition + "] = " + newShopList[newItemPosition].shopName)
        return oldShopList[oldItemPosition].shopIndex == newShopList[newItemPosition].shopIndex
    }

    override fun getOldListSize(): Int = oldShopList.size

    override fun getNewListSize(): Int = newShopList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newShopList[newItemPosition] == oldShopList[oldItemPosition]
    }
}