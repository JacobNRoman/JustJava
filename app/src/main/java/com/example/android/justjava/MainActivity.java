package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    String customerName = "";
    int numberOfCoffees = 0;
    int price = 5;
    Context context = MainActivity.this;
    int duration = Toast.LENGTH_SHORT;

    /**
     * This method is called when the order button is clicked.
     */
    public void increment(View view){
        if (numberOfCoffees < 100) {
            numberOfCoffees += 1;
        } else{
            Toast toast = Toast.makeText(context, getString(R.string.hundred_cups), duration);
            toast.show();
        }
        display(numberOfCoffees);
    }

    public void decrement(View view){
        if (numberOfCoffees > 0) {
            numberOfCoffees -= 1;
        } else {
            Toast toast = Toast.makeText(context, getString(R.string.zero_cups), duration);
            toast.show();
        }
        display(numberOfCoffees);
    }

    public void submitOrder(View view) {
        updateName();
        String priceMessage = buildOrderSummary();
        String subject = getString(R.string.email_subject, customerName);
        composeEmail(subject, priceMessage);
    }

    public String buildOrderSummary(){
        String priceMessage = getString(R.string.order_summary_name, customerName);
        priceMessage += "\n" + getString(R.string.add_whipped) + " " + hasWhippedCream();
        priceMessage += "\n" + getString(R.string.add_chocolate) + " " + hasChocolate();
        priceMessage += "\n" + getString(R.string.quantity_summary) + " " + numberOfCoffees;
        priceMessage += "\n" + getString(R.string.total_due) + (calculatePrice(numberOfCoffees, price));
        priceMessage += "\n" + getString(R.string.thanks);
        return priceMessage;
    }

    public void composeEmail(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public int calculatePrice(int coffees, int price){
        if (hasChocolate().equals(getString(R.string.yes))){
            price += 2;
        }
        if (hasWhippedCream().equals(getString(R.string.no))){
            price += 1;
        }
        return coffees * price;
    }

    private String hasWhippedCream(){
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        if (whippedCream.isChecked()){
            return getString(R.string.yes);
        }else{
            return getString(R.string.no);
        }
    }

    private String hasChocolate(){
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        if (chocolate.isChecked()){
            return getString(R.string.yes);
        }else{
            return getString(R.string.no);
        }
    }

    private void updateName(){
        EditText nameInput = (EditText) findViewById(R.id.name_input);
        customerName = nameInput.getText().toString();
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}