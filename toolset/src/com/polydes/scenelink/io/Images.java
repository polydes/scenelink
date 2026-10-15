package com.polydes.scenelink.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import com.polydes.scenelink.SceneLinkExtension;
import com.polydes.scenelink.ui.combos.ImageReferenceComboModel;
import stencyl.core.io.Files2;
import stencyl.core.util.gfx.ImageUtil;

public class Images
{
	private static ArrayList<String> resourceNames;
	private static HashMap<String, BufferedImage> imageCache = new HashMap<String, BufferedImage>();
	
	public static BufferedImage getImage(String name)
	{
		if(name.equals(""))
			return null;

		if(!imageCache.containsKey(name))
			imageCache.put(name, readImage(name));

		return imageCache.get(name);
	}
	
	public static BufferedImage readImage(String name)
	{
		if(name.equals(""))
			return null;
		
		Path imgPath = SceneLinkExtension.resourcesFolder.resolve(name + ".png");
		
		try
		{
			return ImageUtil.read(imgPath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<String> getResourceNames()
	{
		return resourceNames;
	}

	public static void loadResourceNames()
	{
		resourceNames = new ArrayList<String>();
		for(String s : Files2.list(SceneLinkExtension.resourcesFolder))
		{
			if(s.endsWith(".png"))
				s = s.substring(0, s.length() - 4);
			
			resourceNames.add(s);
		}
		
		ImageReferenceComboModel.updateImages();
	}
}
