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

public class PhrasesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_phrases);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> phrases = new ArrayList<word>() ;

        phrases.add(new word("My name is __","Mera Naam __ Hai",R.raw.phrase_my_name_is));
        phrases.add(new word("What's(what is) Your Name","Tumhara naam Kya Hai",R.raw.phrase_what_is_your_name));
        phrases.add(new word("Where are you from?/Where Do you belong to?","Tum kaha se ho",R.raw.phrase_come_here));
        phrases.add(new word("What are your Hobbies","Tumhara shaukh kya hai",R.raw.phrase_how_are_you_feeling));
        phrases.add(new word("Hindi is our motherTongue","Hindi Hamari Matra Bhasha Hai",R.raw.phrase_im_feeling_good));
        phrases.add(new word("Do not (Don't) fight amoung yourselfs","Aaps me Ladai Mat Karo",R.raw.phrase_im_coming));
        phrases.add(new word("What do you want in Breakfast","Naste me kya Chahiye",R.raw.phrase_lets_go));
        phrases.add(new word("Empty Vessel sounds much","khali ghada awaaz karta hai",R.raw.phrase_where_are_you_going));
        phrases.add(new word("Penny penny Makes Many","Boond boond se sagar banta hai",R.raw.phrase_come_here));
        phrases.add(new word("Don't Shout","Awaz mat karo",R.raw.phrase_yes_im_coming));
        phrases.add(new word("Please give a cup of tea","Kripya kar ke mujhe ek cup chay de do",R.raw.phrase_yes_im_coming));

        wordAdapter adapter = new wordAdapter(this,phrases,R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word newWord =  phrases.get(position);


                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //release the media player if it currently exists because we are about to play  different file
                    releaseMediaPlayer();

                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, newWord.getAudioResourceId());
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
