package training.sati.com.newshunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by acer on 10/19/2016.
 */

public class NewsAdapter extends BaseAdapter {

    List<News> li;
    ListView list;
    private Context context;
    List<News> liSearchResults;


    @Override
    public int getCount() {
        if (li == null){
        return 0;}
        return li.size();
    }

    @Override
    public Object getItem(int position) {

        return li.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final News news = li.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_news,parent,false);
        }
        TextView tvHead = (TextView) convertView.findViewById(R.id.tv_ll_head);
        TextView tvlink = (TextView) convertView.findViewById(R.id.tv_ll_link);
        tvHead.setText(news.getHead());
        tvlink.setText(news.getSummary());

        return convertView;
    }


    public NewsAdapter(Context context, List<News> li){
        this.context = context;
        this.li=li;
        liSearchResults = new ArrayList<News>();
        liSearchResults.addAll(li);
    }
    public void setItemList(List<News> li){
        this.li=li;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());

        li.clear();
        if (charText.length()==0){
            li.addAll(liSearchResults);
        }else {
            for (News news : liSearchResults){
                if (charText.length() != 0 && news.getHead().toLowerCase(Locale.getDefault()).contains(charText)){
                    li.add(news);
                }
                /* else if (charText.length() != 0 && contact.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    li.add(contact);
                }*/
            }
        }
        notifyDataSetChanged();
    }

}
