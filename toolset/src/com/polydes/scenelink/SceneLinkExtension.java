package com.polydes.scenelink;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JPanel;

import com.polydes.scenelink.data.LinkModel;
import com.polydes.scenelink.data.LinkPageModel;
import com.polydes.scenelink.io.Images;
import com.polydes.scenelink.io.XML;
import com.polydes.scenelink.ui.MainPage;
import com.polydes.scenelink.ui.combos.PageComboModel;
import com.polydes.scenelink.util.ColorUtil;

import stencyl.app.ext.PageAddon;
import stencyl.app.ext.PageAddon.EngineExtensionPageAddon;
import stencyl.core.ext.GameExtension;
import stencyl.core.ext.engine.ExtensionInstanceManager.FormatUpdateSubmitter;
import stencyl.core.io.FileHelper;
import stencyl.core.lib.IProject;
import stencyl.sw.app.center.GameLibrary;

public class SceneLinkExtension extends GameExtension
{
	public static IProject project;
	public static File pagesFolder;
	public static File resourcesFolder;
	
	public static HashMap<Integer, LinkPageModel> pages;

	@Override
	protected void onSave() {
		for(LinkPageModel model : pages.values())
		{
			String pageName = pagesFolder.getAbsolutePath() + File.separator + model.getId() + ".xml";
			XML.wrObjectToFile(pageName, model);
		}
	}

	@Override
	protected void onLoad()
	{
		project = getProject();
		resourcesFolder = openFolder(new File(getDataFolder(), "resources"));
		pagesFolder = openFolder(new File(getDataFolder(), "pages"));
		
		Images.loadResourceNames();

		pages = new HashMap<Integer, LinkPageModel>();

		File[] pageFiles = pagesFolder.listFiles();
		for(File f : pageFiles)
		{
			LinkPageModel m = (LinkPageModel) XML.rObjectFromFile(f.getAbsolutePath(), LinkPageModel.class);
			pages.put(m.getId(), m);
		}
		PageComboModel.updatePages();

		if(pageFiles.length == 0)
			generateNewModel();

		PageAddon scenelinkSidebarPage = new EngineExtensionPageAddon(owner())
		{
			@Override
			public JPanel getPage()
			{
				return MainPage.get();
			}
		};

		owner().getAddons().setAddon(GameLibrary.DASHBOARD_SIDEBAR_PAGE_ADDONS, scenelinkSidebarPage);
	}
	
	public File openFolder(File f)
	{
		if(!f.exists())
			f.mkdirs();
		return f;
	}

	@Override
	protected void onUnload()
	{
		project = null;
		pages = null;
	}

	@Override
	protected int detectOldInstall()
	{
		return getProject().getFile("extras", "[ext] scene link").exists() ? 1 : -1;
	}

	@Override
	public void updateFromVersion(int fromVersion, FormatUpdateSubmitter formatUpdateQueue) {
		if(fromVersion <= 1)
		{
			formatUpdateQueue.add(() -> {
				File oldExtrasFolder = getProject().getFile("extras", "[ext] scene link");

				FileHelper.copyDirectory(oldExtrasFolder, getExtrasFolder());
				FileHelper.delete(oldExtrasFolder);
			});
		}
	}

	public static Collection<LinkPageModel> getPages()
	{
		return pages.values();
	}
	
	public static LinkPageModel getPageModel(int id)
	{
		return pages.get(id);
	}
	
	private static int nextPageModelID()
	{
		int id = -1;
		for(int i : pages.keySet())
			id = Math.max(id, i);
		return ++id;
	}
	
	public static LinkPageModel generateNewModel()
	{
		int id = nextPageModelID();
		LinkPageModel newModel = new LinkPageModel
		(
			id,
			"Page " + id,
			"",
			new Dimension(640, 640),
			"",
			new Point(0, 0),
			ColorUtil.decode("#ff333333"),
			new Point(0, 0),
			new Dimension(32, 32),
			ColorUtil.decode("#ff81969a"),
			true,
			true,
			new HashMap<Integer, LinkModel>()
		);
		pages.put(id, newModel);
		PageComboModel.updatePages();
		return newModel;
	}
}