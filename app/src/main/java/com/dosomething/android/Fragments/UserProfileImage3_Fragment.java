package com.dosomething.android.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.dosomething.android.CommonClasses.Jsonfunctions;
import com.dosomething.android.CommonClasses.NetworkCheck;
import com.dosomething.android.CommonClasses.SharedPrefrences;
import com.dosomething.android.CropOption;
import com.dosomething.android.CropOptionAdapter;
import com.dosomething.android.DoSomethingStatus;
import com.dosomething.android.MyApplication;
import com.dosomething.android.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UserProfileImage3_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileImage3_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMG = 3;
    private static final String TAG_SESSIONID = "sessionid";
    private static final String TAG_FIELD = "field";
    private String sessionid;
    private String field;
    private Jsonfunctions jsonfunctions;
    private String json_string;
    private JSONObject json_object;
    private JSONObject json_content;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView user_profile_imageview_three;
    ImageView user_image_three_imageview_camera_inside;
    ImageView user_image_three_imageview_camera_outside;
    SharedPrefrences sharedPrefrences;
    private Uri mImageCaptureUri;
    private ImageView mImageView;

    private static final int PICK_FROM_CAMERA = 6;
    private static final int CROP_FROM_CAMERA = 9;
    private static final int PICK_FROM_FILE = 3;
    private User_profile_image_viewpager_dots_three profile_image_viewpager_dots_three;
    private AQuery aQuery;
    private Dialog dialog;
    private TextView dosomething_alert_pick_image_textview;
    private TextView dosomething_alert_pick_image_textview_gallery;
    private TextView dosomething_alert_pick_image_textview_camera;
    TextView dosomething_alert_pick_image_textview_remove;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileImage3_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileImage3_Fragment newInstance(String param1, String param2) {
        UserProfileImage3_Fragment fragment = new UserProfileImage3_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfileImage3_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_image3, container, false);
        sharedPrefrences = new SharedPrefrences();
        aQuery = new AQuery(getActivity());
        jsonfunctions = new Jsonfunctions(getActivity());
        sessionid = sharedPrefrences.getSessionid(getActivity());
        field = "image3";
        user_profile_imageview_three = (ImageView) view.findViewById(R.id.user_profile_imageview_three);
        user_image_three_imageview_camera_outside = (ImageView) view.findViewById(R.id.user_image_three_imageview_camera_outside);
        user_image_three_imageview_camera_inside = (ImageView) view.findViewById(R.id.user_image_three_imageview_camera_inside);


        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosomething_alert_imagepickandremove);
//        dosomething_alert_pick_image_textview = (TextView) dialog.findViewById(R.id.dosomething_alert_pick_image_textview);
        dosomething_alert_pick_image_textview_gallery = (TextView) dialog.findViewById(R.id.dosomething_alert_pick_image_textview_gallery);
        dosomething_alert_pick_image_textview_camera = (TextView) dialog.findViewById(R.id.dosomething_alert_pick_image_textview_camera);

        dosomething_alert_pick_image_textview_remove = (TextView) dialog.findViewById(R.id.dosomething_alert_pick_image_textview_remove);

        ((MyApplication) getActivity().getApplication()).setUserProfileImage3_fragment(this);
        if (!sharedPrefrences.getProfilePicture2(getActivity()).equals("")) {
//            LoadImageFromURL loadImage = new LoadImageFromURL();
//            loadImage.execute();

            aQuery.id(user_profile_imageview_three).image(sharedPrefrences.getProfilePicture2(getActivity()), true, true, 0, 0, new BitmapAjaxCallback() {
                @Override
                public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    if (status.getCode() == 200) {
                        Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
                        Bitmap conv_bm = getCroppedBitmap(bm);
                        iv.setImageBitmap(conv_bm);
                        user_image_three_imageview_camera_inside.setVisibility(View.GONE);
                        user_image_three_imageview_camera_outside.setVisibility(View.GONE);

                    }
                }
            });
        } else {
            if (!sharedPrefrences.getProfileImageBitmap3(getActivity()).equals(""))
            {
                byte[] b = Base64.decode(sharedPrefrences.getProfileImageBitmap3(getActivity()), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                Bitmap conv_bm = getCroppedBitmap(bitmap);
                user_profile_imageview_three.setImageBitmap(conv_bm);
            }else
            {
                dosomething_alert_pick_image_textview_remove.setVisibility(View.GONE);
            }

        }
        user_image_three_imageview_camera_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionAlert();
                /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Image");
                builder.setMessage("Do you want to go with?");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                        cameraIntent.putExtra("crop", "true");
////                        cameraIntent.putExtra("outputX", 900);
////                        cameraIntent.putExtra("outputY", 900);
////                        cameraIntent.putExtra("aspectX", 4);
////                        cameraIntent.putExtra("aspectY", 16);
////                        cameraIntent.putExtra("scale", true);
//                        startActivityForResult(cameraIntent, 5);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                "crop.jpg"));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        getParentFragment().startActivityForResult(intent, PICK_FROM_CAMERA);
                        dialog.dismiss();


                    }
                });

                builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Imageremoval().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getParentFragment().startActivityForResult(i, PICK_FROM_FILE);

                        *//*Intent intent = new Intent(
                                Intent.ACTION_GET_CONTENT,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image*//**//*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Choose an image"),
                                PICK_FROM_FILE);*//*
                        dialog.dismiss();


                    }
                });
                builder.show();*/
            }
        });

        user_profile_imageview_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSelectionAlert();
                /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Image");
                builder.setMessage("Do you want to go with?");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                        cameraIntent.putExtra("crop", "true");
