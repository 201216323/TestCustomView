/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bruce.chang.testcustomview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件缓存的类
 * @author Michael Yang（www.yangfuhai.com�?update at 2013.08.07
 */
public class FileCacheUtils {
	public static final int TIME_HOUR = 60 * 60;
	public static final int TIME_DAY = TIME_HOUR * 24;
	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
	private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
	private static Map<String, FileCacheUtils> mInstanceMap = new HashMap<String, FileCacheUtils>();
	private ACacheManager mCache;

	public static FileCacheUtils get(Context ctx) {
		return get(ctx, "FileCacheUtils");
	}

	public static FileCacheUtils get(Context ctx, String cacheName) {
		File f = new File(ctx.getCacheDir(), cacheName);
		return get(f, MAX_SIZE, MAX_COUNT);
	}

	public static FileCacheUtils get(File cacheDir) {
		return get(cacheDir, MAX_SIZE, MAX_COUNT);
	}

	public static FileCacheUtils get(Context ctx, long max_zise, int max_count) {
		File f = new File(ctx.getCacheDir(), "FileCacheUtils");
		return get(f, max_zise, max_count);
	}

	public static FileCacheUtils get(File cacheDir, long max_zise, int max_count) {
		FileCacheUtils manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
		if (manager == null) {
			manager = new FileCacheUtils(cacheDir, max_zise, max_count);
			mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
		}
		return manager;
	}

	private static String myPid() {
		return "_" + android.os.Process.myPid();
	}

