package com.fijiacd.moneytreev1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView budget, supermarketText, payDay, confirmResetMessage, monthTextView, confirmDeleteMessage;
    ConstraintLayout mainLayout, purchaseTitleScreen, purchaseDetailsScreen, setupLayout, recapScreen, dueScreen;
    ImageButton acceptButton;
    TableLayout budgetTable, purchasesTable, dueTable, paidTable;
    Button cancelReset, confirmReset, editButton, okDeletePurchase, cancelDeletePurchase;
    EditText salaryText, otherInText, housingText, councilTaxText, utilitiesText;
    EditText loansText, carFinanceText, phoneBillsText, tvInternetText, miscItemsText;
    EditText salaryDateText, otherInDateText, housingDateText, councilTaxDateText, utilitiesDateText;
    EditText loansDateText, carFinanceDateText, phoneBillsDateText, tvInternetDateText, miscItemsDateText;
    EditText purchaseCostEditText, purchaseNotes;
    DatePicker datePicker;
    ProgressBar budgetProgress;
    SharedPreferences budgetRecords;
    Calendar calendar;
    InputMethodManager inputMgr;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    NumberFormat numFormat = NumberFormat.getCurrencyInstance(Locale.UK);
    boolean startNewMonth;
    int daysLeft;
    int currentDay;
    float budgetResult, budgetUpdate;
    String[] budgetKeys = {"Salary", "Other In", "Housing",
            "Council Tax", "Utilities", "Loans/CC",
            "Car Finance", "Phone Bill",
            "TV/Internet", "Additional"};
    String[] monthName = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
    String[] ordinals = {"st", "nd", "rd", "th"};
    float[] budgetValues = new float[10];
    int[] budgetDates = new int[10];
    ArrayList<String> purchasesKeys = new ArrayList<String>();
    ArrayList<Float> purchasesValues = new ArrayList<Float>();
    ArrayList<String> purchasesDates = new ArrayList<String>();
    float currentResult;
    int purchaseTag;
    TableRow tablePurchaseRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budget = (TextView) findViewById(R.id.budget);
        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        setupLayout = (ConstraintLayout) findViewById(R.id.setupLayout);
        acceptButton = (ImageButton) findViewById(R.id.acceptButton);
        payDay = (TextView) findViewById(R.id.until);
        confirmResetMessage = (TextView) findViewById(R.id.confirmMessage);
        confirmReset = (Button) findViewById(R.id.okResetButton);
        cancelReset = (Button) findViewById(R.id.cancelResetButton);
        editButton = (Button) findViewById(R.id.editButton);
        salaryText = (EditText) findViewById(R.id.salaryText);          //SALARY(0)
        otherInText = (EditText) findViewById(R.id.otherInText);        //OTHERIN(1)
        housingText = (EditText) findViewById(R.id.housingText);        //HOUSING(2)
        councilTaxText = (EditText) findViewById(R.id.councilTaxText);  //COUNCILTAX(3)
        utilitiesText = (EditText) findViewById(R.id.utilitiesText);    //UTILITIES(4)
        loansText = (EditText) findViewById(R.id.loansText);            //LOANS(5)
        carFinanceText = (EditText) findViewById(R.id.carFinanceText);  //CARFINANCE(6)
        phoneBillsText = (EditText) findViewById(R.id.phoneBillsText);  //PHONEBILLS(7)
        tvInternetText = (EditText) findViewById(R.id.tvInternetText);  //TVINTERNET(8)
        miscItemsText = (EditText) findViewById(R.id.miscItemText);     //MISCITEMS(9)
        salaryDateText = (EditText) findViewById(R.id.salaryDate);
        otherInDateText = (EditText) findViewById(R.id.otherInDate);
        housingDateText = (EditText) findViewById(R.id.housingDate);
        councilTaxDateText = (EditText) findViewById(R.id.councilTaxDate);
        utilitiesDateText = (EditText) findViewById(R.id.utilitiesDate);
        loansDateText = (EditText) findViewById(R.id.loansDate);
        carFinanceDateText = (EditText) findViewById(R.id.carFinanceDate);
        phoneBillsDateText = (EditText) findViewById(R.id.phoneBillsDate);
        tvInternetDateText = (EditText) findViewById(R.id.tvInternetDate);
        miscItemsDateText = (EditText) findViewById(R.id.miscItemDate);
        budgetTable = (TableLayout) findViewById(R.id.budgetTable);
        supermarketText = (TextView) findViewById(R.id.supermarketEditText);
        purchaseTitleScreen = (ConstraintLayout) findViewById(R.id.purchaseTitleScreen);
        purchaseDetailsScreen = (ConstraintLayout) findViewById(R.id.purchaseDetailsScreen);
        purchaseCostEditText = (EditText) findViewById(R.id.purchaseCostEditText);
        confirmDeleteMessage = (TextView) findViewById(R.id.confirmDeleteMessage);
        cancelDeletePurchase = (Button) findViewById(R.id.okDeleteButton);
        okDeletePurchase = (Button) findViewById(R.id.cancelDeleteButton);
        purchaseNotes = (EditText) findViewById(R.id.purchaseNotesTextEdit);
        datePicker = (DatePicker) findViewById(R.id.purchaseDatePicker);
        budgetProgress = (ProgressBar) findViewById(R.id.budgetProgress);
        purchasesTable = (TableLayout) findViewById(R.id.purchasesTable);
        recapScreen = (ConstraintLayout) findViewById(R.id.recapScreen);
        dueScreen = (ConstraintLayout) findViewById(R.id.dueScreen);
        monthTextView = (TextView) findViewById(R.id.monthTextView);
        dueTable = (TableLayout) findViewById(R.id.dueTable);
        paidTable = (TableLayout) findViewById(R.id.paidTable);

        datePicker.setMaxDate(System.currentTimeMillis());
        purchaseTitleScreen.setVisibility(View.INVISIBLE);
        purchaseDetailsScreen.setVisibility(View.INVISIBLE);
        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        budgetRecords = this.getSharedPreferences("com.fijiacd.moneytreev1", Context.MODE_PRIVATE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setCurrency(Currency.getInstance(Locale.getDefault()));
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        daysLeft = daysUntilPay();
        String mainDaysLeftText = daysLeft + " days left";
        payDay.setText(mainDaysLeftText);
        if(currentDay == 1 && startNewMonth)
        {
            budgetUpdate = budgetResult;
            budgetRecords.edit().putFloat("BudgetResult", budgetResult).apply();
            budgetRecords.edit().putFloat("BudgetUpdate", budgetUpdate).apply();
            ////
            ////
            //Save past purchases into file!!//
            ////
            ////
            purchasesKeys.clear();
            purchasesValues.clear();
            purchasesDates.clear();
            startNewMonth = false;
        }
        if(currentDay != 1)
            startNewMonth = true;

        if(budgetRecords.getFloat("BudgetUpdate", 0) != 0)
        {
            changeTextEdit("disable");
        }

        populateBudgetSetup();

        budgetResult = budgetRecords.getFloat("BudgetResult", 0);
        budgetUpdate = budgetRecords.getFloat("BudgetUpdate", 0);
        currentResult = budgetUpdate;
        budget.setText(numFormat.format(budgetUpdate));

        try {
            //noinspection unchecked
            purchasesKeys = (ArrayList<String>) ObjectSerializer
                    .deserialize(budgetRecords.getString("PurchasesKeys", ObjectSerializer.serialize(new ArrayList<String>())));
            //noinspection unchecked
            purchasesValues = (ArrayList<Float>) ObjectSerializer
                    .deserialize(budgetRecords.getString("PurchasesValues", ObjectSerializer.serialize(new ArrayList<Float>())));
            //noinspection unchecked
            purchasesDates = (ArrayList<String>) ObjectSerializer
                    .deserialize(budgetRecords.getString("PurchasesDates", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setProgressBar();


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setProgressBar(){
        int currentProgress = (int) ((currentResult * 100) / budgetResult);
        int barProgress = (int) ((budgetUpdate * 100) / budgetResult);

//        if(barProgress < 30)
//            //
//        if(barProgress < 10)
//            budgetProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));
//        else budgetProgress.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        budgetProgress.setProgress(barProgress);
        ObjectAnimator animation = ObjectAnimator.ofInt(budgetProgress, "progress", currentProgress, barProgress);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public int daysUntilPay() {
        int payDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return payDate - currentDay;
    }

    public void populateBudgetSetup(){
        for (int i = 0; i < budgetTable.getChildCount(); i++) {
            TableRow row = (TableRow) budgetTable.getChildAt(i);
            EditText valueEditTxt = (EditText) row.getChildAt(1);
            EditText dateEditTxt = (EditText) row.getChildAt(3);
            if(budgetRecords.getFloat("Value" + i, 0) != 0.0) {
                valueEditTxt.setText(numFormat.format(budgetRecords.getFloat("Value" + i, 0)));
                dateEditTxt.setText(makeOrdinal(budgetRecords.getInt("Date" + i, 0)));
            }
        }
    }

    public void setupMenu(View view) {
        mainLayout.setVisibility(View.INVISIBLE);
        setupLayout.setVisibility(View.VISIBLE);
        confirmResetMessage.setVisibility(View.INVISIBLE);
        cancelReset.setVisibility(View.INVISIBLE);
        confirmReset.setVisibility(View.INVISIBLE);
    }

    public void backToMain(View view) {
        mainLayout.setVisibility(View.VISIBLE);
        setupLayout.setVisibility(View.INVISIBLE);
        purchaseDetailsScreen.setVisibility(View.INVISIBLE);
        recapScreen.setVisibility(View.INVISIBLE);
        dueScreen.setVisibility(View.INVISIBLE);
        assert inputMgr != null;
        inputMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void acceptChanges(View view) {
        for (int i = 0; i < budgetTable.getChildCount(); i++) {
            TableRow row = (TableRow) budgetTable.getChildAt(i);
            EditText valueEditTxt = (EditText) row.getChildAt(1);
            EditText dateEditTxt = (EditText) row.getChildAt(3);
            String budgetValueString = valueEditTxt.getText()
                    .toString().replaceAll("^0*£|,|\\.00*.*", "");
            if (!budgetValueString.isEmpty() && Float.parseFloat(budgetValueString) != 0.0f) {
                budgetValues[i] = Float.parseFloat(decimalFormat.format(Float.parseFloat(budgetValueString)));
            }
            else {
                valueEditTxt.setText("");
                budgetValues[i] = 0.0f;
            }
            int budgetDateInt = 0;
            String budgetDateString = dateEditTxt.getText().toString().replaceAll("^0*|\\D","");
            if(!budgetDateString.isEmpty())
                budgetDateInt = Integer.parseInt(budgetDateString);
            if (budgetDateInt != 0 && budgetDateInt < 30)
                    budgetDates[i] = budgetDateInt;
            else {
                dateEditTxt.setText("");
                budgetDates[i] = 30;
            }
        }
        calculateBudget();
        if(budgetResult != 0)
        {
            changeTextEdit("disable");
        }
        addBudgetResultsToMemory();
        addBudgetSetupToMemory();
        setProgressBar();
        backToMain(view);
        populateBudgetSetup();
    }

    public void calculateBudget() {
        currentResult = budgetUpdate;
        if (budgetValues != null) {
            budgetResult = 0;
            for (int i = 0; i < budgetValues.length; i++) {
                if (i == 0 || i == 1)
                    budgetResult += budgetValues[i];
                else budgetResult -= budgetValues[i];
            }
        }
        budgetUpdate = budgetResult;
            for (int i = 0; i < purchasesValues.size(); i++)
                    budgetUpdate -= purchasesValues.get(i);

            budget.setText(numFormat.format(budgetUpdate));
    }

    public void confirmResetMessage(View view) {
        confirmResetMessage.setVisibility(View.VISIBLE);
        cancelReset.setVisibility(View.VISIBLE);
        confirmReset.setVisibility(View.VISIBLE);
        changeTextEdit("disable");
    }

    public void resetBudget(View view) {
        confirmResetMessage.setVisibility(View.INVISIBLE);
        cancelReset.setVisibility(View.INVISIBLE);
        confirmReset.setVisibility(View.INVISIBLE);
        changeTextEdit("clear");
        changeTextEdit("enable");
        setProgressBar();
    }

    public void editTextEdit(View view) {
        changeTextEdit("enable");
    }

    public void changeTextEdit(String command){
        for (int i = 0; i < budgetTable.getChildCount(); i++)
        {
            TableRow row = (TableRow) budgetTable.getChildAt(i);
            for(int idx = 1; idx < row.getChildCount(); idx += 2)
            {
                EditText editTxt = (EditText) row.getChildAt(idx);
                editTxt.setAlpha(1.0f);
                if(command.equals("clear"))
                    editTxt.setText("");
                if(command.equals("enable"))
                    editTxt.setEnabled(true);
                if(command.equals("disable")) {
                    editTxt.setEnabled(false);
                    editTxt.setAlpha(0.65f);
                }
            }
        }
    }

    public void addBudgetSetupToMemory(){
        for (int i = 0; i < budgetValues.length; i++)
        {
            budgetRecords.edit().putFloat("Value" + i, budgetValues[i]).apply();
            budgetRecords.edit().putInt("Date" + i, budgetDates[i]).apply();
        }
    }

    public void addBudgetResultsToMemory(){
        budgetRecords.edit().putFloat("BudgetResult", budgetResult).apply();
        budgetRecords.edit().putFloat("BudgetUpdate", budgetUpdate).apply();
    }

    public void addPurchasesToMemory() throws IOException {
            budgetRecords.edit().putString("PurchasesKeys", ObjectSerializer.serialize(purchasesKeys)).apply();
            budgetRecords.edit().putString("PurchasesValues", ObjectSerializer.serialize(purchasesValues)).apply();
            budgetRecords.edit().putString("PurchasesDates", ObjectSerializer.serialize(purchasesDates)).apply();
    }

    public void newBuyScreen(View view){
        mainLayout.setVisibility(View.INVISIBLE);
        purchaseTitleScreen.setVisibility(View.VISIBLE);
    }

    public void createPurchase(View view){
        purchaseTitleScreen.setVisibility(View.INVISIBLE);
        purchaseDetailsScreen.setVisibility(View.VISIBLE);
        supermarketText.setText(view.getTag().toString());
    }

    public void enableDisableButton(String command){
        for (int i = 0; i < purchasesTable.getChildCount(); i++)
        {
            TableRow row = (TableRow) purchasesTable.getChildAt(i);
            View deleteButton = row.getChildAt(3);
            if(command.equals("Disable") && null!=deleteButton)
                deleteButton.setEnabled(false);
            if(command.equals("Enable") && null!=deleteButton)
                deleteButton.setEnabled(true);
        }
    }
    public void confirmDeleteMessage(View view, int tag) {
        purchaseTag = tag;
        tablePurchaseRow = (TableRow) view.getParent();
        enableDisableButton("Disable");
        tablePurchaseRow.setBackgroundColor(Color.rgb(213,208,196));
        confirmDeleteMessage.setVisibility(View.VISIBLE);
        cancelDeletePurchase.setVisibility(View.VISIBLE);
        okDeletePurchase.setVisibility(View.VISIBLE);
    }

    public void returnToPurchases(View view){
        confirmDeleteMessage.setVisibility(View.INVISIBLE);
        cancelDeletePurchase.setVisibility(View.INVISIBLE);
        okDeletePurchase.setVisibility(View.INVISIBLE);
        tablePurchaseRow.setBackgroundColor(0);
        enableDisableButton("Enable");
    }

    public void deletePurchase(View view){
        tablePurchaseRow.removeAllViews();
        purchasesKeys.set(purchaseTag, "");
        returnToPurchases(view);
    }

    public void acceptChangesToPurchases(View view) throws IOException {
        currentResult = budgetUpdate;

        for(int i = 0; i < purchasesKeys.size(); i++)
        {
            if(purchasesKeys.get(i).equals("")){
                purchasesKeys.remove(i);
                purchasesValues.remove(i);
                purchasesDates.remove(i);
                i--;
            }
        }
        acceptChanges(view);
        calculateBudget();
        addPurchasesToMemory();
        addBudgetResultsToMemory();
        backToMain(view);
    }

    public void displayPurchases(View view){
        recapScreen.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.INVISIBLE);
        purchasesTable.removeAllViews();
        int rowCount = 0;

        for(int i = purchasesKeys.size()-1; i >= 0 ; i--) {
            TextView purchaseKeyText = new TextView(this);
            TextView purchaseValueText = new TextView(this);
            TextView onText = new TextView(this);
            TextView purchaseDateText = new TextView(this);
            ImageView deleteButton = new ImageView(this);
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(10,10,10,10);
            row.setLayoutParams(layoutParams);
            purchaseKeyText.setText(purchasesKeys.get(i));
            purchaseValueText.setText(numFormat.format(purchasesValues.get(i)));
            purchaseDateText.setText(purchasesDates.get(i));
            deleteButton.setImageResource(R.drawable.deleteicon);
            deleteButton.setTag(i);
            deleteButton.setLayoutParams(new TableRow.LayoutParams(80, 80));
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    confirmDeleteMessage(v, Integer.parseInt(v.getTag().toString()));
                }
            });
            row.addView(purchaseKeyText);
            row.addView(purchaseValueText);
            row.addView(purchaseDateText);
            row.addView(deleteButton);
            for(int idx = 0; idx < row.getChildCount()-1; idx ++)
            {
                TextView item = (TextView) row.getChildAt(idx);
                item.setTextSize(22);
                item.setPadding(0,13,0,13);
                item.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                item.setGravity(Gravity.START);
            }
            purchaseKeyText.setWidth(250);
            purchasesTable.addView(row, rowCount);
            rowCount++;
        }
    }

    public void savePurchase(View view) throws IOException {

        if(!purchaseCostEditText.getText().toString().isEmpty()){
            currentResult = budgetUpdate;
            float newPurchaseValue = Float.parseFloat(decimalFormat.format(Float.parseFloat(purchaseCostEditText.getText().toString())));
            String newPurchaseKey = supermarketText.getText().toString();
            purchasesKeys.add(newPurchaseKey);
            String purchaseYear = String.valueOf(datePicker.getYear()).substring(2);
            String newPurchaseDate = datePicker.getDayOfMonth() + "-" + monthName[datePicker.getMonth()].substring(0,3) + "-" + purchaseYear;
            purchasesDates.add(newPurchaseDate);
            purchasesValues.add(newPurchaseValue);
            budgetUpdate -= newPurchaseValue;
            budget.setText(numFormat.format(budgetUpdate));
            purchaseCostEditText.setText("");
            purchaseNotes.setText("");

            addPurchasesToMemory();
            addBudgetResultsToMemory();
            setProgressBar();
        }
        backToMain(view);
    }

    public void displayDue(View view){
        dueScreen.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.INVISIBLE);
        String dateString = makeOrdinal(calendar.get(Calendar.DAY_OF_MONTH)) + " of " + monthName[calendar.get(Calendar.MONTH)];
        monthTextView.setText(dateString);
        int dueRowCount = 0;
        int paidRowCount = 0;
        ArrayList<Integer> dueDatesSort = new ArrayList<Integer>();
        String[] dueKeys = new String[budgetKeys.length-2];
        float[] dueValues = new float[budgetKeys.length-2];
        int[] dueDates = new int[budgetKeys.length-2];
        dueTable.removeAllViews();
        paidTable.removeAllViews();

        for(int i = 2; i < budgetKeys.length; i++){
            dueDatesSort.add(budgetRecords.getInt("Date" + i,30));
            budgetValues[i] = budgetRecords.getFloat("Value" + i, 0);
            budgetDates[i] = budgetRecords.getInt("Date" + i, 30);
        }
        Collections.sort(dueDatesSort);

        for(int i = 2; i < budgetKeys.length; i++){
            for(int idx = 0; idx < dueDatesSort.size(); idx++){
                if (budgetDates[i] == dueDatesSort.get(idx)) {
                    while(dueValues[idx] != 0 && idx < dueDatesSort.size()) {
                        idx++;
                    }
                    if(dueValues[idx] == 0){
                        dueKeys[idx] = budgetKeys[i];
                        dueValues[idx] = budgetValues[i];
                        dueDates[idx] = budgetDates[i];
                        break;
                    }
                }
            }
        }

        for(int i = 0; i < dueKeys.length; i++) {
            if(dueValues[i] != 0) {
                TextView dueDateText = new TextView(this);
                TextView hyphenText = new TextView(this);
                TextView dueKeyText = new TextView(this);
                TextView dueValueText = new TextView(this);
                TableRow row = new TableRow(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(layoutParams);
                int dueDate = dueDates[i];
                dueDateText.setText(makeOrdinal(dueDates[i]));
                hyphenText.setText("-");
                dueKeyText.setText(dueKeys[i]);
                dueValueText.setText(numFormat.format(dueValues[i]));
                row.addView(dueDateText);
                row.addView(hyphenText);
                row.addView(dueKeyText);
                row.addView(dueValueText);

                for (int idx = 0; idx < row.getChildCount(); idx++) {
                    TextView item = (TextView) row.getChildAt(idx);
                    item.setTextSize(28);
                    item.setPadding(6, 6, 6, 6);
                    item.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                    item.setGravity(Gravity.START);
                }

                if (dueDate <= currentDay) {
                    paidTable.addView(row, paidRowCount);
                    paidRowCount++;
                } else {
                    dueTable.addView(row, dueRowCount);
                    dueRowCount++;
                }
            }
        }
    }

    public String makeOrdinal(int date){
        String dueDateString = String.valueOf(date);

        switch(date) {
            case 1:
            case 21:
            case 31:
                dueDateString += ordinals[0];
                break;
            case 2:
            case 22:
                dueDateString += ordinals[1];
                break;
            case 3:
            case 23:
                dueDateString += ordinals[2];
                break;
            default:
                dueDateString += ordinals[3];
        }
        return dueDateString;
    }

}
