package ru.spb.yakovlev.stocksmonitor.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Generic adapter class for RecyclerView widget designed to be used with ViewBinding
 *
 * @param viewHolderInflater
 * @param viewHolderBinder
 *
 */
class BaseRVAdapter<T : ViewBinding, R : RvItemData>(
    private val viewHolderInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    private val viewHolderBinder: (T, R, position: Int) -> Unit,
) : RecyclerView.Adapter<BaseRVAdapter.BaseVH<T, R>>() {

    private val itemsList = mutableListOf<R>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<T, R> {
        val binding = viewHolderInflater(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseVH(binding, viewHolderBinder)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: BaseVH<T, R>, position: Int) {
        holder.bind(itemsList[position], holder.adapterPosition)
    }

    override fun onViewAttachedToWindow(holder: BaseVH<T, R>) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun updateData(newItems: List<R>) {
        val diffCallback =
            BaseDiffCallback(itemsList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemsList.clear()
        itemsList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class BaseDiffCallback(
        private val oldList: List<R>,
        private val newList: List<R>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    class BaseVH<T : ViewBinding, R : RvItemData>(
        private val binding: T,
        private val bindVH: (T, R, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: R, position: Int) {
            bindVH(binding, itemData, position)
        }
    }
}