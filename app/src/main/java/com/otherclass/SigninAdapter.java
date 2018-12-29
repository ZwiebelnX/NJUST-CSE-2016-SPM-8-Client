package com.otherclass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SigninAdapter extends BaseAdapter {
    private List<Student> datas;
    private LayoutInflater inflater;
    public SigninAdapter(Context context, List<Student>datas){
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
    public View getView(int position, View convertView, ViewGroup parent){
        signinViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new signinViewHolder();
            convertView=inflater.inflate(android.R.layout.simple_list_item_2,parent,false);
            viewHolder.text1=convertView.findViewById(android.R.id.text1);
            viewHolder.text2=convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(signinViewHolder) convertView.getTag();

        }
        viewHolder.text1.setText(datas.get(position).getStudentName()+"("+datas.get(position).getStudentID()+")");
        if(datas.get(position).getSignResult().equals("YES")){
            viewHolder.text2.setText("已签到");
            viewHolder.text2.setTextColor(Color.parseColor("#A0A0A0"));
        }
        else{
            viewHolder.text2.setText("未签到");
            viewHolder.text2.setTextColor(Color.parseColor("#FF6666"));
        }

        return convertView;
    }
    private class signinViewHolder{
        private TextView text1;
        private TextView text2;
    }
}
