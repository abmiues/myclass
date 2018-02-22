package com.example.myclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.student.InformActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Fragment_msg extends Fragment {

	private static DefaultHttpClient httpClient;
	private View view;
	private ListView listview;
	private ProgressDialog progerssdialog;
	//private String JSESSIONID;
	//private int host;
	private SharedPreferences localdata;
	private ArrayList<HashMap<String, String>> data;
	private int position;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		progerssdialog=new ProgressDialog(getActivity());
		localdata=getActivity().getSharedPreferences("localdata", 0);
		view=inflater.inflate(R.layout.fragment_msg,container ,false);
		listview=(ListView)view.findViewById(R.id.listview_msg);
		data=new ArrayList<HashMap<String,String>>();
		new getdata().execute();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int m_position, long id) {
				position=m_position;
				new getinfo().execute();
				
			}
		});
		return view;
	}

	public  class getdata extends AsyncTask{

	
		String result="";
		@Override
		protected void onPreExecute() {
			
			progerssdialog.show();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {
			String url="http://"+localdata.getString("ip", "127.0.0.1")+":"
		+localdata.getString("host", "80")+"/myservers/"+localdata.getString("usetype", "stu")+"/stu_getinforlist";
			//创建post请求
			HttpPost httpRequest  = new HttpPost(url);
			httpClient=new DefaultHttpClient();
			//设置超时
			HttpParams params1 = httpClient.getParams();  
	        HttpConnectionParams.setConnectionTimeout(params1, 16000);  
	        HttpConnectionParams.setSoTimeout(params1, 16000);
			try {
				//将session放入请求头中
				httpRequest.setHeader("Cookie", "JSESSIONID="+localdata.getString("JSESSIONID", ""));
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
			
			//ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
			HashMap<String, String> map;
			try {
				JSONArray jsonArray=new JSONArray(result.toString());
				int length=jsonArray.length();
				for(int i=0;i<length;i++)
				{
					map=new  HashMap<String, String>();
					JSONObject jsonObject=new JSONObject();
					jsonObject=jsonArray.getJSONObject(i);
					map.put("title", jsonObject.getString("title"));
					map.put("publisher", jsonObject.getString("publisher"));
					map.put("time", jsonObject.getString("time"));
					map.put("id",jsonObject.getString("id"));
					data.add(map);
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			myAdapter adapter=new myAdapter(getActivity(), data);
			listview.setAdapter(adapter);
			//super.onPostExecute(result);
		}}
	
	public class getinfo extends AsyncTask{
		String result="";
		@Override
		protected void onPreExecute() {
			
			progerssdialog.show();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... param_obj) {
			String url="http://"+localdata.getString("ip", "127.0.0.1")+":"
		+localdata.getString("host", "80")+"/myservers/"+localdata.getString("usetype", "stu")+"/stu_getinfor";
			//创建post请求
			HttpPost httpRequest  = new HttpPost(url);
			httpClient=new DefaultHttpClient();
			//设置超时
			HttpParams params1 = httpClient.getParams();  
	        HttpConnectionParams.setConnectionTimeout(params1, 16000);  
	        HttpConnectionParams.setSoTimeout(params1, 16000);
			try {
				//将session放入请求头中
				ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add( new BasicNameValuePair("id", data.get(position).get("id")));
				httpRequest.setHeader("Cookie", "JSESSIONID="+localdata.getString("JSESSIONID", ""));
				//连接服务器，并发送数据
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
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
			
			Intent intent=new Intent(getActivity(),InformActivity.class);
			intent.putExtra("data",result.toString());
			startActivity(intent);
		}
	}

	class myAdapter extends BaseAdapter{

		private ArrayList<HashMap<String, String>> mdata=new ArrayList<HashMap<String,String>>();
		private LayoutInflater mInflater;

		public myAdapter(Context context,ArrayList<HashMap<String , String>> data) {
			this.mInflater=LayoutInflater.from(context);
			mdata=data;
		}
		@Override
		public int getCount() {
			
			return mdata.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder=new Holder();
			if(convertView==null)
			{
				convertView=mInflater.inflate(R.layout.list_msg, null);
				holder.title=(TextView)convertView.findViewById(R.id.text_title);
				holder.publisher=(TextView)convertView.findViewById(R.id.text_pulisher);
				holder.time=(TextView)convertView.findViewById(R.id.text_time);
				convertView.setTag(holder);
			}
			else 
			{
				holder=(Holder)convertView.getTag();
			}
			holder.title.setText(mdata.get(position).get("title"));
			holder.publisher.setText(mdata.get(position).get("publisher"));
			holder.time.setText(mdata.get(position).get("time"));
			return convertView;
		}
		class Holder
		{
			TextView title,publisher,time;
		}
	}

	
}
