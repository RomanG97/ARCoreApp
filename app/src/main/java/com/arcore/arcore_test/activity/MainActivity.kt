package com.arcore.arcore_test.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arcore.arcore_test.R
import com.arcore.arcore_test.tools.CameraPermissionHelper
import com.arcore.arcore_test.tools.CameraPermissionHelper.hasCameraPermission
import com.arcore.arcore_test.tools.CameraPermissionHelper.launchPermissionSettings
import com.arcore.arcore_test.tools.CameraPermissionHelper.shouldShowRequestPermissionRationale
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment


class MainActivity : AppCompatActivity() {


    // Set to true ensures requestInstall() triggers installation if necessary.
    private var mUserRequestedInstall = true
    private var mSession: Session? = null
    private lateinit var arCoreFragment: ArFragment
    private var anchorNode: AnchorNode? = null

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            val intent = Intent(this, ConfigBrushActivity::class.java)
            startActivity(intent)
        }

        // ARCore requires camera permission to operate.
        if (!hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
            return
        }

        // Make sure Google Play Services for AR is installed and up to date.
        try {
            if (mSession == null) when (ArCoreApk.getInstance()
                .requestInstall(this, mUserRequestedInstall)) {
                ArCoreApk.InstallStatus.INSTALLED -> {
                    mSession = Session(this)
                }
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    mUserRequestedInstall = false
                }
                else -> throw Exception("not INSTALLED/INSTALL_REQUESTED state")
            }
        } catch (e: Exception) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "Exception: $e", Toast.LENGTH_LONG).show();
            return
        }

        arCoreFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as ArFragment

        arCoreFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode ?: return@setOnTapArPlaneListener

            anchorNode!!.setParent(arCoreFragment.arSceneView.scene)

            val viewA = Node()
            val future =
                ViewRenderable.builder().setView(this@MainActivity, R.layout.test_layout).build()
            future.thenAccept {
                viewA.localRotation = Quaternion(Vector3(1f, 0f, 0f), -45f)
//                    viewA.localScale = Vector3(10f, 10f, 10f)
                viewA.renderable = it
                viewA.setParent(anchorNode)

                /*(it.view.findViewById<Button>(R.id.btnPressMe)).setOnClickListener { btn ->
                    var textView = it.view.findViewById<TextView>(R.id.tvTitle)
                    var ivPic = it.view.findViewById<ImageView>(R.id.ivPic)
                    when(counter) {
                        0 -> {
                            textView.text = "Hi"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.sym_contact_card))
                            counter++
                        }

                        1 -> {
                            textView.text = "Cool pressing"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_call))
                            counter++
                        }

                        2 -> {
                            textView.text = "Come on one more time"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.arrow_down_float))
                            counter++
                        }

                        3 -> {
                            textView.text = "And a couple more times"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.arrow_up_float))
                            counter++
                        }

                        4 -> {
                            textView.text = "Yeah"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.bottom_bar))
                            counter++
                        }

                        5 -> {
                            textView.text = "Again?"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.checkbox_off_background))
                            counter++
                        }

                        6 -> {
                            textView.text = "Is your finger tired?"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.checkbox_on_background))
                            counter++
                        }

                        7 -> {
                            textView.text = "Okay enough"
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.btn_star_big_on))
                            counter++
                        }

                        8 -> {
                            textView.text = "That's it, I restart..."
                            ivPic.setImageDrawable(resources.getDrawable(android.R.drawable.btn_star_big_off))
                            counter = 0
                        }
                        else -> {
                            counter = 0
                        }
                    }
                }*/
            }
            arCoreFragment.arSceneView.scene.addChild(viewA)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!hasCameraPermission(this)) {
            Toast.makeText(
                this,
                "Camera permission is needed to run this application",
                Toast.LENGTH_LONG
            ).show()
            if (!shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                launchPermissionSettings(this)
            }
            finish()
        }
    }
}