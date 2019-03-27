package com.anou.trowitter.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import androidx.recyclerview.widget.DiffUtil
import com.anou.trowitter.utils.DiffCallback

import java.util.ArrayList

abstract class BaseRecyclerViewAdapter<T : Comparable<T>, U : RecyclerView.ViewHolder> : RecyclerView.Adapter<U>() {

    private var data: MutableList<T> = ArrayList()
    private val selectedItems = SparseBooleanArray()

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    val selectedItemCount: Int
        get() {
            return selectedItems.size()
        }

    /**
     * Indicates the itemList of selected items
     *
     * @return List of selected items ids
     */
    val selectedItemsPositions: List<Int>
        get() {
            val items = ArrayList<Int>(selectedItems.size())
            for (i in 0 until selectedItems.size()) {
                items.add(selectedItems.keyAt(i))
            }
            return items
        }

    /**
     * Indicates the itemList of selected items
     *
     * @return List of selected items ids
     */
    val selectedItemPosition: Int?
        get() {
            val items = ArrayList<Int>(selectedItems.size())
            for (i in 0 until selectedItems.size()) {
                items.add(selectedItems.keyAt(i))
            }

            return if (items.size > 0) {
                items[0]
            } else -1

        }

    open fun setData(data: MutableList<T>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.data, data))
        diffResult.dispatchUpdatesTo(this)
        this.data.clear()
        this.data = data
        notifyDataSetChanged()
    }

    fun add(element: T) {
        data.add(element)
        notifyDataSetChanged()
    }

    fun addAll(elements: List<T>) {
        data.addAll(elements)
        notifyDataSetChanged()
    }

    fun remove(element: T) {
        data.remove(element)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    operator fun contains(element: T): Boolean {
        return data.contains(element)
    }

    fun indexOf(element: T): Int {
        return data.indexOf(element)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    fun getData(): List<T> {
        return data
    }

    /**
     * Indicates if the item at position position is selected
     *
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    fun isSelected(position: Int): Boolean {
        return selectedItemsPositions.contains(position)
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param position Position of the item to toggle the selection status for
     */
    fun toggleSelection(position: Int) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    /**
     * Set a selection status of an item at a given position
     *
     * @param position Position of an item to set a selection status for
     */
    fun setSelection(position: Int) {
        clearSelection()
        toggleSelection(position)
        notifyItemChanged(position)
    }

    /**
     * Select all items
     */
    fun selectAll() {
        for (i in data.indices) {
            selectedItems.put(i, true)
            notifyItemChanged(i)
        }
    }

    /**
     * Clear the selection status for all items
     */
    fun clearSelection() {
        val selection = selectedItemsPositions
        selectedItems.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    fun getSelectedItems(): List<T> {
        val selection = ArrayList<T>()
        for (i in data.indices) {
            if (isSelected(i)) {
                selection.add(data[i])
            }
        }
        return selection
    }

}
