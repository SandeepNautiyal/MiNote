package com.gp.app.minote.notes.images;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.gp.app.minote.util.MiNoteUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageLocationPathManager
{
	private String imageDirectoryPath = null;
	
	private String mostRecentImageFilePath = null;
	
	public static ImageLocationPathManager imagePathManager = null;
	
	public static ImageLocationPathManager getInstance()
	{
		if(imagePathManager == null)
		{
			imagePathManager = new ImageLocationPathManager();
		}
		
		return imagePathManager;
	}
	
	private ImageLocationPathManager()
	{
		createImageDirectory();
	}
	
	public void deleteImage(String imageName) 
	{
		File file = new File(imageDirectoryPath+imageName+".jpeg");
		
		if(file.exists())
		{
			file.delete();
		}
	}

	public String getCreatedImagePath() 
	{
		return imageDirectoryPath+MiNoteUtil.createImageNameFromTime()+".jpeg";
	}

	private void createImageDirectory()
	{
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ProfessionalPA/"; 
        
		File newdir = new File(dir); 
        
        if(!newdir.exists())
        {
	        newdir.mkdirs();
        }
        
        imageDirectoryPath = dir;
	}
	
//	TODO improve in case image is added by user with different name. will throw number format exception.
	public List<String> getImagesFilePath()
	{
		List<String> imageFilePaths = new ArrayList<String>();
		
		File file = new File(imageDirectoryPath);
		
		if(file.isDirectory())
		{
			File [] files = file.listFiles();
			
			for(int i = 0, size = files != null ? files.length : 0; i < size; i++)
			{
				File imageFile = files[i];
				
//				int endIndex = path.indexOf(".jpeg");
//				
//				String imageName = path.substring(0, endIndex);
				
				imageFilePaths.add(imageFile.getAbsolutePath());
			}
		}
		
		return imageFilePaths;
	}
	
	
	public String getImageName(String imagePath)
	{
		int index = imagePath.indexOf(imageDirectoryPath);
		
		int endIndex = imagePath.indexOf(".jpeg");
		
		String imageFileName = imagePath.substring(index + imageDirectoryPath.length(), endIndex);

		return imageFileName;
	}
	
	public String getMostRecentImageFilePath()
	{
		return mostRecentImageFilePath;
	}

//	public void createAndSaveImage(Bitmap imageNote)
//	{
//        String filePath = imageDirectoryPath + MiNoteUtil.createImageNameFromTime()+".jpeg";
//
//		OutputStream imagefile = null;
//
//		//TODO improve
//		try
//		{
//			imagefile = new FileOutputStream(filePath);
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//
//		// Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
//		imageNote.compress(CompressFormat.JPEG, 100, imagefile);
//
////		compressImage(filePath);
//
//        try
//        {
//            imagefile.flush();
//
//            imagefile.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//	}

    public String getImagePath()
    {
        String filePath = imageDirectoryPath + MiNoteUtil.createImageNameFromTime() + ".jpeg";

        mostRecentImageFilePath = filePath;

        return filePath;
    }

	public String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        
        try {
            out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;

    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    
	public Bitmap getImage(String imageName, boolean isImageName)
	{
        BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = 1;
		
		String filePath = isImageName ? imageDirectoryPath+imageName+".jpeg" : imageName;
		
		Bitmap image = BitmapFactory.decodeFile(filePath, options);
;
        return image;
	}

	public String getImagePath(String imageName) 
	{
		return imageDirectoryPath+imageName+".jpeg";
	}
}
