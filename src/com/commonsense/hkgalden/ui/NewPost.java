package com.commonsense.hkgalden.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.adapter.Icons;
import com.commonsense.hkgalden.async.DialogActivity;
import com.commonsense.hkgalden.async.PostYourStuff;
import com.commonsense.hkgalden.async.UploadImage;
import com.commonsense.hkgalden.model.Channel;
import com.commonsense.hkgalden.security.InputValidation;
import com.commonsense.hkgalden.util.CollectionUtils;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.HSVColorPickerDialog;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.util.HSVColorPickerDialog.OnColorSelectedListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

@SuppressLint("NewApi")
public class NewPost extends Activity implements OnMenuItemClickListener{

	private EditText  edtTopic ;
	private EditText  edtContent ;
	private Spinner  spinner;
	public static final String preference = "LoginPrefs";
	private String resultInput;
	private int RESULT_LOAD_IMAGE = 888;
	private int RESULT_DRAW = 1999;
	private int RESULT_CAPTURE = 2566;
	private int REQUEST_DIALOG = 8964;
	public int startSelection;
	public int endSelection;
	
	private View popUpView;
	private PopupWindow popupWindow;
    private int screenWidth;
    private Spinner colSpin;
    private ArrayAdapter<String> colAdapter;
    
    private Bundle savedInstanceState;

	private static File imageCaptured;
	private Uri fileUri;
	
	private RelativeLayout parentLayout;
	private int keyboardHeight = 0;
	private int keyboardHeight_ = 0;
	
	private List<com.commonsense.hkgalden.model.IconPlus> iconsDB = new ArrayList<com.commonsense.hkgalden.model.IconPlus>();
	final List<String> collections=new ArrayList<String>();
	private ArrayList<Icons> icons = new ArrayList<Icons> ();
	private GridView iconListView;
	private ImageView backspace;
	private ImageView linebreak;
	
