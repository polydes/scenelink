package com.polydes.scenelink.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.polydes.scenelink.SceneLinkExtension;
import com.polydes.scenelink.ui.combos.ImageReferenceComboModel;

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
		
		File imgPath = new File(SceneLinkExtension.resourcesFolder, name + ".png");
		
		try
		{
			return ImageIO.read(imgPath);
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
		for(String s : SceneLinkExtension.resourcesFolder.list())
		{
			if(s.endsWith(".png"))
				s = s.substring(0, s.length() - 4);
			
			resourceNames.add(s);
		}
		
		ImageReferenceComboModel.updateImages();
	}
}
