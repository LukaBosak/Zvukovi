package hr.bosak.zvukovi;


import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    LinearLayout mainActivityRL;

    TabLayout tabLayoutTL;
    ViewPager fragmentPagerVP;

    FragmentAdapter fragmentAdapter;

    public Intent intent;
    MediaPlayer mediaPlayer = MediaPlayerInstance.getInstance().getMediaPlayer();
    public boolean firstBackground = true;
    public boolean secondBackground = false;
    public boolean thirdBackground = false;

    int indexMedia = 0;
    int currentMedia = 0;
    PlayActivity playActivity;


    public static final String POSITION = "position";
    public static final String COVER_4 = "cover 4";
    public static final String COVER_8 = "cover 8";
    public static final int REQUEST_CODE = 0;

    public static final String SEND_INDEX = "send song index";
    public static final String SEND_CURRENT = "send current position";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setUpTabs();
        getSupportActionBar();

    }


    public void initWidgets(){

        mainActivityRL = findViewById(R.id.mainActivtyRL);
        tabLayoutTL = findViewById(R.id.tabLayoutTL);
        fragmentPagerVP = findViewById(R.id.fragmentPagerVP);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_cancel){
            finish();
            mediaPlayer.stop();
            mediaPlayer.release();


        }
        else if(item.getItemId() == R.id.action_background ){

            if(firstBackground) {
                mainActivityRL.setBackgroundResource(R.drawable.cover_4);
                firstBackground = false;
                secondBackground = true;


            } else if(secondBackground) {
                mainActivityRL.setBackgroundResource(R.drawable.cover_8);
                firstBackground = false;
                secondBackground = false;
                thirdBackground = true;
            }
            else {
                mainActivityRL.setBackgroundResource(R.drawable.cover_prog);
                firstBackground = true;
                thirdBackground = false;
            }
        }

        else {
            intent = new Intent(this, PlayActivity.class);
            currentMedia = mediaPlayer.getCurrentPosition();
            intent.putExtra(SEND_INDEX, indexMedia);
            intent.putExtra(SEND_CURRENT, currentMedia);
            if (secondBackground){
                intent.putExtra(COVER_4,"");
            }
            else if(thirdBackground){
                intent.putExtra(COVER_8, "");
            }
            startActivityForResult(intent, REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.hasExtra(PlayActivity.INDEX)){
                indexMedia = data.getIntExtra(PlayActivity.INDEX, 0);
                Toast.makeText(this, String.valueOf(indexMedia), Toast.LENGTH_LONG).show();
            }
        }
        else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
       }

    }


    public void setUpTabs(){
        fragmentAdapter = new FragmentAdapter(this, getSupportFragmentManager());
        fragmentPagerVP.setAdapter(fragmentAdapter);
        tabLayoutTL.setupWithViewPager(fragmentPagerVP);
    }
}
