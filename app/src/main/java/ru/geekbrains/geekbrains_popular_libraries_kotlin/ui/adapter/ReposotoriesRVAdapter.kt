package ru.geekbrains.geekbrains_popular_libraries_kotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.geekbrains_popular_libraries_kotlin.databinding.ItemRepositoryBinding
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.presenter.list.IRepositoryListPresenter
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.view.list.RepositoryItemView

class ReposotoriesRVAdapter(val presenter: IRepositoryListPresenter) : RecyclerView.Adapter<ReposotoriesRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
        }

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = presenter.bindView(holder.apply { pos = position })

    inner class ViewHolder(val vb: ItemRepositoryBinding) : RecyclerView.ViewHolder(vb.root), RepositoryItemView {
        override var pos = -1
        override fun setName(text: String) = with(vb) {
            tvName.text = text
        }
    }
}