	private FileCacheUtils(File cacheDir, long max_size, int max_count) {
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			throw new RuntimeException("can't make dirs in "
					+ cacheDir.getAbsolutePath());
		}
		mCache = new ACacheManager(cacheDir, max_size, max_count);
	}

	// =======================================
	// ============ String数据 读写 ==============
	// =======================================
	/**
	 * 保存 String数据 到缓存中
	 * @param key  保存的key
	 * @param value  保存的String数据
	 */
	public void put(String key, String value) {
		File file = mCache.newFile(key);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	/**
	 * 保存 String数据到 缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的String数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, String value, int saveTime) {
		put(key, Utils.newStringWithDateInfo(saveTime, value));
	}

	/**
	 * 读取 String数据
	 * @param key
	 * @return String 数据
	 */
	public String getAsString(String key) {
		File file = mCache.get(key);
		if (!file.exists())
			return null;
		boolean removeFile = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			if (!Utils.isDue(readString)) {
				return Utils.clearDateInfo(readString);
			} else {
				removeFile = true;
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ============= JSONObject 数据 读写 ==============
	// =======================================
	/**
	 * 保存 JSONObject数据 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSON数据
	 */
	public void put(String key, JSONObject value) {
		put(key, value.toString());
	}

	/**
	 * 保存 JSONObject数据 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONObject数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, JSONObject value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 读取JSONObject数据
	 * 
	 * @param key
	 * @return JSONObject数据
	 */
	public JSONObject getAsJSONObject(String key) {
		String JSONString = getAsString(key);
		try {
			JSONObject obj = new JSONObject(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ============ JSONArray 数据 读写 =============
	// =======================================
	/**
	 * 保存 JSONArray数据到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONArray数据
	 */
	public void put(String key, JSONArray value) {
		put(key, value.toString());
	}

	/**
	 * 保存 JSONArray数据 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONArray数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, JSONArray value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 读取JSONArray数据
	 * 
	 * @param key
	 * @return JSONArray数据
	 */
	public JSONArray getAsJSONArray(String key) {
		String JSONString = getAsString(key);
		try {
			JSONArray obj = new JSONArray(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ============== byte 数据 读写 =============
	// =======================================
	/**
	 * 保存 byte数据到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的数量
	 */
	public void put(String key, byte[] value) {
		File file = mCache.newFile(key);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	/**
	 * 保存 byte数据到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的数量
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, byte[] value, int saveTime) {
		put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
	}

	/**
	 * 获取 byte 数据
	 * 
	 * @param key
	 * @return byte 数据
	 */
	public byte[] getAsBinary(String key) {
		RandomAccessFile RAFile = null;
		boolean removeFile = false;
		try {
			File file = mCache.get(key);
			if (!file.exists())
				return null;
			RAFile = new RandomAccessFile(file, "r");
			byte[] byteArray = new byte[(int) RAFile.length()];
			RAFile.read(byteArray);
			if (!Utils.isDue(byteArray)) {
				return Utils.clearDateInfo(byteArray);
			} else {
				removeFile = true;
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (RAFile != null) {
				try {
					RAFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ============= 序列化数据 读写 ===============
	// =======================================
	/**
	 * 保存 Serializable数据到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的value
	 */
	public void put(String key, Serializable value) {
		put(key, value, -1);
	}

	/**
	 * 保存 Serializable数据到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的value
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, Serializable value, int saveTime) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			byte[] data = baos.toByteArray();
			if (saveTime != -1) {
				put(key, data, saveTime);
			} else {
				put(key, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 读取 Serializable数据
	 * 
	 * @param key
	 * @return Serializable 数据
	 */
	public Object getAsObject(String key) {
		byte[] data = getAsBinary(key);
		if (data != null) {
			ByteArrayInputStream bais = null;
			ObjectInputStream ois = null;
			try {
				bais = new ByteArrayInputStream(data);
				ois = new ObjectInputStream(bais);
				Object reObject = ois.readObject();
				return reObject;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (bais != null)
						bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (ois != null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	// =======================================
	// ============== bitmap 数据 读写 =============
	// =======================================
	/**
	 * 保存 bitmap 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的bitmap数据
	 */
	public void put(String key, Bitmap value) {
		put(key, Utils.Bitmap2Bytes(value));
	}

	/**
	 * 保存 bitmap �?缓存�?
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存�?bitmap 数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, Bitmap value, int saveTime) {
		put(key, Utils.Bitmap2Bytes(value), saveTime);
	}

	/**
	 * 读取 bitmap 数据
	 * 
	 * @param key
	 * @return bitmap 数据
	 */
	public Bitmap getAsBitmap(String key) {
		if (getAsBinary(key) == null) {
			return null;
		}
		return Utils.Bytes2Bimap(getAsBinary(key));
	}

	// =======================================
	// ============= drawable 数据 读写 =============
	// =======================================
	/**
	 * 保存 drawable 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的drawable数据
	 */
	public void put(String key, Drawable value) {
		put(key, Utils.drawable2Bitmap(value));
	}

	/**
	 * 保存 drawable 到缓存
	 * 
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的drawable 数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, Drawable value, int saveTime) {
		put(key, Utils.drawable2Bitmap(value), saveTime);
	}

	/**
	 * 读取 Drawable 数据
	 * 
	 * @param key
	 * @return Drawable 数据
	 */
	public Drawable getAsDrawable(String key) {
		if (getAsBinary(key) == null) {
			return null;
		}
		return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(key)));
	}

	/**
	 * 获取缓存文件
	 * 
	 * @param key
	 * @return value 缓存的文件
	 */
	public File file(String key) {
		File f = mCache.newFile(key);
		if (f.exists())
			return f;
		return null;
	}

	/**
	 * 移除某个key
	 * 
	 * @param key
	 * @return 是否移除成功
	 */
	public boolean remove(String key) {
		return mCache.remove(key);
	}

	/**
	 * 清除缓存数据
	 */
	public void clear() {
		mCache.clear();
	}

	/**
	 * @title 缓存管理类
	 * @author 杨福海（michael�?www.yangfuhai.com
	 * @version 1.0
	 */
	public class ACacheManager {
		/**
		 * 可以用原子方式更新的 long 值,其实和long表面上没什么区别
		 */
		private final AtomicLong cacheSize;
		/**
		 * 一个提供原子操作的Integer的类线程安全
		 */
		private final AtomicInteger cacheCount;
		/**
		 * 字节的限制
		 */
		private final long sizeLimit;
		/**
		 * 大小的限制
		 */
		private final int countLimit;
		private final Map<File, Long> lastUsageDates = Collections
				.synchronizedMap(new HashMap<File, Long>());
		protected File cacheDir;

		private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
			this.cacheDir = cacheDir;
			this.sizeLimit = sizeLimit;
			this.countLimit = countLimit;
			cacheSize = new AtomicLong();//创建具有初始值 0 的新 AtomicLong。
			cacheCount = new AtomicInteger();//创建具有初始值 0 的新 AtomicInteger。
			calculateCacheSizeAndCacheCount();
		}

		/**
		 * 计算 cacheSize和cacheCount
		 */
		private void calculateCacheSizeAndCacheCount() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					/**
					 * 得到cacheDir这个文件中，除文件夹之外的所有文件的字节数，字母占一个字节，汉字占两个字节
					 */
					int size = 0;
					int count = 0;
					//cacheDir这个可能是一个文件夹即目录，里面可能包含的有文件夹或者例如以txt格式结尾的文件，
					File[] cachedFiles = cacheDir.listFiles();
					if (cachedFiles != null) {
						for (File cachedFile : cachedFiles) {
							size += calculateSize(cachedFile);
							count += 1;
							lastUsageDates.put(cachedFile,
									cachedFile.lastModified());//cachedFile.lastModified返回此抽象路径名表示的文件最后一次被修改的时间
						}
						cacheSize.set(size);
						cacheCount.set(count);
					}
				}
			}).start();
		}

		private void put(File file) {
			int curCacheCount = cacheCount.get();
			while (curCacheCount + 1 > countLimit) {
				long freedSize = removeNext();
				cacheSize.addAndGet(-freedSize);

				curCacheCount = cacheCount.addAndGet(-1);
			}
			cacheCount.addAndGet(1);

			long valueSize = calculateSize(file);
			long curCacheSize = cacheSize.get();
			while (curCacheSize + valueSize > sizeLimit) {
				long freedSize = removeNext();
				curCacheSize = cacheSize.addAndGet(-freedSize);
			}
			cacheSize.addAndGet(valueSize);

			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);
		}

		private File get(String key) {
			File file = newFile(key);
			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);

			return file;
		}

		private File newFile(String key) {
			return new File(cacheDir, key.hashCode() + "");
		}

		private boolean remove(String key) {
			File image = get(key);
			return image.delete();
		}

		private void clear() {
			lastUsageDates.clear();
			cacheSize.set(0);
			File[] files = cacheDir.listFiles();
			if (files != null) {
				for (File f : files) {
					f.delete();
				}
			}
		}

		/**
		 * 移除旧的文件
		 * 
		 * @return
		 */
		private long removeNext() {
			if (lastUsageDates.isEmpty()) {
				return 0;
			}

			Long oldestUsage = null;
			File mostLongUsedFile = null;
			Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
			synchronized (lastUsageDates) {
				for (Entry<File, Long> entry : entries) {
					if (mostLongUsedFile == null) {
						mostLongUsedFile = entry.getKey();
						oldestUsage = entry.getValue();
					} else {
						Long lastValueUsage = entry.getValue();
						if (lastValueUsage < oldestUsage) {
							oldestUsage = lastValueUsage;
							mostLongUsedFile = entry.getKey();
						}
					}
				}
			}

			long fileSize = calculateSize(mostLongUsedFile);
			if (mostLongUsedFile.delete()) {
				lastUsageDates.remove(mostLongUsedFile);
			}
			return fileSize;
		}

		private long calculateSize(File file) {
			return file.length();//file.length()返回由此抽象路径名表示的文件的长度。以字节为单位字母1字节汉字2字节
		}
	}

	/**
	 * @title 时间计算工具类
	 * @author 杨福海（michael�?www.yangfuhai.com
	 * @version 1.0
	 */
	private static class Utils {

		/**
		 * 判断缓存的String数据是否到期
		 * 
		 * @param str
		 * @return true：到期了 false：还没有到期
		 */
		private static boolean isDue(String str) {
			return isDue(str.getBytes());
		}

		/**
		 * 判断缓存的byte数据是否到期
		 * 
		 * @param data
		 * @return true：到期了 false：还没有到期
		 */
		private static boolean isDue(byte[] data) {
			String[] strs = getDateInfoFromDate(data);
			if (strs != null && strs.length == 2) {
				String saveTimeStr = strs[0];
				while (saveTimeStr.startsWith("0")) {
					saveTimeStr = saveTimeStr
							.substring(1, saveTimeStr.length());
				}
				long saveTime = Long.valueOf(saveTimeStr);
				long deleteAfter = Long.valueOf(strs[1]);
				if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
					return true;
				}
			}
			return false;
		}

		private static String newStringWithDateInfo(int second, String strInfo) {
			return createDateInfo(second) + strInfo;
		}

		private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
			byte[] data1 = createDateInfo(second).getBytes();
			byte[] retdata = new byte[data1.length + data2.length];
			System.arraycopy(data1, 0, retdata, 0, data1.length);
			System.arraycopy(data2, 0, retdata, data1.length, data2.length);
			return retdata;
		}

		private static String clearDateInfo(String strInfo) {
			if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
				strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
						strInfo.length());
			}
			return strInfo;
		}

		private static byte[] clearDateInfo(byte[] data) {
			if (hasDateInfo(data)) {
				return copyOfRange(data, indexOf(data, mSeparator) + 1,
						data.length);
			}
			return data;
		}

		private static boolean hasDateInfo(byte[] data) {
			return data != null && data.length > 15 && data[13] == '-'
					&& indexOf(data, mSeparator) > 14;
		}

		private static String[] getDateInfoFromDate(byte[] data) {
			if (hasDateInfo(data)) {
				String saveDate = new String(copyOfRange(data, 0, 13));
				String deleteAfter = new String(copyOfRange(data, 14,
						indexOf(data, mSeparator)));
				return new String[] { saveDate, deleteAfter };
			}
			return null;
		}

		private static int indexOf(byte[] data, char c) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] == c) {
					return i;
				}
			}
			return -1;
		}

		private static byte[] copyOfRange(byte[] original, int from, int to) {
			int newLength = to - from;
			if (newLength < 0)
				throw new IllegalArgumentException(from + " > " + to);
			byte[] copy = new byte[newLength];
			System.arraycopy(original, from, copy, 0,
					Math.min(original.length - from, newLength));
			return copy;
		}

		private static final char mSeparator = ' ';

		private static String createDateInfo(int second) {
			String currentTime = System.currentTimeMillis() + "";
			while (currentTime.length() < 13) {
				currentTime = "0" + currentTime;
			}
			return currentTime + "-" + second + mSeparator;
		}

		/*
		 * Bitmap 转byte[]
		 */
		private static byte[] Bitmap2Bytes(Bitmap bm) {
			if (bm == null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		}

		/*
		 * byte[] 转Bitmap
		 */
		private static Bitmap Bytes2Bimap(byte[] b) {
			if (b.length == 0) {
				return null;
			}
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}

		/*
		 * Drawable 转Bitmap
		 */
		private static Bitmap drawable2Bitmap(Drawable drawable) {
			if (drawable == null) {
				return null;
			}
			// 该drawable 的长度
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();
			// 该drawable 的颜色格式
			Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565;
			// 建立对应 bitmap
			Bitmap bitmap = Bitmap.createBitmap(w, h, config);
			// 建立对应 bitmap 的画布
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, w, h);
			// 将drawable 内容画到画布上
			drawable.draw(canvas);
			return bitmap;
		}

		/*
		 * Bitmap 转Drawable
		 */
		@SuppressWarnings("deprecation")
		private static Drawable bitmap2Drawable(Bitmap bm) {
			if (bm == null) {
				return null;
			}
			return new BitmapDrawable(bm);
		}
	}

}
