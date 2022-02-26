package com.wwp.QA.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.ProcessController.ProcesscontrollermainActivity;
import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.DatabaseClient;
import com.wwp.QA.RoomDatabase.LoginnameEntity;
import com.wwp.QA.RoomDatabase.LoginnamelistActivity;
import com.wwp.QA.RoomDatabase.SysadminEntity;
import com.wwp.QA.Utils.CommonMethod;
import com.wwp.QA.Utils.PostJSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntronameandpasswordActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPassword;

    LoginResponse loginFromDatabase;
    IsLoginResponse isLoginFromDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intronameandpassword);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);

        // ===========================================================
        // this activity get the Entity that was sent to it by intent into LoginnamelistAdapter  LoginViewHolder class onClick
        // ===========================================================
        final LoginnameEntity loginnameEntity = (LoginnameEntity) getIntent().getSerializableExtra("loginnameEntity");

        // get the desired name from the list of tablet users
        loadName(loginnameEntity);

        // get the QA_Operator state for the operator with the name from the list
        setQAOperator(loginnameEntity);



        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Log.e("INP", "loginFromDatabase.getPassword() -> " + (loginFromDatabase ==null ? "loginFromDatabase NULL": loginFromDatabase.getPassword()));
                Log.e("INP", "editTextPassword.getText().toString() -> " + (editTextPassword.getText().toString()==null ? "editTextPassword.getText().toString() NULL": editTextPassword.getText().toString()));
                if (loginFromDatabase.getPassword().equals(editTextPassword.getText().toString())) {

                    loginOperator(loginnameEntity);

                } else {
                    Toast.makeText(getApplicationContext()
                            , "QA Operator " + editTextName.getText().toString() + " is not having this password"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    void startProcessControllerActivity(){
        //Log.e("INP", "startProcessControllerActivity()");
        //Log.e("INP", "loginFromDatabase.getLoginname() -> " + loginFromDatabase.getLoginname());
        startActivity(new Intent(IntronameandpasswordActivity.this, ProcesscontrollermainActivity.class)
                .putExtra("loginname", loginFromDatabase.getLoginname())
        );

    }

    void loadName(LoginnameEntity loginnameEntity){
        editTextName.setText(loginnameEntity.getLoginname());
        editTextName.setText("");
    }



    private void setQAOperator(final LoginnameEntity loginnameEntity) {

        if (CommonMethod.isNetworkAvailable(IntronameandpasswordActivity.this)) {

            interogateAPIforQAOperator(loginnameEntity.getLoginname());
        }
    }


    private void loginOperator(final LoginnameEntity loginnameEntity) {

        if (CommonMethod.isNetworkAvailable(IntronameandpasswordActivity.this)) {

            loginAPIforQAOperator(loginnameEntity.getLoginname());
        }
    }


    // get the room database to see the web address where to looking for
    // then the web API address is get from room database the API is called
    private void interogateAPIforQAOperator(String loginname){

        class GetActiveWebadressTasks extends AsyncTask<Void, Void, SysadminEntity> {

            @Override
            protected SysadminEntity doInBackground(Void... voids) {

                // obtain webaddress set into sysadmin room database to call it for article array list
                SysadminEntity sysadminEntity = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getActivewebadress();

                return sysadminEntity; // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(SysadminEntity webaddresses) {

                super.onPostExecute(webaddresses);

                checkLoginRecordFromDatase(webaddresses.getWebaddress(), loginname);

            }
        }

        GetActiveWebadressTasks gt = new GetActiveWebadressTasks();
        gt.execute();

    }


    // get the room database to see the web address where to looking for
    // then the web API address is get from room database the API is called
    private void loginAPIforQAOperator(String loginname){

        class GetActiveWebadressTasks extends AsyncTask<Void, Void, SysadminEntity> {

            @Override
            protected SysadminEntity doInBackground(Void... voids) {

                // obtain webaddress set into sysadmin room database to call it for article array list
                SysadminEntity sysadminEntity = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getActivewebadress();

                return sysadminEntity; // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(SysadminEntity webaddresses) {

                super.onPostExecute(webaddresses);

                doLoginOperatorToday(webaddresses.getWebaddress(), loginname);

            }
        }

        GetActiveWebadressTasks gt = new GetActiveWebadressTasks();
        gt.execute();

    }

    private PostJSON preparePostJSON(String action, String operatorName, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        dataset = new HashMap<String, String>() {{put("qaoperatorname",operatorName);}};
        authkey = new String[]{"authkey"};

        // check JSON syntax here: https://jsonlint.com/

       // https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/

        return new PostJSON(   action
                ,dataset
                ,authkey
        ); //postJSON;
    }


    private void doLoginOperatorToday(String webaddress, String tabletLoginname) {

        QALoginAPI qALoginApi = QARetrofitService.getClient().create(QALoginAPI.class);

        Call<List<IsLoginResponse>> call1 = qALoginApi.getQAOperator(  webaddress
                , preparePostJSON("LOGINQAOPERATOR"
                        , tabletLoginname
                        , ""
                )
        ); // here the interface is implemented

        call1.enqueue(new Callback<List<IsLoginResponse>>() {

            @Override
            public void onResponse(Call<List<IsLoginResponse>> call, Response<List<IsLoginResponse>> response) {

                List<IsLoginResponse> misLoginResponse = response.body();

                Log.e("INP", "LoginResponse 1 --> " + misLoginResponse);
                Log.e("INP", "Server WEB Address --> " + webaddress);

                if (misLoginResponse != null) {

                    misLoginResponse.forEach(name->
                            //Log.e("INP", "getLoginname        -->  " + name.getLoginname());
                            //Log.e("INP", "getPassword         -->  " + name.getPassword());
                            //Log.e("INP", "getLogin            -->  " + loginResponse.getLogin());
                            //Log.e("INP", "getTimestampLogin   -->  " + loginResponse.getTimestampLogin());
                            //Log.e("INP", "getTimestampLogout  -->  " + loginResponse.getTimestampLogout());

                            // save the loginRespose into Activity
                            isLoginFromDatabase = name
                    );



                    if (isLoginFromDatabase.getLoginname().isEmpty()) {

                        // if loginname is empty this mean the tabletname does not match with any qaoperatorname
                        Toast.makeText(getApplicationContext(), "No QA Operators with the name of " + tabletLoginname , Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(IntronameandpasswordActivity.this, LoginnamelistActivity.class));

                    } else {

                        // tabletname does match with one qaoperatorname
                        if (isLoginFromDatabase.getLogin()){

                            //now qaoperator is already login it will finish this activity and start processcontroller task
                            Toast.makeText(getApplicationContext(), "QA Operator " + tabletLoginname + " was successfully loggedin", Toast.LENGTH_LONG).show();
                            finish();
                            startProcessControllerActivity();

                        } else {

                            // qa operator still not login so it have to give the password again because something went wrong on login process
                            Toast.makeText(getApplicationContext(), "Login fail for QA operator " + tabletLoginname , Toast.LENGTH_LONG).show();
                            editTextName.setText(loginFromDatabase.getLoginname());
                            editTextPassword.setText("");
                            //Log.e("INP", "loginFromDatabase.getPassword() --> " + (loginFromDatabase==null ? "loginFromDatabase NULL": loginFromDatabase.getPassword()));
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<IsLoginResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }

        });
    }

    private void checkLoginRecordFromDatase(String webaddress, String tabletLoginname) {

        Log.e("INP", "checkLoginRecordFromDatase -> " + webaddress);
        QAOperatorAPI qAOperatorApi = QARetrofitService.getClient().create(QAOperatorAPI.class);

        Call<List<LoginResponse>> call1 = qAOperatorApi.getQAOperator(  webaddress
                                                                , preparePostJSON("GETLOGINRECORD"
                                                                                   , tabletLoginname
                                                                                   , ""
                                                                                 )
                                                                ); // here the interface is implemented

        call1.enqueue(new Callback<List<LoginResponse>>() {

            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {

                List<LoginResponse> loginResponse = response.body();

                //Log.e("INP", "LoginResponse 1 --> " + loginResponse);
                Log.e("INP", "Server WEB Address --> " + webaddress);

                if (loginResponse != null) {

                    loginResponse.forEach(name->
                            // save the loginRespose into Activity
                            loginFromDatabase = name
                    );

                    Log.e("INP", "getLoginname        -->  " + loginFromDatabase.getLoginname());
                    Log.e("INP", "getPassword         -->  " + loginFromDatabase.getPassword());
                    Log.e("INP", "getLogin            -->  " + loginFromDatabase.getLogin());
                    Log.e("INP", "getTimestampLogin   -->  " + loginFromDatabase.getTimestampLogin());
                    Log.e("INP", "getTimestampLogout  -->  " + loginFromDatabase.getTimestampLogout());

                    if (loginFromDatabase.getLoginname().isEmpty()) {

                        // if loginname is empty this mean the tabletname does not match with any qaoperatorname
                        Toast.makeText(getApplicationContext(), "No QA Operators with the name of " + tabletLoginname , Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(IntronameandpasswordActivity.this, LoginnamelistActivity.class));
                    } else {

                        // tabletname does match with one qaoperatorname
                        if (loginFromDatabase.getLogin()){

                            //now if the qaoperator is already login it will finish this activity and start processcontroller task
                            Toast.makeText(getApplicationContext(), "QA Operator " + tabletLoginname + " is already loggedin", Toast.LENGTH_LONG).show();
                            finish();
                            startProcessControllerActivity();
                        } else {

                            // qa operator is not login so it have to give the password because the name is already known
                            Toast.makeText(getApplicationContext(), "QA Operator " + tabletLoginname + " is not yet loggedin", Toast.LENGTH_LONG).show();
                            editTextName.setText(loginFromDatabase.getLoginname());
                            //Log.e("INP", "loginFromDatabase.getPassword() --> " + (loginFromDatabase==null ? "loginFromDatabase NULL": loginFromDatabase.getPassword()));
                        }
                    }
                } else {
                    Log.e("INPA", "checkLoginRecordFromDatase.onResponse() loginResponse = null");
                    Toast.makeText(getApplicationContext(), "Null response when check the server for " + tabletLoginname + " login state", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                Log.e("INPA", "checkLoginRecordFromDatase.onFailure()" + t.getMessage());
                Toast.makeText(getApplicationContext(), "onFailure called " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }

        });
    }


}