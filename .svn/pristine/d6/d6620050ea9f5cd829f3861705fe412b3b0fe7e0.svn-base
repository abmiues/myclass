package com.example.myclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register_Activity extends Activity {
	Button buttonSend;
	String url,ip,host,result="",msg,usertype;
	ProgressDialog progressDialog;
	List<NameValuePair> params;
	String ID="",pwd1="",pwd2="",email="",name="";
	EditText etid,etpwd1,etpwd2,etemail,etname;
	Spinner spinnertype;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regiser);
		ActionBar actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		
		final SharedPreferences sharedata = getSharedPreferences("localdata", 0);
		ip=sharedata.getString("ip", null);
		host=sharedata.getString("host", null);
		url=ip+":"+host;
		buttonSend=(Button)findViewById(R.id.btnReg);
	//	btnGetCheckCode=(Button)findViewById(R.id.buttonGetCheckCode);
		etid=(EditText)findViewById(R.id.EditID);
		etpwd1=(EditText)findViewById(R.id.EditPwd);
		etpwd2=(EditText)findViewById(R.id.EditConfigPwd);
		etemail=(EditText)findViewById(R.id.EditEmail);
		etname=(EditText)findViewById(R.id.EditName);
		spinnertype=(Spinner)findViewById(R.id.spinner_type);
		progressDialog=new ProgressDialog(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		buttonSend.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				if(spinnertype.getSelectedItemId()==0)
				usertype="stu";
				else usertype="tea";
				sharedata.edit().putString("usertype", usertype).commit();
				pwd1=etpwd1.getText().toString();
				pwd2=etpwd2.getText().toString();
				msg="";
				if(pwd1.equals(""))
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
				else if(!pwd1.equals(pwd2))
					Toast.makeText(getApplicationContext(), "两次密码不同", Toast.LENGTH_SHORT).show();
				else{
					 new sendmsg().execute();
				}
			}
		});
	}
	
	@Override//actionbar返回事件
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Register_Activity.this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	} 

	@SuppressWarnings("rawtypes")
	class sendmsg extends AsyncTask{
		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		protected Object doInBackground(Object... params_obj) {
			params=new ArrayList<NameValuePair>();
			ID=etid.getText().toString();
			email=etemail.getText().toString();
			name=etname.getText().toString();
			params.add(new BasicNameValuePair("userid", ID));
			params.add(new BasicNameValuePair("pwd", pwd1));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("name", name));
			HttpPost httpRequest=new HttpPost("http://"+url+"/myservers/"+usertype+"/register");
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
				StringBuilder builder=new StringBuilder();
				BufferedReader bufferedReader2 = new BufferedReader(  
						new InputStreamReader(httpResponse.getEntity().getContent()));  
				for(String s=bufferedReader2.readLine();s!=null;s=bufferedReader2.readLine())
					builder.append(s);
				result=builder.toString();
			} catch(ClientProtocolException e){  
				e.printStackTrace();  
			}catch(UnsupportedEncodingException e){  
				e.printStackTrace();  
			} catch (IOException e) {
				e.printStackTrace();  
			} 
			return result;
		}

		protected void onPostExecute(Object result) {
			msg=result.toString();
			if(msg.equals("111"))
				Toast.makeText(getApplicationContext(), "注册成功",  Toast.LENGTH_SHORT).show();
			else if (msg.equals("000")) 
				Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
			else if(msg.equals("100"))
				Toast.makeText(getApplicationContext(), "账号已存在", Toast.LENGTH_SHORT).show();

			
		}  
	}
	  

	}
