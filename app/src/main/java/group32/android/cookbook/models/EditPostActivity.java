package group32.android.cookbook.models;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import group32.android.cookbook.R;

/**
 * Created by Hai Bui on 30-Nov-17.
 */

public class EditPostActivity extends AppCompatActivity {
    private EditText editpost_edt_title;
    private EditText editpost_edt_recipe;
    private Button editpost_btn_done;
    private Button editpost_btn_upload;
    private ImageView editpost_iv_uploaded_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        editpost_edt_title = (EditText) findViewById(R.id.editpost_edt_title);
        editpost_edt_recipe = (EditText)findViewById(R.id.editpost_edt_recipe);
        editpost_btn_done = (Button)findViewById(R.id.editpost_btn_done);
        editpost_btn_upload = (Button)findViewById(R.id.editpost_btn_upload);
        editpost_iv_uploaded_photo = (ImageView)findViewById(R.id.editpost_iv_uploaded_photo);

        editpost_btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        editpost_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
