package org.homepisec.android.homepisecapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


class DrawerItem(val id: Int, val icon: Int, val text: String) {
    companion object {
        val READINGS: Int = 0
        val SETTINGS: Int = 1
    }
}

class DrawerItemAdapter(context: Context, objects: List<DrawerItem>) : ArrayAdapter<DrawerItem>(context, R.layout.drawer_list_item, objects) {

    class ViewHolder {
        var icon: ImageView? = null
        var textView: TextView? = null
    }

    val data: List<DrawerItem> = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = data[position]
        val viewHolder: ViewHolder
        val view: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.drawer_list_item, parent, false)
            view.tag = viewHolder
            viewHolder.icon = view.findViewById(R.id.drawerListItemIcon)
            viewHolder.textView = view.findViewById(R.id.drawerListItemText)
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }
        viewHolder.textView!!.text = item.text
        viewHolder.icon!!.setImageResource(item.icon)
        view.setOnClickListener({ _ ->
            Toast.makeText(context, "menu " + item.id, Toast.LENGTH_SHORT).show()
        })
        return view
    }

}