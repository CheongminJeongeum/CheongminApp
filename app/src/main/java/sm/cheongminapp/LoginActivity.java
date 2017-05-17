package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.network.ApiServiceHelper;
import sm.cheongminapp.network.IApiService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLoginMock();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginMock();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }


    private void attemptLogin() {

        // 오류 초기화
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 비밀번호 검사
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("비밀번호를 입력해주세요");
            focusView = mPasswordView;
            cancel = true;
        }

        // 아이디 검사
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("아이디를 입력해주세요");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            IApiService apiService = ApiServiceHelper.getInstance().ApiService;

            apiService.Login(email, password).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    boolean isSuccess = response.body().IsSuccess;
                    if (isSuccess) {
                        Toast.makeText(getBaseContext(), "로그인 성공", Toast.LENGTH_SHORT);

                    } else {
                        mPasswordView.setError("비밀번호를 다시 확인해주세요");
                        mPasswordView.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.d("로그인", "실패");
                }
            });
        }
    }

    private void attemptLoginMock() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}