////                        cameraIntent.putExtra("outputX", 900);
////                        cameraIntent.putExtra("outputY", 900);
////                        cameraIntent.putExtra("aspectX", 4);
////                        cameraIntent.putExtra("aspectY", 16);
////                        cameraIntent.putExtra("scale", true);
//                        startActivityForResult(cameraIntent, 5);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                "crop.jpg"));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        getParentFragment().startActivityForResult(intent, PICK_FROM_CAMERA);
                        dialog.dismiss();


                    }
                });

                builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Imageremoval().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getParentFragment().startActivityForResult(i, PICK_FROM_FILE);

                        *//*Intent intent = new Intent(
                                Intent.ACTION_GET_CONTENT,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image*//**//*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Choose an image"),
                                PICK_FROM_FILE);*//*
                        dialog.dismiss();


                    }
                });
                builder.show();*/


            }
        });
        return view;
    }


    public void OnClickListener() {
        user_profile_imageview_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (((MyApplication) getActivity().getApplication()).getmDoSomething_Friends_profile_fragment() != null) {
                        ((MyApplication) getActivity().getApplication()).getmDoSomething_Friends_profile_fragment().showImage(sharedPrefrences.getProfilePicture2(getActivity()));
                    }
                }

            }
        });
    }


    public void showImageSelectionAlert() {
        dosomething_alert_pick_image_textview_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image*//*");



                getParentFragment().  startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        PICK_FROM_FILE);*/


                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getParentFragment().startActivityForResult(i, PICK_FROM_FILE);

                dialog.dismiss();
            }
        });


        dosomething_alert_pick_image_textview_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPrefrences.getProfilePicture2(getActivity()).equals(""))

                {
                    new Imageremoval().execute();
                }else
                {
                    sharedPrefrences.setProfilePicture2(getActivity(),"");
                    sharedPrefrences.setUpdateProfilePicture2(getActivity(),"");
                    sharedPrefrences.setProfileImageBitmap3(getActivity(),"");
//                    ((MyApplication)getActivity().getApplication()).getFragmentProfile().addimageSlide();
                }

                dialog.dismiss();
            }
        });

        dosomething_alert_pick_image_textview_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "fragmentcrop3.jpg"));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                getParentFragment().startActivityForResult(intent, PICK_FROM_CAMERA);


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public interface User_profile_image_viewpager_dots_three {
        public void user_profile_Image_three();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            ContentResolver sg = getActivity().getContentResolver();
//            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK && null != data) {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = sg.query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
//                Log.d("getImageUri", getPath(getImageUri(getActivity(), bm), getActivity()));
//                sharedPrefrences.setUpdateProfilePicture2(getActivity(), getPath(getImageUri(getActivity(), bm), getActivity()));
//                Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
//                Bitmap conv_bm = getRoundedRectanguleBitmap(resized, 10);
//                user_profile_imageview_three.setImageBitmap(conv_bm);
//                sharedPrefrences.setBooleam(getActivity(),"true");
//                user_image_three_imageview_camera_inside.setVisibility(View.VISIBLE);
//                user_image_three_imageview_camera_outside.setVisibility(View.GONE);
//
//
//            } else {
//                final Bitmap photo = (Bitmap) data.getExtras().get("data");
//                Log.d("getImageUri", getPath(getImageUri(getActivity(), photo), getActivity()));
//                sharedPrefrences.setUpdateProfilePicture2(getActivity(), getPath(getImageUri(getActivity(), photo), getActivity()));
//                Bitmap resized = Bitmap.createScaledBitmap(photo, 200, 200, true);
//                Bitmap conv_bm = getRoundedRectanguleBitmap(resized, 10);
//                user_profile_imageview_three.setImageBitmap(conv_bm);
//                sharedPrefrences.setBooleam(getActivity(),"true");
//                user_image_three_imageview_camera_inside.setVisibility(View.VISIBLE);
//                user_image_three_imageview_camera_outside.setVisibility(View.GONE);
//            }
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }


        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                cropCapturedImage();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                cropCapturedImage();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();
                try {
                    if (getActivity() != null) {
                        if (extras != null) {
                            Bitmap photo = extras.getParcelable("data");
                            Bitmap resized = Bitmap.createScaledBitmap(photo, 540, 540, true);

                            Bitmap conv_bm = getCroppedBitmap(photo);

                            user_profile_imageview_three.setImageBitmap(conv_bm);
                            dosomething_alert_pick_image_textview_remove.setVisibility(View.VISIBLE);
                            user_image_three_imageview_camera_inside.setVisibility(View.GONE);
                            user_image_three_imageview_camera_outside.setVisibility(View.GONE);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            sharedPrefrences.setProfileImageBitmap1(getActivity(), encodedImage);
                            sharedPrefrences.setFBProfilePicture(getActivity(), "");
                            File f = new File(Environment.getExternalStorageDirectory(),
                                    getParentFragment().getResources().getString(R.string.app_name) + "fragment_profie_image2" + ".jpg");
                            f.createNewFile();
                            Log.d("filename", String.valueOf(f));
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(b);
                            fos.flush();
                            fos.close();
                            sharedPrefrences.setUpdateProfilePicture2(getActivity(), String.valueOf(f));
                            sharedPrefrences.setBooleam(getActivity(), "true");


                        }

                        File f = new File(mImageCaptureUri.getPath());

                        if (f.exists()) f.delete();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

        }


    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getHeight() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public void cropCapturedImage() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(mImageCaptureUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("return-data", true);
//            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            getParentFragment().startActivityForResult(cropIntent, CROP_FROM_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 1080);
            intent.putExtra("outputY", 1080);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                getParentFragment().startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    if (!res.activityInfo.packageName.equals("com.google.android.apps.photos")) {
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        getParentFragment().startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }


    private String getPath(Uri uri, Activity activity) {
        Cursor cursor = null;
        int column_index = 0;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Log.d("projection====", "" + projection);
            cursor = activity.managedQuery(uri, projection, null, null, null);
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);

        return Uri.parse(path);
    }

    public static Bitmap getRoundedRectanguleBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(100, 100, 92, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException | OutOfMemoryError ignored) {
        }
        return result;
    }

    public class LoadImageFromURL extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {

                if (getActivity() != null) {
                    URL url = new URL(sharedPrefrences.getProfilePicture2(getActivity()));
                    Log.d("image1", sharedPrefrences.getProfilePicture2(getActivity()));

                    InputStream is = url.openConnection().getInputStream();
                    Bitmap bitMap = BitmapFactory.decodeStream(is);
                    Bitmap resized = Bitmap.createScaledBitmap(bitMap, 200, 200, true);
                    Bitmap conv_bm = getCroppedBitmap(resized);
                    return conv_bm;
                }


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            user_profile_imageview_three.setImageBitmap(result);
            user_image_three_imageview_camera_inside.setVisibility(View.VISIBLE);
            user_image_three_imageview_camera_outside.setVisibility(View.GONE);
//            profile_image_viewpager_dots_one.profile_Image_one();
        }

    }


    private class Imageremoval extends AsyncTask<Void, Void, Boolean> {

        private Exception error;
        String deleteimageApi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getActivity() != null) {
                deleteimageApi = getActivity().getResources().getString(R.string.dosomething_apilink_string_deleteimage);
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, Object> paramsCheck = new HashMap<>();
            paramsCheck.put(TAG_SESSIONID, sessionid);
            paramsCheck.put(TAG_FIELD, field);
            json_string = jsonfunctions.postToURL(deleteimageApi, paramsCheck);
            Log.v("jason url=======>", String.valueOf(paramsCheck));
            try {
                json_object = new JSONObject(json_string);
                json_content = json_object.getJSONObject("deleteprofileimage");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                error = e;
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if (aBoolean) {

                    if (NetworkCheck.isWifiAvailable(getActivity()) || NetworkCheck.isNetworkAvailable(getActivity())) {
                        try {
                            if (json_object.has("deleteprofileimage")) {
                                if (json_content.getString("status").equalsIgnoreCase("success")) {
                                    sharedPrefrences.setProfilePicture2(getActivity(), "");
                                    ((DoSomethingStatus) getActivity()).profileRefresh(true);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (error != null) {

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
