package com.example.asus.miwok;

/**
 * Created by ASUS on 20-07-2017.
 */

public class word {

    /*Defining states
     */

    /**
     * default word that is called in that region
     */
    private String mDefaultTraslation;

    /**
     * miwok translation word
     */
    private String mMiwokTrranslation;

    /**
     * image
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /*
    settng a constant
     */
    private static final int NO_IMAGE_PROVIDED = -1;


    private int mAudioResourceId;

    /*
    state declaration over
     */

    /*
    Defining constructor which should be same as of that of the class
     */

    public word(String defaultTraslation, String miwokTranslation, int audioResourceId) {
        mDefaultTraslation = defaultTraslation;
        mMiwokTrranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    /*
    a second constructor
     */

    public word(String defaultTraslation, String miwokTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTraslation = defaultTraslation;
        mMiwokTrranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    /*
    constructor defined
     */

    /*
    setting methords
     */

    /*
    for dislaying default word
     */

    public String getDefaultTranslation() {
        return mDefaultTraslation;
    }

    /*
    for dislaying miwok word
     */
    public String getMiwokTranslation() {
        return mMiwokTrranslation;
    }

    /*
    for displaying image
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /*
    checking for an image present or not
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public int getAudioResourceId() {
        return mAudioResourceId;
    }



}