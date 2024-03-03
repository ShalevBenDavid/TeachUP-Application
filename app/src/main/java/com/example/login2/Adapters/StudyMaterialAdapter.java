package com.example.login2.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Repositories.StudyMaterialRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.ViewModels.StudyMaterialViewModel;
import com.example.login2.databinding.MaterialRecycleItemBinding;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.StudyMaterialViewHolder> {
    private final Context context;
    private List<StudyMaterialModel> studyMaterials = new ArrayList<>();
    private final StudyMaterialViewModel viewModel;

    public StudyMaterialAdapter(Context context, StudyMaterialViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialRecycleItemBinding binding = MaterialRecycleItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new StudyMaterialViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyMaterialViewHolder holder, int position) {

        holder.bindStudyMaterial(studyMaterials.get(position));
        holder.itemView.setOnClickListener(v -> {
            CustomUtils.showToast(context, "Download Started");
            startDownload(studyMaterials
                    .get(position).getFileUrl(), studyMaterials.get(holder.getAbsoluteAdapterPosition())
                    .getTitle(), context);
        });

        holder.binding.deleteMaterial.setOnClickListener(v -> {
            int currentPosition = holder.getAbsoluteAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                String materialId = studyMaterials.get(currentPosition).getId();
                viewModel.deleteStudyMaterial(materialId, new StudyMaterialRepository.FirestoreCallback() {
                    @Override
                    public void onSuccess() {
                        notifyItemRemoved(currentPosition);
                        CustomUtils.showToast(context, "Deleted");
                    }

                    @Override
                    public void onError(String error) {
                        CustomUtils.showToast(context, error);
                    }
                });
            }
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
        private final MaterialRecycleItemBinding binding;

        public StudyMaterialViewHolder(MaterialRecycleItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindStudyMaterial(StudyMaterialModel material) {
            binding.materialTitle.setText(material.getTitle());
            binding.materialDescription.setText(material.getDescription());
            binding.materialType.setImageResource(getTypeLogo(material.getFileType()));

            if (UserManager.getInstance().getUserType().equals(Constants.TYPE_STUDENT)) {
                binding.deleteMaterial.setVisibility(View.GONE);
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
