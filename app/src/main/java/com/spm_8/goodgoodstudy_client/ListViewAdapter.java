package com.spm_8.goodgoodstudy_client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
//这个类是ListView的小弟
public class ListViewAdapter extends BaseAdapter {
    private List<String> datas;
    private LayoutInflater inflater;
    public ListViewAdapter(Context context, List<String>datas){
        inflater=LayoutInflater.from(context);
        this.datas=datas;
    }
    public Object getItem(int position){
        return datas.get(position);
    }
    public int getCount(){
        return datas.size();
    }
    public long getItemId(int position){
        return position;
    }
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            viewHolder.text1=convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();

        }
        viewHolder.text1.setText(datas.get(position));
        return convertView;
    }
   private class ViewHolder{
        private TextView text1;
   }


}
