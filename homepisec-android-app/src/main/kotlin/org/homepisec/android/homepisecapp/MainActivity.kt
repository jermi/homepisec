package org.homepisec.android.homepisecapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.LinearLayout
import org.homepisec.android.homepisecapp.control.rest.client.ApiClient
import org.homepisec.android.homepisecapp.control.rest.client.api.ReadingsControllerApi
import org.homepisec.android.homepisecapp.control.rest.client.model.Device

class MainActivity : AppCompatActivity() {

    private var readingsLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readingsLayout = findViewById(R.id.readingsLayout)
        refreshReadingsPeriodically()
    }

    private fun refreshReadingsPeriodically() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val apiClient = ApiClient()
        apiClient.basePath = "http://192.168.100.3:8080"
        val readingsControllerApi = ReadingsControllerApi(apiClient)
        Thread(Runnable {
            while (!isDestroyed && readingsLayout != null) {
                val readingsUsingGET = readingsControllerApi.readingsUsingGET
                val layout = readingsLayout
                if (layout != null) {
                    runOnUiThread {
                        layout.removeAllViews()
                        readingsUsingGET.forEach({ reading ->
                            val deviceReadingRowView = DeviceReadingRowView(layout.context, null)
                            deviceReadingRowView.descriptionLabel = reading.device.id
                            deviceReadingRowView.valueLabel = reading.payload
                            deviceReadingRowView.switchable = Device.TypeEnum.RELAY == reading.device.type
                            layout.addView(deviceReadingRowView, layoutParams)
                        })
                    }
                }
                Thread.sleep(3000)
            }
        }).start()
    }

}
