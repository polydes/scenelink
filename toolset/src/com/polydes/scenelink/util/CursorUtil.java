package com.polydes.scenelink.util;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JComponent;

import com.polydes.common.res.ResourceLoader;
import com.polydes.common.res.Resources;

public class CursorUtil
{
	private static Resources res = ResourceLoader.getResources("com.polydes.scenelink");
	
	private static HashMap<String, Cursor> cursors;
	
	public static void setCursor(JComponent comp, String url)
	{
		if(cursors == null)
		{
			cursors = new HashMap<String, Cursor>();
			cursors.put("default", comp.getCursor());
		}
		
		if(!cursors.containsKey(url))
		{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = res.loadIcon("arrows/" + url + ".png").getImage();
			Cursor c = toolkit.createCustomCursor(image , new Point(8, 8), url);
			cursors.put(url, c);
		}
		
		comp.setCursor(cursors.get(url));
	}
}
