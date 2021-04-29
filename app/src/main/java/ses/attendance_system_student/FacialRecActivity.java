package ses.attendance_system_student;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.codelab.mlkit.GraphicOverlay.Graphic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FacialRecActivity extends AppCompatActivity {
    boolean recognized= false;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    FirebaseUser fuser;
    ImageView done;
    private static final String TAG = "Facial REc";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    private ImageView mImageView;
    private ImageButton cameraBtn;
    private Button mFaceButton;
    //skip facial recog
    private Button mSkip;
    DatabaseReference refrence;
    //end
    private Bitmap mSelectedImage;
//    private GraphicOverlay mGraphicOverlay;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;
    StorageReference storageReference;
    File f;
    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;
    /**
     * Dimensions of inputs.
     */
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_rec);
        done = findViewById(R.id.done);
        mImageView = findViewById(R.id.image_view);
        mSkip = findViewById(R.id.skip1);
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });


        cameraBtn = findViewById(R.id.button_text);
//        mFaceButton = findViewById(R.id.button_face);

//        mGraphicOverlay = findViewById(R.id.graphic_overlay);
//        mFaceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                onItemSelected(currentPhotoPath);
////                runFaceContourDetection();
//            }
//        });

        storageReference = FirebaseStorage.getInstance().getReference();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog alertDialog = new AlertDialog.Builder(FacialRecActivity.this).create();
//                alertDialog.setTitle("Attach Picture");
//                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "GALLERY", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
//                    }
//
//                });
//                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        askCameraPermissions();
//                    }
//
//                });
//                alertDialog.show();
//


            }

        });
//animation();

    }

    //USING CAMERA FUNCTIONS

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            Log.d("mytag", "Reached the ask camera permissions");
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
                Log.d("ya rb msh ele fe bale", "ya rb y3ne");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                f = new File(currentPhotoPath);
                mImageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);


            }

        }

//        if (requestCode == GALLERY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri contentUri = data.getData();
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
//                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                mImageView.setImageURI(contentUri);
//                uploadImageToFirebase(imageFileName, contentUri);
//
//            }
//
//        }

    }


//    private String getFileExt(Uri contentUri) {
//        ContentResolver c = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(c.getType(contentUri));
//    }


    private File createImageFile() throws IOException {
        // Create an image file name
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = fuser.getUid();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Log.d("mytag", "Reached the dispatch");

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("mytag", "Reached the file provider thing");

                Uri photoURI = FileProvider.getUriForFile(this,
                        "ses.attendance_system_student",
                        photoFile);
                Log.d("mytag", photoURI.toString());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private void uploadImageToFirebase(String name, Uri contentUri) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = fuser.getUid();
        final StorageReference image = storageReference.child("pictures/"+UID+"/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        Log.d("TAG", "onSuccess: download uri is " + image.getDownloadUrl());
                        Toast.makeText(FacialRecActivity.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();

                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        String encoded = "";
                        try {
                             encoded = URLEncoder.encode(uri.toString(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
//                            decoded = uri.toString();
                        }

                        Log.d("encoded", "onSuccess: "+ encoded);
                        RequestBody body = RequestBody.create(mediaType, "photo="+encoded);
                        refrence = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("facerec");
//                        final int[] val = {Integer.parseInt(null)};
//                        refrence.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
// val[0] = snapshot.getValue(Integer.class);
//        Log.d(TAG, "onDataChange: "+ val[0]);
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//});
                        Log.d(TAG, "onSuccess: "+ 195294);
                        Request request = new Request.Builder()
                                .url("https://luxand-cloud-face-recognition.p.rapidapi.com/photo/verify/"+195294)
                                .post(body)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("x-rapidapi-key", "9ff79279cemsh25a154e23e359f8p18046ajsn4558084a9605")
                                .addHeader("x-rapidapi-host", "luxand-cloud-face-recognition.p.rapidapi.com")
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    final String myResponse = response.body().string();
                                    Log.d("TAG", "run: "+ myResponse);
                                    if(myResponse.contains("success")){
                                    recognized = true;
                                   } animation();
//                                    } else {
//                                        Toast.makeText(FacialRecActivity.this, "Face Not Verefied, Please try again.", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            }
                        });
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FacialRecActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void animation() {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
//                done.setImageDrawable(getResources().getDrawable(R.drawable.avd_done));
        if(recognized){
done.setImageResource(R.drawable.animated_vector_check);
            ((Animatable) done.getDrawable()).start();
            new CountDownTimer(3000, 1000) {
                //
//            @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();}}.start();
        }else{
            done.setImageResource(R.drawable.animated_vector_cross);
            ((Animatable) done.getDrawable()).start();
        }
            }
        });

    }

}

