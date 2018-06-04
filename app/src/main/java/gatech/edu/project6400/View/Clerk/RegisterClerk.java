package gatech.edu.project6400.View.Clerk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gatech.edu.project6400.Controller.Services.UserDBService;
import gatech.edu.project6400.Model.Clerk;
import gatech.edu.project6400.R;

public class RegisterClerk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_clerk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submit = (Button) findViewById(R.id.regclerk_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClerk();
            }
        });

    }

    private void registerClerk(){
        TextView first = (TextView) findViewById(R.id.regclerk_first);
        TextView last = (TextView) findViewById(R.id.regclerk_last);
        TextView password = (TextView) findViewById(R.id.regclerk_pass);
        TextView confirm = (TextView) findViewById(R.id.regclerk_confirm);

        if (!first.getText().toString().isEmpty() &&
                !last.getText().toString().isEmpty() &&
                password.getText().toString().length() < 16 &&
                password.getText().toString().length() > 4 &&
                !password.getText().toString().isEmpty() &&
                confirm.getText().toString().equals(password.getText().toString())) {
            UserDBService userDBService = new UserDBService();
            Clerk clerk = new Clerk(first.getText().toString(), last.getText().toString(), password.getText().toString());
            if (clerk != null) {
                Intent intent = new Intent(this, PickUpReservation.class);
                intent.putExtra("ClerkID", clerk.ID);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Clerk already exists!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
        }
    }

}
