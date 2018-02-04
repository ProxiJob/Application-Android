package proxyjob.proxijob

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.parse.ParseException
import com.parse.SignUpCallback
import org.jetbrains.anko.alert
import proxyjob.proxijob.model.KUser
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import java.text.SimpleDateFormat


/**
 * Created by alexandre on 04/02/2018.
 */
class SubscribeClient : AppCompatActivity() {
    var firstname : EditText?= null
    var lastname : EditText?= null
    var email : EditText?= null
    var password : EditText?= null
    var birthday : EditText?= null
    var homme : CheckBox?= null
    var femme : CheckBox?= null
    var subscribe : Button?=null
    var myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe_demandeur)

        subscribe = findViewById(R.id.subscribe)
        birthday = findViewById(R.id.layoutDate)
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        email = findViewById(R.id.mail)
        password = findViewById(R.id.password)
        homme = findViewById(R.id.homme)
        femme = findViewById(R.id.femme)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        birthday!!.setOnClickListener({
                // TODO Auto-generated method stub
                DatePickerDialog(this@SubscribeClient, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        })
        subscribe!!.setOnClickListener {
            if (firstname!!.text.toString() != "" && lastname!!.text.toString() != ""
                    && email!!.text.toString() != "" && password!!.text.toString() != ""
                    && birthday!!.text.toString() != "" && checkCheckBox()) {
                val user = KUser()
                user.username = email!!.getText().toString()
                user.setPassword(password!!.getText().toString())
                user.business = false
                user.email = user.username
                user.lastname = lastname!!.text.toString()
                user.firstname = firstname!!.text.toString()
                user.birthday = Date(birthday!!.text.toString())
                user.sexe =  if (homme!!.isChecked) "Homme" else "Femme"
                user.signUpInBackground(object : SignUpCallback {
                    override fun done(e: ParseException?) {
                        if (e == null) {
                            alert( "INSCRIPTION ... OKAY") {
                                startActivity(Intent(this@SubscribeClient, Login::class.java))

                            }.show()
                        } else {
                            alert( e.message.toString()) {

                            }.show()
                        }
                    }
                })
            } else {
                alert("Merci de remplir toutes les informations demandées") {}.show()
            }
        }
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.FRENCH)

        birthday!!.setText(sdf.format(myCalendar.time))
    }

    private fun checkCheckBox() : Boolean{
        if ((homme!!.isChecked && femme!!.isChecked) || (!homme!!.isChecked && !femme!!.isChecked)) {
            return false
        }
        return true
    }
}