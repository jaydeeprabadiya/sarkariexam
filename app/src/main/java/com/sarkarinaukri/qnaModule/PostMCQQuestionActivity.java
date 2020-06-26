package com.sarkarinaukri.qnaModule;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.asynkTask.AsyncTaskCallback;
import com.sarkarinaukri.helperClass.AdHandler;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.ImageCompression;
import com.sarkarinaukri.helperClass.ImageFilePath;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.QuestionData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.adapter.CorrectAnswerListAdapter;
import com.sarkarinaukri.qnaModule.adapter.OptionListAdapter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PostMCQQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private TextView title;
    private LinearLayout llApplyFilter;
    private ImageView toolBarImageView;
    private TextView tvToolBarTitle;
    private ImageView ivImageView;
    private EditText etQuestion;
    private ImageView ivQuestionImage;
    private ContentValues values;
    private Uri imageUri;
    private LinearLayout llUploadFromCamera;
    private LinearLayout llUploadFromGallery;
    private String questionImageFile;
    private ImageButton ibAdd;
    private LinearLayout llAdd;
    private LinearLayout llAddLayout;
    private int labelTag = 0;
    private RecyclerView recyclerViewOptions;
    private RecyclerView recyclerViewCorrectAnswer;
    private List<String> optionList = new ArrayList<>();
    private List<String> correctAnswerList = new ArrayList<>();
    private CorrectAnswerListAdapter correctAnswerListAdapter;
    private OptionListAdapter optionListAdapter;
    private int correctAnswerPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_mcq_question);
        AdHandler.showIfLoaded();
        AdHandler.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AdHandler.init(PostMCQQuestionActivity.this);
            }
        });

        init();
    }

    private void init() {
        optionList.add("A");
        optionList.add("B");
        correctAnswerList.add("A");
        correctAnswerList.add("B");
        correctAnswerList.add("?");

        labelTag = correctAnswerList.size();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        title.setText("Ask Multi Choice Question");
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        ivImageView = findViewById(R.id.ivImageView);

        llApplyFilter = findViewById(R.id.llApplyFilter);
        toolBarImageView = findViewById(R.id.toolBarImageView);
        tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        toolBarImageView.setImageResource(R.drawable.apply_icon);
        tvToolBarTitle.setText("Submit");

        etQuestion = findViewById(R.id.etQuestion);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);

        llUploadFromCamera = findViewById(R.id.llUploadFromCamera);
        llUploadFromGallery = findViewById(R.id.llUploadFromGallery);

        llAdd = findViewById(R.id.llAdd);
        ibAdd = findViewById(R.id.ibAdd);
        llAddLayout = findViewById(R.id.llAddLayout);

        recyclerViewOptions = findViewById(R.id.recyclerViewOptions);
        recyclerViewCorrectAnswer = findViewById(R.id.recyclerViewCorrectAnswer);


        optionListAdapter();
        setCorrectAnswerListInAdapter();

        clickListener();
    }

    private void clickListener() {
        ivImageView.setOnClickListener(this);
        llUploadFromCamera.setOnClickListener(this);
        llUploadFromGallery.setOnClickListener(this);
        llApplyFilter.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        ibAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llUploadFromGallery:
                int permissionCheck = ContextCompat.checkSelfPermission(PostMCQQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostMCQQuestionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
                break;
            case R.id.llUploadFromCamera:
                int permissionCheck1 = ContextCompat.checkSelfPermission(PostMCQQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostMCQQuestionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.llApplyFilter:
                final String question = etQuestion.getText().toString();
                if (TextUtils.isEmpty(question) && questionImageFile == null) {
                    HelperClass.showSnackBar(coordinatorLayout, "Type your question or upload image...");
                    return;
                } else {
                    List<String> newOptionList = new ArrayList<>();
                    for (int i = 0; i < optionList.size(); i++) {
                        View view = recyclerViewOptions.getChildAt(i);
                        EditText nameEditText = (EditText) view.findViewById(R.id.editText);
                        String name = nameEditText.getText().toString();
                        if (!TextUtils.isEmpty(name)) {
                            newOptionList.add(name);
                        }
                    }

                    String correctAnswer = "";
                    if (correctAnswerPosition != -1) {
                        try {
                            correctAnswer = newOptionList.get(correctAnswerPosition);
                        } catch (IndexOutOfBoundsException e) {
                            correctAnswer = "";
                        }
                    }

                    JsonArray jsonOptionArray = null;
                    if (optionList != null) {
                        jsonOptionArray = new JsonArray();
                        for (int i = 0; i < optionList.size(); i++) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("option", optionList.get(i));
                            jsonOptionArray.add(jsonObject);
                        }
                    }


                    uploadQuestion(question, questionImageFile, jsonOptionArray, correctAnswer);
                }
                break;
            case R.id.ivImageView:
                finish();
                break;
            case R.id.llAdd:
            case R.id.ibAdd:
                if (labelTag == 3) {
                    optionList.add("C");
                    correctAnswerList.add(2, "C");
                } else if (labelTag == 4) {
                    optionList.add("D");
                    correctAnswerList.add(3, "D");
                } else if (labelTag == 5) {
                    optionList.add("E");
                    correctAnswerList.add(4, "E");
                }
                labelTag = correctAnswerList.size();
                optionListAdapter.notifyDataSetChanged();
                correctAnswerListAdapter.notifyDataSetChanged();

                if (labelTag == 6) {
                    llAdd.setVisibility(View.GONE);
                } else {
                    llAdd.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                String imagePath = ImageFilePath.getPath(PostMCQQuestionActivity.this, imageUri);
                compressImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String imagePath = ImageFilePath.getPath(PostMCQQuestionActivity.this, data.getData());
            compressImage(imagePath);
        }
    }

    private void compressImage(final String imagePath) {
        ImageCompression imageCompression = new ImageCompression(PostMCQQuestionActivity.this, new AsyncTaskCallback<String>() {
            @Override
            public void execute(String imageFile) {
                questionImageFile = imageFile;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap localBitmap = BitmapFactory.decodeFile(imageFile, options);
                ivQuestionImage.setImageBitmap(localBitmap);
            }
        });
        imageCompression.execute(imagePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
                break;
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                break;
            default:
                break;
        }
    }

    private void optionListAdapter() {
        recyclerViewOptions.setVisibility(View.VISIBLE);
        recyclerViewOptions.setNestedScrollingEnabled(false);
        recyclerViewOptions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostMCQQuestionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOptions.setLayoutManager(layoutManager);
        optionListAdapter = new OptionListAdapter(this, optionList, new OptionListAdapter.ItemListener() {
            @Override
            public void onDeleteView(int position) {
                correctAnswerList.remove(correctAnswerList.size() - 2);
                correctAnswerListAdapter.notifyDataSetChanged();
                labelTag = correctAnswerList.size();
                if (labelTag == 6) {
                    llAdd.setVisibility(View.GONE);
                } else {
                    llAdd.setVisibility(View.VISIBLE);
                }

            }
        });
        recyclerViewOptions.setAdapter(optionListAdapter);
        recyclerViewOptions.setItemViewCacheSize(optionList.size());

    }

    private void setCorrectAnswerListInAdapter() {
        recyclerViewCorrectAnswer.setVisibility(View.VISIBLE);
        recyclerViewCorrectAnswer.setNestedScrollingEnabled(false);
        recyclerViewCorrectAnswer.setHasFixedSize(true);
        recyclerViewCorrectAnswer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        correctAnswerListAdapter = new CorrectAnswerListAdapter(this, correctAnswerList, new CorrectAnswerListAdapter.ItemListener() {
            @Override
            public void onItemClick(int position) {
                correctAnswerPosition = position;
            }
        });
        recyclerViewCorrectAnswer.setAdapter(correctAnswerListAdapter);
    }

    private void uploadQuestion(final String question, final String questionImageFile, final JsonArray jsonOptionArray, final String correctAnswer) {
        final ProgressDialog loading = ProgressDialog.show(PostMCQQuestionActivity.this, "Uploading question...", "Please wait...", false, false);

        MultipartBody.Part body = null;
        if (!TextUtils.isEmpty(questionImageFile)) {
            File file = new File(questionImageFile);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("questionImage", file.getName(), reqFile);
        }
        RequestBody questionBody = RequestBody.create(MediaType.parse("text/plain"), question);
        RequestBody categoriesIdBody = RequestBody.create(MediaType.parse("text/plain"), "25");

        RequestBody correctAnswerBody = null;
        if (!TextUtils.isEmpty(correctAnswer)) {
            correctAnswerBody = RequestBody.create(MediaType.parse("text/plain"), correctAnswer);
        }
        RequestBody optionListBody = null;
        if (jsonOptionArray != null) {
            if (jsonOptionArray.size() > 0) {
                optionListBody = RequestBody.create(MediaType.parse("text/plain"), jsonOptionArray.toString());
            }
        }


        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.uploadQuestion(body, PreferenceHelper.getToken(PostMCQQuestionActivity.this), questionBody, optionListBody, correctAnswerBody, categoriesIdBody);
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, retrofit2.Response<ResultEntity> response) {
                llApplyFilter.setClickable(true);
                llApplyFilter.setFocusable(true);
                loading.dismiss();
                ResultEntity resultEntity = response.body();

                if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.QUESTION, resultEntity.getQuestionData().get(0));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadQuestion(question, questionImageFile, jsonOptionArray, correctAnswer);
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                llApplyFilter.setClickable(true);
                llApplyFilter.setFocusable(true);
                loading.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}


