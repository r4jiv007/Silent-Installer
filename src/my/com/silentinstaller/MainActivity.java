package my.com.silentinstaller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import my.com.silentinstaller.utils.DataHolder;
import my.com.silentinstaller.utils.ExecuteAsRootBase;

import org.apache.commons.io.comparator.NameFileComparator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class to show startup activity
 * 
 * 
 */

@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError",
		"ParserError" })
public class MainActivity extends Activity {

	public static ListView filelist;
	public static TextView path, mApkPath;
	public static String apkPath;
	public static File file0;
	public static File[] filearray;
	public static MainActivity Active_Instance;
	public static ArrayList<String> prevPath = new ArrayList<String>();
	static Button select, install;
	static Dialog show_files;
	static Button back;

	// public static File Dir0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		select = (Button) findViewById(R.id.fileselct);
		install = (Button) findViewById(R.id.bInstall);
		mApkPath = (TextView) findViewById(R.id.ftlook);
		Active_Instance = this;

		mApkPath.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int n = DataHolder.OnlyFiles.size();
				if (n > 0) {
					for (int i = 0; i < n; i++) {
						Toast.makeText(
								getApplicationContext(),
								DataHolder.OnlyFiles.get(i).getFile().getName()
										.toString(), 2000).show();
					}
				} else if (n == 0)
					Toast.makeText(getApplicationContext(), "size is 0", 2000)
							.show();

