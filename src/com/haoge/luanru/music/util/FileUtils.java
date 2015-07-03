package com.haoge.luanru.music.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

public class FileUtils {
	public static int delete(String path) {

		int i = -1;
		boolean flag = new File(path).delete();
		return i;
	}
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/乱入的世界/cache";
	public FileUtils(Context context){
		mDataRootPath = context.getCacheDir().getPath();
	}
	/**
	 * 获取储存Image的目录
	 * @return
	 */
	private String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
		if(bitmap == null){
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		//缓存一些质量差的图片
		bitmap.compress(CompressFormat.JPEG, 50, fos);
		fos.flush();
		fos.close();
	}
	/***
	 * 从byte数组中读取一张图片
	 * 
	 * @param bytes
	 *            数据源
	 * @param width
	 *            目标图片的宽度
	 * @param height
	 *            目标图片的高度
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] bytes, int width, int height) {
		Options opt = new Options();
		// 仅仅加载边界属性
		opt.inJustDecodeBounds = true;
		// 解析数据源
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		// 获取图片原本的width height
		int w = opt.outWidth / width;
		int h = opt.outHeight / height;
		int scale = w > h ? w : h;
		opt.inSampleSize = scale;
		// 按照opt的参数设置,解析Bitmap
		opt.inJustDecodeBounds = false;
		Bitmap bit= BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		return bit;
	}
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
	}
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName).length();
	}
	
	
	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		
		dirFile.delete();
	}
	/**
	 * 保存图片
	 * 以后想办法删掉
	 * @param targetFile
	 * @param bitmap
	 * @throws IOException
	 */
	public static void save(File targetFile, Bitmap bitmap) throws IOException {
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		if (!targetFile.exists()) {
			FileOutputStream fos = new FileOutputStream(targetFile);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		}
	}
	//以后要删
	public static Bitmap loadBitmap(String path, int width, int height) {
		Options opt = new Options();
		// 仅仅加载边界属性
		opt.inJustDecodeBounds = true;
		// 解析数据源
		BitmapFactory.decodeFile(path, opt);
		// 获取图片原本的width height
		int w = opt.outWidth / width;
		int h = opt.outHeight / height;
		int scale = w > h ? w : h;
		opt.inSampleSize = scale;
		// 按照opt的参数设置,解析Bitmap
		opt.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, opt);
	}
}
