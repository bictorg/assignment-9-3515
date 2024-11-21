package edu.temple.browsr

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TabTitleAdapter(
    private val titles: MutableList<String> = mutableListOf(),
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<TabTitleAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(32, 24, 32, 24)
            textSize = 16f
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = titles[position]
        holder.textView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount() = titles.size

    fun updateTitles(newTitles: List<String>) {
        titles.clear()
        titles.addAll(newTitles)
        notifyDataSetChanged()
    }
} 