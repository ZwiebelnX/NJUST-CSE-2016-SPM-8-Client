package com.otherclass;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
public class ListViewAdapter_cs extends BaseAdapter{
    private List<Check_Student> datas;
    private LayoutInflater inflater;
    public ListViewAdapter_cs(Context context, List<Check_Student>datas){
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
        ListViewAdapter_cs.csViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ListViewAdapter_cs.csViewHolder();
            convertView=inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            viewHolder.text1=convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ListViewAdapter_cs.csViewHolder) convertView.getTag();

        }
        viewHolder.text1.setText(datas.get(position).getStudentName()+"( 学号："+datas.get(position).getStudentID()+" ) " + " 状态：(" + datas.get(position).getCs_state()+" )");
        return convertView;
    }
    private class csViewHolder{
        private TextView text1;
    }

}
