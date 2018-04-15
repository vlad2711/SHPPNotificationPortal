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
        const val TWEET_URLS = "urls"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TWEETS_TABLE_NAME($TWEET_ID LONG PRIMARY KEY, $TWEET_TEXT TEXT, $TWEET_DATE TEXT, $TWEET_AUTHOR_ICON TEXT, $TWEET_AUTHOR_NAME TEXT, $TWEET_MEDIA_CONTENT TEXT, $TWEET_URLS TEXT)")
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TWEETS_TABLE_NAME")
        onCreate(db)
    }

    fun addTweet(tweet: TwitterModels.Companion.Tweet){
        Log.d(TAG, tweet.text)
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT, TWEET_URLS),
                "$TWEET_ID =?", arrayOf(tweet.id.toString()), null, null, null)

        if(cursor.count == 0) {
            val values = ContentValues()

            values.put(TWEET_ID, tweet.id)
            values.put(TWEET_TEXT, tweet.text)
            values.put(TWEET_AUTHOR_ICON, tweet.icon_url)
            values.put(TWEET_AUTHOR_NAME, tweet.userName)
            values.put(TWEET_DATE, tweet.time)
            values.put(TWEET_MEDIA_CONTENT, Gson().toJson(tweet.mediaUrls))
            values.put(TWEET_URLS, Gson().toJson(tweet.urls))

            writableDatabase.insert(TWEETS_TABLE_NAME, null, values)
            writableDatabase.close()
        }

        cursor.close()
        readableDatabase.close()
    }

    fun getTweet(){
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT, TWEET_URLS),
                null, null, null, null, "$TWEET_ID DESC" )

        cursor.moveToFirst()

        Log.d(TAG, cursor.count.toString())

        for (i in 0 until 10) {
            if(i < cursor.count){

                cursor.moveToPosition(i)

                val media = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), "[]"))
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), ArrayList<TwitterModels.Companion.Media>()::class.java) else ArrayList()


                val urls = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_URLS)), "[]"))
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_URLS)), ArrayList<TwitterModels.Companion.TwitterUrl>()::class.java) else ArrayList()


                val tweet = TwitterModels.Companion.Tweet(cursor.getLong(cursor.getColumnIndex(TWEET_ID)),
                        cursor.getString(cursor.getColumnIndex(TWEET_DATE)),
                        cursor.getString(cursor.getColumnIndex(TWEET_TEXT)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_ICON)),
                        media,
                        urls)

                Utils.news.add(tweet)
            }
        }

        Utils.newsBufSize = Utils.news.size
        cursor.close()
        readableDatabase.close()
    }

    fun getTweet(maxId: Long){
        val cursor = readableDatabase.query(TWEETS_TABLE_NAME,
                arrayOf(TWEET_ID, TWEET_TEXT, TWEET_AUTHOR_ICON, TWEET_AUTHOR_NAME, TWEET_DATE, TWEET_MEDIA_CONTENT, TWEET_URLS),
        "$TWEET_ID <=?", arrayOf(maxId.toString()), null, null, "$TWEET_ID DESC")
        cursor.moveToFirst()

        Log.d(TAG, Utils.newsBufSize.toString() + " " + Utils.news.size)

        Log.d(TAG, cursor.count.toString())
        for (i in 0 until 10) {
            if(Utils.news.size - 1 < cursor.count) {
                cursor.moveToPosition(Utils.news.size - 1)

                val media = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), "[]"))
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_CONTENT)), ArrayList<TwitterModels.Companion.Media>()::class.java) else ArrayList()

                val urls = if(Objects.equals(cursor.getString(cursor.getColumnIndex(TWEET_URLS)), "[]"))
                    Gson().fromJson(cursor.getString(cursor.getColumnIndex(TWEET_URLS)), ArrayList<TwitterModels.Companion.TwitterUrl>()::class.java) else ArrayList()

                val tweet = TwitterModels.Companion.Tweet(cursor.getLong(cursor.getColumnIndex(TWEET_ID)),
                        cursor.getString(cursor.getColumnIndex(TWEET_DATE)),
                        cursor.getString(cursor.getColumnIndex(TWEET_TEXT)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(TWEET_AUTHOR_ICON)),
                        media,
                        urls)
                Utils.news.add(tweet)
            }
        }
        Log.d(TAG, Utils.newsBufSize.toString() + " " + Utils.news.size)

        Utils.newsBufSize = Utils.news.size - 10
        cursor.close()
        readableDatabase.close()
    }
}