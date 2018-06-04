package gatech.edu.project6400.View.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Customer;
import gatech.edu.project6400.R;

public class RegisterActivity extends AppCompatActivity {

    private String extraIDText = "CustomerID";
    private Customer newCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Handy Man Tool Rental");

        Button submit_button = (Button) findViewById(R.id.register_submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_form();
            }
        });
    }

    private void submit_form() {
        EditText email_text = (EditText) findViewById(R.id.register_email_text);
        EditText password_text = (EditText) findViewById(R.id.register_password_text);
        EditText confirm_pass = (EditText) findViewById(R.id.register_confirm_pass);
        EditText first_name = (EditText) findViewById(R.id.register_first_name);
        EditText last_name = (EditText) findViewById(R.id.register_last_name);
        EditText home_phone = (EditText) findViewById(R.id.register_home_phone);
        EditText work_phone = (EditText) findViewById(R.id.register_work_phone);
        EditText address = (EditText) findViewById(R.id.register_address);

        //check password length
        if (password_text.getText().toString().length() >= 16 &&
                password_text.getText().toString().length() <= 4) {
            return;
        }
        //check password
        if (!password_text.getText().toString().equals(confirm_pass.getText().toString())) {
            Toast.makeText(this, "Passwords Don't Match!", Toast.LENGTH_SHORT);
            return;
        }
        //make sure all fields are completed
        if (email_text.getText().toString().isEmpty() ||
                password_text.getText().toString().isEmpty() ||
                confirm_pass.getText().toString().isEmpty() ||
                first_name.getText().toString().isEmpty() ||
                last_name.getText().toString().isEmpty() ||
                home_phone.getText().toString().isEmpty() ||
                work_phone.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT);
            return;
        }

        //save customer to db
        UserDBService userDBService = new UserDBService();
        newCustomer = new Customer(first_name.getText().toString(), last_name.getText().toString(),
                email_text.getText().toString(), password_text.getText().toString(), home_phone.getText().toString(),
                work_phone.getText().toString(), address.getText().toString());

        userDBService.registerCustomer(newCustomer, this);
    }

    public void registerSucessful() {
        Intent intent = new Intent(this, ViewProfile.class);
        intent.putExtra(extraIDText, newCustomer.ID);
        startActivity(intent);
    }

    public void registerFailure() {
        Toast.makeText(this, "Failed to register customer!", Toast.LENGTH_LONG).show();
    }
}