	private ImageView icon;
	private ImageView word;
	private ImageView colour;
	private ImageView img;
	private ImageView size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean dark  =  preferences.getBoolean("dark", false);
		if(dark==true){			
			setTheme(android.R.style.Theme_Holo);
			setContentView(R.layout.new_post_dark);			
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		}else{
			setContentView(R.layout.new_post);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#777777")));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.parseColor("#777777"));
			}
		}
		this.savedInstanceState = savedInstanceState;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//	getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#777777")));
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		GradientDrawable gd = new GradientDrawable(
//				GradientDrawable.Orientation.TOP_BOTTOM,
//				//new int[] {Color.parseColor("#99C5B358"), Color.parseColor("#99cfab7c")});
//				new int[] {Color.parseColor("#7734282C"), Color.parseColor("#777777")});
//
//		//getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(colour)));
//		getActionBar().setBackgroundDrawable(gd);
		
		parentLayout = (RelativeLayout) findViewById(R.id.newPost_parent);
		
		
		setTitle("發表");
		//set the spinner
		spinner = (Spinner) findViewById(R.id.spinner_post);
		
//		SpinnerAdapter a = 
		
		DatabaseManager.init(getApplication());
		
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.   android.R.layout.simple_spinner_item
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			     android.R.layout.simple_spinner_item, DatabaseManager.getInstance().getAllChannelName());

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);


		edtTopic = (EditText)this.findViewById(R.id.topic_post);
		edtContent = (EditText)this.findViewById(R.id.content_post);

		SharedPreferences sp = getSharedPreferences("topic", Activity.MODE_PRIVATE);
		String topic = sp.getString("topic", null);

		//Reply Purpose
		if(!InputValidation.IsNullOrWhiteSpaces(topic)){
			edtTopic.setText(topic);
			edtTopic.setEnabled(false);
		}
		Intent intent = getIntent();		
		if(intent!=null){
			if (Intent.ACTION_SEND.equals(intent.getAction())) {
				String titleString = intent.getExtras().getString(Intent.EXTRA_SUBJECT);
				String contentString = intent.getExtras().getString(Intent.EXTRA_TEXT) + "\n" + intent.getExtras().getString(Intent.EXTRA_STREAM);				
				edtTopic.setText(titleString);
				if(titleString.length() > 0 ) {
				}
				String resultString = "[url]" +contentString  + "[/url]" ;
				edtContent.setText(resultString);	
				edtContent.requestFocus();
			}	else{
				int position = 0; 
				String identString = intent.getStringExtra("ident");
				String [] identListStrings = DatabaseManager.getInstance().getAllChannelIdent();
				position = CollectionUtils.searchIndex(identString , identListStrings);
				spinner.setSelection(position);		
			}	
		}
		
		//check icon database
		/*SharedPreferences settings = getSharedPreferences("iconPrefs", 0);
		if (settings.getString("lastUpdate", "0").equals("0")){
			startActivity(new Intent(NewPost.this, Setting.class));
			SystemUtils.toast(getApplicationContext(), "你未裝icon+喎!");
		}*/
		
		//get keyboard height
		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
		     public void onGlobalLayout(){
		    	 RelativeLayout buttons = (RelativeLayout) findViewById(R.id.button_post);
		    	 keyboardHeight = parentLayout.getRootView().getHeight()- parentLayout.getHeight() - buttons.getHeight()*2;
		         Log.i("KEYBOARD HEIGHT", String.valueOf(keyboardHeight));
		         if (keyboardHeight_ < keyboardHeight){
		        	 keyboardHeight_ = keyboardHeight;
		         }
		         if (keyboardHeight < 100){
		        	 Log.i("KEYBOARD", "IS HIDE");
		        	 if((popupWindow != null)&&(popupWindow.isShowing())){
		        		 popupWindow.dismiss();
		        	 }
		         }
		        }
		  });
		
		//set bottom menu!!
		setBottomMenu();
		
		
	}
	
	
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	NewPost.this.finish();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("認真???").setPositiveButton("吹咩?!", dialogClickListener)
		    .setNegativeButton("你就輸了!", dialogClickListener).show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		
		if(popupWindow!=null){
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
			}
			popupWindow = null;
		}
	//	DatabaseManager.getInstance().close();
