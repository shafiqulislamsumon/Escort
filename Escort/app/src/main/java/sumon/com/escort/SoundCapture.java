package sumon.com.escort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SoundCapture {

    private static UploadFileToServer uploadFileToServer;
    private String audioFileName = null;
    private static Context context;
    private static MediaRecorder mediaRecorder;
    private static SoundCapture soundCapture = null;
    private boolean  isRecordInitiated = true;
    private static SimpleDateFormat simpleDateFormat;
    private static String appDirectoryPath;
    private static String TAG = SoundCapture.class.getSimpleName();

    private SoundCapture(){
        // Singleton Pattern is applied
    }

    public static SoundCapture getSoundCaptureInstance(Context context){
        if(soundCapture == null){
            SoundCapture.context = context;
            soundCapture = new SoundCapture();
            mediaRecorder = new MediaRecorder();
            appDirectoryPath = context.getFilesDir().getAbsolutePath() + File.separator; // /data/data/sumon.com.escort/files/
            simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a", Locale.getDefault());
            uploadFileToServer = new UploadFileToServer(context, appDirectoryPath);
            createUploadFolder();
        }
        return soundCapture;
    }

    public static void createUploadFolder() {

        try {
            String appDirPath = appDirectoryPath + "UploadFolder";
            File uploadFolderDir = new File(appDirPath);

            if (!uploadFolderDir.exists()) {
                uploadFolderDir.mkdirs();
                Log.d(TAG, "Upload folder directory : " + appDirPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSoundCaptureProcess(){

        if(getAvailableDeviceMemory() > 100){
            if(isRecordInitiated){
                isRecordInitiated = false;
                configureMediaRecorderSetting();
                startRecording();
                Log.d(TAG, "First time recording start");
            }else{
                saveRecording();
                configureMediaRecorderSetting();
                startRecording();

                if(uploadFileToServer != null){
                    uploadFileToServer.startUploadingFiles();
                }else{
                    Log.d(TAG, "uploadFileToServer object is null");
                }
            }
        } else{
            // if network available and if uploadfolder contains files then upload to server
            // store it to sd card
            // outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.3gpp";
            //outputFile = context.getFilesDir().getAbsolutePath(); // store to phone memory
        }

    }

    public void configureMediaRecorderSetting(){

        try {
            if(mediaRecorder != null){
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startRecording(){

        try {
            if(mediaRecorder != null){
                String currentDateTime = simpleDateFormat.format(new Date());
                audioFileName = currentDateTime +".3gpp";
                String outputFilePath = appDirectoryPath + audioFileName;
                Log.d(TAG, "Start recording Audio File : " + outputFilePath);
                mediaRecorder.setOutputFile(outputFilePath);
                mediaRecorder.prepare();
                mediaRecorder.start();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            /*
            * if start() is called before prepare() || prepare() is called after start() or setOutputFormat()
            * then IllegalStateException might occur
            * */
        } catch (IOException e) {
            e.printStackTrace();
            // prepare() fails
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRecording() {

        try {
            if(mediaRecorder != null){
                mediaRecorder.stop();
                mediaRecorder.reset();
            }

            if(audioFileName != null){
                String inputPath = appDirectoryPath;
                String outputPath = appDirectoryPath + "UploadFolder"+ File.separator;
                moveToUploadFolder(inputPath, audioFileName, outputPath);
                Log.d(TAG, "Audio file is saved to UploadFolder");
            }else{
                Log.d(TAG, "Recorded audio file is null");
            }
            audioFileName = null;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            // it is called before start()
        } catch (RuntimeException e) {
            e.printStackTrace();
            // no valid audio/video data has been received
        }
    }

    public void moveToUploadFolder(String inputPath, String inputFileName, String outputPath) {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(inputPath + inputFileName);
            outputStream = new FileOutputStream(outputPath + inputFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;

            // write the output file
            outputStream.flush();
            outputStream.close();
            outputStream = null;

            // Delete the original file
            new File(inputPath + inputFileName).delete();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            if(mediaRecorder != null){
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            // it is called before start()
        } catch (RuntimeException e) {
            e.printStackTrace();
            // no valid audio/video data has been received
        }
    }

    public long getAvailableDeviceMemory() {

        long availableMegaBytes = 0;
        long availableBytes = 0;

        try {
            String path = "/storage/emulated/0/"; // phone memory
            //String path = "/storage/sdcard1/"; // sd card memory
            //String path = Environment.getDataDirectory().getPath(); // /data
            //String path = Environment.getExternalStorageDirectory().getPath(); // /storage/emulated/0
            StatFs statFs = new StatFs(path);
            Log.d(TAG, "Storage Path : " + path);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBytes = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
            } else {
                availableBytes = (long)statFs.getBlockSize() * (long)statFs.getAvailableBlocks();
            }

            availableMegaBytes = availableBytes / AppConstants.BYTES_IN_ONE_MB;
            Log.d(TAG, "Available Memory : " + availableMegaBytes + " MB");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableMegaBytes;
    }

}