				Toast.makeText(getApplicationContext(),
						String.valueOf(DataHolder.Dcounter), 2000).show();

			}
		});
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * DataHolder.dirStack.removeAllElements();
				 * DataHolder.OnlyFiles.clear();
				 */
				File root = new File("/");

				showDialog(root);
			}
		});

		install.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (apkPath != null) {
					new ApkInstallerTask(apkPath).execute();

				} else {
					Toast.makeText(getApplicationContext(),
							"Please Select any Apk file to Install",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	/**
	 * method to list the content of the dir in a order
	 * 
	 * @param file2list
	 */

	public void allinOrder(File file2list) {
		File[] allfiles;
		Runtime.getRuntime().gc();
		ArrayList<File> onlyfiles = new ArrayList<File>();
		ArrayList<File> onlydirs = new ArrayList<File>();
		if (file2list.listFiles() != null) {
			allfiles = file2list.listFiles();
			for (File selfile : allfiles) {
				if (selfile.isDirectory()) {
					onlydirs.add(selfile);
				} else if (selfile.getName().contains(".apk")
						|| selfile.getName().contains(".APK")) {
					onlyfiles.add(selfile);
				}

			}

			Collections.sort(onlydirs, NameFileComparator.NAME_COMPARATOR);
			Collections.sort(onlyfiles, NameFileComparator.NAME_COMPARATOR);
			onlydirs.addAll(onlyfiles);

			filelist.setAdapter(new FileAdapter(onlydirs, file2list
					.getAbsolutePath()));

		}
	}

	/**
	 * method to show directory selection dialog accepts an argument to list its
	 * content. should be directory
	 * 
	 * @param filetolist
	 */

	public void showDialog(File filetolist) {
		show_files = new Dialog(MainActivity.this);
		show_files.requestWindowFeature(Window.FEATURE_NO_TITLE);
		show_files.setContentView(R.layout.browser);
		show_files.setCancelable(true);
		filelist = (ListView) show_files.findViewById(R.id.filelist);
		path = (TextView) show_files.findViewById(R.id.path);
		back = (Button) show_files.findViewById(R.id.back);
		back.setEnabled(false);

		allinOrder(filetolist);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!path.getText().toString().equals("/")) {
					// filelist.setAdapter(new FileAdapter(new
					// File(prevPath.get(prevPath.size()-1)).listFiles(),
					// prevPath.get(prevPath.size()-1)));
					allinOrder(new File(prevPath.get(prevPath.size() - 1)));
					// super.onBackPressed();
					prevPath.remove(prevPath.size() - 1);
					if (path.getText().toString().equals("/")) {
						back.setEnabled(false);
					}
				} else {
					show_files.dismiss();
				}

			}
		});

		show_files.show();
	}

	/**
	 * Adapter class to set content to listview extends abstract BaseAdapter
	 * class
	 * 
	 * @author rajiv
	 * 
	 */

	public class FileAdapter extends BaseAdapter {

		// File[] files ;
		ArrayList<File> files;

		public FileAdapter(ArrayList<File> filelist, String fpath) {
			path.setText(fpath);
			this.files = filelist;
		}

		/**
		 * 
		 */
		@Override
		public int getCount() {

			return files.toArray().length;
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View v = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.filename, null);

			ImageView img = (ImageView) v.findViewById(R.id.img);
			TextView filename = (TextView) v.findViewById(R.id.fname);
			LinearLayout namelt = (LinearLayout) v
					.findViewById(R.id.namelayout);

			namelt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (files.get(position).isDirectory()
								&& files.get(position).canRead()) {
							prevPath.add(path.getText().toString());
							// filelist.setAdapter(new
							// FileAdapter(files[position].listFiles(),files[position].getAbsolutePath()));

							Runtime.getRuntime().gc();
							back.setEnabled(true);
							allinOrder(files.get(position));
						} else if (!files.get(position).canRead()
								&& files.get(position).isDirectory()) {
							Toast.makeText(getApplicationContext(),
									"Access Denied", 5000).show();
						}
					} catch (NullPointerException e) {

						Toast.makeText(getApplicationContext(),
								"Error opening folder", 5000).show();
						prevPath.remove(prevPath.size() - 1);
						// filelist.setAdapter(new FileAdapter(new
						// File(prevPath.get(prevPath.size()-1)).listFiles(),
						// prevPath.get(prevPath.size()-1)));

						Runtime.getRuntime().gc();
						allinOrder(new File(prevPath.get(prevPath.size() - 1)));

					}
				}
			});

			namelt.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					if (files.get(position).isFile()) {
						final AlertDialog alertDialog = new AlertDialog.Builder(
								MainActivity.this).create();

						alertDialog.setTitle("DFR");

						alertDialog.setMessage("Select "
								+ files.get(position).getName() + " !!");
						alertDialog.setButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										alertDialog.dismiss();
									}
								});
						alertDialog.setButton2("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// Write your code here to execute after
										// dialog closed
										apkPath = files.get(position)
												.getAbsolutePath();

										Toast.makeText(
												getApplicationContext(),
												"You selected "
														+ files.get(position)
																.getName(),
												Toast.LENGTH_LONG).show();
										mApkPath.setText(apkPath);
										show_files.dismiss();
									}
								});

						// Showing Alert Message
						alertDialog.show();
					}
					return false;
				}
			});

			filename.setText(files.get(position).getName());

			if (files.get(position).isDirectory()) {
				img.setImageResource(R.drawable.foldericon);
			} else {
				img.setImageResource(R.drawable.fileicon);
			}

			return v;
		}

	}

	private boolean installApk(String apkPath) {
		Command installEvent = new Command(getCommandList("pm install "
				+ apkPath));
		return installEvent.executeCommandList();
		// Log.d("tapEvent", success + "");

	}

	private class Command extends ExecuteAsRootBase {

		private ArrayList<String> mCommands;

		public Command(ArrayList<String> commands) {
			mCommands = commands;
		}

		@Override
		protected ArrayList<String> getCommandsToExecute() {

			return mCommands;
		}

	}

	private ArrayList<String> getCommandList(String string) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(string);
		return commands;
	}

	private class ApkInstallerTask extends AsyncTask<Void, Void, Void> {
		boolean status;
		String mPath;

		public ApkInstallerTask(String path) {
			mPath = path;
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(MainActivity.this, "Installing Apk",
					Toast.LENGTH_SHORT).show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			status = installApk(mPath);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (status) {
				Toast.makeText(MainActivity.this, "Installation Successful",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Installation Unsuccessful",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
}