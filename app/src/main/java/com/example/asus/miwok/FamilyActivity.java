package com.example.asus.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer ;
    private AudioManager mAudioManager ;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }  else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> words = new ArrayList<word>();

        words.add(new word("Father","Papa",R.drawable.family_father,R.raw.family_father));
        words.add(new word("Mother","Mummy",R.drawable.family_mother,R.raw.family_mother));
        words.add(new word("Sister","Bahan/Didi",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new word("Brother","Bhai/Bhayia",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new word("Son","Beta",R.drawable.family_son,R.raw.family_son));
        words.add(new word("Daughter","Beti",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new word("Grand Father","Dada Ji",R.drawable.family_grandfather,R.raw.family_grandfather));
        words.add(new word("Grand Mother","Dadi",R.drawable.family_grandmother,R.raw.family_grandmother));
//        words.add(new word("Grand son","Pota"));
//        words.add(new word("Grand Daughter","Poti"));
//        words.add(new word("Uncle","Chacha"));
//        words.add(new word("Unty","Chachi"));
//        words.add(new word("Cousin","Chachere,Mamere,Fufere Bhai/Behen"));
//        words.add(new word("Nephew","Bhatija"));
//        words.add(new word("Niece","Bhatiji"));
//        words.add(new word("Father-In-Law","Sasur"));
//        words.add(new word("Mother-In-Law","Saas"));

        wordAdapter adapter = new wordAdapter(this,words,R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word newWord =  words.get(position);

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //release the media player if it currently exists because we are about to play  different file
                    releaseMediaPlayer();

                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, newWord.getAudioResourceId());
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }


            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
