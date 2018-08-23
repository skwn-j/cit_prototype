package Dialog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minim.cit_prototype.R

class TutorialDialog : DialogFragment() {
    val TAG = this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")

        return inflater.inflate(R.layout.layout_dialog_tutorial, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun initializeUi(){
        Log.d(TAG, "##### initializeUi #####")
    }
}