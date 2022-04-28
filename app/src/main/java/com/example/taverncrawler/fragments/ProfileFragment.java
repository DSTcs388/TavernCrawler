package com.example.taverncrawler.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taverncrawler.R;
import com.example.taverncrawler.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * TODO:Get Images to work properly.
 */
public class ProfileFragment extends Fragment {

    TextView profileUsername;
    ImageView profileImage;
    ActivityResultLauncher<Intent> startActivityForResult;
    public static final String TAG = "ProfileFragment";
    public String photoFileName = "photo.jpg";
    private File photoFile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        profileUsername = (TextView) view.findViewById(R.id.profileUsername);
        Fragment fragment = this;
        profileUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Elements are clickable", Toast.LENGTH_SHORT).show();
            }
        });
        loadProfileInformation(profileImage);
        final CharSequence[] options = { "Choose from Gallery", "Cancel" };
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "This image is clickable!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Choose Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(options[i].equals("Choose from Gallery")) {
                            selectPhotoFromGallery(view);
                        }
                        else if(options[i].equals("Cancel")) {
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                if((data != null) && result.getResultCode() == Activity.RESULT_OK) {
                    if(data.getData() == null) {
                        Log.i(TAG, "getData() is null: " + data.toString());
                        return;
                    }
                    Uri photoUri = data.getData();
                    Bitmap selectedImage = loadFromUri(photoUri);
                    Log.i(TAG, photoFile.toString());
                    try {
                        Log.i(TAG, "try statement onActivityResult");
                        photoFile.createNewFile();
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(photoUri);
                        OutputStream outputStream = new FileOutputStream(photoFile, false);
                        byte[] buffer = new byte[1024];
                        int length;
                        while((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        inputStream.close();
                        outputStream.close();
                    }
                    catch (FileNotFoundException e) {
                        Log.e(TAG, "FileNotFoundException in startActivityForResult:onViewCreated", e);
                        return;
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Input/OutputException in startActivityForResult:onViewCreated", e);
                        return;
                    }
                    Log.i(TAG, "No errors, proceeding to saveImageAsProfilePicture");
                    saveImageAsProfilePicture(photoFile, fragment);
                    profileImage.setImageBitmap(selectedImage);
                }
            }
        });
    }

    public void selectPhotoFromGallery(View view) {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoFile = preparePhotoFile(photoFileName);
        if(selectPhotoIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult.launch(selectPhotoIntent);
        }
    }

    public File preparePhotoFile(String photoFileName) {
        File mediaStorageDir = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        return file;
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if(Build.VERSION.SDK_INT > 27) {
                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            }
            else {
                image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), photoUri);
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Input/Output Exception has occurred in loadFromUri: ", e);
        }
        return image;
    }

    public void saveImageAsProfilePicture(File photoFile, Fragment currentFragment) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        String username = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", username);
        query.setLimit(1);
        final ProgressDialog loading = new ProgressDialog(requireActivity());
        loading.setMessage("Uploading image...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ParseException has occurred in saveImageAsProfilePicture: ", e);
                    Toast.makeText(requireActivity(), "Unexpected error has occurred...", Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser parseUser = objects.get(0);
                    ParseFile photo = new ParseFile(photoFile);
                    photo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                parseUser.put("image", photo);
                                parseUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e != null) {
                                            Log.e(TAG, "ParseException has occurred in saveImageAsProfilePicture: ", e);
                                        }
                                        loading.dismiss();
                                        requireActivity().getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
                                    }
                                });
                                Log.i(TAG, "Save successful I hope");
                            }
                            loading.dismiss();
                        }
                    });
                }
            }
        });
    }

    public void loadProfileInformation(ImageView imageView) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        String username = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", username);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ParseException has occurred in loadProfileImage: ", e);
                }
                else {
                    ParseUser parseUser = objects.get(0);
                    ParseFile parseFile = parseUser.getParseFile("image");
                    if(parseFile == null) {
                        Log.i(TAG, "No image associated with this user");
                        Glide.with(requireActivity()).load("").placeholder(R.drawable.ic_launcher_background).into(imageView);
                    }
                    else {
                        Log.i(TAG, "Image associated with this user");
                        String image = parseFile.getUrl();
                        Uri imageUri = Uri.parse(image);
                        Glide.with(requireActivity()).load(imageUri).override(200, 200).into(imageView);
                    }
                    profileUsername.setText(parseUser.getString("username"));
                    Log.i(TAG, objects.toString());
                }
            }
        });
    }
}