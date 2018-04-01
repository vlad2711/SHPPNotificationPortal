package com.kram.vlad.shppnotificationportal.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.utils.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

/**
 * Created by vlad on 29.03.2018.
 * Do all job with sqlite
 */
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TAG = this::class.java.simpleName

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "tweets"

        const val TWEETS_TABLE_NAME = "tweets"
        const val TWEET_ID = "id"
        const val TWEET_TEXT = "text"
        const val TWEET_DATE = "date"
        const val TWEET_AUTHOR_NAME = "name"
        const val TWEET_AUTHOR_ICON = "icon"
        const val TWEET_MEDIA_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TWEETS_TABLE = "CREATE TABLE $TWEETS_TABLE_NAME($TWEET_ID LONG, $TWEET_TEXT TEXT, $TWEET_DATE TEXT, " +
                "$TWEET_AUTHOR_ICON TEXT, $TWEET_AUTHOR_NAME TEXT, $TWEET_MEDIA_CONTENT TEXT)"
        db!!.execSQL(CREATE_TWEETS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TWEETS_TABLE_NAME")
        onCreate(db)
    }

    fun addTweet(tweet: TwitterModels.Companion.Tweet){
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT),
                "$TWEET_ID == ${tweet.id}", null, null, null, null)

        if(cursor.count == 0) {
            val values = ContentValues()

            values.put(TWEET_ID, tweet.id)
            values.put(TWEET_TEXT, tweet.text)
            values.put(TWEET_AUTHOR_ICON, tweet.icon_url)
            values.put(TWEET_AUTHOR_NAME, tweet.userName)
            values.put(TWEET_DATE, tweet.time)
            values.put(TWEET_MEDIA_CONTENT, Gson().toJson(tweet.mediaUrls))

            writableDatabase.insert(TWEETS_TABLE_NAME, null, values)
        }

        cursor.close()
    }

    fun getTweet(){
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT),
                null, null, null, null, "$TWEET_ID DESC" )

        cursor.moveToFirst()
        val size = Utils.news.size
        for (i in size until size + 10) {
            Log.d(TAG, i.toString())
            if(i < cursor.count){

                cursor.moveToPosition(i)
                val media = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), "[]")){
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), ArrayList<TwitterModels.Companion.Media>()::class.java)
                } else{
                    ArrayList()
                }

                val tweet = TwitterModels.Companion.Tweet(cursor.getLong(cursor.getColumnIndex(TWEET_ID)),
                        cursor.getString(cursor.getColumnIndex(TWEET_DATE)),
                        cursor.getString(cursor.getColumnIndex(TWEET_TEXT)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_ICON)),
                        media)
                Log.d(TAG, cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)))

                Utils.news.add(tweet)
            }
        }

        Utils.newsBufSize = Utils.news.size
        cursor.close()
    }

    fun getTweet(maxId: Long){
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT),
        "$TWEET_ID <= $maxId", null, null, null, null)
        cursor.moveToFirst()

        val size = Utils.news.size
        for (i in size until size + 10) {
            Log.d(TAG, i.toString())
            if(i < cursor.count) {
                cursor.moveToPosition(i)

                val media = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), "[]")){
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), ArrayList<TwitterModels.Companion.Media>()::class.java)
                } else{
                    ArrayList()
                }
                val tweet = TwitterModels.Companion.Tweet(cursor.getLong(cursor.getColumnIndex(TWEET_ID)),
                        cursor.getString(cursor.getColumnIndex(TWEET_DATE)),
                        cursor.getString(cursor.getColumnIndex(TWEET_TEXT)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_ICON)),
                        media)
                Utils.news.add(tweet)
            }
        }
        Utils.newsBufSize = Utils.news.size
        cursor.close()
    }
}