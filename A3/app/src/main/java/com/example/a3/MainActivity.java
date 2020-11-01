package com.example.a3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a3.TitlesFragment.ListSelectionListener;

public class MainActivity extends AppCompatActivity implements ListSelectionListener {

    private static final String KABOOM_PERMISSION =
            "com.example.a3.edu.uic.cs478.f20.kaboom" ;
    private static final String A3_INTENT =
            "edu.uic.cs478.Receiver";


    public static String[] mTitleArray;
    public static int[] mQuoteArray;
    public static String[] mWebpageArray;

    int currentlySelected = -1;


    private final QuotesFragment mQuoteFragment = new QuotesFragment();

    // UB 2/24/2019 -- Android Pie twist: Original FragmentManager class is now deprecated.
    //MH 7/7/2020 added support library in gradle module build file

    FragmentManager mFragmentManager;

    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        if (savedInstanceState != null) {
//            currentlySelected = savedInstanceState.getInt("index");
//            Toast.makeText(this, "index: " + currentlySelected, Toast.LENGTH_LONG).show();
//        }


        // Get the string arrays with the titles and qutoes
        mTitleArray = new String[]{"Maldives", "Bora Bora", "Hawaii"};
        mQuoteArray = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3};
        mWebpageArray = new String[]{
                "https://visitmaldives.com/en",
                "https://www.borabora.com",
                "https://www.hawaii.com"
        };

        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("A3");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_icon);

        // Get references to the TitleFragment and to the QuotesFragment
        mTitleFrameLayout = (FrameLayout) findViewById(R.id.title_fragment_container);
        mQuotesFrameLayout = (FrameLayout) findViewById(R.id.quote_fragment_container);


        // Get a reference to the SupportFragmentManager instead of original FragmentManager
        mFragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction with backward compatibility
        //android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager
        //		.beginTransaction();

        FragmentTransaction fragmentTransaction =mFragmentManager.beginTransaction();

        // Add the TitleFragment to the layout
        // UB: 10/2/2016 Changed add() to replace() to avoid overlapping fragments
        fragmentTransaction.replace(
                R.id.title_fragment_container,
                new TitlesFragment());

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager.addOnBackStackChangedListener(
                // UB 2/24/2019 -- Use support version of Listener
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });



    }


    private void setLayout() {

        // Determine whether the QuoteFragment has been added
        if (!mQuoteFragment.isAdded()) {

            // Make the TitleFragment occupy the entire layout
            mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
        } else {


            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Make the TitleLayout take 1/3 of the layout's width
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                // Make the QuoteLayout take 2/3's of the layout's width
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
            else {
                // Make the TitleLayout take 1/3 of the layout's width
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 0f));

                // Make the QuoteLayout take 2/3's of the layout's width
                mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        }
    }

    @Override
    public void onListSelection(int index) {

        currentlySelected = index;

        // If the QuoteFragment has not been added, add it now
        if (!mQuoteFragment.isAdded()) {

            // Start a new FragmentTransaction
            // UB 2/24/2019 -- Now must use compatible version of FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the QuoteFragment to the layout
            fragmentTransaction.add(R.id.quote_fragment_container,
                    mQuoteFragment);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mQuoteFragment.getShownIndex() != index) {

            // Tell the QuoteFragment to show the quote string at position index
            mQuoteFragment.showQuoteAtIndex(index);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                if (currentlySelected >= 0) {
                    Intent aIntent = new Intent(A3_INTENT);
                    String webpage = mWebpageArray[currentlySelected];
                    aIntent.putExtra("Secret Message", webpage);
                    sendOrderedBroadcast(aIntent, KABOOM_PERMISSION);
                }
                else {
                    Toast.makeText(this, "No item was selected", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.item2:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", currentlySelected);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentlySelected = savedInstanceState.getInt("index");
        Toast.makeText(this, "indexRESTORE: " + currentlySelected, Toast.LENGTH_LONG).show();

        setLayout();
        onListSelection(currentlySelected);
    }
}