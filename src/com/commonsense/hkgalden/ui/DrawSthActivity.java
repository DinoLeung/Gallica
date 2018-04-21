package com.commonsense.hkgalden.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.util.HSVColorPickerDialog.OnColorSelectedListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DrawSthActivity extends Activity {

	private String sign;
	LinearLayout mContent;
	//signature mSignature;
	Button mClear, mGetSign, mCancel;
	public static String tempDir;
	public int count = 1;
	public String current = null;
	private Bitmap mBitmap;
	View mView;
	File mypath;
	LinearLayout signlayout;
	public static final int BUFFER_SIZE = 1024 * 8;
	private OnColorSelectedListener listener = null;
	DrawView dv ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		mContent = (LinearLayout) findViewById(R.id.linearLayout);
		
		dv = new DrawView(this);
		
		dv.setBackgroundColor(Color.WHITE);
		mContent = (LinearLayout) findViewById(R.id.linearLayout);
		mContent.addView(dv);
        dv.requestFocus();
//		signlayout = (LinearLayout) findViewById(R.id.linearLayout1);
//		mContent = (LinearLayout) findViewById(R.id.linearLayout);
//		mSignature = new signature(getApplicationContext(), null , Color.BLACK);
//		mSignature.setBackgroundColor(Color.WHITE);
//		mContent.addView(mSignature, LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT);
//		mView = mContent;


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/*public class signature extends View {
		private static final float STROKE_WIDTH = 10f;
		private Canvas mCanvas;
		private float mX;
		private float mY;
		private Bitmap bitmap; // drawing area for display or saving
		private Path mPath; 
		private ArrayList<Path> paths = new ArrayList<Path>();
		private ArrayList<Path> undonePaths = new ArrayList<Path>();
		private static final float TOUCH_TOLERANCE = 4;    
		private Paint paint;

		public signature(Context context, AttributeSet attrs , int color) {
			super(context, attrs);

			initialisePen(color);	
		}



		private void initialisePen(int color) {
			// TODO Auto-generated method stub
			paint = new Paint();
			paint.setDither(true);
			paint.setAntiAlias(true);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);	
			paint.setStrokeCap(Paint.Cap.ROUND);
			mCanvas = new Canvas();
			mPath = new Path();


		}



		public void save(View v) {

			if (mBitmap == null) {
				mBitmap = Bitmap.createBitmap(mContent.getWidth(),	mContent.getHeight(), Bitmap.Config.RGB_565);
			}



			Canvas canvas = new Canvas(mBitmap);
			try {

				v.draw(canvas);
				String pathF = Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "Gallica";
				File dirF = new File(pathF);
				if(!dirF.exists()){
					dirF.mkdir();
				}
				
				String path = dirF.getAbsolutePath()  + File.separator + "CanvasTest";
				File dir = new File(path);
				if(!dir.exists()){
					dir.mkdir();
				}

				Calendar c = Calendar.getInstance();

				String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
				String name = date + ".jpg";
				File file = new File(dir.getAbsolutePath() + File.separator + name );
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
				mBitmap.compress(CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				fos.close();
				Toast.makeText(getApplicationContext(), file.getName() + " is generated", Toast.LENGTH_SHORT).show();
				mypath = file;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void clear() {


			if (paths.size()>0) 
			{ 
				paths.removeAll(paths);
				invalidate();
			}   
			invalidate();
		}

		public void colorChanged(int color) {
			initialisePen(color);	
		}


		@Override
		protected void onDraw(Canvas canvas) {			  
			for (Path p : paths) {
				canvas.drawPath(p, paint);
			}
			canvas.drawPath(mPath, paint);
		}

		public boolean onTouchEvent(MotionEvent event) 
		{            
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}


		private void touch_start(float x, float y) 
		{
			undonePaths.clear();
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) 
		{
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) 
			{
				mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;
			}
		}  

		private void touch_up() 
		{
			mPath.lineTo(mX, mY);      
			mCanvas.drawPath(mPath, paint);// commit the path to our offscreen  
			paths.add(mPath);
			mPath = new Path(); 
		}


		public void onClickUndo() 
		{ 
			if (paths.size()>0) 
			{ 
				undonePaths.add(paths.remove(paths.size()-1));
				invalidate();
			}      
			else Toast.makeText(getContext(), "nothing more to undo", Toast.LENGTH_SHORT).show();  
		}			
	}*/



	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state
	//	savedInstanceState.putString("signCanvas", sign);
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.draw, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_clear:
			SystemUtils.toast(this, "Clear");
			dv.clearPoints();
			//mSignature.clear();
			//mGetSign.setEnabled(false);
			return true;
		case R.id.action_exit:
			SystemUtils.toast(this, "Exit");
			finish();
			return true;
		case R.id.action_select:
			dv.initialiseColor(this);
			return true;
		case R.id.action_save:
			SystemUtils.toast(this, "Save");
			dv.setDrawingCacheEnabled(true);
			save(dv);
			return true;
		case R.id.action_upload:
			SystemUtils.toast(this, "Upload");
			dv.setDrawingCacheEnabled(true);
			save(dv);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("draw",mypath.getAbsolutePath());
			setResult(RESULT_OK,returnIntent);     
			finish();
			//new UploadImage(DrawSthActivity.this).execute(mypath);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void save(View v) {

		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(mContent.getWidth(),	mContent.getHeight(), Bitmap.Config.RGB_565);
		}



		Canvas canvas = new Canvas(mBitmap);
		try {

			v.draw(canvas);
			String pathF = Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "Gallica";
			File dirF = new File(pathF);
			if(!dirF.exists()){
				dirF.mkdir();
			}
			
			String path = dirF.getAbsolutePath()  + File.separator + "CanvasTest";
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdir();
			}

			Calendar c = Calendar.getInstance();

			String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
			String name = date + ".jpg";
			File file = new File(dir.getAbsolutePath() + File.separator + name );
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			mBitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			fos.close();
			Toast.makeText(getApplicationContext(), file.getName() + " is generated", Toast.LENGTH_SHORT).show();
			mypath = file;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