//		mSensorManager.unregisterListener(mSensorListener);		
	}
	
	private void getPopupWindowInstance() {  
        if (null == popupWindow) {
        	LayoutInflater layoutInflater = LayoutInflater.from(this);  
        	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    		boolean dark  =  preferences.getBoolean("dark", false);		
    		if(dark==true){	
                popUpView = layoutInflater.inflate(R.layout.icons_popup_dark, null);
    		}else{

                popUpView = layoutInflater.inflate(R.layout.icons_popup, null);
    		}
            screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            popupWindow = new PopupWindow(popUpView, screenWidth, keyboardHeight_);
            //set backspace on tray
            backspace = (ImageView)popUpView.findViewById(R.id.backspace);
            backspace.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectText();
					edtContent.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
				}
			});
            //set linebreak
            linebreak = (ImageView)popUpView.findViewById(R.id.linebreak);
            linebreak.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectText();
					edtContent.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
				}
			});
            readCollections();
        }
    }
	
	private void readCollections() {
	// TODO Auto-generated method stub
		
		//check icon database
		SharedPreferences settings = getSharedPreferences("iconPrefs", 0);
		if (settings.getString("lastUpdate", "0").equals("0")){
			startActivity(new Intent(NewPost.this, Setting.class));
			SystemUtils.toast(getApplicationContext(), "你未裝icon+喎!");
		}
		else{
			try {
				DatabaseManager.init(getApplication());
				iconsDB =  DatabaseManager.getInstance().getAllIcon();
				
				for (int i = 0; i < iconsDB.size(); i++) {
					String collection = iconsDB.get(i).getIconName();
					collections.add(collection);
//					Log.i("collection", collections.get(i));
				}
				
				/*set spinner*/
				colSpin = (Spinner) popUpView.findViewById(R.id.collection);
				
				colAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collections);
				colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				colSpin.setAdapter(colAdapter);
				colSpin.setSelection(settings.getInt("position", 0));
				colSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						
						icons = new ArrayList<Icons>();
						String collection = colSpin.getItemAtPosition(position).toString();
						Icons temp;
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getRidicule());
						temp.setCode(iconPath(collection, "Ridicule"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getAdore());
						temp.setCode(iconPath(collection, "Adore"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getAgree());
						temp.setCode(iconPath(collection, "Agree"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getAngel());
						temp.setCode(iconPath(collection, "Angel"));
						icons.add(temp);
						
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getAngry());
						temp.setCode(iconPath(collection, "Angry"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getAss());
						temp.setCode(iconPath(collection, "Ass"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBanghead());
						temp.setCode(iconPath(collection, "Banghead"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBiggrin());
						temp.setCode(iconPath(collection, "Biggrin"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBomb());
						temp.setCode(iconPath(collection, "Bomb"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBouncer());
						temp.setCode(iconPath(collection, "Bouncer"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBouncy());
						temp.setCode(iconPath(collection, "Bouncy"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getBye());
						temp.setCode(iconPath(collection, "Bye"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getCensored());
						temp.setCode(iconPath(collection, "Censored"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getChicken());
						temp.setCode(iconPath(collection, "Chicken"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getClown());
						temp.setCode(iconPath(collection, "Clown"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getCry());
						temp.setCode(iconPath(collection, "Cry"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getDead());
						temp.setCode(iconPath(collection, "Dead"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getDevil());
						temp.setCode(iconPath(collection, "Devil"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getDonno());
						temp.setCode(iconPath(collection, "Donno"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getFire());
						temp.setCode(iconPath(collection, "Fire"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getFlowerface());
						temp.setCode(iconPath(collection, "Flowerface"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getFrown());
						temp.setCode(iconPath(collection, "Frown"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getFuck());
						temp.setCode(iconPath(collection, "Fuck"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getGhost());
						temp.setCode(iconPath(collection, "Ghost"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getGood());
						temp.setCode(iconPath(collection, "Good"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getHehe());
						temp.setCode(iconPath(collection, "Hehe"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getHoho());
						temp.setCode(iconPath(collection, "Hoho"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getKillleft());
						temp.setCode(iconPath(collection, "Killleft"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getKillright());
						temp.setCode(iconPath(collection, "Killright"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getKiss());
						temp.setCode(iconPath(collection, "Kiss"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getLove());
						temp.setCode(iconPath(collection, "Love"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getNo());
						temp.setCode(iconPath(collection, "No"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getOfftopic());
						temp.setCode(iconPath(collection, "Offtopic"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getOh());
						temp.setCode(iconPath(collection, "Oh"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getPhoto());
						temp.setCode(iconPath(collection, "Photo"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getShocking());
						temp.setCode(iconPath(collection, "Shocking"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getSlick());
						temp.setCode(iconPath(collection, "Slick"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getSmile());
						temp.setCode(iconPath(collection, "Smile"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getSosad());
						temp.setCode(iconPath(collection, "Sosad"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getSurprise());
						temp.setCode(iconPath(collection, "Surprise"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getTongue());
						temp.setCode(iconPath(collection, "Tongue"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getWink());
						temp.setCode(iconPath(collection, "Wink"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getWonder());
						temp.setCode(iconPath(collection, "Wonder"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getWould());
						temp.setCode(iconPath(collection, "Would"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getYipes());
						temp.setCode(iconPath(collection, "Yipes"));
						icons.add(temp);
						
						temp = new Icons();
						temp.setName(iconsDB.get(position).getZ());
						temp.setCode(iconPath(collection, "Z"));
						icons.add(temp);
						
					//	IconsAdapter itemAdapter = new IconsAdapter(NewPost.this, R.layout.diapic_row, icons);
//						itemAdapter.clear();
					
						iconListView = (GridView) popUpView.findViewById(R.id.gridIcon);
						iconListView.setCacheColorHint(0);
						iconListView.setAdapter(new RecordAdapter(icons,NewPost.this));
						getSharedPreferences("iconPrefs", 0).edit().putInt("position", position).commit();
						/*iconListView = (GridView) popUpView.findViewById(R.id.gridIcon);
						iconListView.setAdapter(itemAdapter);
						iconListView.setCacheColorHint(0);
						iconListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								selectText();
								Icons selectedicon = (Icons) iconListView.getItemAtPosition(position);
								edtContent.getText().replace(startSelection, endSelection, selectedicon.getName());
							}
						});*/
						OpenHelperManager.releaseHelper();
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
					}
				});
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				startActivity(new Intent(NewPost.this, Setting.class));
				SystemUtils.toast(getApplicationContext(), "你個icon+好似有問題喎！試下強制更新/清除快取");
			}
		}
	}

	public class RecordAdapter extends BaseAdapter
	{
		Context context;
		private List<Icons> data;

		RecordAdapter(List<Icons> data, Context context)
		{
			this.data = data;
			this.context = context;
		}

		public int getCount()
		{
			return data.size();
		}

		public Object getItem(int position)
		{
			return data.get(position);
		}

		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (v == null)
			{
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.icon_single, null);
			}

			final Icons item = data.get(position);
			
			ImageView imageView = (ImageView) v.findViewById(R.id.iconsn);
			Picasso.with(context).load(new File(item.getCode())).resize(100, 100).placeholder(R.drawable.ic_launcher).into(imageView);
//			
			//imageView.setImageDrawable(FileUtil.readImage(context, item + ".png"));
			imageView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectText();
					//Icons selectedicon = (Icons) iconListView.getItemAtPosition(position);
					edtContent.getText().replace(startSelection, endSelection, item.getName());										
				}
			
			});
			return v;
		}
	}
	
	private String iconPath(String collection, String name) {
		// TODO Auto-generated method stub
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Gallica" + File.separator + ".iconPlus" + File.separator + collection + File.separator + name + ".gif";
	}

	public void setBottomMenu(){
		icon = (ImageView) findViewById(R.id.icon);
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "SHOW ICON TRAY", Toast.LENGTH_LONG).show();
				
				getPopupWindowInstance();
				
				if (popupWindow.isShowing()){
					popupWindow.dismiss();
				}
				else{
					edtContent.requestFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT);
					popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				}
				
			}
		});
		
		word = (ImageView) findViewById(R.id.word);
		word.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectText();
				PopupMenu popupMenu = new PopupMenu(NewPost.this, v);
                popupMenu.setOnMenuItemClickListener(NewPost.this);
                popupMenu.inflate(R.menu.popup_word);
                popupMenu.show();
                
			}
		});
		
		size = (ImageView) findViewById(R.id.size);
		size.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectText();
				PopupMenu popupMenu = new PopupMenu(NewPost.this, v);
                popupMenu.setOnMenuItemClickListener(NewPost.this);
                popupMenu.inflate(R.menu.popup_size);
                popupMenu.show();
			}
		});
		
		colour = (ImageView) findViewById(R.id.colour);
		colour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectText();
				OnColorSelectedListener listener =   new OnColorSelectedListener() {
					@Override
					public void colorSelected(Integer color) {
						// Do something with the selected color
				
						String hexColor = String.format("#%06X", (0xFFFFFF & color));						
						insertFlag(hexColor);
						//cpd.cancel();
					}
				} ;
				HSVColorPickerDialog cpd = new HSVColorPickerDialog(NewPost.this, 0xFF4488CC, listener);
				cpd.setTitle( "Pick a color" );
				cpd.setNoColorButton( R.string.no_color );
				cpd.show();
				}
		});
		
		img = (ImageView) findViewById(R.id.img);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectText();
				PopupMenu popupMenu = new PopupMenu(NewPost.this, v);
                popupMenu.setOnMenuItemClickListener(NewPost.this);
                popupMenu.inflate(R.menu.popup_img);
                popupMenu.show();
			}
		});
		
		ImageView other = (ImageView) findViewById(R.id.internet);
		other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectText();
				PopupMenu popupMenu = new PopupMenu(NewPost.this, v);
                popupMenu.setOnMenuItemClickListener(NewPost.this);
                popupMenu.inflate(R.menu.popup_other);
                popupMenu.show();
			}
		});
	}
	
	
	//bottom menu case
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		
		case R.id.item_bold:
			insertFlag("b");
            return true;
		case R.id.item_italic:
			insertFlag("i");
            return true;
		case R.id.item_underline:
			insertFlag("u");
            return true;
		case R.id.item_strike:
			insertFlag("s");
            return true;
		case R.id.item_size1:
			insertFlag("size=1");
            return true;
		case R.id.item_size2:
			insertFlag("size=2");
            return true;
		case R.id.item_size3:
			insertFlag("size=3");
            return true;
		case R.id.item_size4:
			insertFlag("size=4");
            return true;
		case R.id.item_size5:
			insertFlag("size=5");
            return true;
		case R.id.item_size6:
			insertFlag("size=6");
            return true;
		case R.id.item_upload:
			startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
            return true;
		case R.id.item_capture:
			fileUri = getOutputMediaFileUri(1 , "CAPTURE"); 					
			startActivityForResult(new Intent
					(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, fileUri), RESULT_CAPTURE);
			return true;
		case R.id.item_dialog:
			startActivityForResult(new Intent(NewPost.this, DialogActivity.class) , REQUEST_DIALOG);
            return true;
		case R.id.item_drawsth:
			startActivityForResult(new Intent(NewPost.this , DrawSthActivity.class), RESULT_DRAW);
            return true;
		case R.id.item_url:
			insertFlag("url");
			return true;
		case R.id.item_img:
			insertFlag("img");
            return true;
		case R.id.item_quote:
			insertFlag("quote");
            return true;
		case R.id.item_hide:
			insertFlag("hide");
            return true;
		default: return false;
		}
	}
	
	//select text hack
	public void selectText(){
		startSelection = edtContent.getSelectionStart();
		endSelection = edtContent.getSelectionEnd();
		edtContent.setSelection(startSelection, endSelection);
		edtContent.requestFocus();
	}
	
	public void insertFlag(final String flag){
		
		//get selected text
		resultInput = edtContent.getText().toString().substring(startSelection, endSelection);
		
		if (resultInput.equals("")){
			AlertDialog.Builder alert = new AlertDialog.Builder(NewPost.this);							
			alert.setTitle(" [" + flag + "] ... [/" + flag + "] "); //Set Alert dialog title here
//			alert.setMessage("請輸入..."); //Message here

			// Set an EditText view to get user input 
			final EditText input = new EditText(NewPost.this);
			alert.setView(input);

			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//You will get as string input data in this variable.
					// here we convert the input to a string and show in a toast.
					resultInput = input.getEditableText().toString();
					String newText = " [" + flag + "]" + resultInput + "[/" + flag + "] ";
					edtContent.getText().replace(startSelection, endSelection, newText);
					edtContent.setSelection(edtContent.getText().toString().length());
				}
			});
			
			alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alert.create();
			alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			alertDialog.show();
		}
		else {
			String newText = " [" + flag + "]" + resultInput + "[/" + flag + "] ";
			edtContent.getText().replace(startSelection, endSelection, newText);
			edtContent.setSelection(edtContent.getText().toString().length());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newpost_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//this.finish();
			return true;
		case R.id.action_publish:
			SharedPreferences settings = getSharedPreferences(preference, 0);
			String token = settings.getString("tokenplus", "");

			//String channel= spinner.getSelectedItem().toString();
			String channel = mapToIdent(spinner. getSelectedItemPosition ());

			String title = edtTopic.getText().toString().trim();
			String content = edtContent.getText().toString().trim();
			if(checkInput(title, content)){
				String argument = title + "|"  + content  + "|"+ token + "|" + channel;
				//	SystemUtils.toast(getApplicationContext(), argument);		
				new PostYourStuff (NewPost.this).execute(argument);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public boolean checkInput(String tit , String cont){
		boolean result = true;


		if(InputValidation.IsNullOrWhiteSpaces(tit)){
			SystemUtils.toast(getApplicationContext(), "Title is empty");
			edtTopic.requestFocus();
			result = false;
			return result;
		}
		if(InputValidation.IsNullOrWhiteSpaces(cont)){
			SystemUtils.toast(getApplicationContext(), "Content is empty");
			edtContent.requestFocus();
			result = false;
			return result;
		}

		return result;
	}
	

	private static Uri getOutputMediaFileUri(int type , String typePhoto) {
		return Uri.fromFile(getOutputMediaFile(type ,typePhoto));
	}

	private static File getOutputMediaFile(int type ,  String typePhoto) {

		if (Environment.getExternalStorageState() == null) {
			return null;
		}
		File mediaStorageDirF = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +  File.separator + "Gallica");
		// Create the storage directory if it does not exist
		if (!mediaStorageDirF.exists()) {
			if (!mediaStorageDirF.mkdirs()) {
				return null;
			}
		}
		
		File mediaStorageDir = new File(mediaStorageDirF.getAbsolutePath() +  File.separator + "GallicaCap");
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile; 
		if (type == 1) {
			//int value = photoFileCount + 1;
			mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator	+  "IMG_"   +typePhoto + ".jpg");
			imageCaptured = mediaFile;			

		} else {
			return null;
		}
		return mediaFile;
	}
	
	public void loadDialogMessage (final String flag , String title ,
			final String []  dialogA){
		final Dialog dialog = new Dialog(NewPost.this);
		dialog.setContentView(R.layout.icon_list_layout);
		dialog.setTitle(title);
		ListView listView = (ListView) dialog.findViewById(R.id.list);
		//	ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.dialogpic_detail, dialogA);

		ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dialogA);

		listView.setAdapter(itemAdapter);
		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				//do something on click
				String link = getResources().getStringArray(R.array.dialog_array_link)[position].toString();
				dialog.dismiss();
				edtContent = (EditText)NewPost.this.findViewById(R.id.content_post);
				//				edtContent.append(link);
				edtContent.getText().insert(edtContent.getSelectionStart(), link);
			}
		});

		dialog.show();
	}


	public String mapToIdent(int position){
		DatabaseManager.init(getApplication());
		List<Channel> channels = DatabaseManager.getInstance().getAllChannel();
		return channels.get(position).getIdent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);

			File f= new File(picturePath);

			new UploadImage(NewPost.this).execute(f);


			cursor.close();

			// String picturePath contains the path of selected Image
		}

		if (requestCode == RESULT_DRAW && resultCode == RESULT_OK && null != data) {

			String filePath = data.getExtras().getString("draw");
			File fileDrawFile = new File(filePath);
			new UploadImage(NewPost.this).execute(fileDrawFile);
			// String picturePath contains the path of selected Image
		}

		if (requestCode == REQUEST_DIALOG) {

			if(resultCode == RESULT_OK){      
				String linkString = data.getExtras().getString("dialog");		
				linkString = data.getStringExtra("dialog");
				Toast.makeText(NewPost.this, linkString, Toast.LENGTH_LONG).show();
				edtContent.getText().insert(edtContent.getSelectionStart(), "[img]"+linkString+"[/img]");       
			}
			if (resultCode == RESULT_CANCELED) {    
				//Write your code if there's no result
				Toast.makeText(NewPost.this, "no result", Toast.LENGTH_LONG).show();
				return;
			}			
		}

		if (requestCode == RESULT_CAPTURE && resultCode == RESULT_OK) {
			try {

				System.out.println("photoPath: " + imageCaptured.getAbsolutePath());
			
				new UploadImage(NewPost.this).execute(imageCaptured);			

			} catch (Exception e) {

			}

		}
	}
}
