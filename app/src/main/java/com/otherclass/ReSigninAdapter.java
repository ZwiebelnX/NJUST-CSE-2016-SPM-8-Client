package com.otherclass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.R;

import java.util.List;

public class ReSigninAdapter extends BaseAdapter {
    private List<Student> datas;
    private LayoutInflater inflater;
    public ReSigninAdapter(Context context, List<Student>datas){
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
        ReSigninHolder viewHolder;
        if(convertView==null){
            viewHolder=new ReSigninHolder();
            convertView=inflater.inflate(R.layout.listview,parent,false);
            viewHolder.text1=convertView.findViewById(R.id.checkBox);
            viewHolder.text2=convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ReSigninHolder) convertView.getTag();

        }
        viewHolder.text1.setText(datas.get(position).getStudentName());


        viewHolder.text1.setChecked(datas.get(position).isIschecked());
        viewHolder.text2.setText(datas.get(position).getStudentID());


        return convertView;
    }
    private class ReSigninHolder{
        private CheckBox text1;
        private TextView text2;
    }

}
