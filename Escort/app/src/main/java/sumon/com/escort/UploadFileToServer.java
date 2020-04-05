package sumon.com.escort;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UploadFileToServer {

	private Context context;
	private NetworkChecker networkChecker;
	private String appDirectoryPath;
	private String TAG = UploadFileToServer.class.getSimpleName();

	public UploadFileToServer(Context context, String appDirectoryPath) {
		this.context = context;
		this.appDirectoryPath = appDirectoryPath;
		networkChecker = NetworkChecker.getNetworkCheckerInstance(context);
	}

	public void startUploadingFiles() {

		if (networkChecker.isNetworkAvailable()) {
			UploadTask uploadTask = new UploadTask();
			uploadTask.execute();
		} else {
			Log.d(TAG, "Network not available");
		}
	}

	private class UploadTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			
			String uploadResultString =  null;
			String filePath = appDirectoryPath + "UploadFolder"+ File.separator;
			File file = new File(filePath);
			File fileArray[] = file.listFiles();

			for (int i = 0; i < fileArray.length; i++) {
			    String uploadingFilePath = fileArray[i].getAbsolutePath();
				Log.d(TAG, "Uploading File Path : " + uploadingFilePath);
				uploadResultString = uploadFile(uploadingFilePath);
			}
			return uploadResultString;
		}

		private String uploadFile(String uploadingFilePath){

			String serverResponseMessage = "";
			HttpURLConnection connection;
			DataOutputStream dataOutputStream;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead;
			int bytesAvailable;
			int bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024; // 1MB
			File sourceFile = new File(uploadingFilePath);

			String[] parts = uploadingFilePath.split("/");
			final String fileName = parts[parts.length-1];

			if (!sourceFile.isFile()){
				Log.d(TAG, "Source File Doesn't Exist");
				serverResponseMessage = "Source File Doesn't Exist";
			}else{
				try{
					FileInputStream fileInputStream = new FileInputStream(sourceFile);
					URL url = new URL(Config.FILE_UPLOAD_URL);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);//Allow Inputs
					connection.setDoOutput(true);//Allow Outputs
					connection.setUseCaches(false);//Don't use a cached Copy
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Connection", "Keep-Alive");
					connection.setRequestProperty("ENCTYPE", "multipart/form-data");
					connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
					connection.setRequestProperty("uploaded_file",uploadingFilePath);

					dataOutputStream = new DataOutputStream(connection.getOutputStream());
					dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + uploadingFilePath + "\"" + lineEnd);
					dataOutputStream.writeBytes(lineEnd);

					bytesAvailable = fileInputStream.available(); //returns no. of bytes present in fileInputStream
					bufferSize = Math.min(bytesAvailable,maxBufferSize); //selecting the buffer size as minimum of available bytes or 1 MB
					buffer = new byte[bufferSize]; //setting the buffer as byte array of size of bufferSize
					bytesRead = fileInputStream.read(buffer,0, bufferSize); //reads bytes from FileInputStream(from 0th index of buffer to buffer size)

					//loop repeats till bytesRead = -1, i.e., no bytes are left to read
					while (bytesRead > 0){
						dataOutputStream.write(buffer,0, bufferSize); //write the bytes read from input stream
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable,maxBufferSize);
						bytesRead = fileInputStream.read(buffer,0,bufferSize);
					}

					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

					int serverResponseCode = connection.getResponseCode();
					serverResponseMessage = connection.getResponseMessage();

					Log.d(TAG, "Server Response : " + serverResponseMessage + " : " + serverResponseCode);

					if(serverResponseCode == 200){
						sourceFile.delete(); // delete the source file
						Log.d(TAG, "File uploaded successfully");
					} else{
						Log.d(TAG, "File was not uploaded successfully");
					}

					fileInputStream.close();
					dataOutputStream.flush();
					dataOutputStream.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return serverResponseMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(TAG,"Server Response : " + result);
		}

	}

}
