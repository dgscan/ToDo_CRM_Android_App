package com.example.user.todocrmappmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<TaskList> taskLists;
    private Context context;
    private RecyclerView recyclerView;
    MySQLiteDatabase db;


    public ItemAdapter(List<TaskList> myDataSet, Context context, RecyclerView recyclerView){
        taskLists=myDataSet;
        this.context=context;
        this.recyclerView=recyclerView;
    }

    /* CREATE NEW VIEWS (INVOKED BY LAYOUT MANAGER)
    * */
    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        /*CREATE NEW VIEW*/
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.todo_task_cardview, parent, false);

        /*SET THE VIEW'S LAYOUT PARAMETERS*/
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final TaskList taskList = taskLists.get(position);

        holder.companyName.setText(taskList.getCompanyname()+"");
        holder.taskTxt.setText(taskList.getTaskTxt()+"");
        holder.startDateTxt.setText(taskList.getStartDate()+"");
        holder.uptoDateTxt.setText(taskList.getUptoDate()+"");
        holder.taskid.setText(taskList.getTaskid());

        /*TASK IDSINI AL VE DB'DE GÜNCELLEME YAP
        * */
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Butona basıldı"+taskList.getTaskid(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context,UpdateTask.class);
                intent.putExtra("taskid",taskList.getTaskid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    public void Add(int position, TaskList taskList){

        taskLists.add(position,taskList);
        notifyItemInserted(position);

    }

    public void Update(String taskID){

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView companyName;
        public TextView taskTxt;
        public TextView startDateTxt;
        public TextView uptoDateTxt;
        public TextView taskid;

        public Button updateBtn;

        public View layout;

        public ViewHolder(View v){
            super(v);

            layout = v;

            companyName = v.findViewById(R.id.customerNameTxt);
            taskTxt = v.findViewById(R.id.taskTxt);
            startDateTxt = v.findViewById(R.id.startDateTxt);
            uptoDateTxt = v.findViewById(R.id.uptoDateTxt);
            updateBtn = v.findViewById(R.id.updateBtn);
            taskid = v.findViewById(R.id.taskid);


        }
    }
}
