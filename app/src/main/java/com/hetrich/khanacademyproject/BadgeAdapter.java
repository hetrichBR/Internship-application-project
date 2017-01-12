package com.hetrich.khanacademyproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Data.BadgeInfo;


/**
 * Created by Bradley on 12/15/2015.
 * BaseAdapter code taken and modified from WINGNITY (http://www.wingnity.com/)
 */
public class BadgeAdapter extends ArrayAdapter<BadgeInfo> {

    //creating params constructir needs
    ArrayList<BadgeInfo> ArrayListBadgeInfo;

    int Resource, Resource2, index;
    MainActivity context;
    LayoutInflater inflater;



    public BadgeAdapter(MainActivity context, int resource, ArrayList<BadgeInfo> objects){
        super(context, resource, objects);

        ArrayListBadgeInfo = objects;
        Resource = resource;
        //global context = local context
        this.context = context;

        //define LayoutInflater
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //inflate row.xml layout

        final ViewHolder holder;

        //if null first list item is being created only create once
        if (convertView == null) {
            convertView = inflater.inflate(Resource, null);
            holder = new ViewHolder();

            //referencing views in their layouts
            holder.ivBadge = (ImageView) convertView.findViewById(R.id.IVBadge);
            holder.shortDescription = (TextView) convertView.findViewById(R.id.TVshortdescrption);
            holder.badgeName = (TextView) convertView.findViewById(R.id.TVBadgeName);
            holder.count = (TextView) convertView.findViewById(R.id.TVCount);



            //save rhe view with a tag to be recycled
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //set the data to the views each badge has a position in the arraylist
        //use third party library picasso to turn url into image
        String imageSmallUrl = ArrayListBadgeInfo.get(position).getSmallImageUrl();
        Picasso.with(context).load(imageSmallUrl).into(holder.ivBadge);
        holder.badgeName.setText(ArrayListBadgeInfo.get(position).getName());
        holder.shortDescription.setText(ArrayListBadgeInfo.get(position).getShortDescription());

        //keep a count of the cards for later identification
        holder.count.setText(ArrayListBadgeInfo.get(position).getBadgeCount()+ "");
        //if scrolling down then increment





        //if a view is clicked
        convertView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //convert text to string then to int
                String strCount = holder.count.getText().toString();
                int clickedPosition = Integer.parseInt(strCount);
                Dialog badgeDetail = new Dialog(context);
                Log.d("COUNT:", clickedPosition + "");
                //title
                badgeDetail.setTitle("Badge Info");
                badgeDetail.setContentView(R.layout.detail);


                TextView badgeNameDetail = (TextView) badgeDetail.findViewById(R.id.TVDetailName);
                TextView longDescription = (TextView) badgeDetail.findViewById(R.id.TVdesdetail);
                ImageView ivBadgeLarge = (ImageView) badgeDetail.findViewById(R.id.IVdetail);

                //getting the tag set to each inflated view to get corresponding name image etc
                badgeNameDetail.setText(ArrayListBadgeInfo.get(clickedPosition).getName());
                longDescription.setText(ArrayListBadgeInfo.get(clickedPosition).getShortDescription());
                String imageSmallUrl = ArrayListBadgeInfo.get(clickedPosition).getSmallImageUrl();
                Picasso.with(context).load(imageSmallUrl).into(ivBadgeLarge);

                badgeDetail.show();
            }
        });




        //convertView.setOnClickListener(null);


        return convertView;

    }

    //place holder for all the views defined in row.xml
    public class ViewHolder{
        ImageView ivBadge, ivBadgeLarge;
        TextView shortDescription, longDescription, badgeName, badgeNameDetail, count;

    }





}
