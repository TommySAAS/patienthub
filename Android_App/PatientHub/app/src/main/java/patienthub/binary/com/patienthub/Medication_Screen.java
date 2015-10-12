package patienthub.binary.com.patienthub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;

import patienthub.binary.com.patienthub.Scheduling.Scheduler;
import patienthub.binary.com.patienthub.adapters.MedicationListAdapter;
import patienthub.binary.com.patienthub.data.Dosage;
import patienthub.binary.com.patienthub.webservice.HttpManager;


public class Medication_Screen extends Activity {

    public final static String DOSAGES_FILENAME = "dosages.txt";
    private static LayoutInflater inflater = null;

    private ListView listview;
    private MedicationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication__screen);
        listview = (ListView) findViewById(R.id.medicationListView);
        List<Dosage> dosageList = new ArrayList<>();

        String timeOfDay = getIntent().getStringExtra("timeOfDay");

        String json = "";
        try {
            json = readFromFile(DOSAGES_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            Dosage[] dosages = new ObjectMapper().readValue(json, Dosage[].class);
            for(Dosage dose : dosages){
                if(dose.isScheduledToday() && timeOfDay.equals(dose.getTime_taken())) {
                    dosageList.add(dose);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter  = new MedicationListAdapter(this,dosageList);

        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(R.layout.medication_listview_footer, null);
        listview.addFooterView(footer);
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medication__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onCheckboxClicked(View v){

    }

    private String readFromFile(String filename) throws IOException{

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void postMedsTaken(List<Dosage> dosagesList){
        String token = "";
        HttpManager httpMan = new HttpManager();
        try {
            token = readFromFile("token.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Dosage dosage : dosagesList){
            try {
                httpMan.postMedicationData("The medication was taken",token,dosage.getId(),true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchQuizForNotTakenMeds(List<Dosage> dosagesList){
        String token = "";
        HttpManager httpMan = new HttpManager();
        try {
            token = readFromFile("token.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> dosageIdsList = new ArrayList<String>();
        String [] dosageIds;
        List<String> dosageNamesList = new ArrayList<String>();
        String [] dosageNames;

        for(Dosage dosage : dosagesList){
            dosageIdsList.add(dosage.getId() + "");
            dosageNamesList.add(dosage.getTreatment_name());
        }
        dosageIds = (String[]) dosageIdsList.toArray();
        dosageNames = (String[]) dosageNamesList.toArray();

        Intent myIntent = new Intent(Medication_Screen.this, QuizPage.class);
        myIntent.putExtra("questionNum", 0);
        myIntent.putExtra("dosageFeedbackIDs", dosageIds);
        myIntent.putExtra("dosageNames", dosageNames);
        myIntent.putExtra("numQuestions",dosageIds.length);
        myIntent.putExtra("token",token);

        startActivity(myIntent);
    }


}

