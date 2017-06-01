package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.model.data.LoginResult;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_toolbar)
    Toolbar toolbar;

    @BindView(R.id.login_id)
    EditText mEmailView;

    @BindView(R.id.login_password)
    EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mEmailView.setText("admin1");
        mPasswordView.setText("1234");
    }

    @OnClick(R.id.login_submit)
    public void login_submit() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("아이디를 입력해주세요");
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("비밀번호를 입력해주세요");
            focusView = mPasswordView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
            return;
        }

        IApiService apiService = ApiService.getInstance().getService();
        apiService.Login(email, password).enqueue(new Callback<Result<LoginResult>>() {
            @Override
            public void onResponse(Call<Result<LoginResult>> call, Response<Result<LoginResult>> response) {
                boolean isSuccessful = response.body().IsSuccessful;

                if (isSuccessful) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", mEmailView.getText().toString());
                    startActivity(intent);
                } else {
                    mPasswordView.setError("비밀번호를 다시 확인해주세요");
                    mPasswordView.requestFocus();
                }
            }
            @Override
            public void onFailure(Call<Result<LoginResult>> call, Throwable t) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}