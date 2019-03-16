package com.example.hangman;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {
//ActionBarActivity
    TextView tv, tv2;
    EditText et;
    ImageView iv;
    int index, drawable, x, number;
    char t;
    List<Character> tab = new ArrayList<>();
    String[] words = {""};
    String message = "You Loose!";
    protected String mysteryWord = "", temp;
    StringBuilder guessWord;//by złożoność nie była duża


    //int lines = 0;
    ArrayList<String> mysteryWords = new ArrayList<>();
    //String line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et = findViewById(R.id.editText);
        iv = findViewById(R.id.imagine_v);
        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);



        /*dr = ResourcesCompat.getDrawable(getResources(), R.drawable.hang_zero, null);
        iv.setImageDrawable(dr);*/
        index = 0;
        choosePicture(index);

        words = getResources().getStringArray(R.array.wordsy);



        //pobieranie danych z pliku
        InputStream is = getResources().openRawResource(R.raw.mystery_words);
        InputStreamReader isr = new InputStreamReader(is);
        try {
            //BufferedReader reader = new BufferedReader(isr);
            LineNumberReader lnr = new LineNumberReader(isr);
            //Scanner sc = new Scanner(isr);

            while((temp = lnr.readLine()) != null){
                if(!temp.equals(""))
                    mysteryWords.add(temp);

                /*if(!temp.equals("")) {
                    mysteryWords.add(temp);
                    lines++;
                }
                else
                    lines--;*/
            }
            //lines = lnr.getLineNumber(); //łączna liczba linii

            lnr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button cl = findViewById(R.id.button);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sprawdzamy czy wprowadziliśmy cokolwiek do edittext
                if (!mysteryWord.equals("")) {
                    iv = findViewById(R.id.imagine_v);
                    x = 0;
                    try {
                        if (et.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "write letter", Toast.LENGTH_LONG).show();
                        } else if (et.getText().charAt(1) != NULL) {
                            Toast.makeText(MainActivity.this, "write only one letter", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(IndexOutOfBoundsException eb){
                        t = et.getText().charAt(0);

                        //sprawdzasz czy powtórzyła się litera
                        for (int k = 0; k < tab.size(); k++) {
                            if (tab.get(k) == t) {
                                x++;
                            }
                        }

                        if (x == 0) {
                            tab.add(t);
                            tv2.setText(tab.toString());

                            //zmienna przed zamianą
                            temp = guessWord.toString();

                            //zamieniamy * na daną liczbę
                            //sprawdza i doczasu gdy wartość i istnieje wgl!
                            for (int i = 0; (i = mysteryWord.indexOf(t, i)) >= 0; i++) {
                                //Log.d("i = ", "" + i);
                                guessWord.setCharAt(i, t);
                            }

                            if (guessWord.toString().equals(temp)) {
                                index++;
                                if (index > 8) {
                                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                                }
                                choosePicture(index);
                            } else {
                                tv.setText(guessWord.toString());
                                if (mysteryWord.equals(guessWord.toString())) {
                                    Snackbar.make(v, "You win", Snackbar.LENGTH_LONG).show();
                                    result();
                                }
                            }
                        } else {
                            Snackbar.make(v,"you've already entered this letter", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }else
                    Snackbar.make(v,"Press Play Button",Snackbar.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() { //1 pobieramy dane
            @Override
            public void onClick(View view) {

                //sprawdzamy czy lista jest pusta
                try {
                    mysteryWord = mysteryWords.get((int) (Math.random() * mysteryWords.size()));
                }catch (IndexOutOfBoundsException e){
                    Snackbar.make(view, "Your List is empty", Snackbar.LENGTH_LONG).show();
                    return;
                }

                tab.clear();
                tv2.setText(""); //przy nowej rundzie zeruje poprzednie wyświetlone wyrazy;
                index = 0;
                choosePicture(index);
                hashcode(); //haszujemy nasz wyraz
                Snackbar.make(view, mysteryWord, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Losowanie wyrazu za pomocą Item Menu z String Array
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            number = (int) (words.length * Math.random()); //losujemy który wyraz będziemy zgadywać
            mysteryWord = words[number];

            tab.clear();
            tv2.setText(""); //przy nowej rundzie zeruje poprzednie wyświetlone wyrazy;
            index = 0;
            choosePicture(index);
            hashcode(); //2 haszujemy nasz wyraz
            Snackbar.make(getWindow().getDecorView(), mysteryWord, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //zamiana wyrazu na *
    public void hashcode(){
        guessWord = new StringBuilder();
        //if(mysteryWord != null) {
        for (int i = 0; i < mysteryWord.length(); i++) {
            guessWord.append("*");
        }
        tv.setText(guessWord.toString());
        //}
    }

    public void checkWord(View view) {
        if (!mysteryWord.equals("")) { //prawdzamy czy słowo które mamy zgadnąć jest wybrane
            if (!et.getText().toString().equals("")) {//sprawdzamy czy wprowadziliśmy jakiekolwiek słowo
                if (et.getText().toString().equals(mysteryWord)) {
                    iv.setImageResource(R.drawable.hang_zero);
                    Snackbar.make(view, "You win", Snackbar.LENGTH_LONG).show();
                    result();
                } else {
                    index++;
                    if (index > 8) {
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }
                    choosePicture(index);
                }
            } else {
                Toast.makeText(MainActivity.this, "write letter", Toast.LENGTH_LONG).show();
            }
        }else {
            Snackbar.make(view, "Press Play Button", Snackbar.LENGTH_LONG).show();
        }
    }

    public void choosePicture(int i){//wybór obrazka
        switch (i){
            case 0:
                drawable = R.drawable.hang_zero;
                break;
            case 1:
                drawable = R.drawable.hang_one;
                break;
            case 2:
                drawable = R.drawable.hang_two;
                break;
            case 3:
                drawable = R.drawable.hang_three;
                break;
            case 4:
                drawable = R.drawable.hang_four;
                break;
            case 5:
                drawable = R.drawable.hang_five;
                break;
            case 6:
                drawable = R.drawable.hang_six;
                break;
            case 7:
                drawable = R.drawable.hang_seven;
                break;
            case 8:
                drawable = R.drawable.hang_eight;
                break;
            default:
                drawable = R.drawable.hang_nine;
                result();
        }
        iv.setImageResource(drawable);
    } //wybór obrazka

    public void result(){
        et.setText("");
        tv.setText("");
        mysteryWord = "";
    }
}
