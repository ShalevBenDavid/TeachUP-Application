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
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.StudyMaterialViewHolder> {
    private final Context context;
    private List<StudyMaterialModel> studyMaterials = new ArrayList<>();

    public StudyMaterialAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_recycle_item, parent, false);
        return new StudyMaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyMaterialViewHolder holder, int position) {
        Log.e("StudyMaterial", studyMaterials.get(position).getTitle());
        holder.bindStudyMaterial(studyMaterials.get(position));
        holder.itemView.setOnClickListener(v -> {
            CustomUtils.showToast(context, "Download Started");
            startDownload(studyMaterials.get(position).getFileUrl(), studyMaterials.get(position).getTitle(), context);
        });



    }

    @Override
    public int getItemCount() {
        return studyMaterials.size();
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

    public void setStudyMaterials(List<StudyMaterialModel> studyMaterials) {
        this.studyMaterials = studyMaterials;
        notifyDataSetChanged();
    }

    public static class StudyMaterialViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fileType;
        private final TextView fileTitle;
        private final TextView fileDescription;
        private final ImageView deleteMaterial;

        public StudyMaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            fileType = itemView.findViewById(R.id.materialType);
            fileTitle = itemView.findViewById(R.id.materialTitle);
            fileDescription = itemView.findViewById(R.id.materialDescription);
            deleteMaterial = itemView.findViewById(R.id.deleteMaterial);
        }

        public void bindStudyMaterial(StudyMaterialModel material) {
            fileTitle.setText(material.getTitle());
            fileDescription.setText(material.getDescription());
            fileType.setImageResource(getTypeLogo(material.getFileType()));

            if (UserManager.getInstance().getUserType().equals(Constants.TYPE_STUDENT)) {
                deleteMaterial.setVisibility(View.GONE);
            }
        }

        private int getTypeLogo(String fileType) {
            switch (fileType) {
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
