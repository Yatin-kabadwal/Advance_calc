package com.example.advancecalc;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView displayTextView;
    private StringBuilder inputStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        displayTextView = findViewById(R.id.tv1);
        inputStringBuilder = new StringBuilder();

        // Set OnClickListener for each button
        setButtonClickListeners();
    }

    private void setButtonClickListeners() {
        // Get all buttons
        Button[] buttons = new Button[]{
                findViewById(R.id.b1), findViewById(R.id.b2), findViewById(R.id.b3),
                findViewById(R.id.b4), findViewById(R.id.b5), findViewById(R.id.b6),
                findViewById(R.id.b7), findViewById(R.id.b8), findViewById(R.id.b9),
                findViewById(R.id.b10), findViewById(R.id.b11), findViewById(R.id.b12),
                findViewById(R.id.b13), findViewById(R.id.b14), findViewById(R.id.b15),
                findViewById(R.id.b16), findViewById(R.id.b17), findViewById(R.id.b18),
                findViewById(R.id.b19)
        };

        // Set OnClickListener for each button
        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle button click
                    handleButtonClick(view);
                }
            });
        }
    }

    private void handleButtonClick(View view) {
        Button clickedButton = (Button) view;
        String buttonText = clickedButton.getText().toString();

        // Handle different button clicks
        switch (buttonText) {
            case "=":
                // Perform calculation and display result
                calculateResult();
                break;
            case "C":
                // Clear input
                clearInput();
                break;
            case "Del":
                // Delete last character
                deleteLastCharacter();
                break;
            default:
                // Append clicked button text to input
                appendToInput(buttonText);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateResult() {
        try {
            // Evaluate the expression
            evaluateExpression(inputStringBuilder.toString());
        } catch (Exception e) {
            // Handle invalid expression
            displayTextView.setText("Error");
            inputStringBuilder.setLength(0);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void evaluateExpression(String expression) {
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Use evaluateJavascript to get the result asynchronously
                webView.evaluateJavascript(expression, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        // Display the result
                        displayTextView.setText(value);

                        // Clear input for the next calculation
                        inputStringBuilder.setLength(0);
                    }
                });
            }
        });

        // Load a blank page to trigger onPageFinished
        webView.loadData("", "text/html", null);
    }

    private void clearInput() {
        // Clear input and display
        inputStringBuilder.setLength(0);
        displayTextView.setText("");
    }

    private void deleteLastCharacter() {
        // Delete the last character from input if it's not empty
        int length = inputStringBuilder.length();
        if (length > 0) {
            inputStringBuilder.deleteCharAt(length - 1);
            displayTextView.setText(inputStringBuilder.toString());
        }
    }

    private void appendToInput(String text) {
        // Append the clicked button text to input
        inputStringBuilder.append(text);
        displayTextView.setText(inputStringBuilder.toString());
    }
}
