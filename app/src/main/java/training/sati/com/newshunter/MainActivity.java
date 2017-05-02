package training.sati.com.newshunter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.entries;
import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    private Activity activity;
    ListView lvNews;
    NewsAdapter adapter;
    List<News> listNews;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ///*** setOnQueryTextFocusChangeListner***///////
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.filter(searchQuery.toString().trim());
                lvNews.invalidate();
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        lvNews = (ListView) findViewById(R.id.lv_rl);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                News news = (News) parent.getAdapter().getItem(position);
                Uri uriUrl = Uri.parse(news.getLink());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
//
//        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               //News news =(News)parent.getAdapter().getItem(position);
//                News news = (News) parent.getAdapter().getItem(position);
//                System.out.println(news.getLink());
//                Uri uri = Uri.parse(news.getLink());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//
//            }
//        });
        (new AsynchListViewLoader()).execute();

    }

    ////////////////////Recieving News from website///////////////////////////
   ///////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////

    private List<News> readNewsFromFeed()throws Exception{

        List<News> listNews = new ArrayList<News>();
        URL url = new URL("http://timesofindia.indiatimes.com/rssfeedstopstories.cms");
        HttpURLConnection httpCom = (HttpURLConnection)url.openConnection();
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpCom));
        List<SyndEntry> entries =feed.getEntries();
        Iterator<SyndEntry> iterator =  entries.iterator();

        while (iterator.hasNext()){


            SyndEntry entry = iterator.next();

                News news = new News();
                news.setHead(entry.getTitle());
                news.setSummary(entry.getDescription().getValue());
                news.setLink(entry.getLink());
               // System.out.println(news.getLink());
                listNews.add(news);

        }


/////////////////////Providing data by hard strings/////////////////////////
       /* News one = new News();
        one.setHead("Akhil");
        one.setSummary("007");
        listNews.add(one);
        */
        //////////////////////////////////////////////////////////////////////////
        /////////////////////////////////Reading Contacts from address book////////////////////////////////////////////
      /*
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            News record = new News();
            record.setHead(name);
            record.setSummary(number);
            listNews.add(record);

        }
        phones.close();
        */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return listNews;
        }


    ////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

        private class AsynchListViewLoader extends AsyncTask<String,Void,List<News>> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);



            @Override
            protected List<News> doInBackground(String... params) {
                listNews = new ArrayList<News>();
                try{
                    listNews = readNewsFromFeed();


                }catch(Exception e){
                    System.out.println(e);
                }

                return listNews;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Down loading Content");
                dialog.show();
            }

            @Override
            protected void onPostExecute(List<News> listNews) {
                super.onPostExecute(listNews);
                if (listNews!=null){
                    adapter = new NewsAdapter(activity,listNews);
                    lvNews.setAdapter(adapter);                             //setting news in ListView in xml file.
                   // adapter.setItemList(listNews);                        //doubling set data
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        }
    /*  public void tvClicked(View view){
        Toast.makeText(this,"akhil",Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        ]
        */

}
