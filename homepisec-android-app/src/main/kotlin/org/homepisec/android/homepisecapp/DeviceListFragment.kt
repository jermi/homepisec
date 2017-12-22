package org.homepisec.android.homepisecapp

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.homepisec.android.homepisecapp.control.rest.client.ApiClient
import org.homepisec.android.homepisecapp.control.rest.client.api.ReadingsControllerApi
import org.homepisec.android.homepisecapp.control.rest.client.api.RelayControllerApi
import org.homepisec.android.homepisecapp.control.rest.client.model.Device

class DeviceListFragment : Fragment() {

    private val TAG = "DeviceListFragment"

    private var readingsLayout: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.devices_list_fragment, container, false)
        readingsLayout = view.findViewById(R.id.readingsLayout)
        refreshReadingsPeriodically()
        return view
    }

    private fun refreshReadingsPeriodically() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val apiClient = ApiClient()
        apiClient.basePath = "http://192.168.100.6:8080"
        val readingsControllerApi = ReadingsControllerApi(apiClient)
        val relayControllerApi = RelayControllerApi(apiClient)
        Thread(Runnable {
            while (activity != null && !activity.isDestroyed && readingsLayout != null) {
                try {
                    val readingsUsingGET = readingsControllerApi.readingsUsingGET
                    val layout = readingsLayout
                    if (layout != null) {
                        activity.runOnUiThread {
                            layout.removeAllViews()
                            readingsUsingGET.forEach({ reading ->
                                val deviceReadingRowView = DeviceReadingRowView(layout.context, null)
                                deviceReadingRowView.descriptionLabel = reading.device.id
                                deviceReadingRowView.valueLabel = reading.payload
                                deviceReadingRowView.switchable = Device.TypeEnum.RELAY == reading.device.type
                                deviceReadingRowView.deviceId = reading.device.id
                                deviceReadingRowView.type = reading.device.type.value
                                deviceReadingRowView.switchListener = { deviceId, value ->
                                    Thread(Runnable {
                                        relayControllerApi.switchRelayUsingPOST(deviceId, value)
                                    }).start()
                                }
                                layout.addView(deviceReadingRowView, layoutParams)
                            })
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "failed to get readings: " + e.message, e)
                }
                Thread.sleep(2000)

            }
        }).start()
    }

}