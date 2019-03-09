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

public class NumberActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_number);


        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Creating arrayList and initialising it.

         final ArrayList<word> words = new ArrayList<word>();

        /*instead of adding code :
        word w = new word("one",lutti);
        words.add(w) ;
        we use it in a more consize way
         */
        words.add(new word("One","a go",R.drawable.number_one,R.raw.number_one));
        words.add(new word("Two","do go",R.drawable.number_two,R.raw.number_two));
        words.add(new word("Three","teen go",R.drawable.number_three,R.raw.number_three));
        words.add(new word("Four","char go",R.drawable.number_four,R.raw.number_four));
        words.add(new word("Five","panch go",R.drawable.number_five,R.raw.number_five));
        words.add(new word("Six","chao go",R.drawable.number_six,R.raw.number_six));
        words.add(new word("Seven","saat go",R.drawable.number_seven,R.raw.number_seven));
        words.add(new word("Eight","aath go",R.drawable.number_eight,R.raw.number_eight));
        words.add(new word("Nine","nao go",R.drawable.number_nine,R.raw.number_nine));
        words.add(new word("Ten","dus go",R.drawable.number_ten,R.raw.number_ten));
//        words.add(new word("Eleven","Egarah"));
//        words.add(new word("Twelve","Barah"));
//        words.add(new word("Thirteen","Terah"));
//        words.add(new word("Fourteen","Chaudah"));
//        words.add(new word("Fifteen","Pandrah"));
//        words.add(new word("Sixteen","Solah"));
//        words.add(new word("Seventeen","Satrah"));
//        words.add(new word("Eighteen","Atharah"));
//        words.add(new word("Nineteen","Unees"));
//        words.add(new word("Twenty","Bees"));

        wordAdapter adapter = new wordAdapter(this, words,R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word newWord =  words.get(position);

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //release the media player if it currently exists because we are about to play  different file
                    releaseMediaPlayer();

                    mMediaPlayer = MediaPlayer.create(NumberActivity.this, newWord.getAudioResourceId());
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
