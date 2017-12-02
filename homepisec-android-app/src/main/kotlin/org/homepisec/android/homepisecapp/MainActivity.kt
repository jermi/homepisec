package org.homepisec.android.homepisecapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.homepisec.android.homepisecapp.control.rest.client.ApiClient
import org.homepisec.android.homepisecapp.control.rest.client.ApiException
import org.homepisec.android.homepisecapp.control.rest.client.api.ReadingsControllerApi
import org.homepisec.android.homepisecapp.control.rest.client.model.DeviceEvent

class MainActivity : AppCompatActivity() {

    private var list: ListView? = null
    private var adapter: DeviceListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list: ListView = findViewById(R.id.readingsListView)
        val adapter = DeviceListAdapter(this)
        list.adapter = (adapter)

        this.list = list
        this.adapter = adapter
        Thread(Runnable { getReadings() }).start()
    }

    private fun getReadings() {
        try {
            val apiClient = ApiClient()
            apiClient.basePath = "http://192.168.100.3:8080"
            val readingsControllerApi = ReadingsControllerApi(apiClient)
            val readingsUsingGET = readingsControllerApi.readingsUsingGET
            Log.i("MAIN", readingsUsingGET.toString())
            val listView = list
            runOnUiThread {
                adapter?.sList = readingsUsingGET
                adapter?.notifyDataSetChanged()
                setListViewHeightBasedOnChildren(listView!!)
            }
//            readingsControllerApi.subscribeUpdatesUsingGET()
        } catch (e: ApiException) {
            Log.e("MAIN", e.message, e)
        }

    }

    fun onButtonClick(view: View) {
        val toast: Toast = Toast.makeText(this, "test", Toast.LENGTH_LONG)
        toast.show()
        //        startActivity(new Intent(this, SettingsActivity.class));
    }

    private class DeviceListAdapter(context: Context) : BaseAdapter() {
        public var sList: List<DeviceEvent> = emptyList()

        private val mInflator: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return sList.size
        }

        override fun getItem(position: Int): Any {
            return sList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.mInflator.inflate(R.layout.list_devices_row, parent, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }

            vh.valueLabel.text = sList[position].payload
            vh.descriptionLabel.text = sList[position].device.id
            return view
        }
    }

    private class ListRowHolder(row: View?) {
        public val valueLabel: TextView
        public val descriptionLabel: TextView

        init {
            this.valueLabel = row?.findViewById<TextView>(R.id.deviceReadingValue) as TextView
            this.descriptionLabel = row?.findViewById<TextView>(R.id.deviceReadingDescription) as TextView
        }
    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.width, MeasureSpec.AT_MOST)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0) {
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

}
