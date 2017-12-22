package org.homepisec.android.homepisecapp

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import org.homepisec.android.homepisecapp.control.rest.client.model.Device

/**
 *
 */
class DeviceReadingRowView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    var deviceId: String = ""
    var switchListener: ((deviceId: String, value: Boolean) -> Unit) = { _, _ -> }
    var descriptionLabel: String = ""
    var valueLabel: String = ""
    var switchable: Boolean = false
    var type: String = ""

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        updateView()
    }

    private fun updateView() {
        val valueTextView = findViewById<TextView>(R.id.deviceReadingValue)
        valueTextView?.text = valueLabel
        val descriptionTextView = findViewById<TextView>(R.id.deviceReadingDescription)
        descriptionTextView?.text = descriptionLabel
        val toggleButton = findViewById<Button>(R.id.toggleButton)
        toggleButton.visibility = if (switchable) View.VISIBLE else View.INVISIBLE
        if (switchable) {
            toggleButton.setOnClickListener({ switchListener.invoke(deviceId, !valueLabel.toBoolean()) })
        }
        val iconImgView = findViewById<ImageView>(R.id.deviceReadingIcon)
        val iconRes: Int = when (type) {
            Device.TypeEnum.RELAY.value -> R.drawable.ic_power_black_24dp
            Device.TypeEnum.SENSOR_MOTION.value -> R.drawable.ic_visibility_black_24dp
            Device.TypeEnum.SENSOR_TEMP.value -> R.drawable.ic_tune_black_24dp
            else -> R.drawable.ic_info_black_24dp
        }
        iconImgView.setImageResource(iconRes)
    }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DeviceReadingRow, 0, 0)
            valueLabel = a.getString(R.styleable.DeviceReadingRow_valueLabel)
            descriptionLabel = a.getString(R.styleable.DeviceReadingRow_descriptionLabel)
            type = if (a.hasValue(R.styleable.DeviceReadingRow_type)) a.getString(R.styleable.DeviceReadingRow_type) else ""
            switchable = this.type == "RELAY"
            a.recycle()
        } else {
            valueLabel = "";
            descriptionLabel = "";
        }
        LayoutInflater.from(context).inflate(R.layout.device_reading_row, this)
        updateView()
    }


}
