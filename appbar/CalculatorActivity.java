package com.example.appbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

public class CalculatorActivity extends AppCompatActivity {

    private EditText goldWeightEditText;
    private EditText goldValueEditText;
    private TextView totalValueTextView;
    private TextView zakatPayableTextView;
    private TextView totalZakatTextView;
    private Spinner goldTypeSpinner;

    Toolbar calculatorToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        calculatorToolbar = findViewById(R.id.calculator_toolbar);

        setSupportActionBar(calculatorToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Calculator");

        goldWeightEditText = findViewById(R.id.gold_weight_edit_text);
        goldValueEditText = findViewById(R.id.gold_value_edit_text);
        totalValueTextView = findViewById(R.id.total_value_text_view);
        zakatPayableTextView = findViewById(R.id.zakat_payable_text_view);
        totalZakatTextView = findViewById(R.id.total_zakat_text_view);
        goldTypeSpinner = findViewById(R.id.gold_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gold_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goldTypeSpinner.setAdapter(adapter);

        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all the EditTexts and TextViews
                goldWeightEditText.getText().clear();
                goldValueEditText.getText().clear();
                totalValueTextView.setText("0");
                zakatPayableTextView.setText("0");
                totalZakatTextView.setText("0");
                goldTypeSpinner.setSelection(0); // Reset Spinner to the first item
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    private void calculateZakat() {
        String goldWeightStr = goldWeightEditText.getText().toString().trim();
        String goldType = goldTypeSpinner.getSelectedItem().toString().toLowerCase();
        String goldValueStr = goldValueEditText.getText().toString().trim();

        if (goldWeightStr.isEmpty() || goldValueStr.isEmpty()) {
            // Show an error message if any of the fields are empty
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal goldWeight = new BigDecimal(goldWeightStr);
        BigDecimal goldValue = new BigDecimal(goldValueStr);

        // Validate weight for "keep" and "wear"
        if (goldType.equals("keep") && goldWeight.compareTo(new BigDecimal(85)) < 0) {
            // Show an error message for "keep" less than 85 grams
            Toast.makeText(this, "Minimum weight for 'Keep' type is 85 grams", Toast.LENGTH_SHORT).show();
            return;
        } else if (goldType.equals("wear") && goldWeight.compareTo(new BigDecimal(200)) < 0) {
            // Show an error message for "wear" less than 200 grams
            Toast.makeText(this, "Minimum weight for 'Wear' type is 200 grams", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal totalValue = goldWeight.multiply(goldValue);

        BigDecimal zakatPayable = BigDecimal.ZERO;
        if (goldType.equals("keep")) {
            zakatPayable = goldWeight.subtract(new BigDecimal(85)).multiply(goldValue);
        } else if (goldType.equals("wear")) {
            zakatPayable = goldWeight.subtract(new BigDecimal(200)).multiply(goldValue);
        }

        BigDecimal totalZakat = zakatPayable.multiply(new BigDecimal(0.025)).setScale(2, BigDecimal.ROUND_HALF_UP);

        totalValueTextView.setText("RM"+totalValue.toString());
        zakatPayableTextView.setText("RM"+zakatPayable.toString());
        totalZakatTextView.setText("RM"+totalZakat.toString());
    }



}
