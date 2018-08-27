package srjhlab.com.myownbarcode.Dialog

import Common.CommonEventBusObject
import Common.ConstVariables
import Common.Utils.CommonUtils
import android.Manifest
import android.app.DialogFragment
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_scanimage.*
import org.greenrobot.eventbus.EventBus

import com.example.minim.cit_prototype.R

class ScreenCaptureDialog : DialogFragment() {
    private val TAG = this.javaClass.simpleName


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.fragment_scanimage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        var params = dialog.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as android.view.WindowManager.LayoutParams

        var layoutParams = layout_capture_image.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

            val permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
            }else {
                EventBus.getDefault().post(CommonEventBusObject(ConstVariables.EVENTBUS_TRAINING_START, CommonUtils.viewToBitmap(activity, layout_capture_image) as Object))
                dismiss()
            }
    }

}