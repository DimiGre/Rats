package com.example.mityha.rrimer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public Forebot forebot;
    private DrawThread df;
    private int RatsLeft = 0;
    private Animal[] animals;
    private int generation = 0;

    private ArrayList<Integer> a = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
    private int step = 1;


    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data) {
        if (reqestCode == 1)
            if (resultCode == 1) {
                for (int gen = 0; gen < 1000; gen += 1) {
                    for (int i = 0; ; i++)
                        if (!(new File(getFilesDir(), "rat-" + i + " " + gen + ".dat")).delete())
                            break;
                }
                generation = 0;
                arrayLists = new ArrayList<>();
                a = new ArrayList<>();
                (new File(getFilesDir(), "generation.dat")).delete();
                if (df != null || forebot != null) {
                    df.interrupt();
                    forebot.interrupt();
                    startornot = true;
                    animals = null;
                    start();
                    df.start();
                    forebot.pause();
                    forebot.start();
                }
            }
            if (reqestCode == 2)
                if (resultCode == RESULT_OK) {
                    int rC = 0;
                    arenaMode = true;
                    ArrayList<Animal> animalArrayList = new ArrayList<>();
                    df.interrupt();
                    forebot.interrupt();
                    df = new DrawThread(this);
                    forebot = new Forebot();
                    for (int gen = 0; gen < generation; gen++) {
                        Object obj = data.getExtras().get("gen" + gen);
                        if (obj != null) {
                            for (int i = 0; (new File(getFilesDir(), "rat-" + i + " " + ((int) obj) + ".dat")).exists(); i++)
                                try {
                                    ObjectInputStream ois = new ObjectInputStream(this.openFileInput("rat-" + i + " " + ((int) obj) + ".dat"));
                                    Animal animal = new Monkey(df, forebot, (int[][]) ois.readObject());
                                    animal.name = ((int) obj) + "";
                                    animalArrayList.add(animal);
                                    rC++;
                                    ois.close();
                                } catch (Exception e) {
                                }
                        }
                    }
                    animals = animalArrayList.toArray(new Animal[animalArrayList.size()]);
                    RatsLeft = rC;
                    df.start();
                    for (int i = 0; i < 8333 * Settings.maxPopulation; i++) ;
                    forebot.start();
                    first = true;
                }

        }

    String message;

    public void AndryhaUNasTrup(){
        RatsLeft--;
        if(!arenaMode)
        if(RatsLeft == Settings.minPopulation){
            int gi = 0;

            forebot.interrupt();
            df.interrupt();

            forebot = new Forebot();
            df = new DrawThread(this);

            arrayLists.add(a);
            a = new ArrayList<>();
            step = 0;

            Animal[] goodRats = new Animal[Settings.minPopulation];
            for(int i = 0; i < Settings.maxPopulation; i++){
                if(animals[i].AmIalive()){
                    goodRats[gi] = animals[i];
                    gi++;
                }
                animals[i] = null;
            }

            generation++;
            if(Settings.saveFrequency > 0)
            if((generation)%Settings.saveFrequency == 0){
                for(int i = 0; i < Settings.minPopulation; i++) goodRats[i].SaveMeToFile(i, generation);
            }

            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(this.openFileOutput("generation.dat", MODE_PRIVATE));
                oos.writeObject(generation);
                oos.close();
            }
            catch(Exception e){
            }

            for (gi = 0; gi < Settings.minPopulation; gi++)
                for(int ai = 0; ai < Settings.numberOfClones; ai++){
                    if(ai == Settings.numberOfClones / 2) goodRats[gi].teach();
                    animals[gi*Settings.numberOfClones+ai] = new Monkey(df, forebot, goodRats[gi].getMemory());
                }
            for(int ai = gi * Settings.numberOfClones; ai < Settings.maxPopulation; ai++){
                animals[ai] = new Monkey(df, forebot);
            }
        RatsLeft = Settings.maxPopulation;
            if(Settings.save)
            for(int i = 0; i < Settings.maxPopulation; i++)
                animals[i].SaveMeToFile(i, 0);
        df.start();
        for (int i = 0; i < 8333*Settings.maxPopulation; i++);
        forebot.start();
        }


        if(arenaMode){
            if(RatsLeft == Settings.minPopulation || RatsLeft == 1){
                forebot.interrupt();
                df.interrupt();
                boolean flag = true;
                message = "Выжили крысы из поколений: ";
                for(int i = 0;flag; i++){
                    flag = false;
                    try{
                        if(animals[i].AmIalive() && message.indexOf(animals[i].name) == -1) message += animals[i].name + " ";
                        flag = true;
                    } catch (Exception e){}
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        (new AlertDialog.Builder(MainActivity.this)).setMessage(message).create().show();
                    }
                });
                arenaMode = false;
                first = true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ObjectInputStream ois = new ObjectInputStream(this.openFileInput("generation.dat"));
            generation = (int)ois.readObject();
            ois.close();
        }
        catch(Exception e){}

        imageView = findViewById(R.id.imageView);
        animals = null;
        Settings.context = getApplicationContext();
        Settings.readObj();
    }

    boolean startornot = false;
    boolean first = true;

    private void start(){
        forebot = new Forebot();
        df = new DrawThread(this);
        animals = new Animal[Settings.maxPopulation];
        int i = 0;
            if(Settings.save){
                boolean flag = true;
                for(i = 0; flag && i < Settings.maxPopulation; i++) {
                    flag = false;
                    try {
                        ObjectInputStream ois = new ObjectInputStream(this.openFileInput("rat-" + i + " 0.dat"));
                        animals[i] =  new Monkey(df, forebot, (int[][])ois.readObject());
                        ois.close();
                        flag = true;
                    } catch (Exception e) {
                        i--;
                    }
                }
            }
        for(;i < Settings.maxPopulation; i++) animals[i] = new Monkey(df, forebot);
        RatsLeft = i;
    }

    public void stop(){
        if(forebot != null) {
            forebot.pause();
            ((Button)findViewById(R.id.button)).setText("Старт");
            startornot = true;
        }
    }

    public void onClickBtn(View v){
        if(!arenaMode) {
            if (first) {
                start();
                first = false;
                df.start();
                forebot.start();
            } else if (startornot) {
                forebot.go();
                ((Button) v).setText("Пауза");
                startornot = false;
            } else {
                stop();
            }
        } else {
            forebot.go();
        }
    }

    public void onChangeSettings(View v){
        stop();
        startActivityForResult(new Intent(this, Settings.class), 1);
    }

    boolean arenaMode = false;


    public void onStartArena(View v){
            stop();
            Intent intent = new Intent(this, Arena.class);
            intent.putExtra("generation", 1000);
            startActivityForResult(intent, 2);
    }

    public void onShowStatistics(View v){
        stop();
        Intent intent = new Intent(getApplicationContext(), Statistics.class);
        intent.putExtra("a", arrayLists);
        startActivity(intent);
    }

    public void stepFinished(){
        if(step % 100 == 0)
        a.add(RatsLeft);
        step++;
    }
}