// Face Detection Code
//    private void runFaceContourDetection() {
//        InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
//        FaceDetectorOptions options =
//                new FaceDetectorOptions.Builder()
//                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
//                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
//                        .build();
//
//        mFaceButton.setEnabled(false);
//        FaceDetector detector = FaceDetection.getClient(options);
//        detector.process(image)
//                .addOnSuccessListener(
//                        new OnSuccessListener<List<Face>>() {
//                            @Override
//                            public void onSuccess(List<Face> faces) {
//                                mFaceButton.setEnabled(true);
//                                processFaceContourDetectionResult(faces);
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(Exception e) {
//                                // Task failed with an exception
//                                mFaceButton.setEnabled(true);
//                                e.printStackTrace();
//                            }
//                        });
//
//    }
//
//    private void processFaceContourDetectionResult(List<Face> faces) {
//        // Task completed successfully
//        if (faces.size() == 0) {
//            showToast("No face found");
//            return;
//        }
//        mGraphicOverlay.clear();
//        for (int i = 0; i < faces.size(); ++i) {
//            Face face = faces.get(i);
//            FaceContourGraphic faceGraphic = new FaceContourGraphic(mGraphicOverlay);
//            mGraphicOverlay.add(faceGraphic);
//            faceGraphic.updateFace(face);
//        }
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    // Functions for loading images from app assets.
//
//    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
//    // landscape mode.
//    private Integer getImageMaxWidth() {
//        if (mImageMaxWidth == null) {
//            // Calculate the max width in portrait mode. This is done lazily since we need to
//            // wait for
//            // a UI layout pass to get the right values. So delay it to first time image
//            // rendering time.
//            mImageMaxWidth = mImageView.getWidth();
//        }
//
//        return mImageMaxWidth;
//    }
//
//    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
//    // landscape mode.
//    private Integer getImageMaxHeight() {
//        if (mImageMaxHeight == null) {
//            // Calculate the max width in portrait mode. This is done lazily since we need to
//            // wait for
//            // a UI layout pass to get the right values. So delay it to first time image
//            // rendering time.
//            mImageMaxHeight =
//                    mImageView.getHeight();
//        }
//
//        return mImageMaxHeight;
//    }
//
//    // Gets the targeted width / height.
//    private Pair<Integer, Integer> getTargetedWidthHeight() {
//        int targetWidth;
//        int targetHeight;
//        int maxWidthForPortraitMode = getImageMaxWidth();
//        int maxHeightForPortraitMode = getImageMaxHeight();
//        targetWidth = maxWidthForPortraitMode;
//        targetHeight = maxHeightForPortraitMode;
//        return new Pair<>(targetWidth, targetHeight);
//    }
//
//    public void onItemSelected( String pp) {
//        mGraphicOverlay.clear();
////        try (Image im = new ImageFormat(f)) {
////        }
//        mSelectedImage = BitmapFactory.decodeFile(currentPhotoPath);
////        mSelectedImage = getBitmapFromAsset(this, pp);
//        if (mSelectedImage != null) {
//            // Get the dimensions of the View
//            Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();
//
//            int targetWidth = targetedSize.first;
//            int maxHeight = targetedSize.second;
//
//            // Determine how much to scale down the image
//            float scaleFactor =
//                    Math.max(
//                            (float) mSelectedImage.getWidth() / (float) targetWidth,
//                            (float) mSelectedImage.getHeight() / (float) maxHeight);
//
//            Bitmap resizedBitmap =
//                    Bitmap.createScaledBitmap(
//                            mSelectedImage,
//                            (int) (mSelectedImage.getWidth() / scaleFactor),
//                            (int) (mSelectedImage.getHeight() / scaleFactor),
//                            true);
//
//            mImageView.setImageBitmap(resizedBitmap);
//            mSelectedImage = resizedBitmap;
//        }
//    }
//
//    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
//        AssetManager assetManager = context.getAssets();
//
//        InputStream is;
//        Bitmap bitmap = null;
//        try {
//            is = assetManager.open(filePath);
//            bitmap = BitmapFactory.decodeStream(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
