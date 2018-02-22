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
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Login_Activity extends Activity {
	
	private static DefaultHttpClient httpClient;
	SharedPreferences sharedata;//用于将数据保存到本地
	String url,ip, host,phone ,deviceid,usetype;
	ProgressDialog progressDialog;//创建进度条，在连接服务器时阻止用户操作
	EditText ID,Pwd,edittextip,edittexthost;
	Button imageButtonLogin,buttonRegister,buttonSetting;
	RadioButton radioButton;
	TelephonyManager tm;
	String JSESSIONID;//用于存放session
	int length;
	List<NameValuePair> params=null;
	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);//加载xml布局文件，生成界面
		/*ActionBar actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);*/
		
		
		ID=(EditText)findViewById(R.id.editTextID);
		Pwd=(EditText)findViewById(R.id.editTextPwd);
		imageButtonLogin=(Button)findViewById(R.id.login_login_btn);
		buttonRegister=(Button)findViewById(R.id.register);
		buttonSetting=(Button)findViewById(R.id.ButtonSetting);
		radioButton=(RadioButton)findViewById(R.id.radioButton1);
		
		
		sharedata = getSharedPreferences("localdata", 0);//读取本地储存的信息。
		JSESSIONID=sharedata.getString("sessionid", null);
		ip=sharedata.getString("ip", null);
		host=sharedata.getString("host", null);
		
		tm = (TelephonyManager)Login_Activity.this.getSystemService(Context.TELEPHONY_SERVICE);//用于获得设备识别码，暂时没用
		progressDialog = new ProgressDialog(this);
		if(JSESSIONID!=null)//如果本地存储的session不为空，则启动自动登录
			new autologin().execute();
		
		imageButtonLogin.setOnClickListener(new OnClickListener() {//登录按钮事件
			@Override
			public void onClick(View v) {
				if(radioButton.isChecked())
				{	usetype="stu";
				sharedata.edit().putString("usetype", usetype).commit();
				}
				else
				{
					usetype="tea";
					sharedata.edit().putString("usetype", usetype).commit();
				}
//				deviceid=tm.getDeviceId();//获取设备识别号
				new AT().execute();
			}
		});
		buttonRegister.setOnClickListener(new OnClickListener() {//注册按钮事件

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Login_Activity.this,Register_Activity.class);
				startActivity(intent);//跳转activity，此activity不关闭，便于切换。
			}
		});
		buttonSetting.setOnClickListener(new OnClickListener() {//设置按钮事件	
			public void onClick(View v) {
				//创建弹出窗口
				LayoutInflater layoutInflater=LayoutInflater.from(Login_Activity.this);
				@SuppressLint("InflateParams")
				View myloginview =layoutInflater.inflate(R.layout.dialog_settingurl, null);
				edittextip=(EditText)myloginview.findViewById(R.id.editext_ip);
				edittexthost=(EditText)myloginview.findViewById(R.id.editext_host);
				//将当前ip和host显示在edittext中
				edittexthost.setText(sharedata.getString("host", null));
				edittextip.setText(sharedata.getString("ip", null));

				AlertDialog.Builder text=new AlertDialog.Builder(Login_Activity.this);
				text
				.setTitle("设置url")
				.setView(myloginview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//保存修改后的ip、host
						sharedata.edit().putString("ip", edittextip.getText().toString()).commit();
						sharedata.edit().putString("host", edittexthost.getText().toString()).commit();
						ip=edittextip.getText().toString();
						host=edittexthost.getText().toString();
					}
				})
				.show();
			}
		});

	}
	/*@Override//actionBar
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home: 
			Login_Activity.this.finish();//
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}*/

	//[start]自动登录
	@SuppressWarnings("rawtypes")
	class autologin extends AsyncTask{

		
		String result="";
		@Override
		protected void onPreExecute() {
			//显示progressDialog
			progressDialog.show();
		}

		@Override
		protected Object doInBackground(Object... params_obj) {
			url="http://"+ip+":"+host+"/myservers/"+usetype+"/autologin";
			//创建post请求
			HttpPost httpRequest  = new HttpPost(url);
			httpClient=new DefaultHttpClient();
			//设置超时
			HttpParams params1 = httpClient.getParams();  
	        HttpConnectionParams.setConnectionTimeout(params1, 16000);  
	        HttpConnectionParams.setSoTimeout(params1, 16000);
			try {
				//将session放入请求头中
				httpRequest.setHeader("Cookie", "JSESSIONID="+JSESSIONID);
				//连接服务器，并发送数据
				HttpResponse httpResponse=httpClient.execute(httpRequest);
				//创建数据流读取，获得服务器返回内容
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

		@Override
		protected void onPostExecute(Object result) {
			//返回数据
			String msg="";
			msg=result.toString();
			if(msg.equals("111")){
				Intent intent=new Intent(Login_Activity.this,MainActivity.class);
				startActivity(intent);
				Login_Activity.this.finish();
			}
			else if(msg.equals("000"))
			{
				Toast.makeText(getApplicationContext(), "账号过期，重新登录", Toast.LENGTH_SHORT).show();
			}
			else if(msg.equals("110"))
				Toast.makeText(getApplicationContext(), "账号过期，重新登录", Toast.LENGTH_SHORT).show();
			else if (msg.equals(""))
				Toast.makeText(getApplicationContext(), "连接超时，请检查ip和端口", Toast.LENGTH_SHORT).show();
			//关闭进度条
			progressDialog.cancel();

		} 
	}
	//[end]

	//[start]手动登录
	@SuppressWarnings("rawtypes")
	class AT extends AsyncTask{

		String result="";
		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		@Override
		protected Object doInBackground(Object... params_obj) {
			CharSequence username="";
			CharSequence password="";
			username=ID.getText();
			password =Pwd.getText();
			httpClient=new DefaultHttpClient();
			if(!username.equals("")&&!password.equals("")){
				url="http://"+ip+":"+host+"/myservers/"+usetype+"/login";
				HttpPost httpRequest  = new HttpPost(url);
				//将要发送的数据装入params中
				params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userid", username.toString()));
				params.add(new BasicNameValuePair("pwd", password.toString()));
				//超时处理
				//HttpParams params1 = httpClient.getParams();  
				//HttpConnectionParams.setConnectionTimeout(params1, 16000);  
			 //   HttpConnectionParams.setSoTimeout(params1, 16000);
				try {
					//将要发送的数据加入到post请求中
					httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					HttpResponse httpResponse=httpClient.execute(httpRequest);
					//获取session
					CookieStore cookieStore = httpClient.getCookieStore();  
					List<Cookie> cookies = cookieStore.getCookies();  
					for(int i=0;i<cookies.size();i++){  
						if("JSESSIONID".equals(cookies.get(i).getName())){  
							JSESSIONID = cookies.get(i).getValue();  
						}
					}  
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

			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			String msg="";
			msg=result.toString();
			if(msg.equals("111")){
				Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
				Editor sha = getSharedPreferences("localdata", 0).edit();
				sha.putString("userid",ID.getText().toString());
				sha.putString("session", JSESSIONID);//将session保存到本地
				sha.commit();

				Intent intent=new Intent();
				intent.setClass(Login_Activity.this,MainActivity.class);
				startActivity(intent);
				Login_Activity.this.finish();
			}
			else if(msg.equals("100"))
				Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_SHORT).show();
			else if(msg.equals("000"))
				Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
			else if (msg.equals(""))
				Toast.makeText(getApplicationContext(), "连接超时，请检查ip和端口", Toast.LENGTH_SHORT).show();
			progressDialog.cancel();

		} 
	}//[end]
}