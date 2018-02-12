package example.vishalpachori.texttospeechkotlin

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
    private var txtSpeechInput:EditText? = null
	private var btn_Speak: ImageButton?=null
	private var REQ_CODE: Int = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val tab1 =  findViewById<View>(R.id.tab1) as LinearLayout
        val tab2 =  findViewById<View>(R.id.tab2) as LinearLayout


        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.text_to_speech))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.speech_to_text))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                 }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when ( tab!!.position){
                    0 -> {
                        tab1.visibility = View.VISIBLE
                        tab2.visibility = View.GONE
                    }
                    1 -> {

                        tab1.visibility = View.GONE
                        tab2.visibility = View.VISIBLE
                    }
                }
              }

        })


        txtSpeechInput =  this.tv_speech
		btn_Speak =  this.btn_speak

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }

        btn_Speak!!.setOnClickListener{
            promptSpeechInput()
        }
    }

     private fun promptSpeechInput() {
		val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))

		try {
			startActivityForResult(intent, REQ_CODE)

		} catch ( a : Exception) {
			Toast.makeText(applicationContext, getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show()
		}
	}

    /**
     * Receiving speech input
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK) {


                    val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    txtSpeechInput!!.text.clear()
                    txtSpeechInput!!.text.append(result[0])
                }

            }
        }
    }


    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

