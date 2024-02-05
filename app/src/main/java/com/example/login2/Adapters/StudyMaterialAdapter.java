package com.example.login2.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Utils.CustomUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudyMaterialAdapter extends FirestoreRecyclerAdapter<StudyMaterialModel, StudyMaterialAdapter.StudyMaterialViewHolder> {
    private Context context;

    public StudyMaterialAdapter(@NonNull FirestoreRecyclerOptions<StudyMaterialModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudyMaterialViewHolder holder, int position, @NonNull StudyMaterialModel model) {
        holder.bindStudyMaterial(model);
        holder.itemView.setOnClickListener(v -> {
            CustomUtils.showToast(context, "Download Started");
            startDownload(model.getFileUrl(), model.getTitle(), context);
        });

    }

    @NonNull
    @Override
    public StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_recycle_item,parent,false);
        return new StudyMaterialViewHolder(view);
    }

    private void startDownload(String fileUrl, String title, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle(title)
                .setDescription("Downloading " + title)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }


    public static class StudyMaterialViewHolder extends RecyclerView.ViewHolder {
        private ImageView fileType;
        private TextView fileTitle;
        private TextView fileDescription;
        private String downloadUrl;


        public StudyMaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.materialType);
            fileTitle = itemView.findViewById(R.id.materialTitle);
            fileDescription = itemView.findViewById(R.id.materialDescription);
        }

        public void bindStudyMaterial(StudyMaterialModel material){
            fileTitle.setText(material.getTitle());
            fileDescription.setText(material.getDescription());
            downloadUrl = material.getFileUrl();

            fileType.setImageResource(getTypeLogo(material.getFileType()));
        }

        private int getTypeLogo(String fileType) {
            switch(fileType){
                case "document":
                    return R.drawable.document_material;
                case "image":
                    return R.drawable.photo_material;
                case "video":
                    return R.drawable.video_material;
                default:
                    return R.drawable.unknown_material;
            }
        }
    }
}
