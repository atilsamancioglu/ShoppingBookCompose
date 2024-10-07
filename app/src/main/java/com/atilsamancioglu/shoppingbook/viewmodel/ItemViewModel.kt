package com.atilsamancioglu.shoppingbook.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.atilsamancioglu.shoppingbook.model.Item
import com.atilsamancioglu.shoppingbook.roomdb.ItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        ItemDatabase::class.java, "Items"
    ).build()

    private val itemDao = db.itemDao()

    val itemList = mutableStateOf<List<Item>>(listOf())
    val selectedItem = mutableStateOf<Item>(Item("","","", ByteArray(1)))

    /*
    init {
        getItemList()
    }
     */

    fun getItemList() {
        viewModelScope.launch(Dispatchers.IO) {
           itemList.value = itemDao.getItemWithNameAndId()
        }
    }

    fun getItem(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = itemDao.getItemById(id)
            item?.let {
                selectedItem.value = it
            }
        }
    }

    fun saveItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.insert(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }

}