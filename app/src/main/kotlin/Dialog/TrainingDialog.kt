package Dialog

import Common.CommonEventBusObject
import Common.ConstVariables
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minim.cit_prototype.R
import kotlinx.android.synthetic.main.layout_training.*
import org.greenrobot.eventbus.EventBus

class TrainingDialog : DialogFragment(), View.OnClickListener {
    val TAG = this.javaClass.simpleName

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")

        return inflater.inflate(R.layout.layout_training, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            imageview_training_1.id -> {
                EventBus.getDefault().post(CommonEventBusObject(ConstVariables.EVENTBUS_TRAINING_START, null))
                dismiss()
            }
            btn_training_close.id -> dismiss()
        }
    }

    fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        imageview_training_1.setOnClickListener(this)
        btn_training_close.setOnClickListener(this)

    }
}