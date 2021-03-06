package sample.example.com.proxitask.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import sample.example.com.proxitask.R;
import sample.example.com.proxitask.activity.TaskItem;


public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        List<TaskItem> rowItems;

        public CustomBaseAdapter(Context context, List<TaskItem> items) {
            this.context = context;
            this.rowItems = items;
        }

        /*private view holder class*/
        private class ViewHolder {
            RoundedImageView imageView;
            TextView txtTitle;
            TextView txtDesc;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.show_created_tasks_listview, null);
                holder = new ViewHolder();
                holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
                holder.imageView = (RoundedImageView) convertView.findViewById(R.id.profile_pic);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            TaskItem rowItem = (TaskItem) getItem(position);

            holder.txtDesc.setText(rowItem.getDesc());
            holder.txtTitle.setText(rowItem.getTitle());
            //holder.imageView.setImageResource(rowItem.getImageId());

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(getItem(position));
        }
    }
