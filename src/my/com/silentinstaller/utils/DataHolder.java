package my.com.silentinstaller.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Stack;

public class DataHolder {

	
	public static ArrayList<File> FilesWithD = new ArrayList<File>();
	public static HashMap<String, ArrayList<String>> DMAP = new HashMap<String, ArrayList<String>>(); 
	public static boolean copyExist;
	public static int Dcounter;
	public static int noFileScanned;
	public static int noDupliFound;
	public static File selFolder;
	public static ArrayList<FileX> OnlyFiles = new ArrayList<FileX>();
	
	
	
}
