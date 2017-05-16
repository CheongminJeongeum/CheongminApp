package sm.cheongminapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class RequestActivity extends AppCompatActivity {
    private EditText eLocation, eDate, eStartTime, eEndTime, eGoal;
    private Button bDatePicker, bSubmit;
    private DatePicker dpPicker;

    private int centerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Intent intent = getIntent();
        centerId = intent.getIntExtra("centerId", -1);
        String location = intent.getStringExtra("location");

        eLocation = (EditText) findViewById(R.id.location);
        eDate = (EditText) findViewById(R.id.date);
        eStartTime = (EditText) findViewById(R.id.start_time);
        eEndTime = (EditText) findViewById(R.id.end_time);
        eGoal = (EditText) findViewById(R.id.goal);
        bDatePicker = (Button) findViewById(R.id.btn_pick);
        bDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(RequestActivity.this,
                        R.layout.dialog_datepicker, null);
                dpPicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                AlertDialog.Builder dlg = new AlertDialog.Builder(RequestActivity.this);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String retDate = dpPicker.getYear() + "-" + (dpPicker.getMonth() +1 ) + "-" +
                                dpPicker.getDayOfMonth();
                        eDate.setText(retDate);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
        eLocation.setText(location);
        bSubmit = (Button) findViewById(R.id.btn_submit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 요청
            }
        });
    }
}
