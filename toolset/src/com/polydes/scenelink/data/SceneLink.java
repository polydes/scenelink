package com.polydes.scenelink.data;

import com.polydes.scenelink.SceneLinkExtension;
import stencyl.app.doc.IWorkspace;
import stencyl.core.SWC;
import stencyl.sw.core.lib.scene.SceneModel;

public class SceneLink extends Link
{
	public SceneLink(int id)
	{
		super(id);
	}

	@Override
	public void open()
	{
		SceneModel model = SceneLinkExtension.project.getResource(SceneModel.class, id);
		if(model == null)
		{
			System.out.println("Cannot open null scene: " + id);
			return;
		}

		SWC.get(IWorkspace.class).openResource(model, false);
	}
	
	@Override
	public Object getModel()
	{
		return SceneLinkExtension.project.getResource(SceneModel.class, id);
	}
}
